/**
 * 
 */
package de.yaams.extensions.rgssproject.database;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jruby.RubyArray;
import org.jruby.RubyFixnum;
import org.jruby.RubyHash;
import org.jruby.RubyObject;
import org.jruby.runtime.builtin.IRubyObject;

import de.yaams.extensions.rgssproject.RGSSProjectHelper;
import de.yaams.extensions.rgssproject.RTP;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.ress.RessRess;

/**
 * @author abt
 * 
 */
public class RGSS1Load {

	/**
	 * Helpervars to load
	 */
	public static String sFile;
	public static Object sSave;

	/**
	 * Get the maps name
	 * 
	 * @param p
	 * @param id
	 * @return
	 */
	private static RubyHash getMapsName(Project p) {
		return (RubyHash) loadFile(p, new File(getDataFile(p), "MapInfos.rxdata"));
	}

	/**
	 * Get the map folder
	 * 
	 * @param p
	 * @param id
	 * @return
	 */
	public static File getMapFile(Project p, int id) {
		return new File(getDataFile(p), String.format("Map%03d.rxdata", id));
	}

	/**
	 * Get the data dir
	 * 
	 * @param p
	 * @return
	 */
	public static File getDataFile(Project p) {
		return new File(p.getPath(), "Data");
	}

	/**
	 * Helpermethod to load a map
	 * 
	 * @param p
	 * @param id
	 * @return
	 */
	public static RubyObject loadMapData(Project p, int id) {
		// Load Map
		RubyObject map = (RubyObject) loadFile(p, getMapFile(p, id));
		map.setInstanceVariable("@name", ((RubyObject) getMapsName(p).get(id)).getInstanceVariable("@name"));
		map.setInstanceVariable("@id", RubyFixnum.newFixnum(map.getRuntime(), id));

		return map;
	}

	/**
	 * Helpermethod to load a map
	 * 
	 * @param p
	 * @param id
	 * @return
	 */
	// public static Object load(Project p, File file) {
	// // Load Map
	// sFile = file.getAbsolutePath();
	// return
	// RGSSProjectHelper.getInterpreter(p).interpretInternFile(RGSS1Load.class,
	// "R1load.rb");
	// }

	/**
	 * Helpermethod to load a tileset
	 * 
	 * @param p
	 * @param id
	 * @return
	 */
	public static ArrayList<BufferedImage> loadAutotilesAsImage(Project p, int id) {

		ArrayList<BufferedImage> b = new ArrayList<BufferedImage>();

		// load tileset
		RubyObject ts = RGSS1Helper.get(p, Type.TILESET).get(id).getObject();

		// load images

		// add autotiles
		List<?> autotiles = new ArrayList<Object>();
		autotiles.add(null);
		autotiles.addAll(((RubyArray) ts.getInstanceVariable("@autotile_names")).getList());
		for (Object autotile : autotiles) {
			// empty?
			if (autotile == null || autotile.toString().length() == 0) {
				for (int i = 0; i < 48; i++) {
					b.add(null);
				}

				continue;
			}

			// load image
			BufferedImage base = RessRess.getGraphic(p, RTP.AUTOTILE, autotile.toString());
			for (int i = 0; i < 48; i++) {
				b.add(getAutotile(i, base));
			}

		}

		return b;
	}

	/**
	 * Helpermethod to load a tileset
	 * 
	 * @param p
	 * @param id
	 * @return
	 */
	public static ArrayList<BufferedImage> loadTilesetAsImage(Project p, int id) {

		ArrayList<BufferedImage> b = new ArrayList<BufferedImage>();

		// load tileset
		RubyObject ts = RGSS1Helper.get(p, Type.TILESET).get(id).getObject();

		// load images

		// has tileset?
		String tilename = ts.getInstanceVariable("@tileset_name").asJavaString();
		if (tilename == null || tilename.length() == 0) {
			return b;
		}

		// add normal tileset
		BufferedImage base = RessRess.getGraphic(p, RTP.TILESET, tilename);

		for (int y = 0, r = base.getHeight() / 32; y < r; y++) {
			for (int x = 0; x < 8; x++) {
				b.add(base.getSubimage(x * 32, y * 32, 32, 32));
			}
		}
		return b;
	}

	/**
	 * Create Autotile
	 * 
	 * @param tileset
	 * @param id
	 * @param base
	 */
	private static BufferedImage getAutotile(int id, BufferedImage base) {
		int Autotiles[][][] = {
				{ { 27, 28, 33, 34 }, { 5, 28, 33, 34 }, { 27, 6, 33, 34 }, { 5, 6, 33, 34 }, { 27, 28, 33, 12 }, { 5, 28, 33, 12 },
						{ 27, 6, 33, 12 }, { 5, 6, 33, 12 } },
				{ { 27, 28, 11, 34 }, { 5, 28, 11, 34 }, { 27, 6, 11, 34 }, { 5, 6, 11, 34 }, { 27, 28, 11, 12 }, { 5, 28, 11, 12 },
						{ 27, 6, 11, 12 }, { 5, 6, 11, 12 } },
				{ { 25, 26, 31, 32 }, { 25, 6, 31, 32 }, { 25, 26, 31, 12 }, { 25, 6, 31, 12 }, { 15, 16, 21, 22 }, { 15, 16, 21, 12 },
						{ 15, 16, 11, 22 }, { 15, 16, 11, 12 } },
				{ { 29, 30, 35, 36 }, { 29, 30, 11, 36 }, { 5, 30, 35, 36 }, { 5, 30, 11, 36 }, { 39, 40, 45, 46 }, { 5, 40, 45, 46 },
						{ 39, 6, 45, 46 }, { 5, 6, 45, 46 } },
				{ { 25, 30, 31, 36 }, { 15, 16, 45, 46 }, { 13, 14, 19, 20 }, { 13, 14, 19, 12 }, { 17, 18, 23, 24 }, { 17, 18, 11, 24 },
						{ 41, 42, 47, 48 }, { 5, 42, 47, 48 } },
				{ { 37, 38, 43, 44 }, { 37, 6, 43, 44 }, { 13, 18, 19, 24 }, { 13, 14, 43, 44 }, { 37, 42, 43, 48 }, { 17, 18, 47, 48 },
						{ 13, 18, 43, 48 }, { 1, 2, 7, 8 } } };

		// build tiles
		BufferedImage b = new BufferedImage(32, 32, BufferedImage.TYPE_4BYTE_ABGR);

		// Collects Auto-Tile Tile Layout
		int tiles[] = Autotiles[id / 8][id % 8];
		// Draws Auto-Tile Rects
		for (int i = 0; i < 4; i++) {
			int tile_position = tiles[i] - 1;
			// src_rect = Rect.new(tile_position % 6 * 16 + frame_id,
			// tile_position / 6 * 16, 16, 16);
			int dx = i % 2 * 16, dy = i / 2 * 16;
			int sx = tile_position % 6 * 16, sy = tile_position / 6 * 16;
			b.getGraphics().drawImage(base, dx, dy, dx + 16, dy + 16, sx, sy, sx + 16, sy + 16, null);
			// bitmap.blt(i % 2 * 16, i / 2 * 16, autotile, src_rect);
		}

		// set tile
		return b;
	}

	/**
	 * Load the file & array
	 */
	public static ArrayList<RubyObject> loadDataFileAsArray(Project project, final String name) {

		// open code
		final Object[] objects = ((RubyArray) loadFile(project, new File(RGSS1Load.getDataFile(project), name + ".rxdata"))).toArray();

		// convert code
		ArrayList<RubyObject> elements = new ArrayList<RubyObject>();
		for (Object o : objects) {
			final RubyObject r = (RubyObject) o;
			elements.add(r);
		}
		return elements;
	}

	/**
	 * Load the file & array
	 */
	public static Object loadFile(Project p, File file) {
		try {
			// open code
			return RGSSProjectHelper.getInterpreter(p).runScriptlet("return load_dataE('" + file.getAbsolutePath() + "');");
			// open code
			// sFile = file.getAbsolutePath();
			// return RBRunTime.interpretInternFile(RGSS1Load.class,
			// "R1load.rb");

		} catch (Throwable t) {
			YEx.warn("Can not load " + file, t);
			return null;
		}
	}

	/**
	 * Save a loaded map an remove the special attributs from the clone
	 * 
	 * @param project
	 * @param map
	 */
	public static void saveMapData(Project project, RubyObject map) {
		// create filename
		File file = getMapFile(project, (Integer) map.getInstanceVariable("@id").toJava(Integer.class));
		// modi map
		RubyObject sMap = (RubyObject) map.rbClone();
		// remove attributs
		sMap.removeInstanceVariable("@name");
		sMap.removeInstanceVariable("@id");
		// save
		saveFile(project, file, sMap);
	}

	/**
	 * Save a ruby object with marshal in a file
	 * 
	 * @param project
	 * @param file
	 * @param obj
	 */
	public static void saveFile(Project p, File file, IRubyObject obj) {
		try {
			sFile = file.getAbsolutePath();
			sSave = obj;

			// open code
			RGSSProjectHelper.getInterpreter(p).interpretInternFile(RGSS1Load.class, "R1save.rb");
		} catch (Throwable t) {
			try {
				// recover old version
				RGSSProjectHelper.getInterpreter(p).interpretInternFile(RGSS1Load.class, "R1saveRecover.rb");

				YEx.info("Error while saving " + file + ". Old version was recovered. Nothing was saved.", t);
			} catch (Throwable t2) {
				YEx.info("Error while saving " + file + ". Recovering failed. File is broken.", t2);
			}

		}

	}
}
