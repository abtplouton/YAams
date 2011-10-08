/**
 * 
 */
package de.yaams.maker.programm.plugins;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.extensions.IExtension;
import de.yaams.maker.helper.gui.YMessagesDialog;
import de.yaams.maker.helper.gui.YSettingHelper;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.helper.gui.tabs.SplitActionListElement;
import de.yaams.maker.programm.YAamsCore;
import de.yaams.maker.programm.plugins.core.PluginFolder;
import de.yaams.maker.programm.plugins.core.PluginManager;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.tabs.OptionsTab;

/**
 * @author Praktikant
 * 
 */
public class PluginPlugin {

	public static final String AUTO = "plugins.autoupdate", CHECK = "plugins.check";

	/**
	 * @param args
	 */
	public static void start(YMessagesDialog mess) {

		// check folder count
		if (PluginManager.getFolder().size() == 0) {
			PluginManager.addFolder(new PluginFolder(new File(YAamsCore.programPath, "plugins"), "Default"));
		}

		PluginManager.start(mess);
		PluginManager.installOnlineInfo(false);

		// run over all plugins again
		// PluginManager.start(mess);

		// install options
		ExtentionManagement.add(OptionsTab.EXSYS, new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {
				FormBuilder f = (FormBuilder) objects.get("form");

				// add it
				f.addHeader("plugins", new FormHeader(I18N.t("Plugins"), "plugin").setColumn(4));
				f.addElement("plugins.inet", YSettingHelper.bool(null, I18N.t("Internetzugriff aktivieren"), "net.access", true));
				f.addElement(AUTO, YSettingHelper.bool(null, I18N.t("Autom. Installation von Plugins"), AUTO, true));
				f.addElement(CHECK, YSettingHelper.combo(null, I18N.t("Updatecheck"), CHECK, "168", new String[] { "0", "24", "168", "714",
						"-1" }, new String[] { "Bei jedem Start", "Täglich", "Wöchentlich", "Monatlich", "Manuelle Prüfung" }));
			}
		});

		// install page
		ExtentionManagement.add(OptionsTab.EXADD, new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {

				ArrayList<BasisListElement> ary = (ArrayList<BasisListElement>) objects.get("list");

				// add it
				ary.add(new SplitActionListElement(I18N.t("Plugins"), null, "plugin") {

					@Override
					protected Component getComponent(Project p) {
						return new PluginPanel();
					}
				});

			}
		});
	}

}
