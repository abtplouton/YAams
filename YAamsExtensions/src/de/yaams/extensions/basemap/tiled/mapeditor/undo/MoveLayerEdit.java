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

import java.awt.Point;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import de.yaams.extensions.basemap.tiled.core.MapLayer;
import de.yaams.extensions.basemap.tiled.mapeditor.Resources;

/**
 * @version $Id$
 */
public class MoveLayerEdit extends AbstractUndoableEdit {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2303243351407326527L;
	private final MapLayer layer;
	private final Point moveDist;

	public MoveLayerEdit(MapLayer layer, Point moveDist) {
		this.layer = layer;
		this.moveDist = moveDist;
	}

	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		layer.translate(-moveDist.x, -moveDist.y);
	}

	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		layer.translate(moveDist.x, moveDist.y);
	}

	@Override
	public String getPresentationName() {
		return Resources.getString("action.layer.move.name");
	}
}
