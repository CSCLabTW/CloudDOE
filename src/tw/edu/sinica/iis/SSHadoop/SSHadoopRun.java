package tw.edu.sinica.iis.SSHadoop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jcraft.jsch.Session;

public class SSHadoopRun extends SSHExec {
	private String jobId = null;
	Pattern pattern = Pattern.compile("Running job: (.*)");

	public SSHadoopRun(final Session sess, final String command) {
		setSession(sess);
		setCommand(command);
	}

	/* Wait for only the job id */
	private void waitForFinish() {
		if (channel != null && channel.isConnected()) {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(execStdout));

			String line = null;
			try {
				while (true) {
					if ((line = bufferedReader.readLine()) != null) {
						System.out.println(line);
						Matcher matcher = pattern.matcher(line);
						if (matcher.find()) {
							jobId = matcher.group(1).trim();

							break;
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String call() throws Exception {
		openChannel();
		execChannel();
		waitForFinish();
		closeChannel();

		return jobId;
	}
}
