/**
 * 
 */
package de.yaams.maker.programm.plugins.core;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Level;
import org.ini4j.Wini;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.Log;
import de.yaams.maker.helper.NetHelper;
import de.yaams.maker.helper.SwingHelper;
import de.yaams.maker.helper.SystemHelper;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.Run;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.YMessagesDialog;
import de.yaams.maker.helper.gui.icons.IconCache;
import de.yaams.maker.programm.YAamsCore;

/**
 * @author abt
 * 
 */
public class PluginInfo {

	public static final int USEABLE = 1, WAIT = 2, NOTUSEABLE = 3;

	protected String id, icon;
	protected Map<String, String> online, install;
	protected BasePlugin startedPluginClass;
	protected boolean active;
	protected PluginFolder pluginFolder;
	protected Boolean update;
	protected HashMap<String, JButton> buttons;

	/**
	 * Create a new PluginInfo
	 */
	public PluginInfo(String id, PluginFolder pluginFolder) {
		this.id = id;
		this.pluginFolder = pluginFolder;

		// check
		Validate.notEmpty(id, "ID is null");
		Validate.notNull(pluginFolder, "PluginFolder is null");
	}

	/**
	 * Add the information from the online info
	 * 
	 * @param info
	 */
	public void installOnline(Map<String, String> info) {
		online = info;
	}

	/**
	 * Check if an update avaible and return it
	 * 
	 * @return
	 */
	public boolean isUpdateAvaible() {

		if (update == null) {
			if (online == null) {
				update = Boolean.FALSE;
			}

			update = Float.valueOf(getOnlineElement("version", "-1")) > Float.valueOf(getElement("version", "-1")) ? Boolean.TRUE
					: Boolean.FALSE;
		}
		return update;
	}

	/**
	 * Check if an update avaible and return it
	 * 
	 * @return
	 */
	public JLabel getUpdateMessage() {

		// has info?
		JLabel updateMessage;
		String ov = getOnlineElement("version", "MISSING OVERSION"), v = getElement("version", "MISSING VERSION");

		if (install == null) {
			updateMessage = new JLabel(I18N.t("Installation von {0} möglich.", ov));
			updateMessage.setIcon(IconCache.get("web"));
		} else if (online == null) {
			updateMessage = new JLabel(I18N.t("Keine Onlineinfos verfügbar."));
			updateMessage.setIcon(IconCache.get("warn"));
		} else if (isUpdateAvaible()) {
			updateMessage = new JLabel(I18N.t("Update von {1} auf {0} verfügbar.", ov, v));
			updateMessage.setIcon(IconCache.get("update"));
		} else {
			updateMessage = new JLabel(I18N.t("Neuste Version {0} installiert.", v));
			updateMessage.setIcon(IconCache.get("ok"));
		}

		return updateMessage;
	}

	/**
	 * Method to add the plugin to the search path and do other starting
	 * elements
	 */
	public void start(YMessagesDialog md) throws Throwable {

		// extract some new yex?
		File f = new File(pluginFolder.getFolder(), id + ".yex");
		if (f.exists()) {
			pluginFolder.extractYex(f, md);
		}

		try {
			// add path
			pluginFolder.addSearchFolder(new File(pluginFolder.getFolder(), id));
		} catch (Throwable t) {
			md.add(YEx.toString(I18N.t("Kann Ordner {0} nicht hinzufügen", pluginFolder.getFolder()), t, true), Level.WARN_INT);
		}
		// load ini
		File i = new File(new File(pluginFolder.getFolder(), id), id + ".ini");
		try {
			Wini ini = new Wini(i);
			install(ini.get(id));
		} catch (Throwable t) {
			md.add(YEx.toString(I18N.t("Kann {0} nicht lesen.", i), t, true), Level.INFO_INT);
		}
	}

	/**
	 * Add the information from the online info
	 * 
	 * @param info
	 */
	public void install(Map<String, String> info) {
		install = info;
	}

	/**
	 * @return Get the ID
	 */
	public String getOnlineElement(String name, String value) {
		if (online == null || !online.containsKey(name)) {
			return value;
		}
		return online.get(name);
	}

	/**
	 * @return Get the ID
	 */
	public String getOnlineElementFirst(String name, String value) {
		// has online?
		String erg = getOnlineElement(name, null);
		if (erg != null) {
			return erg;
		}

		// otherwise use normal
		return getElement(name, value);
	}

	/**
	 * @return Get the ID
	 */
	public String getElement(String name, String value) {
		if (install == null || !install.containsKey(name)) {
			return value;
		}
		return install.get(name);
	}

	/**
	 * Get the title of this plugin
	 * 
	 * @return
	 */
	public String getDesc() {
		if (install != null) {
			return getElement("desc", id);
		} else {
			return getOnlineElement("desc", id);
		}
	}

	/**
	 * Get the title of this plugin
	 * 
	 * @return
	 */
	public String getTitle() {
		if (install != null) {
			return I18N.t("{0} {1}", getElement("title", id), getElement("version", "0"));
		} else {
			return I18N.t("{0} {1}", getOnlineElement("title", id), getOnlineElement("version", "0"));
		}
	}

	/**
	 * Get the version of this plugin
	 * 
	 * @return
	 */
	public double getVersion() {
		if (install != null) {
			return Double.valueOf(getElement("version", "0"));
		} else {
			return Double.valueOf(getOnlineElement("version", "0"));
		}
	}

	/**
	 * Check if the plugin usable
	 * 
	 * @param md
	 * @param nextPluginIDs
	 * @return
	 */
	public int canUse(YMessagesDialog md, String[] nextPluginIDs) {
		// check core
		if (getElement("core", null) != null && getElement("core", " ").indexOf(Double.toString(YAamsCore.VERSION)) == -1) {
			md.add(I18N.t("Kann {0} unter {1} nicht starten, da es für {2} geschrieben wurde.", getTitle(), YAamsCore.TITLE,
					getElement("core", "MISSING CORE")), Level.INFO_INT);
			Log.ger.info(getTitle() + " is disabled: Wrong YAams Version, written for " + getElement("core", "MISSING CORE"));
			return NOTUSEABLE;
		}

		// check class
		if (getElement("class", null) == null) {
			md.add(I18N.t("Kann {0} nicht starten, da keine Startklasse angegeben wurde.", getTitle()), Level.INFO_INT);
			Log.ger.info(getTitle() + " is disabled: Start Class is missing");
			return NOTUSEABLE;
		}

		// check depend
		String[] depend = getElement("depend", "").split(" ");
		for (String id : depend) {
			// skip?
			if (id == null || id.length() == 0) {
				continue;
			}

			// check it
			if (PluginManager.isInstall(id)) {
				if (PluginManager.isDisabled(id)) {
					md.add(I18N.t("Kann {0} nicht starten, da benötigtes Plugin {1} deaktiviert ist.", getTitle(), id), Level.INFO_INT);
					Log.ger.info(getTitle() + " is disabled: required Plugin is disabled: " + id);
					return NOTUSEABLE;
				}
			} else {
				// in wait query?
				if (Arrays.asList(nextPluginIDs).contains(id)) {
					return WAIT;
				}

				// not found?
				md.add(I18N.t("Kann {0} nicht starten, da benötigtes Modul {1} nicht installiert ist.", getTitle(), id), Level.INFO_INT);
				Log.ger.info(getTitle() + " is disabled: required Plugin is missing: " + id);
				return NOTUSEABLE;
			}
		}

		return USEABLE;
	}

	/**
	 * Start the plugin
	 */
	public BasePlugin startPluginClass(YMessagesDialog md) {

		try {
			startedPluginClass = (BasePlugin) Class.forName(getElement("class", null)).newInstance();
			startedPluginClass.setInfo(this);

			// start?
			if (!startedPluginClass.useable(md)) {
				startedPluginClass = null;
			}

			// start it
			if (startedPluginClass != null) {
				startedPluginClass.start();
			}

		} catch (Throwable t) {
			startedPluginClass = null;
			md.add(YEx.toString(I18N.t("Kann Plugin {0} nicht starten.", getTitle()), t, true), Level.WARN_INT);
		}

		return startedPluginClass;
	}

	/**
	 * Get the icon for this plugin
	 * 
	 * @return
	 */
	public String getIcon() {
		if (icon == null) {
			icon = getElement("icon", id);
			if (!IconCache.exist(icon)) {
				if (install == null) {
					icon = "web_plugin";
				} else {
					icon = "plugin";
				}
			}
		}
		return icon;
	}

	/**
	 * @return the pluginFolder
	 */
	public PluginFolder getPluginFolder() {
		return pluginFolder;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Helpermethod to build the action buttons for this plugin
	 * 
	 * @return
	 */
	public HashMap<String, JButton> getButtons() {
		if (buttons != null) {
			return buttons;
		}

		buttons = new HashMap<String, JButton>();

		// add en/disable
		if (install != null) {
			buttons.put("active", YFactory.b(I18N.t(PluginManager.isDisabled(id) ? "Aktivieren" : "Deaktivieren"),
					PluginManager.isDisabled(id) ? "on" : "off", new AE() {

						@Override
						public void run() {
							if (PluginManager.isDisabled(id)) {
								PluginManager.enable(id);
								buttons.get("active").setText("Deaktivieren");
								buttons.get("active").setIcon(IconCache.get("off"));
							} else {
								PluginManager.disable(id);
								buttons.get("active").setText("Aktivieren");
								buttons.get("active").setIcon(IconCache.get("on"));
							}

						}
					}));
		}

		// add homepage?
		if (getOnlineElementFirst("homepage", null) == null) {
			buttons.put("hp", YFactory.b(I18N.t("Homepage"), "web", new AE() {

				@Override
				public void run() {
					SystemHelper.openUrl(getOnlineElementFirst("homepage", "http://www.google.de?q=" + getTitle()));

				}
			}));
		}

		// add uninstall
		// b.put("uninstall", YFactory.b(I18N.t("Uninstall"), "plugin_del", new
		// AE() {
		//
		// @Override public void run() {
		// SystemHelper.openUrl(getOnlineElementFirst("homepage",
		// "http://www.google.de?q=" + getTitle()));
		//
		// }
		// }));

		// add update
		if (isUpdateAvaible()) {

			JButton b = YFactory.b(I18N.t("Update zu {0}", getOnlineElement("version", "MISSING VERSION")), "update", new AE() {

				@Override
				public void run() {

					SystemHelper.runThread(new Run() {

						@Override
						public void go() {
							updateInstall();
						}
					}, false);

				}
			});

			// only online avaible?
			if (install == null) {
				b.setText(I18N.t("Installation von {0}", getOnlineElement("version", "MISSING VERSION")));
				b.setIcon(IconCache.get("plugin_add"));
			}

			buttons.put("update", b);
		}

		return buttons;
	}

	/**
	 * Download the update, don't check if an update avaible, please use
	 * isUpdateAvaible()
	 */
	public void updateInstall() {
		if (buttons != null && buttons.containsKey("update")) {
			new SwingHelper() {

				@Override
				public void run() {
					buttons.get("update").setIcon(IconCache.get("restart"));
					buttons.get("update").setText(I18N.t("Benötigt Neustart"));
					buttons.get("update").setEnabled(false);
				}
			};
		}
		NetHelper.downloadFile(new File(pluginFolder.getFolder(), id + ".yex"), getOnlineElement("download", "??"));
	}
}
