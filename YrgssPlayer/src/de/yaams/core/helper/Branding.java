package de.yaams.core.helper;

import java.util.ArrayList;

import de.yaams.core.RGSSGame;

public class Branding {
	public static String get(RGSSGame p, String key) {
		if (!p.getBranding().containsKey(key)) {
			return null;
		}
		return p.getBranding().get(key);
	}

	public static String get(RGSSGame p, String key, String value) {
		if (!p.getBranding().containsKey(key)) {
			return value;
		}
		return p.getBranding().get(key);
	}

	public static int get(RGSSGame p, String key, int value) {
		if (!p.getBranding().containsKey(key)) {
			return value;
		}
		return Integer.valueOf(p.getBranding().get(key)).intValue();
	}

	public static boolean get(RGSSGame p, String key, boolean value) {
		if (!p.getBranding().containsKey(key)) {
			return value;
		}
		return Boolean.valueOf(p.getBranding().get(key)).booleanValue();
	}

	public static String[] get(RGSSGame p, String key, String[] value) {
		if (!p.getBranding().containsKey(key + "." + 0)) {
			return value;
		}

		ArrayList<String> list = new ArrayList<String>();
		int i = 0;
		while (p.getBranding().containsKey(key + "." + i)) {
			list.add(p.getBranding().get(key + "." + i));
			i++;
		}

		String[] x = new String[list.size()];

		int j = 0;
		for (int l = list.size(); j < l; j++) {
			x[j] = list.get(j);
		}
		return x;
	}

	public static void put(RGSSGame p, String key, String value) {
		p.getBranding().put(key, value);
	}

	public static void put(RGSSGame p, String key, boolean value) {
		p.getBranding().put(key, Boolean.toString(value));
	}

	public static void put(RGSSGame p, String key, int value) {
		p.getBranding().put(key, Integer.toString(value));
	}

	public static void put(RGSSGame p, String key, String[] values) {
		int j = 0;
		while (p.getBranding().containsKey(key + "." + j)) {
			p.getBranding().remove(key + "." + j);
			j++;
		}

		int i = 0;
		for (int l = values.length; i < l; i++) {
			p.getBranding().put(key + "." + i, values[i]);
		}
	}
}