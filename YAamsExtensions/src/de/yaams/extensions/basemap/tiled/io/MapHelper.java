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

import java.io.File;
import java.util.HashMap;

import javax.swing.JFileChooser;

import de.yaams.extensions.basemap.tiled.core.Map;
import de.yaams.extensions.basemap.tiled.core.TileSet;
import de.yaams.extensions.basemap.tiled.mapeditor.util.TiledFileFilter;
import de.yaams.extensions.rgssproject.database.RGSS1Load;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.JavaHelper;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.gui.YDialog;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.programm.environment.YLevel;
import de.yaams.maker.programm.project.Project;

/**
 * A handler for saving and loading maps.
 */
public class MapHelper {

	public static final String SAVE = "basemapeditor.save";
	public static final String FILEXT = "basemapeditor.getFileExtension";

	/**
	 * Saves the current map. Use the extension (.xxx) of the filename to
	 * determine the plugin to use when writing the file. Throws an exception
	 * when the extension is not supported by either the TMX writer or a plugin.
	 * (Unlikely)
	 * 
	 * @param filename
	 *            filename to save the current map to
	 * @param currentMap
	 *            {@link de.yaams.extensions.basemap.tiled.core.Map} instance to
	 *            save to the file
	 * @see MapWriter#writeMap(Map, String)
	 * @exception Exception
	 */
	public static void saveMap(final Map currentMap, final File filename) throws Exception {
		// create
		HashMap<String, Object> data = JavaHelper.createHashStringObj("map", currentMap, "file", filename);

		// ask
		ExtentionManagement.work(SAVE, data);

		// saved it?
		if (!data.containsKey("saved")) {
			YDialog.ok(I18N.t("Kann Map unter {0} nicht speichern.", filename.getAbsoluteFile()),
					YLevel.IS_DEVELOPER ? I18N.t("Flag saved fehlt.") : "", "disk_error");
		}
	}

	/**
	 * Saves a tileset. Use the extension (.xxx) of the filename to determine
	 * the plugin to use when writing the file. Throws an exception when the
	 * extension is not supported by either the TMX writer or a plugin.
	 * 
	 * @param filename
	 *            Filename to save the tileset to.
	 * @param set
	 *            The TileSet instance to save to the file
	 * @see MapWriter#writeTileset(TileSet, String)
	 * @exception Exception
	 */
	public static void saveTileset(final TileSet set, final String filename) throws Exception {
		throw new Exception("Unsupported tileset format");

	}

	/**
	 * Loads a map. Use the extension (.xxx) of the filename to determine the
	 * plugin to use when reading the file. Throws an exception when the
	 * extension is not supported by either the TMX writer or a plugin.
	 * 
	 * @param file
	 *            filename of map to load
	 * @return a new Map, loaded from the specified file by a plugin
	 * @throws Exception
	 * @see MapReader#readMap(String)
	 */
	public static Map load2Map(Project p, int id) throws Exception {
		Map ret = null;
		try {
			MapReader mr = null;
			// if (file.endsWith(".tmx") || file.endsWith(".tmx.gz")) {
			// // Override, so people can't overtake our format
			// mr = new XMLMapTransformer();
			// } else {
			// mr = (MapReader) pluginLoader.getReaderFor(p, id);
			// }

			if (mr != null) {
				ret = mr.readMap(p, id);
				ret.setProject(p);
				ret.setMid(id);
				ret.setFilename(RGSS1Load.getMapFile(p, id));
			} else {
				throw new Exception("Unsupported map format");
			}
		} catch (final Throwable t) {
			YEx.error("Can not load map " + id + " from project " + p, t);
		}

		return ret;
	}

	/**
	 * Loads a tileset. Use the extension (.xxx) of the filename to determine
	 * the plugin to use when reading the file. Throws an exception when the
	 * extension is not supported by either the TMX writer or a plugin.
	 * 
	 * @param file
	 *            filename of map to load
	 * @return A new TileSet, loaded from the specified file by a plugin
	 * @throws Exception
	 * @see MapReader#readTileset(String)
	 */
	public static TileSet loadTileset(final String file) throws Exception {
		throw new Exception("Unsupported tileset format");
	}

	/**
	 * @param chooser
	 */
	public static void addExtension(JFileChooser chooser) {

		// ask
		ExtentionManagement.work(FILEXT, JavaHelper.createHashStringObj("chooser", chooser));

		// add default
		chooser.addChoosableFileFilter(new TiledFileFilter(TiledFileFilter.FILTER_TSX));

	}
}
