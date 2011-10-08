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

package de.yaams.extensions.basemap.tiled.mapeditor.brush;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import de.yaams.extensions.basemap.tiled.core.MultilayerPlane;
import de.yaams.extensions.basemap.tiled.view.MapView;

public abstract class AbstractBrush extends MultilayerPlane implements Brush {
	protected int numLayers = 1;
	protected MultilayerPlane affectedMp;
	protected boolean paintingStarted = false;
	protected int initLayer;

	public AbstractBrush() {}

	public AbstractBrush(AbstractBrush ab) {
		numLayers = ab.numLayers;
	}

	/**
	 * This will set the number of layers to affect, the default is 1 - the
	 * layer specified in commitPaint.
	 * 
	 * @see Brush#doPaint(int, int)
	 * @param num
	 *            the number of layers to affect.
	 */
	public void setAffectedLayers(int num) {
		numLayers = num;
	}

	@Override
	public int getAffectedLayers() {
		return numLayers;
	}

	@Override
	public void startPaint(MultilayerPlane mp, int x, int y, int button, int layer) {
		affectedMp = mp;
		initLayer = layer;
		paintingStarted = true;
	}

	@Override
	public Rectangle doPaint(int x, int y) throws Exception {
		if (!paintingStarted)
			throw new Exception("Attempted to call doPaint() without calling startPaint()!");
		return null;
	}

	@Override
	public void endPaint() {
		paintingStarted = false;
	}

	@Override
	public void drawPreview(Graphics2D g2d, Dimension dimension, MapView mv) {
		// todo: draw an off-map preview here
	}

	public abstract Shape getShape();
}
