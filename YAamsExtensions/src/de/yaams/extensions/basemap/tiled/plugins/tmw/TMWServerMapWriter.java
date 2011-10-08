/*
 * The Mana World Plugin for Tiled, (c) 2004-2006
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * Adam Turk <aturk@biggeruniverse.com> Bjorn Lindeijer <bjorn@lindeijer.nl>
 */

package de.yaams.extensions.basemap.tiled.plugins.tmw;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import de.yaams.extensions.basemap.tiled.core.Map;
import de.yaams.extensions.basemap.tiled.core.TileSet;
import de.yaams.extensions.basemap.tiled.io.MapWriter;
import de.yaams.maker.helper.Log;

/**
 * An exporter for TMW server map files, used to determine where a character can
 * walk. The format is very simple:
 * 
 * <pre>
 *  short (width)
 *  short (height)
 *  char[] (data)
 * </pre>
 * 
 * @version $Id$
 */
public class TMWServerMapWriter implements MapWriter {
	private static final int FIRST_BYTE = 0x000000FF;

	/**
	 * Loads a map from a file.
	 * 
	 * @param filename
	 *            the filename of the map file
	 */
	@Override
	public void writeMap(Map map, File filename) throws Exception {
		writeMap(map, new FileOutputStream(filename));
	}

	/**
	 * Loads a tileset from a file.
	 * 
	 * @param filename
	 *            the filename of the tileset file
	 */
	@Override
	public void writeTileset(TileSet set, File filename) throws Exception {
		Log.ger.error("Tilesets are not supported!");
		Log.ger.error("(asked to write " + filename + ")");
	}

	@Override
	public void writeMap(Map map, OutputStream out) throws Exception {
		WLKWriter.writeMap(map, out);
	}

	@Override
	public void writeTileset(TileSet set, OutputStream out) throws Exception {
		System.out.println("Tilesets are not supported!");
	}

	/**
	 * @see de.yaams.extensions.basemap.tiled.io.PluggableMapIO#getFilter()
	 */
	@Override
	public String getFilter() throws Exception {
		return "*.wlk";
	}

	@Override
	public String getDescription() {
		return "+---------------------------------------------+\n" + "|    An exporter for The Mana World server    |\n"
				+ "|                  map files.                 |\n" + "|          (c) 2005 Bjorn Lindeijer           |\n"
				+ "|              bjorn@lindeijer.nl             |\n" + "+---------------------------------------------+";
	}

	@Override
	public String getPluginPackage() {
		return "The Mana World export plugin";
	}

	@Override
	public String getName() {
		return "The Mana World exporter";
	}

	@Override
	public boolean accept(File pathname) {
		try {
			String path = pathname.getCanonicalPath().toLowerCase();
			if (path.endsWith(".wlk")) {
				return true;
			}
		} catch (IOException e) {
		}
		return false;
	}
}
