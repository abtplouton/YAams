/**
 * 
 */
package de.yaams.extensions.diamant;

import java.util.HashMap;

import de.yaams.extensions.diamant.maps.CMapEditorTab;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.tabs.TabEvent;
import de.yaams.maker.programm.tabs.YaTab;

/**
 * @author abby
 * 
 */
public class DiamantTabEvent extends TabEvent {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.tabs.TabEvent#getTab(java.lang.String,
	 * de.yaams.maker.programm.project.Project, java.util.HashMap,
	 * java.util.HashMap)
	 */
	@Override
	public YaTab getTab(String id, Project p, HashMap<String, String> parameters, HashMap<String, String> arguments) {

		// check for map

		if (CMapEditorTab.ID.equals(id)) {
			return new CMapEditorTab(p, parameters.get("typ"), Integer.valueOf(parameters.get("map")));
		}

		return null;
	}
}
