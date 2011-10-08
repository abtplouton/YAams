/**
 * 
 */
package de.yaams.extensions.rgssproject.database;

import java.util.HashMap;

import de.yaams.extensions.rgssproject.RGSSProjectHelper;
import de.yaams.extensions.rgssproject.RTP;
import de.yaams.extensions.rgssproject.RessourceObjectManager;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.help.HelpAdder;
import de.yaams.extensions.rgssproject.tab.RGSSRessourceTab;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.extensions.IExtension;
import de.yaams.maker.programm.project.Project;

/**
 * @author Nebli
 * 
 */
public class DatabasePlugin {

	/**
	 * 
	 */
	public static void start() {

		// add help
		new HelpAdder();

		// add db
		ExtentionManagement.add(Project.EXLOAD, new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {
				final Project p = (Project) objects.get("project");

				// is xp proj?
				if (!RGSSProjectHelper.is(p, true, true)) {
					return;
				}

				// add folders?
				if (!p.getCache().containsKey("check.folder")) {

					RTP.checkCreateGraphicAudioFolder(p);
					p.getCache().put("check.folder", true);
				}

				// add rb?
				if (!p.getCache().containsKey("database.rgss")) {
					// and addon
					if (RGSSProjectHelper.getRGSSVersion(p) == 1) {
						RGSSProjectHelper.getInterpreter(p).interpretInternFile(getClass(), "color.rb");
						RGSSProjectHelper.getInterpreter(p).interpretInternFile(getClass(), "tone.rb");
					}

					p.getCache().put("database.rgss", true);
				}

				// add
				add(Type.ACTOR, p, "Held", true, false);
				add(Type.CLASS, p, "Held", true, false);
				add(Type.SKILL, p, "Held", true, false);
				add(Type.ITEM, p, "Gegenstände", true, false);
				add(Type.WEAPON, p, "Gegenstände", true, false);
				add(Type.ARMOR, p, "Gegenstände", true, false);
				add(Type.ENEMY, p, "Feinde", true, false);
				add(Type.TROOP, p, "Feinde", true, false);
				add(Type.STATUS, p, "Held", true, false);
				add(Type.ANIMATION, p, "System", true, false);

				// add ress
				// check to add
				if (RGSSProjectHelper.is(p, true, true) && !p.getObjects().containsKey(RGSSRessourceTab.ID)) {
					p.addObjManager(new RessourceObjectManager(), RGSSRessourceTab.ID);
				}
			}
		});
	}

	/**
	 * Add element
	 * 
	 * @param type
	 * @param ary
	 */
	public static void add(Type type, Project p, String group, boolean rgss1, boolean rgss2) {
		// check to add
		if (RGSSProjectHelper.is(p, rgss1, rgss2) && !p.getObjects().containsKey(RGSS1Helper.getTabID(type))) {
			p.addObjManager(new DatabaseObjectManager(type, group), RGSS1Helper.getTabID(type));
		}

	}

}
