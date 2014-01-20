package tw.edu.sinica.iis.GUI.Extend;

import static javafx.concurrent.Worker.State.FAILED;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import netscape.javascript.JSObject;

public class WebPanel extends JPanel {

	private static final long serialVersionUID = 2341954265L;
	
	private static final String repoURL = "http://cloudbrush.iis.sinica.edu.tw/plugin.html";

	public JPanel webView;
	public JLabel webTitle;
	public JButton logoutBT;
	public JFXPanel jfxPanel;
	public WebEngine we;

	public WebPanel() {
		super();
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		this.add(getNorthPanel(), BorderLayout.NORTH);
		this.add(getCenterPanel(), BorderLayout.CENTER);
		this.add(getSouthPanel(), BorderLayout.SOUTH);
	}

	public JPanel getNorthPanel() {
		JPanel nPanel = new JPanel();
		nPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		nPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		webTitle = new JLabel();
		webTitle.setText("WebTitle");
		nPanel.add(webTitle);
		
		return nPanel;
	}

	public JPanel getCenterPanel() {
		JPanel cPanel = new JPanel();
		cPanel.setLayout(new BorderLayout());
		cPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		webView = new JPanel();
		webView.setBackground(Color.RED);
		cPanel.add(webView, BorderLayout.CENTER);

		createJFXPanel();
		cPanel.add(jfxPanel, BorderLayout.CENTER);

		return cPanel;
	}

	public JPanel getSouthPanel() {
		JPanel sPanel = new JPanel();
		sPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		sPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		logoutBT = new JButton("Logout");
		sPanel.add(logoutBT);

		return sPanel;
	}

	public void registerAction(final String name,final Object obj){
		Platform.runLater(new Runnable() {
            @Override public void run() {
            	we.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
        			@Override
        			public void changed(ObservableValue<? extends State> ov,
        					State oldS, State newS) {
        				if(newS == State.SUCCEEDED) {
        					JSObject window = (JSObject) we.executeScript("window");
        		            window.setMember(name, obj);
        				}
        			}
        		});
            }
		});
	}
	
	public void createJFXPanel() {
		jfxPanel = new JFXPanel();

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				WebView view = new WebView();
				we = view.getEngine();
				
				we.titleProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(
							ObservableValue<? extends String> observable,
							String oldValue, final String newValue) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								webTitle.setText(newValue);
							}
						});
					}
				});

				we.getLoadWorker().exceptionProperty().addListener(
						new ChangeListener<Throwable>() {

							public void changed(
									ObservableValue<? extends Throwable> o,
									Throwable old, final Throwable value) {
								if (we.getLoadWorker().getState() == FAILED) {
									SwingUtilities.invokeLater(new Runnable() {
										@Override
										public void run() {
											JOptionPane
													.showMessageDialog(
															WebPanel.this,
															(value != null) ? we
																	.getLocation()
																	+ "\n"
																	+ value
																			.getMessage()
																	: we
																			.getLocation()
																			+ "\nUnexpected error.",
															"Loading error...",
															JOptionPane.ERROR_MESSAGE);
										}
									});
								}
							}
						});

				jfxPanel.setScene(new Scene(view));
			}
		});
		loadURL(repoURL);
		
		return;
	}
	
	public void loadURL() {
        Platform.runLater(new Runnable() {
            @Override public void run() {
            	we.load(getClass().getResource("/assets/index.html").toString());
            }
        });
	}
      
    public void loadURL(final String url) {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                String tmp = toURL(url);

                if (tmp == null) {
                    tmp = toURL("http://" + url);
                }
                
                we.load(tmp);
            }
        });
    }
    
    private static String toURL(String str) {
        try {
            return new URL(str).toExternalForm();
        } catch (MalformedURLException exception) {
                return null;
        }
    }

	public static void main(String[] args) {
		JFrame window = new JFrame();

		window.getContentPane().add(new WebPanel());
		window.pack();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

}
