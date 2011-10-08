package de.yaams.rgssplayer.core.rb;

import java.io.File;

import de.yaams.core.YrgssCore;
import de.yaams.core.helper.Log;
import de.yaams.core.helper.gui.YEx;
import de.yaams.core.rb.RBRunTime;
import de.yaams.rgssplayer.core.java.Yrgss;

public class RBLocater {
	public static void interpretFile(File file) {
		RBRunTime.interpretFile(file);
	}

	protected static void interpretInternFile(String file) {
		RBRunTime.interpretInternFile(RBLocater.class, file);
	}

	public static void loadTest() {
		interpretInternFile("java.rb");
		interpretInternFile("methods.rb");
		interpretInternFile("javaMethod.rb");
		interpretInternFile("test.rb");
	}

	public static void loadScripts() {
		File f = new File(YrgssCore.tmpFolder, "rb");
		f.mkdir();

		Log.ger.debug("Read Scriptfolder " + f + " " + f.isDirectory());

		interpretInternFile("runScripts.rb");

		int i = 0;
		for (int l = Yrgss.code.length; i < l; i++) {
			Log.ger.debug("Create " + Yrgss.code[i][0]);
			try {
				RBRunTime.interpreter.runScriptlet(RBLocater.class.getResourceAsStream("runScriptsStep.rb"), "runScriptsStep.rb");
			} catch (Throwable t) {
				YEx.error("Can not interpret RGSS script " + i + ". " + Yrgss.code[Yrgss.codeStep][0], t);
			}
		}
	}

	public static void loadCore() {
		interpretInternFile("java.rb");
		interpretInternFile("methods.rb");
		interpretInternFile("javaMethod.rb");

		String[] basics = { "graphics.rb", "bitmap.rb", "color.rb", "plane.rb", "input.rb", "tilemap.rb", "window.rb", "tone.rb" };

		String[] arrayOfString1 = basics;
		int j = basics.length;
		for (int i = 0; i < j; i++) {
			String s = arrayOfString1[i];
			interpretInternFile("core/" + s);
		}

		if (Yrgss.game.getRgssVersion() == 1) {
			RBRunTime.loadRGSS1();
			loadRGSS1();
		} else if (Yrgss.game.getRgssVersion() == 2) {
			RBRunTime.loadRGSS2();
		}
	}

	public static void loadRGSS1() {
		String[] rpg = { "cache.rb", "sprite.rb", "weather.rb" };

		String[] arrayOfString1 = rpg;
		int j = rpg.length;
		for (int i = 0; i < j; i++) {
			String s = arrayOfString1[i];
			interpretInternFile("core/rgss1/" + s);
		}
	}

	public static void loadRGSSData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					RBLocater.loadCore();
					RBLocater.loadScripts();
				} catch (Throwable t) {
					YEx.error("Can not load game scripts", t);
				}
			}
		}).start();
	}
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.rb.RBLocater JD-Core Version: 0.6.0
 */