/*
 * Mappy Plugin for Tiled, (c) 2004-2006
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * Adam Turk <aturk@biggeruniverse.com> Bjorn Lindeijer <bjorn@lindeijer.nl>
 */

package de.yaams.extensions.diamant.maps;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import de.yaams.extensions.basemap.tiled.core.Map;
import de.yaams.extensions.basemap.tiled.core.Tile;
import de.yaams.extensions.basemap.tiled.core.TileLayer;
import de.yaams.extensions.basemap.tiled.core.TileSet;
import de.yaams.extensions.diamant.tileset.CTileset;
import de.yaams.maker.helper.FileHelper;
import de.yaams.maker.programm.project.Project;

/**
 * @version $Id$
 */
public class CMapReader {

	/**
	 * Loads a map from a file.
	 * 
	 * @param filename
	 *            the filename of the map file
	 */
	public Map readMap(Project p, File path, int id, String type) throws Exception {

		// load map
		CMapInfo info = (CMapInfo) p.getObjects().get(type).getObjects().get(id);
		CMap map = (CMap) FileHelper.loadXML(path);
		if (map == null) {
			map = new CMap();
		}

		// build map
		final Map m = new Map(map.getWidth(), map.getHeight());
		m.setFilename(path);
		m.setProject(p);
		m.setMid(0);

		TileSet tileset = buildTileset(info.getTileset(), p);
		m.addTileset(tileset);
		m.setTileHeight(32);
		m.setTileWidth(32);
		// tileset.

		// def [](x,y=0,z=0)
		// @data[x+y*@xsize+z*@xsize*@ysize]

		// build layers
		// load map data

		for (int z = 0, n = map.getLayer(); z < n; z++) {
			final TileLayer l = (TileLayer) m.addLayer();
			l.setName(z == 0 ? "Base" : z == 1 ? "Objects" : Integer.toString(z));
			// set tiles
			for (int x = 0, u = m.getWidth(); x < u; x++) {
				for (int y = 0, v = m.getHeight(); y < v; y++) {
					if (map.get(x, y, z) >= 0) {
						l.setTileAt(x, y, tileset.getTile(map.get(x, y, z)));
					}
				}
			}
		}
		return m;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	protected TileSet buildTileset(int id, Project p) throws IOException {
		// load tileset
		CTileset ct = (CTileset) p.getObjects().get("tileset").getObjects().get(id);

		// build tileset
		TileSet tileset = new TileSet();

		BufferedImage b = ct.getTilesetGraphic(p);

		// add tileset
		// add tiles
		for (int i = 0, l = b.getWidth() / 32 * (b.getHeight() / 32); i < l; i++) {
			Tile t = new Tile();
			t.setId(i);
			t.setImage(b.getSubimage(i % (b.getWidth() / 32) * 32, i / (b.getWidth() / 32) * 32, 32, 32));
			t.setTileSet(tileset);
			tileset.addTile(t);
		}

		tileset.setName(ct.getTitle());

		return tileset;
	}
}
