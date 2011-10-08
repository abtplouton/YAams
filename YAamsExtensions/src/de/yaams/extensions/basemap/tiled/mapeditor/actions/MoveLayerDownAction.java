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

/**
 * Swaps the currently selected layer with the layer below.
 * 
 * @version $Id$
 */
public class MoveLayerDownAction extends AbstractLayerAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8765095609761701994L;

	public MoveLayerDownAction(MapEditor editor) {
		super(editor, Resources.getString("action.layer.movedown.name"), Resources.getString("action.layer.movedown.tooltip"), Resources
				.getIcon("gnome-down.png"));

		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("shift PAGE_DOWN"));
	}

	@Override
	protected void doPerformAction() {
		Map map = editor.getCurrentMap();
		int layerIndex = editor.getCurrentLayerIndex();

		if (layerIndex > 0) {
			map.swapLayerDown(layerIndex);
			editor.setCurrentLayer(layerIndex - 1);
		}
	}
}
