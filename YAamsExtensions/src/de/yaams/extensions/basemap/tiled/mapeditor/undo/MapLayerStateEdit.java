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

package de.yaams.extensions.basemap.tiled.mapeditor.undo;

import java.util.Vector;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import de.yaams.extensions.basemap.tiled.core.Map;
import de.yaams.extensions.basemap.tiled.core.MapLayer;

/**
 * A change in the layer state. Used for adding, removing and rearranging the
 * layer stack of a map.
 */
public class MapLayerStateEdit extends AbstractUndoableEdit {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5436958218779510330L;
	private final Map map;
	private final Vector<MapLayer> layersBefore;
	private final Vector<MapLayer> layersAfter;
	private final String name;

	public MapLayerStateEdit(Map m, Vector<MapLayer> before, Vector<MapLayer> after, String name) {
		map = m;
		layersBefore = before;
		layersAfter = after;
		this.name = name;
	}

	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		map.setLayerVector(layersBefore);
	}

	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		map.setLayerVector(layersAfter);
	}

	@Override
	public String getPresentationName() {
		return name;
	}
}
