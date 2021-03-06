/**
 * 
 */
package de.yaams.maker.programm.plugins;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.Log;
import de.yaams.maker.helper.gui.YDialog;
import de.yaams.maker.helper.gui.YMessagesDialog;
import de.yaams.maker.programm.YAamsCore;
import de.yaams.maker.programm.environment.YLevel;
import de.yaams.maker.programm.plugins.PluginInfo.STAGE;

/**
 * @author abt
 * 
 */
public abstract class BasePlugin {

	protected PluginInfo info;

	/**
	 * Start Plugin, is there some code, who has a depend use a extra class,
	 * based on the error while loading missing code
	 */
	public abstract void start();

	/**
	 * Can use this plugin? also check special depends? Use Level from
	 * InfoPlugin
	 * 
	 * @param md
	 * @return
	 */
	public abstract boolean useable(YMessagesDialog md);

	/**
	 * @return the info
	 */
	public PluginInfo getInfo() {
		return info;
	}

	/**
	 * @param info
	 *            the info to set
	 */
	public void setInfo(PluginInfo info) {
		this.info = info;
	}

	/**
	 * Helpermethod to check if the version of the plugin the right
	 * 
	 * @param id
	 *            , null means Core/YAams
	 * @param minversion
	 *            , if -1 it will not checked
	 * @param maxversion
	 *            , if -1 it will not checked
	 * @param mess
	 * @return
	 */
	public boolean isVersionInstall(String id, double minversion, double maxversion, YMessagesDialog mess) {
		// if core?
		if (id == null || id.equals("core")) {
			return internVersionCheck(YAamsCore.TITLE, YAamsCore.VERSION, minversion, maxversion, mess);
		}

		// exist plugin?
		if (!PluginLoader.isInstall(id)
				&& YLevel.IS_ADVANCED
				&& YDialog.askUser(I18N.t("{0} benötigt {1}. Runterladen und installieren?", info.getTitle(), id), "plugin.autoinstall."
						+ id, "web_plugin", I18N.t("Damit {0} auf diesem Computer funktionieren kann, wird das Plugin {1} benötigt, <br>"
						+ "welches aber nicht installiert ist. Es kann nun online nach den Plugin gesucht werden "
						+ "<br>und installiert werden, oder {0} wird deaktiviert.", info.getTitle(), id),
						I18N.t("Online suchen und {0} installieren (empfohlen)", id), I18N.t("{0} nicht benutzen", info.getTitle()),
						"web_plugin", "close_plugin")) {

			// restart
			// mess.add(I18N.t("Damit die Änderungen übernommen werden können, muss {0} neugestartet werden.",
			// YAamsCore.TITLE),
			// Level.INFO_INT);

			// say it
			return PluginLoader.installFromOnline(id);
		}

		double ver = PluginLoader.get(id).getVersion();

		// has the version?
		return internVersionCheck(PluginLoader.get(id).getTitle(), ver, minversion, maxversion, mess);
	}

	/**
	 * @param id
	 * @param minversion
	 * @param maxversion
	 * @param mess
	 * @return
	 */
	private boolean internVersionCheck(String core, double actVersion, double minversion, double maxversion, YMessagesDialog mess) {
		// check version
		if ((minversion == -1 || minversion <= actVersion) && (maxversion == -1 || maxversion > actVersion)) {
			return true;
		} else if (minversion > actVersion) {
			info.setMessage(I18N.t("{0} benötigt mind. {1}. Es ist aber nur {2} installiert. Bitte update {3}.", info.getTitle(),
					minversion, actVersion, core), mess, STAGE.NOTUSEABLE, true);
		} else {
			info.setMessage(
					I18N.t("{0} benötigt max. {1}. Es ist schon {2} installiert. Bitte update {0}.", info.getTitle(), maxversion, core),
					mess, STAGE.NOTUSEABLE, true);
		}

		// inform and return
		Log.ger.info("Plugin disabled: " + info.getTitle() + " | Has " + core + ", Allowed:" + minversion + "-" + maxversion);
		return false;
	}
}
