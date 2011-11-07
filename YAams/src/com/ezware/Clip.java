package com.ezware;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.yaams.maker.helper.gui.rightclick.ClipboardHelper;

public class Clip {
	public static void main(String args[]) {

		ClipboardHelper.set(null);

		Runnable runner = new Runnable() {
			@Override
			public void run() {
				JFrame frame = new JFrame("Clip");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				final Clipboard clipboard = frame.getToolkit().getSystemClipboard();

				final JTextArea jt = new JTextArea();

				JScrollPane pane = new JScrollPane(jt);
				frame.add(pane, BorderLayout.CENTER);

				JButton copy = new JButton("Copy");
				copy.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String selection = jt.getSelectedText();
						StringSelection data = new StringSelection(selection);
						clipboard.setContents(data, data);
						ClipboardHelper.set(null);
					}
				});

				JButton paste = new JButton("Paste");
				paste.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent actionEvent) {
						Transferable clipData = clipboard.getContents(clipboard);
						if (clipData != null) {
							try {
								if (clipData.isDataFlavorSupported(DataFlavor.stringFlavor)) {
									String s = (String) clipData.getTransferData(DataFlavor.stringFlavor);
									jt.replaceSelection(s);
								}
							} catch (UnsupportedFlavorException ufe) {
								System.err.println("Flavor unsupported: " + ufe);
							} catch (IOException ioe) {
								System.err.println("Data not available: " + ioe);
							}
						}
					}
				});
				JPanel p = new JPanel();
				p.add(copy);
				p.add(paste);
				frame.add(p, BorderLayout.SOUTH);
				frame.setSize(300, 300);
				frame.setVisible(true);
			}
		};
		EventQueue.invokeLater(runner);
	}
}