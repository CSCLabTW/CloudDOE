package tw.edu.sinica.iis.SSHadoop;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SSHadoopUtils {
	Pattern patMapStatus = Pattern.compile("map\\(\\) completion: (.*)");
	Pattern patRedStatus = Pattern.compile("reduce\\(\\) completion: (.*)");

	Pattern patJobId = Pattern.compile(".*(Running job: )(.*)");
	Pattern patToolStatus = Pattern
			.compile(".*(Tool name: )(.*) \\[((\\d+)/(\\d+)|\\.\\.\\.)\\]");

	Pattern patCBEnd = Pattern.compile(".*(== Ending time )(.*)");
	Pattern patCBTer = Pattern.compile(".*(Program terminated)(.*)");

	public enum CBStatus {
		DEFAULT, ERROR, SUCCESS
	}

	public double[] getJobStatus(final String status) {
		double mapStatus = -1.0;
		double redStatus = -1.0;

		Matcher matMapStatus = patMapStatus.matcher(status);
		Matcher matRedStatus = patRedStatus.matcher(status);

		if (matMapStatus.find()) {
			if (matMapStatus.group(1) != null) {
				mapStatus = Float.parseFloat(matMapStatus.group(1).trim()) * 100.0;
			}
		}

		if (matRedStatus.find()) {
			if (matRedStatus.group(1) != null) {
				redStatus = Float.parseFloat(matRedStatus.group(1).trim()) * 100.0;
			}
		}

		return (new double[] { mapStatus, redStatus });
	}

	public CBStatus getCBStatus(final String status) {
		Matcher matCBEnd = patCBEnd.matcher(status);
		Matcher matCBTer = patCBTer.matcher(status);

		if (matCBTer.find()) {
			return CBStatus.ERROR;
		}

		if (matCBEnd.find()) {
			return CBStatus.SUCCESS;
		}

		return CBStatus.DEFAULT;
	}

	public CBStatus getOPStatus(final String status) {
		String[] currentStatus = status.split(";");
		
		if (currentStatus.length != 2) {
			return CBStatus.ERROR;
		}

		if (currentStatus[0].trim().equals("")) {
			if (currentStatus[1].trim().equals("")) {
				return CBStatus.ERROR;
			} else {
				return CBStatus.SUCCESS;
			}
		}

		return CBStatus.DEFAULT;
	}

	public String[] getCBStepAndJobId(final String status) {
		String toolName = "";
		String toolStep = "";
		String jobId = "";

		Matcher matJobId = patJobId.matcher(status);
		Matcher matToolName = patToolStatus.matcher(status);

		while (matToolName.find()) {
			if (matToolName.group(2) != null) {
				toolName = matToolName.group(2).trim();
			}

			if (matToolName.group(3) != null) {
				if (matToolName.group(4) != null
						&& matToolName.group(5) != null) {
					toolStep = String
							.valueOf((int) (Double.valueOf(matToolName.group(4)
									.trim())
									/ Double.valueOf(matToolName.group(5)
											.trim()) * 100.0));
				}
			}
		}

		while (matJobId.find()) {
			if (matJobId.group(2) != null) {
				jobId = matJobId.group(2).trim();
			}
		}
		return (new String[] { toolName, toolStep, jobId });
	}
}
