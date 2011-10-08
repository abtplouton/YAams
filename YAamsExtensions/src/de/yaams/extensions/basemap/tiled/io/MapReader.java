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

package de.yaams.extensions.basemap.tiled.io;

import java.io.FileFilter;
import java.io.InputStream;

import de.yaams.extensions.basemap.tiled.core.Map;
import de.yaams.extensions.basemap.tiled.core.TileSet;
import de.yaams.maker.programm.project.Project;

/**
 * Used by Tiled to denote a plugin for reading maps. The map file can have any
 * format, as long as the MapReader implementor returns instances of {@link Map}
 * and {@link TileSet}.
 */
public interface MapReader extends PluggableMapIO, FileFilter {
	/**
	 * Loads a map from a file.
	 * 
	 * @param filename
	 *            the filename of the map file
	 * @return A {@link de.yaams.extensions.basemap.tiled.core.Map} instance
	 *         with the relevant data
	 * @throws Exception
	 */
	public Map readMap(Project p, int id) throws Exception;

	/**
	 * Loads a tileset from a file.
	 * 
	 * @param filename
	 *            the filename of the tileset file
	 * @return A {@link de.yaams.extensions.basemap.tiled.core.TileSet} instance
	 *         with the relevant data
	 * @throws Exception
	 */
	public TileSet readTileset(String filename) throws Exception;

	/**
	 * Overload this to load a map from an already opened stream. Useful for
	 * maps which are part of a larger binary dataset
	 * 
	 * @param in
	 * @return A {@link de.yaams.extensions.basemap.tiled.core.Map} object with
	 *         the relevant data
	 * @throws Exception
	 */
	public Map readMap(InputStream in) throws Exception;

	/**
	 * Overload this to load a tileset from an open stream.
	 * 
	 * @param in
	 * @return A (@link tiled.core.TileSet} instance
	 * @throws Exception
	 */
	public TileSet readTileset(InputStream in) throws Exception;
}
