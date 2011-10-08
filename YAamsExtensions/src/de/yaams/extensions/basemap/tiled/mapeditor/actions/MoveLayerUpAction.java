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
 * Swaps the currently selected layer with the layer above.
 * 
 * @version $Id$
 */
public class MoveLayerUpAction extends AbstractLayerAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1154785766855971629L;

	public MoveLayerUpAction(MapEditor editor) {
		super(editor, Resources.getString("action.layer.moveup.name"), Resources.getString("action.layer.moveup.tooltip"), Resources
				.getIcon("gnome-up.png"));

		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("shift PAGE_UP"));
	}

	@Override
	protected void doPerformAction() {
		Map map = editor.getCurrentMap();
		int layerIndex = editor.getCurrentLayerIndex();
		int totalLayers = map.getTotalLayers();

		if (layerIndex < totalLayers - 1) {
			map.swapLayerUp(layerIndex);
			editor.setCurrentLayer(layerIndex + 1);
		}
	}
}
