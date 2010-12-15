package de.juwimm.cms.content.panel.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;

import de.juwimm.cms.content.panel.PanDocuments;

public class PdfPreviewFrame extends JFrame implements DocumentPreviewComponent {
	private static Logger log = Logger.getLogger(PanDocuments.class);

	public PdfPreviewFrame() {
		this("Document preview");
	}
	private PDFFile pdffile;
	private PagePanel pagePanel = new PagePanel();
	private int currentPage;
	private JButton next;
	private JButton previous;

	public PdfPreviewFrame(String windowTitle) {
		super(windowTitle);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.centerWithinParent();

	}

	/**
	 * @inheritDoc
	 */
	public void displayComponent() {

		// show the first page
		PDFPage page = pdffile.getPage(0);
		currentPage = 0;
		Dimension dimension = new Dimension(Float.valueOf(page.getWidth()).intValue(), Float.valueOf(page.getHeight()).intValue());
		pagePanel.setSize(dimension);
		pagePanel.setMinimumSize(dimension);
		pagePanel.setMaximumSize(dimension);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(pagePanel, BorderLayout.CENTER);
		this.getContentPane().add(getButtonsPanel(), BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
		pagePanel.showPage(page);
		centerWithinParent();

	}

	/**
	 * Creates and returns the <b>Previous</b> and <b>Next</b> buttons panel to be displayed underneath the visible area of the document.
	 * @return
	 */
	private JPanel getButtonsPanel() {
		next = new JButton(">");
		next.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				actionNext(e);
			}
		});
		previous = new JButton("<");
		previous.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				actionPrevious(e);
			}
		});
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(previous, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.LINE_END, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
		panel.add(next, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
		return panel;
	}

	/**
	 * @inheritDoc
	 */
	public void setDocumentContent(byte[] content) {
		File file = null;
		try {
			file = File.createTempFile("doc_", ".pdf");
			OutputStream out = new FileOutputStream(file);
			out.write(content);
			out.close();
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			FileChannel channel = raf.getChannel();
			ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
			pdffile = new PDFFile(buf);
		} catch (IOException e) {
			log.error(e);
		}
	}

	/**
	 * Centers the component <b>wind</b> into the visible rectangle of the parent component. If parent is not found, will call <i>centerWithinScreen</i> method.
	 */
	private void centerWithinParent() {
		final Container parent = this.getParent();
		if (parent != null && parent.isVisible()) {
			center(this, new Rectangle(parent.getLocationOnScreen(), parent.getSize()));
		} else {
			centerWithinScreen(this);
		}
	}

	/**
	 * Centers the component <b>wind</b> into the visible areea of the screen.
	 * @param wind
	 */
	private void centerWithinScreen(Window wind) {
		if (wind == null) {
			throw new IllegalArgumentException("null Window passed");
		}
		final Toolkit toolKit = Toolkit.getDefaultToolkit();
		final Rectangle rcScreen = new Rectangle(toolKit.getScreenSize());
		final Dimension windSize = wind.getSize();
		final Dimension parentSize = new Dimension(rcScreen.width, rcScreen.height);
		if (windSize.height > parentSize.height) {
			windSize.height = parentSize.height;
		}
		if (windSize.width > parentSize.width) {
			windSize.width = parentSize.width;
		}
		center(wind, rcScreen);
	}

	/**
	 * Centers the component <b>wind</b> into the given rectangle <b>rect</b>
	 * @param wind
	 * @param rect
	 */
	private void center(Component wind, Rectangle rect) {
		if (wind == null || rect == null) {
			throw new IllegalArgumentException("null Window or Rectangle passed");
		}
		Dimension windSize = wind.getSize();
		int x = ((rect.width - windSize.width) / 2) + rect.x;
		int y = ((rect.height - windSize.height) / 2) + rect.y;
		if (y < rect.y) {
			y = rect.y;
		}
		wind.setLocation(x, y);
	}

	/**
	 * Method called when pressing <b>Next</b> button.
	 * Should display the next page.
	 * @param e
	 */
	private void actionNext(ActionEvent e) {
		if (currentPage == pdffile.getNumPages() - 1) {
			return;
		}
		currentPage++;
		PDFPage page = pdffile.getPage(currentPage);
		pagePanel.showPage(page);
	}

	/**
	 * Method called when pressing <b>Previous</b> button.
	 * Should display the previous page.
	 * @param e
	 */
	private void actionPrevious(ActionEvent e) {
		if (currentPage == 0) {
			return;
		}
		currentPage--;
		PDFPage page = pdffile.getPage(currentPage);
		pagePanel.showPage(page);
	}
}
