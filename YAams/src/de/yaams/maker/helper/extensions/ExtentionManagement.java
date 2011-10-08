/**
 * 
 */
package de.yaams.maker.helper.extensions;

import java.util.ArrayList;
import java.util.HashMap;

import de.yaams.maker.helper.gui.YEx;

/**
 * @author abt
 * 
 */
public class ExtentionManagement {

	public static final String SAVE = "save";
	/**
	 * for the only specific project
	 */
	private static HashMap<String, ArrayList<IExtension>> data = new HashMap<String, ArrayList<IExtension>>();

	/**
	 * Register a new ExtensionPoint
	 * 
	 * @param id
	 */
	public static void registerPoint(final String id) {
		// exist?
		if (exist(id)) {
			return;
		}

		// add it
		data.put(id, new ArrayList<IExtension>());
	}

	/**
	 * Look of this point exist
	 * 
	 * @param id
	 * @return
	 */
	public static boolean exist(final String id) {
		return data.containsKey(id);
	}

	/**
	 * Add to a ExtensionPoint a new IExtension
	 * 
	 * @param ID
	 *            of the ExtensionPoint
	 * @param e
	 *            , IExtension
	 */
	public static void remove(final String id, final IExtension e) {
		// not exist?
		if (!exist(id)) {
			registerPoint(id);
		}

		// add it
		data.get(id).remove(e);
	}

	/**
	 * Add to a ExtensionPoint a new IExtension
	 * 
	 * @param ID
	 *            of the ExtensionPoint
	 * @param e
	 *            , IExtension
	 */
	public static IExtension add(final String id, final IExtension e) {
		// not exist?
		if (!exist(id)) {
			registerPoint(id);
		}

		// add it
		data.get(id).add(e);
		return e;
	}

	/**
	 * Work/Run/Do things with this extension object
	 * 
	 * @param ID
	 *            of the ExtensionPoint
	 * @param o
	 *            , the object
	 */
	public static void work(final String id, HashMap<String, Object> objects) {
		try {
			// not exist?
			if (!exist(id)) {
				registerPoint(id);
			}

			// run it
			for (final IExtension e : data.get(id)) {
				try {
					e.work(objects);
				} catch (final Throwable t) {
					YEx.info("Can not run Objet " + objects.entrySet() + " with " + e + " from extensionpoint " + id, t);
				}
			}

		} catch (final Throwable t) {
			YEx.info("Can not run Object " + objects.entrySet() + " from extensionpoint " + id, t);
		}
	}
}
