/**
 * 
 */
package de.yaams.core.helper;

import java.text.MessageFormat;

/**
 * @author Nebli
 * 
 */
public class I18N {

	public static final String[] lang = new String[] { "Deutsch (Integriert)" };

	/**
	 * Default Lang Strings
	 */
	public static final String CANCEL = t("Abbrechen");

	/**
	 * 
	 */
	public I18N() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Translate t
	 * 
	 * @param t
	 * @return
	 */
	public static String t(String t) {
		return t;
	}

	/**
	 * Translate t
	 * 
	 * @param t
	 * @return
	 */
	public static String t(String t, Object... data) {
		return MessageFormat.format(t(t), data);
	}
}
