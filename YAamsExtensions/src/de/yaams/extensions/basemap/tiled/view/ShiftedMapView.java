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

package de.yaams.extensions.basemap.tiled.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import javax.swing.SwingConstants;

import de.yaams.extensions.basemap.tiled.core.Map;
import de.yaams.extensions.basemap.tiled.core.ObjectGroup;
import de.yaams.extensions.basemap.tiled.core.TileLayer;

/**
 * @version $Id$
 */
public class ShiftedMapView extends MapView {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6050320262727027224L;
	private int horSide; // Length of horizontal sides
	private int verSide; // Length of vertical sides

	/**
	 * Creates a new shifted map view that displays the specified map.
	 * 
	 * @param map
	 *            the map to be displayed by this map view
	 */
	public ShiftedMapView(Map map) {
		super(map);

		horSide = 16;
		verSide = 0;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		int unit = getScrollableUnitIncrement(visibleRect, orientation, direction);

		if (orientation == SwingConstants.VERTICAL) {
			return (visibleRect.height / unit) * unit;
		} else {
			return (visibleRect.width / unit) * unit;
		}
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		Dimension tsize = getTileSize();
		if (orientation == SwingConstants.VERTICAL) {
			return tsize.height - (tsize.height - (int) (verSide * zoom)) / 2;
		} else {
			return tsize.width - (tsize.width - (int) (horSide * zoom)) / 2;
		}
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension tsize = getTileSize();
		int border = showGrid ? 1 : 0;
		int onceX = (tsize.width - (int) (horSide * zoom)) / 2;
		int repeatX = tsize.width - onceX;
		int onceY = (tsize.height - (int) (verSide * zoom)) / 2;
		int repeatY = tsize.height - onceY;

		return new Dimension(map.getWidth() * repeatX + onceX + border, map.getHeight() * repeatY + onceY + border);
	}

	@Override
	protected void paintLayer(Graphics2D g2d, TileLayer layer) {}

	@Override
	protected void paintObjectGroup(Graphics2D g2d, ObjectGroup og) {}

	@Override
	protected void paintGrid(Graphics2D g2d) {
		// Determine tile size
		Dimension tsize = getTileSize();
		if (tsize.width <= 0 || tsize.height <= 0)
			return;
		int onceX = (tsize.width - (int) (horSide * zoom)) / 2;
		int repeatX = tsize.width - onceX;
		int onceY = (tsize.height - (int) (verSide * zoom)) / 2;
		int repeatY = tsize.height - onceY;
		if (repeatX <= 0 || repeatY <= 0)
			return;

		// Determine lines to draw from clipping rectangle
		Rectangle clipRect = g2d.getClipBounds();
		int startX = clipRect.x / repeatX;
		int startY = clipRect.y / repeatY;
		int endX = (clipRect.x + clipRect.width) / repeatX + 1;
		int endY = (clipRect.y + clipRect.height) / repeatY + 1;
		int p = startY * repeatY;

		// These are temp debug lines not the real grid, draw in light gray
		Color prevColor = g2d.getColor();
		g2d.setColor(Color.gray);

		for (int y = startY; y < endY; y++) {
			g2d.drawLine(clipRect.x, p, clipRect.x + clipRect.width - 1, p);
			p += repeatY;
		}
		p = startX * repeatX;
		for (int x = startX; x < endX; x++) {
			g2d.drawLine(p, clipRect.y, p, clipRect.y + clipRect.height - 1);
			p += repeatX;
		}

		g2d.setColor(prevColor);
	}

	@Override
	protected void paintCoordinates(Graphics2D g2d) {}

	@Override
	protected void paintPropertyFlags(Graphics2D g2d, TileLayer layer) {
		throw new RuntimeException("Not yet implemented"); // todo
	}

	@Override
	public void repaintRegion(Rectangle region) {}

	@Override
	public Point screenToTileCoords(int x, int y) {
		return new Point(0, 0);
	}

	@Override
	public Point screenToPixelCoords(int x, int y) {
		// TODO: add proper implementation
		return new Point();
	}

	protected Dimension getTileSize() {
		return new Dimension((int) (map.getTileWidth() * zoom), (int) (map.getTileHeight() * zoom));
	}

	@Override
	protected Polygon createGridPolygon(int tx, int ty, int border) {
		return new Polygon();
	}

	@Override
	public Point tileToScreenCoords(int x, int y) {
		return new Point(0, 0);
	}
}
