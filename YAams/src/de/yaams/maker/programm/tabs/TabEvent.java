/**
 * 
 */
package de.yaams.maker.programm.tabs;

import java.util.HashMap;

import de.yaams.maker.programm.YaFrame;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.ProjectManagement;

/**
 * @author abt
 * 
 */
public abstract class TabEvent {

	/**
	 * Create a new YaTab
	 * 
	 * @param id
	 * @return the tab or null, found nothing
	 */
	public static YaTab getTab(String id) {

		// build vars
		String uid;
		HashMap<String, String> parameter = new HashMap<String, String>();
		Project p = null;
		YaTab tab;

		// get id
		if (id.contains("#")) {
			uid = id.substring(0, id.indexOf("#"));
		} else if (id.contains("?")) {
			uid = id.substring(0, id.indexOf("?"));
		} else {
			uid = id;
		}

		// add parameters
		if (id.contains("#")) {
			// build string
			String w = id.substring(id.indexOf("#") + 1);
			if (w.contains("?")) {
				w = w.substring(0, w.indexOf("?"));
			}
			String rest[] = w.split("&");

			// build hashmap
			for (String s : rest) {
				String[] t = s.split("=");
				// add project?
				if ("proj".equals(t[0])) {
					p = ProjectManagement.getProject(t[1]);
				} else {
					parameter.put(t[0], t[1]);
				}

			}
		}

		// run over all events
		for (TabEvent t : YaFrame.get().getTabs()) {
			tab = t.getTab(uid, p, parameter, getArguments(id));
			// found?
			if (tab != null) {
				return tab;
			}
		}
		return null;
	}

	/**
	 * Get the arguments or an empty map
	 * 
	 * @param id
	 * @param arguments
	 */
	public static HashMap<String, String> getArguments(String id) {
		HashMap<String, String> arguments = new HashMap<String, String>();
		// add arguments
		if (id.contains("?")) {
			String rest[] = id.substring(id.indexOf("?") + 1).split("&");

			// build hashmap
			for (String s : rest) {
				String[] t = s.split("=");
				arguments.put(t[0], t[1]);

			}
		}

		return arguments;
	}

	/**
	 * Overwrite to create a new Tab
	 * 
	 * @param id
	 * @param parameters
	 * @return the tab, or null, in nothing found
	 */
	public abstract YaTab getTab(String id, Project p, HashMap<String, String> parameters, HashMap<String, String> arguments);

	/**
	 * Helpermethod to build a right tab call parameter string
	 * 
	 * @param uid
	 * @param p
	 * @param data
	 * @return
	 */
	public static String buildParameter(String uid, Project p, HashMap<String, String> arguments, String... data) {
		StringBuffer s = new StringBuffer(uid);

		// add parameter
		if (data.length > 0 || p != null) {
			s.append("#");

			// has proj?
			if (p != null) {
				s.append("proj=");
				s.append(p.getHash());
				s.append("&");
			}

			// has parameters?
			if (data.length > 0) {
				for (int i = 0, l = data.length / 2; i < l; i++) {
					s.append(data[i * 2]);
					s.append("=");
					s.append(data[i * 2 + 1]);
					s.append("&");
				}
			}
		}

		// has something?
		if (arguments == null || arguments.size() == 0) {
			return s.toString();
		}

		s.append("?");

		// add arguments
		for (String key : arguments.keySet()) {
			// add it
			s.append(key);
			s.append("=");
			s.append(arguments.get(key));
			s.append("&");
		}

		return s.toString();
	}

}
