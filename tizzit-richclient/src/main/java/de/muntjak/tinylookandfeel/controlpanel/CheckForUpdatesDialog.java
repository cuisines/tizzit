package de.muntjak.tinylookandfeel.controlpanel;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import de.muntjak.tinylookandfeel.TinyLookAndFeel;


public class CheckForUpdatesDialog extends JDialog {
	
//	local - resolves to D:\\htdocs\tinylaf\...
//	private static final String CHECK_UPDATES_URL =
//		"http://localhost:8080/tinylaf/checkforupdate.html";

	// web
	private static final String CHECK_UPDATES_URL =
		"http://www.muntjak.de/hans/java/tinylaf/checkforupdate.html";
	
	private CheckForUpdatesDialog(Frame parent) {
		super(parent, "Check for Updates", true);
		
		setupUI(parent);
	}

	static void showDialog(Frame parent) {
		new CheckForUpdatesDialog(parent);
	}
	
	private void setupUI(Frame frame) {
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel p = new JPanel(new BorderLayout(0, 12));
		JLabel l = new JLabel("<html>" +
			"When checking for updates, TinyLaF will connect to <b>muntjak.de</b>" +
			"<br>via HTTP. No personal data will be transmitted.");
		l.setBorder(new EmptyBorder(8, 8, 0, 8));
		p.add(l, BorderLayout.NORTH);
		
		JButton b = new JButton("Check for updates now");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = checkForUpdates();
				int index = msg.indexOf("Exception was: ");
				
				if(index != -1) {
					String title = msg.substring(index + 15);
					
					JOptionPane.showMessageDialog(
						CheckForUpdatesDialog.this, msg,
						title, JOptionPane.PLAIN_MESSAGE);
				}
				else {
					if(msg.startsWith("No ")) {
						JOptionPane.showMessageDialog(
							CheckForUpdatesDialog.this, msg,
							"Update Information",
							JOptionPane.PLAIN_MESSAGE);
					}
					else {
						new UpdateDialog(CheckForUpdatesDialog.this, msg);
					}
				}
			}
		});
		
		JPanel flow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
		flow.add(b);
		p.add(flow, BorderLayout.CENTER);
		p.add(new JSeparator(), BorderLayout.SOUTH);
		
		getContentPane().add(p, BorderLayout.CENTER);
		
		b = new JButton("Close");
		getRootPane().setDefaultButton(b);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		flow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
		flow.add(b);
		getContentPane().add(flow, BorderLayout.SOUTH);
		
		pack();
		
		Dimension size = getSize();
		setLocation(frame.getLocationOnScreen().x + 
			(frame.getWidth() - getSize().width) / 2,
			frame.getLocationOnScreen().y + 
			(frame.getHeight() - getSize().height) / 2);
		setVisible(true);
	}
	
	private String checkForUpdates() {
		String answer = checkForUpdate();
//		String answer = "TinyLaF 1.3.6 (2005/10/30)";
//		String answer = "TinyLaF " +
//			TinyLookAndFeel.VERSION_STRING + " (2005/10/30)";
		
		if(answer.indexOf("Exception") != -1) return answer;
		
		if(!(answer.startsWith("TinyLaF ") && answer.endsWith(")") &&
			answer.indexOf("(") != -1 && answer.indexOf("/") != -1))
		{
			return "An exception occured while checking for updates." +
					"\n\nException was: Invalid response.";
		}
		
		String version = answer.substring(8, answer.indexOf("(") - 1);

		if(!TinyLookAndFeel.VERSION_STRING.equals(version)) {
			return answer;
		}
		else {
			return "No updated version of TinyLaF available.";
		}
	}
	
	private String checkForUpdate() {
		InputStream is = null;
		
		try {
			URL url = new URL(CHECK_UPDATES_URL);

			try {
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				
				conn.setRequestProperty("User-Agent", "TinyLaF");
					
				Object content = conn.getContent();

				if(!(content instanceof InputStream)) {
					return "An exception occured while checking for updates." +
						"\n\nException was: Content is no InputStream";
				}
				
				is = (InputStream)content;
			} catch(ConnectException ex) {
//					ex.printStackTrace();
				return "An exception occured while checking for updates." +
					"\n\nException was: " + ex.getClass().getName();
			} catch(UnknownHostException ex) {
//					ex.printStackTrace();
				return "An exception occured while checking for updates." +
					"\n\nException was: " + ex.getClass().getName();
			} catch (IOException ex) {
//				ex.printStackTrace();
				return "An exception occured while checking for updates." +
					"\n\nException was: " + ex.getClass().getName();
			}
		}
		catch(MalformedURLException ex) {
//			ex.printStackTrace();
			return "An exception occured while checking for updates." +
				"\n\nException was: " + ex.getClass().getName();
		}

		// read message returned from muntjak server
		try {
			BufferedReader in = new BufferedReader(
				new InputStreamReader(is));
			
			StringBuffer buff = new StringBuffer();
			String line;
			
			while((line = in.readLine()) != null) {
				buff.append(line);
			}
			
			in.close();

			return buff.toString();
		} catch (IOException ex) {
//			ex.printStackTrace();
			return "An exception occured while checking for updates." +
				"\n\nException was: " + ex.getClass().getName();
		}
	}
	
	private class UpdateDialog extends JDialog {
		
		UpdateDialog(Dialog owner, String version) {
			super(CheckForUpdatesDialog.this, "Update Information", true);
			
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);

			getContentPane().setLayout(new BorderLayout());
			
			String msg = "<html>" +
				"An updated version of TinyLaF is available:<br>" +
				version + "<br>" +
				"It can be downloaded at www.muntjak.de/hans/java/tinylaf/.";
			JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
			p.add(new JLabel(msg));
			getContentPane().add(p, BorderLayout.CENTER);
			
			p = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
			
			JButton b = new JButton("Copy Link");
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
					
					if(cb == null) {
						JOptionPane.showMessageDialog(UpdateDialog.this,
							"System Clipboard not available.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					}
					else {
						StringSelection ss = new StringSelection(
							"http://www.muntjak.de/hans/java/tinylaf/");
						cb.setContents(ss, ss);
					}
				}
			});
			p.add(b);
			
			b = new JButton("Close");
			getRootPane().setDefaultButton(b);
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					UpdateDialog.this.dispose();
				}
			});
			p.add(b);
			
			getContentPane().add(p, BorderLayout.SOUTH);
			
			pack();
			
			Point loc = owner.getLocationOnScreen();
			loc.x += (owner.getWidth() - getWidth()) / 2;
			loc.y += (owner.getHeight() - getHeight()) / 2;
			
			setLocation(loc);
			setVisible(true);
		}
	}
}
