/*
 * (C) Copyright 2013 The CloudDOE Project and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *      Wei-Chun Chung (wcchung@iis.sinica.edu.tw)
 *      Yu-Chun Wang (zxaustin@iis.sinica.edu.tw)
 * 
 * CloudDOE Project:
 *      http://clouddoe.iis.sinica.edu.tw/
 */

package tw.edu.sinica.iis.SSHadoop;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SSHadoopUtils {
	Pattern patMapStatus = Pattern.compile("map\\(\\) completion: (.*)");
	Pattern patRedStatus = Pattern.compile("reduce\\(\\) completion: (.*)");

	Pattern patJobId = Pattern.compile(".*(Running job: )(.*)");
	Pattern patToolStatus = Pattern
			.compile(".*(Tool name: )(.*) \\[((\\d+)/(\\d+)|\\.\\.\\.)\\]");

	public enum OperateStatus {
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

	public OperateStatus getOPStatus(final String status) {
		String[] currentStatus = status.split(";");
		
		if (currentStatus.length != 2) {
			return OperateStatus.ERROR;
		}

		if (currentStatus[0].trim().equals("")) {
			if (currentStatus[1].trim().equals("")) {
				return OperateStatus.ERROR;
			} else {
				return OperateStatus.SUCCESS;
			}
		}

		return OperateStatus.DEFAULT;
	}

	public String[] getOPStepAndJobId(final String status) {
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
		
		if(toolName.equals("")) {
			toolName = jobId;
		}
		
		return (new String[] { toolName, toolStep, jobId });
	}
}
