/**
 * 
 */
package de.yaams.extensions.basemap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFileChooser;

import de.yaams.extensions.basemap.tiled.core.Map;
import de.yaams.extensions.basemap.tiled.io.MapHelper;
import de.yaams.extensions.basemap.tiled.io.MapWriter;
import de.yaams.extensions.basemap.tiled.mapeditor.util.TiledFileFilter;
import de.yaams.extensions.basemap.tiled.plugins.json.JSONMapWriter;
import de.yaams.extensions.basemap.tiled.plugins.lua.LuaMapWriter;
import de.yaams.extensions.basemap.tiled.plugins.mappy.MappyMapWriter;
import de.yaams.extensions.basemap.tiled.plugins.tmw.TMWServerMapWriter;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.extensions.IExtension;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.gui.YMessagesDialog;
import de.yaams.maker.helper.gui.icons.IconCache;
import de.yaams.maker.programm.plugins.BasePlugin;

/**
 * @author Nebli
 * 
 */
public class BaseMapPlugin extends BasePlugin {

	private static ArrayList<MapWriter> plugs;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.plugins.core.BasePlugin#start()
	 */
	@Override
	public void start() {
		// add icon
		IconCache.addPNG(BaseMapPlugin.class, "map");

		// fill plugin array
		plugs = new ArrayList<MapWriter>();
		plugs.add(new JSONMapWriter());
		plugs.add(new LuaMapWriter());
		plugs.add(new MappyMapWriter());
		plugs.add(new TMWServerMapWriter());

		// register extenstions
		ExtentionManagement.add(MapHelper.SAVE, new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {

				File file = (File) objects.get("file");
				Map map = (Map) objects.get("map");

				// check all types
				for (MapWriter m : plugs) {
					if (objects.containsKey("saved")) {
						break;
					}
					if (m.accept(file)) {
						try {
							m.writeMap(map, file);
							objects.put("saved", true);
						} catch (Exception e) {
							YEx.info("Can not save " + map + " under " + file, e);
						}
					}
				}
			}
		});

		// register extenstions
		ExtentionManagement.add(MapHelper.FILEXT, new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {
				JFileChooser chooser = (JFileChooser) objects.get("chooser");

				// check all types
				for (MapWriter m : plugs) {
					try {
						chooser.addChoosableFileFilter(new TiledFileFilter(m.getFilter(), m.getName()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.plugins.core.BasePlugin#useable(de.yaams.maker
	 * .helper.gui.YMessagesDialog)
	 */
	@Override
	public boolean useable(YMessagesDialog md) {
		return isVersionInstall(null, 0.0053, -1, md);
	}

	/**
	 * @return the plugs
	 */
	public static ArrayList<MapWriter> getPlugs() {
		return plugs;
	}

}
