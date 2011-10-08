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

package de.yaams.extensions.basemap.tiled.mapeditor.actions;

import de.yaams.extensions.basemap.tiled.core.Map;
import de.yaams.extensions.basemap.tiled.mapeditor.MapEditor;
import de.yaams.extensions.basemap.tiled.mapeditor.Resources;

/**
 * Adds a object group layer to the current map and selects it.
 * 
 * @version $Id$
 */
public class AddObjectGroupAction extends AbstractLayerAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 382066871104744610L;

	public AddObjectGroupAction(MapEditor editor) {
		super(editor, Resources.getString("action.objectgroup.add.name"), Resources.getString("action.objectgroup.add.tooltip"), Resources
				.getIcon("gnome-new.png"));
	}

	@Override
	protected void doPerformAction() {
		Map currentMap = editor.getCurrentMap();
		currentMap.addObjectGroup();
		editor.setCurrentLayer(currentMap.getTotalLayers() - 1);
	}
}
