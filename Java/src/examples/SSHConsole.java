package examples;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHConsole {
	public static void main(String[] args) throws JSchException, IOException {
		JFrame frame = new JFrame("Console");
		final JTextArea textarea = new JTextArea();
		
		DefaultCaret caret = (DefaultCaret) textarea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 400);
		frame.add(new JScrollPane(textarea));
		frame.setVisible(true);

		JSch jsch = new JSch();
		
		Session session = jsch.getSession("hadoop", "140.109.18.202");
		session.setPassword("1qazse4");
		session.setConfig("StrictHostKeyChecking", "no");
		session.connect();
		
		Channel channel = session.openChannel("shell");
		
		final OutputStream out = channel.getOutputStream();
		
		textarea.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				try {
					out.write(e.getKeyChar());
					out.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		channel.setOutputStream(new PrintStream(new OutputStream() {
			
			@Override
			public void write(int b) throws IOException {
				textarea.append(String.valueOf((char)b));
			}
		}));
		
		channel.connect();
	}
}
