package tw.edu.sinica.iis.SSHadoop;

import com.jcraft.jsch.Session;

public class SSHCBRun extends SSHExec {
	public SSHCBRun(final Session sess, final String command) {
		setSession(sess);
		setCommand(command);
	}

	@Override
	public String call() throws Exception {
		openChannel();
		execChannel();
		closeChannel();

		return null;
	}
}
