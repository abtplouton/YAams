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

import java.text.MessageFormat;

import de.yaams.extensions.basemap.tiled.core.Map;
import de.yaams.extensions.basemap.tiled.core.MapLayer;
import de.yaams.extensions.basemap.tiled.mapeditor.MapEditor;
import de.yaams.extensions.basemap.tiled.mapeditor.Resources;

/**
 * Clones the current layer, adds the clone to the map at the top of the layer
 * stack and then selects it.
 * 
 * @version $Id$
 */
public class CloneLayerAction extends AbstractLayerAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1781772335605047859L;

	public CloneLayerAction(MapEditor editor) {
		super(editor, Resources.getString("action.layer.duplicate.name"), Resources.getString("action.layer.duplicate.tooltip"), Resources
				.getIcon("gimp-duplicate-16.png"));
	}

	@Override
	protected void doPerformAction() {
		MapLayer currentLayer = editor.getCurrentLayer();
		Map currentMap = editor.getCurrentMap();

		if (currentLayer != null) {
			try {
				MapLayer clone = (MapLayer) currentLayer.clone();
				String newName = Resources.getString("action.layer.duplicate.newlayer.name");
				clone.setName(MessageFormat.format(newName, new Object[] { clone.getName() }));
				currentMap.addLayer(clone);
				editor.setCurrentLayer(currentMap.getTotalLayers() - 1);
			} catch (CloneNotSupportedException ex) {
				ex.printStackTrace();
			}
		}
	}
}
