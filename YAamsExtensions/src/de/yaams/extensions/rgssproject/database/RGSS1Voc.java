/**
 * 
 */
package de.yaams.extensions.rgssproject.database;

import org.jruby.RubyObject;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.maker.programm.project.Project;

/**
 * @author abby
 * 
 */
public class RGSS1Voc {

	/**
	 * Get the named string for the voc
	 * 
	 * @param name
	 * @return
	 */
	private static String getString(String name, Project p) {
		// build it
		RubyObject words = (RubyObject) RGSS1Helper.get(p, Type.SYSTEM).get(0).getObject().getInstanceVariable("@words");
		return RubyHelper.toString(words, name);
	}

	/**
	 * Get Gold
	 * 
	 * @param p
	 * @return
	 */
	public static String gold(Project p) {
		return getString("@gold", p);
	}

	/**
	 * Get hp
	 * 
	 * @param p
	 * @return
	 */
	public static String hp(Project p) {
		return getString("@hp", p);
	}

	/**
	 * Get sp
	 * 
	 * @param p
	 * @return
	 */
	public static String sp(Project p) {
		return getString("@sp", p);
	}

	/**
	 * Get Strength
	 * 
	 * @param p
	 * @return
	 */
	public static String str(Project p) {
		return getString("@str", p);
	}

	/**
	 * Get Dexterity
	 * 
	 * @param p
	 * @return
	 */
	public static String dex(Project p) {
		return getString("@dex", p);
	}

	/**
	 * Get Agility
	 * 
	 * @param p
	 * @return
	 */
	public static String agi(Project p) {
		return getString("@agi", p);
	}

	/**
	 * Get Intelligence
	 * 
	 * @param p
	 * @return
	 */
	public static String inte(Project p) {
		return getString("@int", p);
	}

	/**
	 * Get Attack Power
	 * 
	 * @param p
	 * @return
	 */
	public static String atk(Project p) {
		return getString("@atk", p);
	}

	/**
	 * Get Physical Defense
	 * 
	 * @param p
	 * @return
	 */
	public static String pdef(Project p) {
		return getString("@pdef", p);
	}

	/**
	 * Get Magic Defense
	 * 
	 * @param p
	 * @return
	 */
	public static String mdef(Project p) {
		return getString("@mdef", p);
	}

	/**
	 * Get Weapon
	 * 
	 * @param p
	 * @return
	 */
	public static String weapon(Project p) {
		return getString("@weapon", p);
	}

	/**
	 * Get Shield
	 * 
	 * @param p
	 * @return
	 */
	public static String shield(Project p) {
		return getString("@armor1", p);
	}

	/**
	 * Get Helmet
	 * 
	 * @param p
	 * @return
	 */
	public static String helm(Project p) {
		return getString("@armor2", p);
	}

	/**
	 * Get Body armor
	 * 
	 * @param p
	 * @return
	 */
	public static String armor(Project p) {
		return getString("@armor3", p);
	}

	/**
	 * Get Accessory
	 * 
	 * @param p
	 * @return
	 */
	public static String acc(Project p) {
		return getString("@armor4", p);
	}

	/**
	 * Get Attack
	 * 
	 * @param p
	 * @return
	 */
	public static String attack(Project p) {
		return getString("@attack", p);
	}

	/**
	 * Get Skill
	 * 
	 * @param p
	 * @return
	 */
	public static String skill(Project p) {
		return getString("@skill", p);
	}

	/**
	 * Get Defense
	 * 
	 * @param p
	 * @return
	 */
	public static String def(Project p) {
		return getString("@guard", p);
	}

	/**
	 * Get Item
	 * 
	 * @param p
	 * @return
	 */
	public static String item(Project p) {
		return getString("@item", p);
	}

	/**
	 * Get Equip
	 * 
	 * @param p
	 * @return
	 */
	public static String equip(Project p) {
		return getString("@equip", p);
	}
}
