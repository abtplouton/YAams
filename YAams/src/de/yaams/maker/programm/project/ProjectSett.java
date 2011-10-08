/**
 * 
 */
package de.yaams.maker.programm.project;

import java.util.ArrayList;

/**
 * @author abt
 * 
 */
public class ProjectSett {

	/**
	 * Helpermethod to check, if the setting exist
	 * 
	 * @param project
	 * @param id
	 * @param standard
	 * @return
	 */
	public static boolean exist(Project p, String id) {
		return (p.getData().containsKey(id));
	}

	/**
	 * Helpermethod to get a string setting
	 * 
	 * @param project
	 * @param id
	 * @param standard
	 * @return
	 */
	public static String get(Project p, String id) {
		if (!p.getData().containsKey(id)) {
			return null;
		}
		// get
		return p.getData().get(id);
	}

	/**
	 * Helpermethod to get a string setting
	 * 
	 * @param project
	 * @param id
	 * @param standard
	 * @return
	 */
	public static String get(Project p, String id, String standard) {

		// key exist?
		if (p.getData().containsKey(id)) {
			return p.getData().get(id);
		}

		// no? add it
		p.getData().put(id, standard);
		return standard;
	}

	/**
	 * Helpermethod to get a int setting
	 * 
	 * @param project
	 * @param id
	 * @param standard
	 * @return
	 */
	public static int get(Project p, String id, int standard) {
		return Integer.valueOf(get(p, id, Integer.toString(standard)));
	}

	/**
	 * Helpermethod to get a boolean setting
	 * 
	 * @param project
	 * @param id
	 * @param standard
	 * @return
	 */
	public static boolean get(Project p, String id, boolean standard) {
		return Boolean.valueOf(get(p, id, Boolean.toString(standard)));
	}

	/**
	 * Get special settings
	 * 
	 * @param key
	 * @param alternativ
	 *            value
	 * @return
	 */
	public static String[] get(Project p, final String key, final String[] value) {
		if (!p.getData().containsKey(key + "." + 0)) {
			return value;
		}

		// load it
		final ArrayList<String> list = new ArrayList<String>();
		int i = 0;
		while (p.getData().containsKey(key + "." + i)) {
			list.add(p.getData().get(key + "." + i));
			i++;
		}

		final String[] x = new String[list.size()];
		// convert
		for (int j = 0, l = list.size(); j < l; j++) {
			x[j] = list.get(j);
		}

		return x;
	}

	/**
	 * Helpermethod to set a string setting
	 * 
	 * @param project
	 * @param id
	 * @param standard
	 * @return
	 */
	public static void set(Project p, String id, Object standard) {
		// add it
		p.getData().put(id, standard.toString());
	}

	/**
	 * Put special settings
	 * 
	 * @param name
	 * @param alternative
	 */
	public static void set(Project p, final String key, final String[] values) {
		// clear old
		int j = 0;
		while (p.getData().containsKey(key + "." + j)) {
			p.getData().remove(key + "." + j);
			j++;
		}

		// save
		for (int i = 0, l = values.length; i < l; i++) {
			p.getData().put(key + "." + i, values[i]);
		}
	}
}
