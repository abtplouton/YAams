/*
 * Tiled Map Editor, (c) 2004-2006
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * Adam Turk <aturk@biggeruniverse.com> Bjorn Lindeijer <bjorn@lindeijer.nl>
 */

package de.yaams.extensions.basemap.tiled.mapeditor.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.PixelGrabber;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.yaams.extensions.basemap.tiled.mapeditor.Resources;
import de.yaams.extensions.basemap.tiled.mapeditor.widget.ImageViewPanel;
import de.yaams.extensions.basemap.tiled.mapeditor.widget.VerticalStaticJPanel;

public class ImageColorDialog extends JDialog implements MouseListener, MouseMotionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4292975556903994304L;
	private Image image;
	private JButton cancelButton;
	private Color color;
	private JPanel colorPanel;
	private int[] pixels;

	/* LANGUAGE PACK */
	private static final String DIALOG_TITLE = Resources.getString("dialog.imagecolor.title");
	private static final String CANCEL_BUTTON = Resources.getString("general.button.cancel");

	/* -- */

	public ImageColorDialog(Image image) {
		this.image = image;
		PixelGrabber pg = new PixelGrabber(image, 0, 0, -1, -1, true);

		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		pixels = (int[]) pg.getPixels();

		init();
		pack();
		setLocationRelativeTo(getOwner());
		setModal(true);
	}

	private void init() {
		ImageViewPanel imagePanel = new ImageViewPanel(image);
		imagePanel.addMouseListener(this);
		imagePanel.addMouseMotionListener(this);

		setTitle(DIALOG_TITLE);

		color = new Color(255, 103, 139); // Evil pink
		colorPanel = new JPanel();
		colorPanel.setPreferredSize(new Dimension(25, 25));
		colorPanel.setBackground(color);

		cancelButton = new JButton(CANCEL_BUTTON);

		JScrollPane imageScrollPane = new JScrollPane(imagePanel);
		imageScrollPane.setAutoscrolls(true);

		VerticalStaticJPanel mainPanel = new VerticalStaticJPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		mainPanel.add(imageScrollPane);

		JPanel buttonPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		buttonPanel.add(colorPanel);
		c.gridx = 1;
		buttonPanel.add(Box.createRigidArea(new Dimension(25, 5)));
		c.gridx = 2;
		buttonPanel.add(cancelButton);

		mainPanel.add(buttonPanel);

		setContentPane(mainPanel);

		// create actionlisteners
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				color = null;
				dispose();
			}
		});
	}

	public Color showDialog() {
		setVisible(true);
		return color;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		grabColor(e.getX(), e.getY());
		dispose();
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		grabColor(e.getX(), e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		grabColor(e.getX(), e.getY());
	}

	private void grabColor(int x, int y) {
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		if (pixels != null && (x < w && y < h)) {
			int r = pixels[y * w + x] >> 16 & 0xff;
			int g = pixels[y * w + x] >> 8 & 0xff;
			int b = pixels[y * w + x] & 0xff;

			color = new Color(r, g, b);
			colorPanel.setBackground(color);
		}
	}
}
