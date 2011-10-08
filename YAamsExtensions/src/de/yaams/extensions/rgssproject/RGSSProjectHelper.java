/**
 * 
 */
package de.yaams.extensions.rgssproject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.ini4j.Wini;

import de.yaams.maker.helper.FileHelper;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.ProjectSett;

/**
 * @author Praktikant
 * 
 */
public class RGSSProjectHelper {

	/**
	 * Get the ruby interpreter
	 * 
	 * @param p
	 * @return
	 */
	public static synchronized RGSSProjectRubyRunTime getInterpreter(Project p) {
		// exist it?
		if (!p.getCache().containsKey("interpreter")) {
			p.getCache().put("interpreter", new RGSSProjectRubyRunTime(p));
		}
		return (RGSSProjectRubyRunTime) p.getCache().get("interpreter");
	}

	/**
	 * Check if the project an rpgxp project
	 * 
	 * @param p
	 * @param rgss1
	 * @param rgss2
	 * @return
	 */
	public static boolean is(Project p, boolean rgss1, boolean rgss2) {
		// right project?
		return p != null && p.getTypeAsString().equals("rpgxp") && (rgss1 && getRGSSVersion(p) == 1 || rgss2 && getRGSSVersion(p) == 2);
	}

	/**
	 * Get the ruby interpreter
	 * 
	 * @param p
	 * @return
	 */
	public static File getGameIniFile(Project p) {
		// exist it?
		if (!p.getCache().containsKey("gameinifile")) {
			p.getCache().put("gameinifile", new File(p.getPath(), "Game.ini"));
		}
		return (File) p.getCache().get("gameinifile");
	}

	/**
	 * Get the ruby interpreter
	 * 
	 * @param p
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getGameIni(Project p) {
		// exist it?
		if (!p.getCache().containsKey("gameini")) {

			// read game ini
			if (FileHelper.checkPath("Can not read " + RGSSProjectHelper.getGameIniFile(p) + " for " + p.getTitle(),
					RGSSProjectHelper.getGameIniFile(p), false, false)) {
				try {
					Wini ini = new Wini(RGSSProjectHelper.getGameIniFile(p));
					p.getCache().put("gameini", ini.get("Game"));

				} catch (Throwable t) {
					YEx.info("Can not read " + RGSSProjectHelper.getGameIniFile(p) + " for " + p.getTitle(), t);
				}
			} else {
				p.getCache().put("gameini", new HashMap<String, String>());
			}
		}
		return (Map<String, String>) p.getCache().get("gameini");
	}

	/**
	 * Get the file for the scripts, read from game.ini
	 * 
	 * @param p
	 * @return
	 */
	public static File getScriptPath(Project p) {
		// exist?
		if (!p.getCache().containsKey("scriptPath")) {
			// load scripts
			String scriptPath = getGameIni(p).get("Scripts");
			if (scriptPath == null) {
				scriptPath = "Data\\Scripts.rxdata";
			}

			// exist?
			File f = new File(p.getPath(), scriptPath);
			if (!f.exists()) {
				// translate /?
				scriptPath = scriptPath.replace("/", "\\");
				scriptPath = scriptPath.replace("\\", File.separator);
				// exist?
				f = new File(p.getPath(), scriptPath);

				if (!FileHelper.checkPath("Can not read Script Path", f, false, true)) {

				}
			}

			p.getCache().put("scriptPath", f);
		}

		// get it
		return (File) p.getCache().get("scriptPath");
	}

	/**
	 * Get the rgss version of the project
	 * 
	 * @param p
	 * @return
	 */
	public static int getRGSSVersion(Project p) {
		// doesn't exist?
		if (!ProjectSett.exist(p, "rgss")) {
			int rgss = 0;
			// check rgss
			if (getScriptPath(p).getName().endsWith(".rxdata")) {
				rgss = 1;
			} else if (getScriptPath(p).getName().endsWith(".rvdata")) {
				rgss = 2;
			}
			ProjectSett.set(p, "rgss", rgss);
		}
		return ProjectSett.get(p, "rgss", 0);
	}
}
