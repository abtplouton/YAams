/**
 * 
 */
package de.yaams.maker.helper;

import java.util.HashMap;

import org.apache.commons.lang.Validate;

/**
 * @author abby
 * 
 */
public class JavaHelper {

	/**
	 * 
	 */
	public JavaHelper() {
		// TODO Auto-generated constructor stub
	}

	public static HashMap<String, Object> createHashStringObj(Object... data) {
		// check parameter
		Validate.notNull(data);
		Validate.isTrue(data.length % 2 == 0, "data isn't even", data);

		// build
		HashMap<String, Object> o = new HashMap<String, Object>();
		// fill
		for (int i = 0, l = data.length / 2; i < l; i++) {
			o.put((String) data[i * 2], data[i * 2 + 1]);
		}
		return o;
	}

	public static HashMap<String, String> createHashString(String... data) {
		// check parameter
		Validate.notNull(data);
		Validate.isTrue(data.length % 2 == 0, "data isn't even", data);

		// build
		HashMap<String, String> o = new HashMap<String, String>();
		// fill
		for (int i = 0, l = data.length / 2; i < l; i++) {
			o.put(data[i * 2], data[i * 2 + 1]);
		}
		return o;
	}

	/**
	 * Create an Array of Numers in String form
	 * 
	 * @param max
	 *            , 0..<=max
	 * @return
	 */
	public static String[] numAry(int max) {
		String[] ary = new String[max + 1];

		// fill
		for (int i = 0; i <= max; i++) {
			ary[i] = Integer.toString(i);
		}

		return ary;
	}
}
