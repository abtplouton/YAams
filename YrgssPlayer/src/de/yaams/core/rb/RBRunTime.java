package de.yaams.core.rb;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import org.jruby.embed.ScriptingContainer;
import org.jruby.util.KCode;

import de.yaams.core.helper.Log;
import de.yaams.core.helper.gui.YEx;

public class RBRunTime {
	public static ScriptingContainer interpreter = get();

	// public static Ruby getRuntime() {
	// return interpreter.getRuntime();
	// }

	protected static ScriptingContainer get() {
		interpreter = new ScriptingContainer();

		interpreter.setKCode(KCode.UTF8);
		interpretInternFile(RBRunTime.class, "java.rb");

		String[] basics = { "table.rb" };

		String[] arrayOfString1 = basics;
		int j = basics.length;
		for (int i = 0; i < j; i++) {
			String s = arrayOfString1[i];
			interpretInternFile(RBRunTime.class, "data/" + s);
		}

		return interpreter;
	}

	public static Object interpretFile(File file) {
		try {
			if (file == null || !file.exists()) {
				throw new FileNotFoundException("File " + file + " not found.");
			}

			Log.ger.debug("Read & Interpret " + file.getName());

			return interpreter.runScriptlet(new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(file)),
					"UTF-8")), file.getName());
		} catch (Throwable t) {
			YEx.error("Can not interpret scriptfile " + (file != null ? file.getName() : file), t);
		}

		return null;
	}

	public static Object interpretInternFile(Class<?> c, String file) {
		try {
			if (c.getResourceAsStream(file) == null) {
				throw new FileNotFoundException("Filestream " + file + " in Class " + c + " not found.");
			}
			return interpreter.runScriptlet(c.getResourceAsStream(file), file);
		} catch (Throwable t) {
			YEx.error("Can not interpret scriptstreamfile " + file, t);
		}

		return null;
	}

	public static void loadRGSS1() {
		String[] rpg = { "actor.rb", "animation.rb", "animationFrame.rb", "animationTiming.rb", "armor.rb", "audioFile.rb", "class.rb",
				"classLearning.rb", "commonEvent.rb", "enemy.rb", "enemyAction.rb", "event.rb", "eventCommand.rb", "eventPage.rb",
				"eventPageCondition.rb", "eventPageGraphic.rb", "item.rb", "map.rb", "mapInfo.rb", "moveCommand.rb", "moveRoute.rb",
				"skill.rb", "state.rb", "system.rb", "systemTestBattle.rb", "systemWords.rb", "tileset.rb", "troop.rb", "troopMember.rb",
				"troopPage.rb", "troopPageCondition.rb", "weapon.rb" };

		String[] arrayOfString1 = rpg;
		int j = rpg.length;
		for (int i = 0; i < j; i++) {
			String s = arrayOfString1[i];
			interpretInternFile(RBRunTime.class, "data/rgss1/" + s);
		}
	}

	public static void loadRGSS2() {
		String[] rpg = { "actor.rb", "animation.rb", "animationFrame.rb", "animationTiming.rb", "armor.rb", "audioFile.rb", "cache.rb",
				"class.rb", "classLearning.rb", "commonEvent.rb", "enemy.rb", "enemyAction.rb", "event.rb", "eventCommand.rb",
				"eventPage.rb", "eventPageCondition.rb", "eventPageGraphic.rb", "item.rb", "map.rb", "mapInfo.rb", "moveCommand.rb",
				"moveRoute.rb", "skill.rb", "sprite.rb", "state.rb", "system.rb", "systemTestBattle.rb", "systemWords.rb", "tileset.rb",
				"troop.rb", "troopMember.rb", "troopPage.rb", "troopPageCondition.rb", "weapon.rb", "weather.rb" };

		String[] arrayOfString1 = rpg;
		int j = rpg.length;
		for (int i = 0; i < j; i++) {
			String s = arrayOfString1[i];
			interpretInternFile(RBRunTime.class, "data/rgss2/" + s);
		}
	}
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.core.rb.RBRunTime JD-Core Version: 0.6.0
 */