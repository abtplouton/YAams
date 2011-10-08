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

package de.yaams.extensions.basemap.tiled.mapeditor.widget;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ImageViewPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4377805476196658932L;
	private final Image image;

	public ImageViewPanel(Image image) {
		this.image = image;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(150, 150);
	}

	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(image, 0, 0, null);
	}
}
