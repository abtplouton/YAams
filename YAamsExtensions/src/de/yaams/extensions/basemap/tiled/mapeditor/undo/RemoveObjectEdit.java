/*
 * Tiled Map Editor, (c) 2008
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

import de.yaams.extensions.basemap.tiled.core.MapObject;
import de.yaams.extensions.basemap.tiled.core.ObjectGroup;
import de.yaams.extensions.basemap.tiled.mapeditor.Resources;

/**
 * Removes an object from an object group.
 * 
 * @version $Id$
 */
public class RemoveObjectEdit extends AbstractUndoableEdit {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6228802764624263532L;
	private final ObjectGroup objectGroup;
	private final MapObject mapObject;

	public RemoveObjectEdit(ObjectGroup objectGroup, MapObject mapObject) {
		this.objectGroup = objectGroup;
		this.mapObject = mapObject;
	}

	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		objectGroup.addObject(mapObject);
	}

	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		objectGroup.removeObject(mapObject);
	}

	@Override
	public String getPresentationName() {
		return Resources.getString("action.object.remove.name");
	}
}
