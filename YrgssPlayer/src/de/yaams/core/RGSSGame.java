package de.yaams.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Priority;
import org.ini4j.Wini;

import de.yaams.core.helper.FileHelper;
import de.yaams.core.helper.I18N;
import de.yaams.core.helper.Log;
import de.yaams.core.helper.gui.YEx;
import de.yaams.core.helper.gui.YMessagesDialog;

public class RGSSGame {
	protected boolean valid;
	protected int rgssVersion;
	protected String name;
	protected String scriptPath;
	protected File path;
	protected HashMap<String, String> branding;
	protected HashMap<String, Object> objects;
	protected String hash;

	protected RGSSGame(File path, YMessagesDialog errors) {
		this.path = path;
		objects = new HashMap<String, Object>();
		valid = readGameIni(errors);
		hash = Integer.toString(path.hashCode());

		if (valid) {
			branding = (HashMap<String, String>) FileHelper.loadXML(new File(path, "branding.xml"));
			if (branding == null) {
				branding = new HashMap<String, String>();
			}
		} else {
			branding = new HashMap<String, String>();
		}
	}

	public static RGSSGame loadProject(File path) {
		YMessagesDialog errors = new YMessagesDialog(I18N.t("Can not read project from {0}.", path), "game.start");
		RGSSGame p = new RGSSGame(path, errors);
		if (!errors.showQuestion()) {
			return null;
		}
		return p;
	}

	public boolean readGameIni(YMessagesDialog errors) {
		File f = new File(path, "Game.ini");

		if (!f.exists()) {
			errors.add(I18N.t("File {0} not found.", f.getAbsolutePath()), Priority.WARN_INT);
			return false;
		}

		HashMap<String, String> ini = FileHelper.interpretFile(f, true);
		name = ini.get("Title");

		Log.ger.info("Loading " + name);
		Log.ger.info("from " + path);

		if (new File(path, "Game.rgssad").exists() || new File(path, "Game.rgssa2").exists()) {
			errors.add(I18N.t("Encrypted Games not supported."), Priority.WARN_INT);
		}

		scriptPath = ini.get("Scripts");

		f = new File(path, scriptPath);
		if (!f.exists()) {
			scriptPath = scriptPath.replace("/", "\\");
			scriptPath = scriptPath.replace("\\", File.separator);

			f = new File(path, scriptPath);

			if (!f.exists()) {
				errors.add(I18N.t("File {0} not found.", f.getAbsolutePath()), Priority.WARN_INT);
			}

		}

		if (scriptPath.endsWith(".rxdata")) {
			rgssVersion = 1;
		} else if (scriptPath.endsWith(".rvdata")) {
			rgssVersion = 2;
			errors.add(I18N.t("RGSS {0} not full supported.", Integer.valueOf(rgssVersion)), Priority.INFO_INT);
		} else {
			errors.add(I18N.t("RGSS {0} not supported.", Integer.valueOf(rgssVersion)), Priority.ERROR_INT);
		}
		Log.ger.info("RGSS-Version " + rgssVersion);

		RTP.readRTP(this);

		return !errors.hasErrors();
	}

	/**
	 * Get the ruby interpreter
	 * 
	 * @param p
	 * @return
	 */
	public File getGameIniFile() {
		// exist it?
		if (!objects.containsKey("gameinifile")) {
			objects.put("gameinifile", new File(getPath(), "Game.ini"));
		}
		return (File) objects.get("gameinifile");
	}

	/**
	 * Get the ruby interpreter
	 * 
	 * @param p
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getGameIni() {
		// exist it?
		if (!objects.containsKey("gameini")) {

			// read game ini
			try {
				Wini ini = new Wini(getGameIniFile());
				objects.put("gameini", ini.get("Game"));

			} catch (Throwable t) {
				YEx.info("Can not read " + getGameIniFile() + " for rtp", t);
			}
		}
		return (Map<String, String>) objects.get("gameini");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getPath() {
		return path;
	}

	@Override
	public String toString() {
		return name;
	}

	public HashMap<String, String> getBranding() {
		return branding;
	}

	public int getRgssVersion() {
		return rgssVersion;
	}

	public String getScriptPath() {
		return scriptPath;
	}

	public HashMap<String, File> getRtp() {
		return (HashMap<String, File>) getObjects().get("rtpFiles");
	}

	public String getHash() {
		return hash;
	}

	/**
	 * @return the objects
	 */
	public HashMap<String, Object> getObjects() {
		return objects;
	}
}