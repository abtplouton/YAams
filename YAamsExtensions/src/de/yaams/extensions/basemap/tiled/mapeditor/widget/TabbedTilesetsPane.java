/*
 * Tiled Map Editor, (c) 2004-2008
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * Adam Turk <aturk@biggeruniverse.com> Bjorn Lindeijer <bjorn@lindeijer.nl>
 * 
 * This class is based on TilesetChooserTabbedPane from Stendhal Map Editor by
 * Matthias Totz <mtotz@users.sourceforge.net>
 */

package de.yaams.extensions.basemap.tiled.mapeditor.widget;

import java.awt.Component;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.jidesoft.swing.JideTabbedPane;

import de.yaams.extensions.basemap.tiled.core.Map;
import de.yaams.extensions.basemap.tiled.core.MapChangeListener;
import de.yaams.extensions.basemap.tiled.core.MapChangedEvent;
import de.yaams.extensions.basemap.tiled.core.TileSet;
import de.yaams.extensions.basemap.tiled.core.TilesetChangeListener;
import de.yaams.extensions.basemap.tiled.core.TilesetChangedEvent;
import de.yaams.extensions.basemap.tiled.mapeditor.MapEditor;
import de.yaams.extensions.basemap.tiled.mapeditor.brush.CustomBrush;
import de.yaams.extensions.basemap.tiled.mapeditor.util.TileRegionSelectionEvent;
import de.yaams.extensions.basemap.tiled.mapeditor.util.TileSelectionEvent;
import de.yaams.extensions.basemap.tiled.mapeditor.util.TileSelectionListener;

/**
 * Shows one tab for each Tileset.
 * 
 * @version $Id$
 */
public class TabbedTilesetsPane extends JideTabbedPane implements TileSelectionListener {

	private static final long serialVersionUID = -1190594964935357694L;
	/**
	 * Map of tile sets to tile palette panels
	 */
	private final HashMap<TileSet, TilePalettePanel> tilePanels = new HashMap<TileSet, TilePalettePanel>();
	private final MyChangeListener listener = new MyChangeListener();
	private final MapEditor mapEditor;
	private Map map;

	/**
	 * Constructor.
	 * 
	 * @param mapEditor
	 *            reference to the MapEditor instance, used to change the
	 *            current tile and brush
	 */
	public TabbedTilesetsPane(MapEditor mapEditor) {
		this.mapEditor = mapEditor;
		setHideOneTab(true);
		setBorder(BorderFactory.createEmptyBorder());
	}

	/**
	 * Sets the tiles panes to the the ones from this map.
	 * 
	 * @param map
	 *            the map of which to display the tilesets
	 */
	public void setMap(Map map) {
		if (this.map != null) {
			this.map.removeMapChangeListener(listener);
		}

		if (map == null) {
			removeAll();
		} else {
			recreateTabs(map.getTilesets());
			map.addMapChangeListener(listener);
		}

		this.map = map;
	}

	/**
	 * Creates the panels for the tilesets.
	 * 
	 * @param tilesets
	 *            the list of tilesets to create panels for
	 */
	private void recreateTabs(List<TileSet> tilesets) {
		// Stop listening to the tile palette panels and their tilesets
		for (TilePalettePanel panel : tilePanels.values()) {
			panel.removeTileSelectionListener(this);
			panel.getTileset().removeTilesetChangeListener(listener);
		}
		tilePanels.clear();

		// Remove all tabs
		removeAll();

		if (tilesets != null) {
			// Add a new tab for each tileset of the map
			for (TileSet tileset : tilesets) {
				if (tileset != null) {
					addTabForTileset(tileset);
				}
			}
		}
	}

	/**
	 * Adds a tab with a {@link TilePalettePanel} for the given tileset.
	 * 
	 * @param tileset
	 *            the given tileset
	 */
	private void addTabForTileset(TileSet tileset) {
		tileset.addTilesetChangeListener(listener);
		TilePalettePanel tilePanel = new TilePalettePanel();
		tilePanel.setTileset(tileset);
		tilePanel.addTileSelectionListener(this);
		JScrollPane paletteScrollPane = new JScrollPane(tilePanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		addTab(tileset.getName(), paletteScrollPane);
		tilePanels.put(tileset, tilePanel);
	}

	/**
	 * Informs the editor of the new tile.
	 */
	@Override
	public void tileSelected(TileSelectionEvent e) {
		mapEditor.setCurrentTile(e.getTile());
	}

	/**
	 * Creates a stamp brush from the region contents and sets this as the
	 * current brush.
	 */
	@Override
	public void tileRegionSelected(TileRegionSelectionEvent e) {
		mapEditor.setBrush(new CustomBrush(e.getTileRegion()));
	}

	private class MyChangeListener implements MapChangeListener, TilesetChangeListener {
		@Override
		public void mapChanged(MapChangedEvent e) {}

		@Override
		public void tilesetAdded(MapChangedEvent e, TileSet tileset) {
			addTabForTileset(tileset);
		}

		@Override
		public void tilesetRemoved(MapChangedEvent e, int index) {
			JScrollPane scroll = (JScrollPane) getComponentAt(index);
			TilePalettePanel panel = (TilePalettePanel) scroll.getViewport().getView();
			TileSet set = panel.getTileset();
			panel.removeTileSelectionListener(TabbedTilesetsPane.this);
			set.removeTilesetChangeListener(listener);
			tilePanels.remove(set);
			removeTabAt(index);
		}

		@Override
		public void tilesetsSwapped(MapChangedEvent e, int index0, int index1) {
			int sIndex = getSelectedIndex();

			String title0 = getTitleAt(index0);
			String title1 = getTitleAt(index1);

			Component comp0 = getComponentAt(index0);
			Component comp1 = getComponentAt(index1);

			removeTabAt(index1);
			removeTabAt(index0);

			insertTab(title1, null, comp1, null, index0);
			insertTab(title0, null, comp0, null, index1);

			if (sIndex == index0) {
				sIndex = index1;
			} else if (sIndex == index1) {
				sIndex = index0;
			}

			setSelectedIndex(sIndex);
		}

		@Override
		public void tilesetChanged(TilesetChangedEvent event) {}

		@Override
		public void nameChanged(TilesetChangedEvent event, String oldName, String newName) {
			TileSet set = event.getTileset();
			int index = map.getTilesets().indexOf(set);

			setTitleAt(index, newName);
		}

		@Override
		public void sourceChanged(TilesetChangedEvent event, String oldSource, String newSource) {}
	}
}
