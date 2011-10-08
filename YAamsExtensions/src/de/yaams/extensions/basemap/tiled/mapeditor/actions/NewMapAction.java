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

import javax.swing.KeyStroke;

import de.yaams.extensions.basemap.tiled.core.Map;
import de.yaams.extensions.basemap.tiled.mapeditor.MapEditor;
import de.yaams.extensions.basemap.tiled.mapeditor.Resources;
import de.yaams.extensions.basemap.tiled.mapeditor.dialogs.NewMapDialog;
import de.yaams.maker.programm.YaFrame;

/**
 * Creates a new map.
 */
public class NewMapAction extends AbstractFileAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2210841048778741700L;

	public NewMapAction(MapEditor editor, SaveAction saveAction) {
		super(editor, saveAction, Resources.getString("action.map.new.name"), Resources.getString("action.map.new.tooltip"));

		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
	}

	@Override
	protected void doPerformAction() {
		NewMapDialog nmd = new NewMapDialog(YaFrame.get());
		Map newMap = nmd.create();
		if (newMap != null) {
			editor.setCurrentMap(newMap);
		}
	}
}
