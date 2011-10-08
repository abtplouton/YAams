package de.yaams.rgssplayer.core.java;

import java.io.File;
import java.util.HashMap;

import de.yaams.core.RGSSGame;
import de.yaams.core.YrgssCore;
import de.yaams.core.helper.FileHelper;
import de.yaams.core.helper.gui.YMessagesDialog;

public class Yrgss {
	public static RGSSGame game;
	public static String[] scriptNames;
	public static String[][] code;
	public static int codeStep;
	public static int loadingStep;
	public static int loadingMax;
	public static boolean musik = true;
	public static boolean sound = true;
	public static boolean fullscreen = false;
	public static boolean smoothmodus = true;
	public static boolean debug = false;
	public static boolean active = true;

	public static void init(YMessagesDialog errors) {
		YrgssCore.init(errors);

		HashMap<String, Boolean> s = (HashMap<String, Boolean>) FileHelper.loadXML(new File(YrgssCore.programPath, "gameSettings.xml"));
		if (s != null) {
			musik = s.get("music").booleanValue();
			sound = s.get("sound").booleanValue();
			fullscreen = s.get("fullscreen").booleanValue();
			smoothmodus = s.get("smoothmodus").booleanValue();
			debug = s.get("debug").booleanValue();
			active = s.get("active").booleanValue();
		}
	}

	public static void saveSettings() {
		HashMap<String, Boolean> s = new HashMap<String, Boolean>();
		s.put("music", Boolean.valueOf(musik));
		s.put("sound", Boolean.valueOf(sound));
		s.put("fullscreen", Boolean.valueOf(fullscreen));
		s.put("smoothmodus", Boolean.valueOf(smoothmodus));
		s.put("debug", Boolean.valueOf(debug));
		s.put("active", Boolean.valueOf(active));

		FileHelper.saveXML(new File(YrgssCore.programPath, "gameSettings.xml"), s);
	}
}