/**
 * 
 */
package de.yaams.maker.programm.environment;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.Setting;

/**
 * @author abt
 * 
 */
public class YLevel {

	public static boolean IS_BEGINNER;
	public static boolean IS_ADVANCED;
	public static boolean IS_DEVELOPER;
	public static final int BEGINNER = 0;
	public static final int ADVANCED = 1;
	public static final int DEVELOPER = 2;
	public static int TYP;
	protected static int dummy = dummy();

	/**
	 * Init system
	 */
	protected static int dummy() {
		// set level
		load(Setting.get("gui.level", BEGINNER));

		return TYP;
	}

	/**
	 * Init system
	 */
	public static void load(int id) {
		TYP = id;

		// set level
		IS_BEGINNER = TYP >= BEGINNER;
		IS_ADVANCED = TYP >= ADVANCED;
		IS_DEVELOPER = TYP >= DEVELOPER;
	}

	/**
	 * Has the user the special level?
	 * 
	 * @param level
	 */
	public static boolean is(final int level) {
		return level <= TYP;
	}

	/**
	 * Has the user the special level?
	 * 
	 * @param level
	 */
	public static String getName(int level) {
		return level == BEGINNER ? I18N.t("Einfach") : level == ADVANCED ? I18N.t("Erweitert") : I18N.t("Entwickler");
	}

}
