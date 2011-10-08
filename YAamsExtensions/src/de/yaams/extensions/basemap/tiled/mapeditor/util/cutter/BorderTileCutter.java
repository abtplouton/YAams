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

package de.yaams.extensions.basemap.tiled.mapeditor.util.cutter;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * @version $Id$
 */
public class BorderTileCutter implements TileCutter {
	@Override
	public String getName() {
		return "Border";
	}

	@Override
	public void setImage(BufferedImage image) {
		// TODO Auto-generated method stub
	}

	@Override
	public Image getNextTile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}

	@Override
	public Dimension getTileDimensions() {
		// TODO Auto-generated method stub
		return null;
	}
}
