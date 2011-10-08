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

package de.yaams.extensions.basemap.tiled.mapeditor.util;

import java.awt.Component;
import java.awt.Image;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

import de.yaams.extensions.basemap.tiled.core.Tile;
import de.yaams.extensions.basemap.tiled.mapeditor.Resources;

public class TileDialogListRenderer extends DefaultListCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2411210717980692242L;
	private static final String TILE = Resources.getString("general.tile.tile");
	private static final String NOTILE = Resources.getString("general.tile.notile");

	private double zoom = 1;

	public TileDialogListRenderer() {
		setOpaque(true);
	}

	public TileDialogListRenderer(double zoom) {
		this();
		this.zoom = zoom;
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		Tile tile = (Tile) value;

		if (tile != null) {
			Image scaledImage = tile.getScaledImage(zoom);
			if (scaledImage != null) {
				setIcon(new ImageIcon(scaledImage));
			}
			setText(TILE + " " + tile.getId());
		} else {
			setIcon(null);
			setText(NOTILE);
		}

		return this;
	}
}
