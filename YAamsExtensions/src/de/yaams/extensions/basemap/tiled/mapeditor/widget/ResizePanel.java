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
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.event.MouseInputAdapter;

import de.yaams.extensions.basemap.tiled.core.Map;
import de.yaams.extensions.basemap.tiled.view.MapView;

/**
 * A special widget designed as an aid for resizing the map. Based on a similar
 * widget used by the GIMP when resizing the image.
 * 
 * @version $Id$
 */
public class ResizePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8241643061775773568L;
	private MapView inner;
	private Map currentMap;
	private Dimension oldDim, newDim;
	private int offsetX, offsetY;
	private Point startPress;
	private double zoom;

	public ResizePanel() {
		setLayout(new OverlayLayout(this));
		setBorder(BorderFactory.createLoweredBevelBorder());
	}

	public ResizePanel(Map map) {
		this();
		zoom = 0.1;
		currentMap = map;

		DragHandler dragHandler = new DragHandler();

		inner = MapView.createViewforMap(map);
		inner.setZoom(zoom);
		inner.addMouseListener(dragHandler);
		inner.addMouseMotionListener(dragHandler);
		add(inner);

		Dimension old = inner.getPreferredSize();
		// TODO: get smaller dimension, zoom based on that...
		oldDim = old;
		setSize(old);
	}

	public ResizePanel(Dimension size, Map map) {
		this(map);
		oldDim = size;
		newDim = size;
		setSize(size);
	}

	public void moveMap(int x, int y) {
		// snap!
		inner.setLocation((int) (x * (currentMap.getTileWidth() * zoom)), (int) (y * (currentMap.getTileHeight() * zoom)));
	}

	public void setNewDimensions(Dimension n) {
		newDim = n;
		// TODO: recalc the map size...
	}

	@Override
	public Dimension getPreferredSize() {
		return oldDim;
	}

	public double getZoom() {
		return zoom;
	}

	private class DragHandler extends MouseInputAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			startPress = e.getPoint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			startPress = null;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			int newOffsetX = offsetX + (e.getX() - startPress.x);
			int newOffsetY = offsetY + (e.getY() - startPress.y);

			newOffsetX /= currentMap.getTileWidth() * zoom;
			newOffsetY /= currentMap.getTileHeight() * zoom;

			if (newOffsetX != offsetX) {
				firePropertyChange("offsetX", offsetX, newOffsetX);
				offsetX = newOffsetX;
			}

			if (newOffsetY != offsetY) {
				firePropertyChange("offsetY", offsetY, newOffsetY);
				offsetY = newOffsetY;
			}
		}
	}
}
