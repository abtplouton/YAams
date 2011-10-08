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

package de.yaams.extensions.basemap.tiled.mapeditor.util;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import de.yaams.extensions.basemap.tiled.core.Map;
import de.yaams.extensions.basemap.tiled.core.MapChangeListener;
import de.yaams.extensions.basemap.tiled.core.MapChangedEvent;
import de.yaams.extensions.basemap.tiled.core.MapLayer;
import de.yaams.extensions.basemap.tiled.core.Tile;
import de.yaams.extensions.basemap.tiled.core.TileLayer;
import de.yaams.extensions.basemap.tiled.core.TileSet;
import de.yaams.extensions.basemap.tiled.core.TilesetChangeListener;
import de.yaams.extensions.basemap.tiled.core.TilesetChangedEvent;
import de.yaams.extensions.basemap.tiled.mapeditor.Resources;

public class TilesetTableModel extends AbstractTableModel implements MapChangeListener, TilesetChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 546262793242745218L;
	private Map map;
	private static final String[] columnNames = { Resources.getString("dialog.tilesetmanager.table.name"),
			Resources.getString("dialog.tilesetmanager.table.source") };

	private static final String EMBEDDED = Resources.getString("dialog.tilesetmanager.embedded");

	public TilesetTableModel(Map map) {
		this.map = map;

		for (TileSet tileset : map.getTilesets()) {
			tileset.addTilesetChangeListener(this);
		}
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public int getRowCount() {
		if (map != null) {
			return map.getTilesets().size();
		} else {
			return 0;
		}
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		Vector<?> tilesets = map.getTilesets();
		if (row >= 0 && row < tilesets.size()) {
			TileSet tileset = (TileSet) tilesets.get(row);
			if (col == 0) {
				return tileset.getName();
			} else {
				String ret = tileset.getSource();

				if (ret == null) {
					ret = EMBEDDED;
				}

				return ret;
			}
		} else {
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return col == 0;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		if (col != 0)
			return;

		Vector<?> tilesets = map.getTilesets();
		if (row >= 0 && row < tilesets.size()) {
			TileSet tileset = (TileSet) tilesets.get(row);
			if (col == 0) {
				tileset.setName(value.toString());
			}
			fireTableCellUpdated(row, col);
		}
	}

	private int checkSetUsage(TileSet set) {
		int used = 0;
		Iterator<?> tileIterator = set.iterator();

		while (tileIterator.hasNext()) {
			Tile tile = (Tile) tileIterator.next();
			Iterator<?> itr = map.getLayers();

			while (itr.hasNext()) {
				MapLayer ml = (MapLayer) itr.next();

				if (ml instanceof TileLayer) {
					if (((TileLayer) ml).isUsed(tile)) {
						used++;
						break;
					}
				}
			}
		}

		return used;
	}

	@Override
	public void mapChanged(MapChangedEvent event) {}

	@Override
	public void tilesetAdded(MapChangedEvent event, TileSet tileset) {
		int index = map.getTilesets().indexOf(tileset);

		if (index == -1)
			return;

		tileset.addTilesetChangeListener(this);

		fireTableRowsInserted(index, index);
	}

	@Override
	public void tilesetRemoved(MapChangedEvent event, int index) {
		fireTableRowsDeleted(index - 1, index);
	}

	@Override
	public void tilesetsSwapped(MapChangedEvent event, int index0, int index1) {
		fireTableRowsUpdated(index0, index1);
	}

	@Override
	public void tilesetChanged(TilesetChangedEvent event) {}

	@Override
	public void nameChanged(TilesetChangedEvent event, String oldName, String newName) {
		int index = map.getTilesets().indexOf(event.getTileset());

		if (index == -1)
			return;

		fireTableCellUpdated(index, 0);
	}

	@Override
	public void sourceChanged(TilesetChangedEvent event, String oldSource, String newSource) {
		int index = map.getTilesets().indexOf(event.getTileset());

		if (index == -1)
			return;

		fireTableCellUpdated(index, 1);
	}

	public void clearListeners() {
		for (Iterator<?> it = map.getTilesets().iterator(); it.hasNext();) {
			((TileSet) it.next()).removeTilesetChangeListener(this);
		}
	}
}
