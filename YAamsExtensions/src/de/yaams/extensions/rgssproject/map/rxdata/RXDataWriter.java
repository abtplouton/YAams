/**
 * 
 */
package de.yaams.extensions.rgssproject.map.rxdata;

// TutorialMapWriter.java

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.jruby.RubyObject;

import de.yaams.extensions.basemap.tiled.core.Map;
import de.yaams.extensions.basemap.tiled.core.Tile;
import de.yaams.extensions.basemap.tiled.core.TileLayer;
import de.yaams.extensions.basemap.tiled.core.TileSet;
import de.yaams.extensions.basemap.tiled.io.MapWriter;
import de.yaams.extensions.rgssproject.RGSSProjectHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.Log;
import de.yaams.maker.helper.gui.YDialog;

public class RXDataWriter implements MapWriter {

	/**
	 * Helperparameter for rb script load/save
	 */
	public static Object file, data;
	public static RubyObject rmap;

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
		Integer d[][][] = new Integer[3][map.getWidth()][map.getHeight()];

		// check size
		if (map.getLayerVector().size() > 3
				&& !YDialog.showErrors(I18N.t("Map Limits erreicht"), I18N
						.t("Auf Grund der Kompitabilitäseinschränkungen vom RPG Maker XP, werden nur 3 Ebenen unterstützt. <br>"
								+ "Wenn das Speichern fortgesetzt wird, gehen alle zusätzlichen Ebenen verloren. <br>"
								+ "Speichern wirklich fortsetzen?"))) {
			return;
		}

		// create data
		for (int z = 0, n = map.getLayerVector().size() > 3 ? 3 : map.getLayerVector().size(); z < n; z++) {
			TileLayer layer = (TileLayer) map.getLayer(z);
			for (int x = 0, l = layer.getWidth(); x < l; x++) {
				for (int y = 0, m = layer.getHeight(); y < m; y++) {
					Tile t = layer.getTileAt(x, y);

					// is set?
					if (t == null) {
						d[z][x][y] = 0;
						continue;
					}

					// check tileset
					if (t.getTileSet().getName().equals("AutoTiles")) {
						d[z][x][y] = t.getId();

					} else {

						d[z][x][y] = t.getId() + 384;
					}
				}

			}
		}

		// save map
		file = filename;
		data = d;

		rmap = RGSS1Helper.get(map.getProject(), Type.MAP).get(map.getMid()).getObject();
		RGSSProjectHelper.getInterpreter(map.getProject()).interpretInternFile(RXDataWriter.class, "saveMap.rb");
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
		return "*.rxdata";
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
		return "RXData Writer";
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
		try {
			final String path = pathname.getCanonicalPath();
			if (path.endsWith(".rxdata")) {
				return true;
			}
		} catch (final IOException e) {
		}
		return false;
	}
}
