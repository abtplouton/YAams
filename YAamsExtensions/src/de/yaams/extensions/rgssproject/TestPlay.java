/**
 * 
 */
package de.yaams.extensions.rgssproject;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.SystemUtils;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.NetHelper;
import de.yaams.maker.helper.SystemHelper;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.extensions.IExtension;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.YSettingHelper;
import de.yaams.maker.helper.gui.form.FormButton;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.helper.gui.tabs.SplitActionListElement;
import de.yaams.maker.programm.YAamsCore;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.ProjectSett;
import de.yaams.maker.programm.project.tab.ProjectOptionsTab;
import de.yaams.maker.programm.project.tab.ProjectTab;
import de.yaams.maker.programm.tabs.YaTab;

/**
 * @author abby
 * 
 */
public class TestPlay {

	/**
	 * @param args
	 */
	public static void addTestPlay() {
		// add play button
		ExtentionManagement.add("yaframe.openTab", new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {
				YaTab tab = (YaTab) objects.get("tab");

				// right tab?
				if (tab instanceof ProjectTab) {

					// has project?
					final Project p = ((ProjectTab) tab).getProject();
					if (!RGSSProjectHelper.is(p, true, true)) {
						return;
					}

					// add run button
					if (ProjectSett.get(p, "run_showOnEveryTab", true) && tab.getToolbar() != null) {
						tab.getToolbar().add(YFactory.tb(I18N.t("Start ein Testspiel"), "monitor_opts", new AE() {

							@Override
							public void run() {
								runProject(p);

							}
						}));
					}
				}

			}
		});

		// add options
		// add run settings
		ExtentionManagement.add(ProjectOptionsTab.EXADD, new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {
				ArrayList<BasisListElement> eles = (ArrayList<BasisListElement>) objects.get("list");

				// right project?
				if (!RGSSProjectHelper.is((Project) objects.get("project"), true, true)) {
					return;
				}

				// add it
				eles.add(new SplitActionListElement(I18N.t("Starte Spiel"), "", "monitor_opts") {

					@Override
					protected Component getComponent(final Project p) {

						// add settings
						FormBuilder f = new FormBuilder("testgame");
						f.getHeader("basic").setTitle(I18N.t("Starte Spiel")).setIcon("monitor_opts");
						f.addElement("basic.native",
								YSettingHelper.bool(p, I18N.t("Benutze native Engine, wenn möglich. (Game.exe)"), "run_native", true));

						// add display
						f.addElement("basic.button",
								YSettingHelper.bool(p, I18N.t("Zeige 'Start ein Testspiel' auf jedem Tab."), "run_showOnEveryTab", true));

						// add display
						f.addElement("basic.play", new FormButton(I18N.t("Starte Spiel"), "monitor_opts", new AE() {

							@Override
							public void run() {
								runProject(p);

							}
						}));

						return f.getPanel(true);
					}
				});
			}
		});

	}

	/**
	 * Start the testplay button
	 * 
	 * @param p
	 */
	public static void runProject(final Project p) {
		// save all open tabs
		p.save();

		new Thread(new Runnable() {

			@Override
			public void run() {

				// native or generic launcher?
				if (SystemUtils.IS_OS_WINDOWS && ProjectSett.get(p, "run_native", true)) {
					SystemHelper.viewFile(new File(p.getPath(), "Game.exe"));
				} else {
					File file = new File(YAamsCore.programPath, "YAamsRGSSPlayer.jar");

					// exist?
					if (!file.exists()) {
						// download it!
						NetHelper.downloadFile(file, NetHelper.getContentAsString("http://www.yaams.de/file/index.php?typ=player"));
					}

					// run it
					SystemHelper.runExternal(new String[] { "java", "-jar", "-Xms128m", "-Xmx512M", file.getAbsolutePath(),
							p.getPath().getAbsolutePath() }, false);
				}
			}
		}).start();

	}

}
