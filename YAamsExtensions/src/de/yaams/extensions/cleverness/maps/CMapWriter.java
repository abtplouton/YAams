/**
 * 
 */
package de.yaams.extensions.cleverness.maps;

// TutorialMapWriter.java

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import de.yaams.extensions.basemap.tiled.core.Map;
import de.yaams.extensions.basemap.tiled.core.Tile;
import de.yaams.extensions.basemap.tiled.core.TileLayer;
import de.yaams.extensions.basemap.tiled.core.TileSet;
import de.yaams.extensions.basemap.tiled.io.MapWriter;
import de.yaams.maker.helper.FileHelper;
import de.yaams.maker.helper.Log;

public class CMapWriter implements MapWriter {

	/**
	 * Saves a map to a file.
	 * 
	 * @param map
	 *            the map to be saved
	 * @param filename
	 *            the filename of the map file
	 * @throws java.io.IOException
	 */
	@Override
	public void writeMap(final Map map, final File filename) throws IOException {

		// convert to cmap
		CMap cm = new CMap();
		cm.setWidth(map.getWidth());
		cm.setHeight(map.getHeight());
		cm.setLayer(map.getLayerVector().size());

		// create data
		for (int z = 0, n = map.getLayerVector().size(); z < n; z++) {
			TileLayer layer = (TileLayer) map.getLayer(z);
			for (int x = 0, l = layer.getWidth(); x < l; x++) {
				for (int y = 0, m = layer.getHeight(); y < m; y++) {
					Tile t = layer.getTileAt(x, y);

					// is empty?
					if (t == null) {
						cm.set(x, y, z, -1);
						continue;
					}

					// set
					cm.set(x, y, z, t.getId());
				}

			}
		}

		// save map
		FileHelper.saveXML(filename, cm);
	}

	/**
	 * Writes a map to an already opened stream. Useful for maps which are part
	 * of a larger binary dataset
	 * 
	 * @param map
	 *            the Map to be written
	 * @param out
	 *            the output stream to write to
	 * @throws java.io.IOException
	 */
	@Override
	public void writeMap(final Map map, final OutputStream out) throws IOException {
		Log.ger.error("Write Map Stream is not supported!");
		// blank function for now
	}

	/**
	 * Overload this to write a tileset to an open stream. Tilesets are not
	 * supported by this writer.
	 * 
	 * @param set
	 * @param out
	 * @throws Exception
	 */
	@Override
	public void writeTileset(final TileSet set, final OutputStream out) throws Exception {
		Log.ger.error("Tilesets are not supported!");
	}

	/**
	 * Saves a tileset to a file. Tilesets are not supported by this writer.
	 * 
	 * @param set
	 * @param filename
	 *            the filename of the tileset file
	 * @throws Exception
	 */
	@Override
	public void writeTileset(final TileSet set, final File filename) throws Exception {
		Log.ger.error("Tilesets are not supported!");
		Log.ger.error("(asked to write " + filename + ")");
	}

	/**
	 * Lists supported file extensions. This function is used by the editor to
	 * find the plugin to use for a specific file extension.
	 * 
	 * @return a comma delimited string of supported file extensions
	 * @throws Exception
	 */
	@Override
	public String getFilter() throws Exception {
		return "*.cmap";
	}

	/**
	 * Returns a short description of the plugin, or the plugin name. This
	 * string is displayed in the list of loaded plugins under the Help menu in
	 * Tiled.
	 * 
	 * @return a short name or description
	 */
	@Override
	public String getName() {
		return "Cleverness Maps";
	}

	/**
	 * Returns a long description (no limit) that details the plugin's
	 * capabilities, author, contact info, etc.
	 * 
	 * @return a long description of the plugin
	 */
	@Override
	public String getDescription() {
		return "This is a simple plugin that writes a blank file.\n" + "You may distribute this plugin under the terms of the GNU GPLv2.\n";
	}

	/**
	 * Returns the base Java package string for the plugin
	 * 
	 * @return String the base package of the plugin
	 */
	@Override
	public String getPluginPackage() {
		return "Tiled Tutorial Writer Plugin";
	}

	/**
	 * java.io.FileFilter Interface
	 */
	@Override
	public boolean accept(final File pathname) {
		return pathname.getAbsolutePath().endsWith(".cmap");
	}
}
