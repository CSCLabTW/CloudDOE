package tw.edu.sinica.iis.GUI.Extend;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import tw.edu.sinica.iis.SSHadoop.SSHShell;

public class JFXAction {
	public PluginRepo frame;

	private SSHShell sshShell;
	
	private final String protocol = "cbppi://";
	private final String shscript = ".sh";
	private final String CBPURL   = "http://clouddoe.iis.sinica.edu.tw/plugins/";
	
	private String cbppiToURL(final String internalURI) {
		String url = null;
		
		if(internalURI.startsWith(protocol)) {
			url = CBPURL + internalURI.substring(protocol.length()) + shscript;
		}
		
		return url;
	}
	
	private String replaceProtocol(final String internalURI) {
		if(internalURI.startsWith(protocol)) {
			return internalURI.substring(protocol.length());
		}
		
		return null;
	}
	
	public JFXAction(PluginRepo f) {
		frame = f;
	}
	
	public void intro(final String pkgName, final String pkgDesc) {
		JOptionPane.showMessageDialog(frame, new JLabel("<html><body style='width:400px'>"+pkgDesc+"</body></html>"),
				"Introduction of "+pkgName, JOptionPane.INFORMATION_MESSAGE );
	}

	public void install(final String pkgName, final String pkgContent) {
		if (JOptionPane.showConfirmDialog(frame, "Install " + pkgName + " ?") == JOptionPane.OK_OPTION) {
			final String instScript = cbppiToURL(pkgContent);
			if(instScript == null) {
				JOptionPane.showMessageDialog(frame, "Server error. Please contact to CloudBrushPack team for support.",
						"Error message", JOptionPane.ERROR_MESSAGE);
			}
			else {
				// TODO: need refinement
				final String logFilePath = "~/" + replaceProtocol(pkgContent) + ".log";
				final String pidFilePath = "~/" + replaceProtocol(pkgContent) + "_install.pid";
				
				final String instCmd = "install.sh " + PluginRepo.IP + " " + PluginRepo.Username + " " + PluginRepo.Password + " " + replaceProtocol(pkgContent);
				
				String testCmd = PluginRepo.HadoopCmd.PPIRun(instCmd, instScript, logFilePath);

				sshShell = new SSHShell(PluginRepo.HadoopSession.getSession(), testCmd);

				try {
					sshShell.call();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error.");
				}
				
				MonitorDialog md = new MonitorDialog(frame, true);
				md.setPIDFile(pidFilePath);
				md.setLogFile(logFilePath);
				md.init();
				md.setVisible(true);
			}
		}
	}
}
