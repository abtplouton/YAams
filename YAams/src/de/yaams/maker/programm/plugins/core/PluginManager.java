/**
 * 
 */
package de.yaams.maker.programm.plugins.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Level;
import org.ini4j.Wini;

import de.yaams.maker.helper.FileHelper;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.Log;
import de.yaams.maker.helper.NetHelper;
import de.yaams.maker.helper.Setting;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.gui.YMessagesDialog;
import de.yaams.maker.programm.YAamsCore;
import de.yaams.maker.programm.plugins.PluginPlugin;

/**
 * @author abt
 * 
 */
public class PluginManager {

	private static HashMap<String, PluginInfo> plugins = new HashMap<String, PluginInfo>();
	private static File blockPluginsPath = new File(YAamsCore.programPath, "blockPlugins.xml");
	// private static ArrayList<String> mess = new ArrayList<String>();
	private static ArrayList<PluginFolder> folder = getInitFolder();
	private static ArrayList<String> blockPlugins;
	private static boolean checkedOnline = false;

	/**
	 * @return the info
	 */
	public static HashMap<String, PluginInfo> getInfo() {
		return plugins;
	}

	/**
	 * Get Init Folders
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private synchronized static ArrayList<PluginFolder> getInitFolder() {
		// load block file
		blockPlugins = (ArrayList<String>) FileHelper.loadXML(blockPluginsPath);
		// exist?
		if (blockPlugins == null) {
			blockPlugins = new ArrayList<String>();
		}

		// load folder
		ArrayList<PluginFolder> pf = new ArrayList<PluginFolder>();

		return pf;
	}

	/**
	 * @return the info
	 */
	public static PluginInfo getInfo(String id) {
		return plugins.get(id);
	}

	/**
	 * Is this plugin install?
	 * 
	 * @param id
	 * @return
	 */
	public static boolean isInstall(String id) {
		return plugins.containsKey(id);
	}

	/**
	 * @param info
	 *            the info to set
	 */
	// protected static void addInfo(final PluginInfo info) {
	//
	// // create plugin
	// try {
	// // black list?
	// if (isDisabled(info.getID())) {
	// return;
	// }
	//
	// Log.ger.info("Add Plugin " + info.getTitle());
	// info.getStartedPluginClass().start();
	// plugins.put(info.getID(), info);
	// } catch (Throwable t) {
	// YException.info("Can not create instance for " + info.getTitle(), t);
	// }
	// }

	/**
	 * Is the plugin disabled?
	 * 
	 * @param info
	 * @return
	 */
	public static boolean isDisabled(String id) {
		return blockPlugins.contains(id);
	}

	/**
	 * Read from all folders the plugins and start it
	 */
	public static void start(YMessagesDialog mess) {

		try {

			// collect all plugins
			HashMap<String, PluginInfo> info = new HashMap<String, PluginInfo>();
			ArrayList<PluginInfo> order = new ArrayList<PluginInfo>();

			// run over all files
			for (PluginFolder f : folder) {

				// load infos
				for (String id : f.getPluginIDs(mess)) {

					// contain id? than skip it
					if (plugins.containsKey(id)) {
						continue;
					}

					// create it
					PluginInfo i = new PluginInfo(id, f);
					i.start(mess);

					// add
					info.put(id, i);
				}
			}

			// load plugins
			while (true) {
				boolean chance = false;
				for (String id : info.keySet().toArray(new String[info.keySet().size()])) {
					// can start?
					int erg = info.get(id).canUse(mess, info.keySet().toArray(new String[info.keySet().size()]));

					// add it?
					if (erg == PluginInfo.USEABLE) {
						plugins.put(id, info.get(id));
						// can start?
						if (!isDisabled(id)) {
							order.add(info.get(id));
						}
						info.remove(id);
						chance = true;
					} else if (erg == PluginInfo.NOTUSEABLE) {
						info.remove(id);
						chance = true;
					}

					// right version?
					// if (Double.valueOf(info.get(id).getYAamsVersion()) !=
					// YAamsCore.yaamsversion) {
					// // show error
					// errors.add(
					// I18N.t("Kann {0} (ID: {1}) nicht starten, weil es YAams Version {2} braucht. Du hast aber {3}.",
					// info.get(id).getTitle(), id,
					// info.get(id).getYAamsVersion(),
					// YAamsCore.yaamsversion), Level.INFO_INT);
					// // remove it
					// info.remove(id);
					// continue;
					// }

					// has depend?
					// if (isInstall(info.get(id).getDepend())) {
					// add it
					// addInfo(info.get(id));
					// info.remove(id);
					// chance = true;
					// } else {
					// if (!firstRun) {
					// // show error
					// errors.add(I18N.t("Kann {0} (ID: {1}) nicht starten, weil {2} fehlt.",
					// info.get(id)
					// .getTitle(), id,
					// isInstallAsString(info.get(id).getDepend())),
					// Level.INFO_INT);
					// }
					// }
				}
				if (chance == false) {
					break;
				}
			}

			// are modules missing?
			for (String id : info.keySet()) {
				mess.add(I18N.t("Kann Plugin {0} nicht starten.", info.get(id).getTitle()), Level.INFO_INT);
			}

			// start all
			for (PluginInfo i : order) {
				i.startPluginClass(mess);
				Log.ger.info("Start Plugin " + i.getTitle());
			}
		} catch (Throwable t) {
			YEx.error("Can not start plugins", t);
		}

		// errors.addAll(mess);
	}

	/**
	 * @return the folder
	 */
	public static ArrayList<PluginFolder> getFolder() {
		return folder;
	}

	/**
	 * @param folder
	 *            the folder to set
	 */
	public static void addFolder(final PluginFolder folder) {
		PluginManager.folder.add(folder);
	}

	/**
	 * Enable the plugin with this id
	 * 
	 * @param id
	 */
	public static void enable(String id) {
		if (blockPlugins.contains(id)) {
			blockPlugins.remove(id);
		}
		FileHelper.saveXML(blockPluginsPath, blockPlugins);
	}

	/**
	 * Disable the plugin with this id
	 * 
	 * @param id
	 */
	public static void disable(String id) {
		blockPlugins.add(id);
		FileHelper.saveXML(blockPluginsPath, blockPlugins);
	}

	/**
	 * Uninstall a plugin
	 * 
	 * @param id
	 */
	public static boolean uninstall(String id) {
		// exisit?
		if (!isInstall(id)) {
			return false;
		}

		// uninstall
		return getInfo(id).getPluginFolder().uninstall(id);
	}

	/**
	 * Helpermethod to install all online infos
	 */
	public static void installOnlineInfo(boolean manuell) {
		// check settings
		if (!Setting.get("net.access", true) && !manuell) {
			return;
		}

		File catalog = new File(YAamsCore.programPath, "catalog.ini");

		// update catalog?
		if (manuell || Setting.get(PluginPlugin.CHECK, 168) != -1
				&& System.currentTimeMillis() - Setting.get("plugins.updateCheck", 0L) >= Setting.get(PluginPlugin.CHECK, 168L) * 3600000) {
			Setting.set("plugins.updateCheck", System.currentTimeMillis());
			Log.ger.info("Check updates");

			// has internet?
			if (!NetHelper.hasInternet(manuell)) {
				return;
			}

			// delete file
			if (catalog.exists()) {
				FileHelper.deleteFile(catalog);
			}

			// has internet?
			String link = NetHelper.getContentAsString("http://www.yaams.de/plugin/?action=catalog");

			// dl file?
			if (!NetHelper.downloadFile(catalog, link)) {
				return;
			}
		} else {
			// no update check?
			if (!catalog.exists()) {
				Log.ger.info("No update check");
				return;
			}
		}

		// exist?
		if (!FileHelper.checkPath(I18N.t("Kann Pluginkatalog nicht öffnen"), catalog, false, false)) {
			return;
		}

		try {
			// run over all plugins
			final Wini ini = new Wini(catalog);
			YMessagesDialog mess = new YMessagesDialog(I18N.t("Kann Onlineinfo nicht updaten"), "plugin.update");
			// add all
			for (final String id : ini.keySet()) {
				// exist?
				PluginInfo i = getInfo(id);
				if (i == null) {
					getInfo().put(id, new PluginInfo(id, getFolder().get(0)));
				}

				// set it
				PluginManager.getInfo(id).installOnline(ini.get(id));
			}

			mess.showOk();
		} catch (Throwable t) {
			YEx.info("Can not read catalog " + catalog, t);
		}
		checkedOnline = true;
	}

	/**
	 * Check for alle plugins autoupdate
	 */
	public static void checkAutoUpdate() {
		if (!Setting.get("net.access", true) || !Setting.get("plugins.autoupdate", true)) {

		}
		// run over all plugins
		for (String key : plugins.keySet()) {
			// build update button
			plugins.get(key).getButtons();

			// do it
			plugins.get(key).updateInstall();
		}
	}

	/**
	 * Helpermethod to install a plugin
	 * 
	 * @param id
	 */
	public static boolean installFromOnline(String id) {
		// check online
		if (!checkedOnline) {
			installOnlineInfo(true);
		}

		YMessagesDialog mess = new YMessagesDialog(I18N.t("Plugin {0} nicht gefunden.", id), "plugin.install.online");

		// found it?
		if (getInfo(id) == null) {
			mess.add(I18N.t("Konnte keine Onlineinfos abrufen"), Level.WARN_INT);
		} else {
			// install
			getInfo(id).getButtons();
			getInfo(id).updateInstall();
			try {
				getInfo(id).start(mess);
			} catch (Throwable t) {
				Log.ger.info("Can not install plugin", t);
				mess.add(YEx.toString(I18N.t("Kann Plugin {0} nicht installieren.", getInfo(id).getTitle()), t, true), Level.WARN_INT);
			}
		}

		// inform
		mess.showOk();
		return !mess.hasErrors();
	}
}
