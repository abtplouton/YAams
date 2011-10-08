/**
 * 
 */
package de.yaams.maker.programm.project;

import java.util.HashMap;

import de.yaams.maker.helper.gui.bcb.BcbBuilder;
import de.yaams.maker.programm.project.objects.BasicObjectManager;
import de.yaams.maker.programm.project.objects.simple.SimpleObjectTab;
import de.yaams.maker.programm.project.tab.ProjectOptionsTab;
import de.yaams.maker.programm.tabs.BasicTabEvent;
import de.yaams.maker.programm.tabs.TabEvent;
import de.yaams.maker.programm.tabs.YaTab;

/**
 * @author abby
 * 
 */
public class ProjectTabEvent extends TabEvent {

	public static final String HOME = "projhome", RESS = "projress";

	/**
	 * 
	 */
	public ProjectTabEvent() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.tabs.TabEvent#getTab(java.lang.String,
	 * de.yaams.maker.programm.project.Project, java.util.HashMap)
	 */
	@Override
	public YaTab getTab(String id, Project p, HashMap<String, String> parameters, HashMap<String, String> arguments) {

		// has project?
		if (p == null) {
			return null;
		}

		if (ProjectTabEvent.HOME.equals(id)) {
			return p.getType().getHomeTab(p);
		}

		if (ProjectOptionsTab.ID.equals(id)) {
			return new ProjectOptionsTab(p);
		}

		// special tab?
		if (SimpleObjectTab.ID.equals(id) && p.getObjects().containsKey(parameters.get("obj"))) {
			return new SimpleObjectTab(p, parameters.get("obj"),
					arguments != null && arguments.containsKey("selected") ? Integer.valueOf(arguments.get("selected")) : -1);
		}

		return null;
	}

	/**
	 * Build for the project the navi
	 * 
	 * @param bcb
	 */
	public static void buildBcb(BcbBuilder bcb, Project p, String uid) {
		// add basic
		BasicTabEvent.buildBcb(bcb, p.getHash());

		bcb.addSeperator();

		// add project objects
		for (String key : p.getObjects().keySet()) {
			BasicObjectManager bom = p.getObjects().get(key);
			// add it
			bcb.addElement(bom.getTitle(), bom.getIcon(), bom.getTabId(), uid.equals(bom.getTabUId()));
		}
	}

}
