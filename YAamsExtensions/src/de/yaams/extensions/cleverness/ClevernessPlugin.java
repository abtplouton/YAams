/**
 * 
 */
package de.yaams.extensions.cleverness;

import java.util.HashMap;

import de.yaams.extensions.basemap.BaseMapPlugin;
import de.yaams.extensions.cleverness.maps.CMapWriter;
import de.yaams.extensions.cleverness.maps.MapsObjManager;
import de.yaams.extensions.cleverness.tileset.TilesetObjManager;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.extensions.IExtension;
import de.yaams.maker.helper.gui.YMessagesDialog;
import de.yaams.maker.helper.gui.icons.IconCache;
import de.yaams.maker.programm.YaFrame;
import de.yaams.maker.programm.plugins.core.BasePlugin;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.ProjectManagement;

/**
 * @author Nebli
 * 
 */
public class ClevernessPlugin extends BasePlugin {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.core.helper.plugins.BasePlugin#useable(de.yaams.core.helper.
	 * gui.YMessagesDialog, int)
	 */
	@Override
	public boolean useable(YMessagesDialog md) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.core.helper.plugins.BasePlugin#start()
	 */
	@Override
	public void start() {

		// add icon
		IconCache.addPNG(ClevernessPlugin.class, "cleverness", "map", "tileset");

		// add gametype
		ProjectManagement.registerType(new ClevernessProjectType());

		// add tabs
		YaFrame.registerTab(new ClevernessTabEvent());

		// add mapinfo
		BaseMapPlugin.getPlugs().add(new CMapWriter());

		// add ex
		ExtentionManagement.add(Project.EXLOAD, new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {
				Project p = (Project) objects.get("project");

				// add it
				if ("cleverness".equals(p.getTypeAsString()) && !p.getObjects().containsKey("tileset")) {
					p.addObjManager(new TilesetObjManager(), "tileset");
				}

				// add it
				if ("cleverness".equals(p.getTypeAsString()) && !p.getObjects().containsKey("map")) {
					p.addObjManager(new MapsObjManager("Rätselkarten", "map"), "map");
				}

				// add it
				if ("cleverness".equals(p.getTypeAsString()) && !p.getObjects().containsKey("tutorial")) {
					p.addObjManager(new MapsObjManager("Tutorialkarten", "tutorial"), "tutorial");
				}
			}
		});
	}
}
