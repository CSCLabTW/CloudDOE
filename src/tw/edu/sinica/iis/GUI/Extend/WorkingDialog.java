package tw.edu.sinica.iis.GUI.Extend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class WorkingDialog extends JDialog implements Runnable {

	private static final long serialVersionUID = -7301793567606079680L;
	public String Content;
	public int Count;
	public JLabel cLabel;

	public boolean isRunning;

	public WorkingDialog(JFrame f, boolean b, String s) {
		super(f, b);
		Content = s;
		Count = 0;
		this.setSize(200, 40);
		setCenter(f);
		this.setUndecorated(true);

		JPanel tmpP = new JPanel();
		tmpP.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		tmpP.setBackground(Color.WHITE);
		cLabel = new JLabel();
		cLabel.setHorizontalAlignment(SwingConstants.CENTER);
		cLabel.setVerticalAlignment(SwingConstants.CENTER);
		Font oldFont = cLabel.getFont();
		cLabel.setFont(new Font(oldFont.getName(), oldFont.getStyle(), 18));
		tmpP.add(cLabel);

		this.add(tmpP);
		isRunning = true;

		new Thread(this).start();

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		isRunning = false;
	}

	public void setCenter(JFrame f) {
		Point p = f.getLocation();
		Dimension d = f.getSize();
		Dimension d2 = this.getSize();
		this.setLocation(p.x + d.width / 2 - d2.width / 2, p.y + d.height / 2
				- d2.height / 2);

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isRunning) {
			try {
				String zs = "";
				for (int i = 0; i < Count; i++) {
					zs += ".";
				}
				cLabel.setText(Content + zs);
				Count++;
				if (Count > 4)
					Count = 0;
				// System.out.println("working");
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO: handle exception

			}
		}
	}

	public static void main(String[] args) {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton b = new JButton();
		b.setText("test");
		b.addActionListener(new ActionListener() {
			public WorkingDialog tmp;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				tmp = new WorkingDialog(null, true, "testing");

				new Thread(new Runnable() {

					@Override
					public void run() {

						tmp.pack();
						tmp.setVisible(true);
						// System.out.println("hi");
					}
				}).start();

				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(15000);
						} catch (Exception e) {
							// TODO: handle exception
						}
						tmp.dispose();
					}
				}).start();

			}
		});
		window.add(b);
		window.pack();
		window.setSize(new Dimension(200, 200));
		window.setVisible(true);

	}

}
