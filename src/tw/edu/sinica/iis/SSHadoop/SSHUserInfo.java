package tw.edu.sinica.iis.SSHadoop;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

public class SSHUserInfo implements UserInfo, UIKeyboardInteractive {
	private String passwd;
	private String passphrase;
	private Container panel;
	/*
	 * private JTextField passwordField = (JTextField)(new JPasswordField(20));
	 */
	private JTextField passphraseField = (new JPasswordField(20));
	private final GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1,
			1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
			new Insets(0, 0, 0, 0), 0, 0);

	@Override
	public String getPassword() {
		return passwd;
	}

	@Override
	public boolean promptYesNo(final String str) {
		Object[] options = { "yes", "no" };

		int result = JOptionPane.showOptionDialog(null, str, "Warning",
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
				options, options[0]);

		return (result == 0);
	}

	@Override
	public String getPassphrase() {
		return passphrase;
	}

	@Override
	public boolean promptPassphrase(final String message) {
		Object[] ob = { passphraseField };

		int result = JOptionPane.showConfirmDialog(null, ob, message,
				JOptionPane.OK_CANCEL_OPTION);

		if (result == JOptionPane.OK_OPTION) {
			passphrase = passphraseField.getText();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean promptPassword(final String message) {
		return false;

		/*
		 * Object[] ob = { passwordField };
		 * 
		 * int result = JOptionPane.showConfirmDialog( null, ob, message,
		 * JOptionPane.OK_CANCEL_OPTION );
		 * 
		 * if(result == JOptionPane.OK_OPTION) { passwd =
		 * passwordField.getText(); return true; } else { return false; }
		 */
	}

	@Override
	public void showMessage(final String message) {
		JOptionPane.showMessageDialog(null, message);
	}

	@Override
	public String[] promptKeyboardInteractive(String destination, String name,
			String instruction, String[] prompt, boolean[] echo) {

		JTextField[] texts = new JTextField[prompt.length];

		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridx = 0;

		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(new JLabel(instruction), gbc);

		gbc.gridy++;
		gbc.gridwidth = GridBagConstraints.RELATIVE;

		for (int i = 0; i < prompt.length; i++) {
			gbc.fill = GridBagConstraints.NONE;
			gbc.gridx = 0;
			gbc.weightx = 1;
			panel.add(new JLabel(prompt[i]), gbc);

			gbc.gridx = 1;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weighty = 1;

			if (echo[i]) {
				texts[i] = new JTextField(20);
			} else {
				texts[i] = new JPasswordField(20);
			}

			panel.add(texts[i], gbc);
			gbc.gridy++;
		}

		int result = JOptionPane.showConfirmDialog(null, panel, destination
				+ ": " + name, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			String[] response = new String[prompt.length];

			for (int i = 0; i < prompt.length; i++) {
				response[i] = texts[i].getText();
			}

			return response;
		} else {
			return null;
		}
	}
}
