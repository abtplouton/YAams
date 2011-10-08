/**
 * 
 */
package de.yaams.extensions.baseexport;

import java.util.HashMap;

import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.tabs.TabEvent;
import de.yaams.maker.programm.tabs.YaTab;

/**
 * @author abby
 * 
 */
public class ExportTabEvent extends TabEvent {

	/**
	 * 
	 */
	public ExportTabEvent() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.tabs.TabEvent#getTab(java.lang.String,
	 * de.yaams.maker.programm.project.Project, java.util.HashMap,
	 * java.util.HashMap)
	 */
	@Override
	public YaTab getTab(String id, Project p, HashMap<String, String> parameters, HashMap<String, String> arguments) {
		// check it
		if ("project.export".equals(id)) {
			return new ExportTab(p);
		}

		return null;
	}

}
