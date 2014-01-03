package tw.edu.sinica.iis.SSHadoop;

import java.util.Properties;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

public class SSHSession {
	private JSch jsch = null;
	private Session session = null;

	private String sshHost = "";
	private String sshUser = "";
	private String sshPass = "";
	private int sshPort = 22;

	private String sshIdentity = "";
	private String sshKnownHost = "";

	public void setHost(final String host) {
		sshHost = host;
	}

	public void setUser(final String user) {
		sshUser = user;
	}

	public void setPass(final String pass) {
		sshPass = pass;
	}

	public void setPort(final int port) {
		sshPort = port;
	}

	public void setKnownHosts(final String hostFile) {
		sshKnownHost = hostFile;
	}

	public void setIdentity(final String identFile) {
		sshIdentity = identFile;
	}
	
	public SSHSession() {
	}

	public SSHSession(final String user, final String host) {
		setHost(host);
		setUser(user);
	}

	public Session getSession() {
		return session;
	}

	public void initSession() {
		jsch = new JSch();

		try {
			if (!"".equals(sshKnownHost)) {
				jsch.setKnownHosts(sshKnownHost);
			}
		} catch (JSchException e) {
			e.printStackTrace();
		}

		try {
			if (!"".equals(sshIdentity)) {
				jsch.addIdentity(sshIdentity);
			}
		} catch (JSchException e) {
			e.printStackTrace();
		}

	}

	public boolean openSession() {
		if (session == null || !session.isConnected()) {
			try {
				session = jsch.getSession(sshUser, sshHost, sshPort);

				Properties config = new Properties();
				config.put("StrictHostKeyChecking", "no");
				session.setConfig(config);

				UserInfo ui = new SSHUserInfo();
				session.setUserInfo(ui);

				if (!"".equals(sshPass))
					session.setPassword(sshPass);

				if (!session.isConnected())
					session.connect();

				if (session.isConnected())
					return true;
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
		return false;
		// if(session != null && session.isConnected())
		// System.out.println("Open Session");
	}

	public void closeSession() {
		if (session != null && session.isConnected()) {
			session.disconnect();
		}

		// if(session == null || !session.isConnected())
		// System.out.println("Close Session");
	}
}
