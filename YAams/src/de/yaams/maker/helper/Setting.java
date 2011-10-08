/**
 * yaams-maker - de.yaams.maker GameSettings.java
 */
package de.yaams.maker.helper;

import java.util.ArrayList;

import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.programm.YAamsCore;

/**
 * Save all basics Infos about the game
 * 
 * @author Administrator
 */
public class Setting {

	/**
	 * Helpermethod to look if a setting exist
	 * 
	 * @param id
	 * @return
	 */
	public static boolean exist(String id) {
		// get
		return YAamsCore.getSetting().containsKey(id);
	}

	/**
	 * Helpermethod to get a string setting
	 * 
	 * @param id
	 * @return
	 */
	public static String get(String id) {
		if (!YAamsCore.getSetting().containsKey(id)) {
			return null;
		}
		// get
		return YAamsCore.getSetting().get(id);
	}

	/**
	 * Helpermethod to get a string setting
	 * 
	 * @param project
	 * @param id
	 * @param standard
	 * @return
	 */
	public static String get(String id, String standard) {
		// key exist?
		if (YAamsCore.getSetting().containsKey(id)) {
			return YAamsCore.getSetting().get(id);
		}

		// no? add it
		YAamsCore.getSetting().put(id, standard);
		return standard;
	}

	/**
	 * Helpermethod to get a long setting
	 * 
	 * @param project
	 * @param id
	 * @param standard
	 * @return
	 */
	public static long get(String id, long standard) {
		try {
			return Long.valueOf(get(id, Long.toString(standard)));
		} catch (Throwable t) {
			YEx.info("Can not read long setting " + id + " value " + get(id) + " is'nt supported", t);
			return standard;
		}
	}

	/**
	 * Helpermethod to get a int setting
	 * 
	 * @param project
	 * @param id
	 * @param standard
	 * @return
	 */
	public static int get(String id, int standard) {
		try {
			return Integer.valueOf(get(id, Integer.toString(standard)));
		} catch (Throwable t) {
			YEx.info("Can not read integer setting " + id + " value " + get(id) + " is'nt supported", t);
			return standard;
		}
	}

	/**
	 * Helpermethod to get a boolean setting
	 * 
	 * @param project
	 * @param id
	 * @param standard
	 * @return
	 */
	public static boolean get(String id, boolean standard) {
		try {
			return Boolean.valueOf(get(id, Boolean.toString(standard)));
		} catch (Throwable t) {
			YEx.info("Can not read boolean setting " + id + " value " + get(id) + " is'nt supported", t);
			return standard;
		}
	}

	/**
	 * Get special settings
	 * 
	 * @param key
	 * @param alternativ
	 *            value
	 * @return
	 */
	public static String[] get(final String key, final String[] value) {
		if (!YAamsCore.getSetting().containsKey(key + "." + 0)) {
			return value;
		}

		// load it
		final ArrayList<String> list = new ArrayList<String>();
		int i = 0;
		while (YAamsCore.getSetting().containsKey(key + "." + i)) {
			list.add(YAamsCore.getSetting().get(key + "." + i));
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
	public static void set(String id, Object standard) {
		// add it
		YAamsCore.getSetting().put(id, standard.toString());
	}

	/**
	 * Put special settings
	 * 
	 * @param name
	 * @param alternative
	 */
	public static void set(final String key, final String[] values) {
		// clear old
		int j = 0;
		while (YAamsCore.getSetting().containsKey(key + "." + j)) {
			YAamsCore.getSetting().remove(key + "." + j);
			j++;
		}

		// save
		for (int i = 0, l = values.length; i < l; i++) {
			YAamsCore.getSetting().put(key + "." + i, values[i]);
		}
	}

}
