package tw.edu.sinica.iis.GUI.Extend;

import java.awt.AWTEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class MonitorDialog extends JDialog {

	private static final long serialVersionUID = 2854142358L;

	private String pidFilePath = null;
	private String logFilePath = null;
	
	public MonitorPanel mp;

	public MonitorDialog(JFrame f, boolean b) {
		super(f, b);
	}
	
	public void setPIDFile(final String pidFile) {
		pidFilePath = pidFile;
	}
	
	public void setLogFile(final String logFile) {
		logFilePath = logFile;
	}

	public void init() {
		this.setTitle("Installation Progress...");
		mp = new MonitorPanel(pidFilePath, logFilePath);
		
		mp.setRefreshPeriod(5000);
		
		mp.closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MonitorDialog.this.setVisible(false);
			}
		});
		
		this.add(mp);
		this.pack();
		
		mp.startMonitor();
	}

	@Override
	protected void processEvent(AWTEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			int res = JOptionPane.showConfirmDialog(MonitorDialog.this, "Close program without completed installation may lead to\n unexpeteced errors. Quit anyway?");
			if(res == JOptionPane.OK_OPTION){
				super.processEvent(e);
			}else{
				return;
			}
		}
		super.processEvent(e);
	}
	
	public static void main(String[] args) {
		final JFrame window = new JFrame();
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton bt = new JButton("test");
		bt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MonitorDialog md = new MonitorDialog(window, true);
				md.init();
				md.setVisible(true);
			}
		});
		
		window.getContentPane().add(bt);
		window.pack();
		window.setVisible(true);
	}
}
