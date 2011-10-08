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

package de.yaams.extensions.rgssproject.map.rxdata;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import org.jruby.RubyArray;
import org.jruby.RubyObject;
import org.jruby.runtime.builtin.IRubyObject;

import de.yaams.extensions.basemap.tiled.core.Map;
import de.yaams.extensions.basemap.tiled.core.Tile;
import de.yaams.extensions.basemap.tiled.core.TileLayer;
import de.yaams.extensions.basemap.tiled.core.TileSet;
import de.yaams.extensions.basemap.tiled.io.MapReader;
import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.RGSS1Load;
import de.yaams.maker.helper.Log;
import de.yaams.maker.programm.project.Project;

/**
 * @version $Id$
 */
public class RXDataReader implements MapReader {

	/**
	 * Helperparameter for rb script load/save
	 */
	public static Object data, data2;

	protected Project project;

	/**
	 * Loads a map from a file.
	 * 
	 * @param filename
	 *            the filename of the map file
	 */
	@Override
	public Map readMap(Project p, int id) throws Exception {

		// load basics
		project = p;

		// load rgss
		// RBRunTime.loadRGSS1();

		// load map
		final RubyObject o = RGSS1Helper.get(p, Type.MAP).get(id).getObject();

		// build map
		final Map m = new Map(RubyHelper.toInt(o, "@width"), RubyHelper.toInt(o, "@height"));
		m.setFilename(RGSS1Load.getMapFile(project, id));
		m.setProject(project);
		m.setMid(id);

		TileSet autotiles = buildAutoTileset(RubyHelper.toInt(o, "@tileset_id"));
		m.addTileset(autotiles);

		TileSet tileset = buildTileset(RubyHelper.toInt(o, "@tileset_id"));
		m.addTileset(tileset);
		m.setTileHeight(32);
		m.setTileWidth(32);
		// tileset.

		// def [](x,y=0,z=0)
		// @data[x+y*@xsize+z*@xsize*@ysize]

		// build layers
		// load map data
		IRubyObject[] data = ((RubyArray) ((RubyObject) o.getInstanceVariable("@data")).getInstanceVariable("@data")).toJavaArray();
		final String[] names = { "Down", "Middle", "Up" };
		int z = 0;

		for (final String a : names) {
			final TileLayer l = (TileLayer) m.addLayer();
			l.setName(a);
			// set tiles
			for (int x = 0, u = m.getWidth(); x < u; x++) {
				for (int y = 0, v = m.getHeight(); y < v; y++) {

					int tid = Integer.valueOf(data[x + y * u + z * u * v].toString());
					// get id
					if (tid < 384) {
						l.setTileAt(x, y, autotiles.getTile(tid));
					} else {
						// t.setId(((Long) data[x + y * u + z * u * v]);
						l.setTileAt(x, y, tileset.getTile(tid - 384));
					}
				}
			}
			z++;
		}
		return m;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	protected TileSet buildAutoTileset(int id) throws IOException {

		// build tileset
		TileSet tileset = new TileSet();

		ArrayList<BufferedImage> images = RGSS1Load.loadAutotilesAsImage(project, Integer.valueOf(id));

		// add all
		for (int i = 0, l = images.size(); i < l; i++) {
			Tile t = new Tile();
			t.setId(i);
			if (images.get(i) != null) {
				t.setImage(images.get(i));
			}
			t.setTileSet(tileset);
			tileset.addTile(t);
		}

		tileset.setName("AutoTiles");

		return tileset;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	protected TileSet buildTileset(int id) throws IOException {
		// load tileset
		RubyObject o = RGSS1Helper.get(project, Type.TILESET).get(id).getObject();

		// build tileset
		TileSet tileset = new TileSet();

		ArrayList<BufferedImage> images = RGSS1Load.loadTilesetAsImage(project, Integer.valueOf(id));

		// add all
		for (int i = 0, l = images.size(); i < l; i++) {
			Tile t = new Tile();
			t.setId(i);
			if (images.get(i) != null) {
				t.setImage(images.get(i));
			}
			t.setTileSet(tileset);
			tileset.addTile(t);
		}

		tileset.setName(o.getInstanceVariable("@name").toString());

		return tileset;
	}

	@Override
	public Map readMap(final InputStream in) throws Exception {
		Log.ger.error("Read Map Stream is not supported!");
		return null;
	}

	/**
	 * Loads a tileset from a file.
	 * 
	 * @param filename
	 *            the filename of the tileset file
	 */
	@Override
	public TileSet readTileset(final String filename) throws Exception {
		Log.ger.error("Tilesets aren't supported!");
		return null;
	}

	@Override
	public TileSet readTileset(final InputStream in) {
		Log.ger.error("Tilesets aren't supported!");
		return null;
	}

	/**
	 * @see de.yaams.extensions.basemap.tiled.io.PluggableMapIO#getFilter()
	 */
	@Override
	public String getFilter() throws Exception {
		return "*.rxdata";
	}

	@Override
	public String getPluginPackage() {
		return "RXDATA input plugin";
	}

	@Override
	public String getDescription() {
		return "+---------------------------------------------+\n" + "|      An experimental reader for Mappy       |\n"
				+ "|                 FMAP v0.36                  |\n" + "|            (c) Adam Turk 2004               |\n"
				+ "|          aturk@biggeruniverse.com           |\n" + "|                                             |\n"
				+ "| Currently unsupported:                      |\n" + "|  * Animated tiles                           |\n"
				+ "|  * The ATHR block                           |\n" + "|  * Collision bits on BLKSTR structures      |\n"
				+ "|  * bitdepths other than 16bit               |\n" + "|  * object layers                            |\n"
				+ "+---------------------------------------------+";
	}

	@Override
	public String getName() {
		return "RXDATA Reader";
	}

	@Override
	public boolean accept(final File pathname) {
		final String path = pathname.getAbsolutePath().toLowerCase(Locale.ENGLISH);
		if (path.endsWith(".xml")) {
			return true;
		}
		return false;
	}
}
