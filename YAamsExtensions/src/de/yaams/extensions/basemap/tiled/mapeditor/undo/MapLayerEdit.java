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

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import de.yaams.extensions.basemap.tiled.core.MapLayer;

/**
 * @version $Id$
 */
public class MapLayerEdit extends AbstractUndoableEdit {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7551280335563595777L;
	private final MapLayer editedLayer;
	private MapLayer layerUndo, layerRedo;
	private String name;
	private boolean inProgress;

	public MapLayerEdit(MapLayer layer) {
		editedLayer = layer;
	}

	public MapLayerEdit(MapLayer layer, MapLayer before) {
		this(layer);
		start(before);
	}

	public MapLayerEdit(MapLayer layer, MapLayer before, MapLayer after) {
		this(layer, before);
		end(after);
	}

	public void start(MapLayer fml) {
		layerUndo = fml;
		inProgress = true;
	}

	public void end(MapLayer fml) {
		if (!inProgress) {
			new Exception("end called before start").printStackTrace();
		}
		if (fml != null) {
			layerRedo = fml;
			inProgress = false;
		}
	}

	public MapLayer getStart() {
		return layerUndo;
	}

	/* inherited methods */
	@Override
	public void undo() throws CannotUndoException {
		if (editedLayer == null) {
			throw new CannotUndoException();
		}
		layerUndo.copyTo(editedLayer);
	}

	@Override
	public boolean canUndo() {
		return layerUndo != null && editedLayer != null;
	}

	@Override
	public void redo() throws CannotRedoException {
		if (editedLayer == null) {
			throw new CannotRedoException();
		}
		layerRedo.copyTo(editedLayer);
	}

	@Override
	public boolean canRedo() {
		return layerRedo != null && editedLayer != null;
	}

	@Override
	public void die() {
		layerUndo = null;
		layerRedo = null;
		inProgress = false;
	}

	@Override
	public boolean addEdit(UndoableEdit anEdit) {
		if (inProgress && anEdit.getClass() == getClass()) {
			// TODO: absorb the edit
			// return true;
		}
		return false;
	}

	public void setPresentationName(String s) {
		name = s;
	}

	@Override
	public String getPresentationName() {
		return name;
	}
}
