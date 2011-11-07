/**
 * 
 */
package de.yaams.extensions.rgssproject;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.SystemUtils;

import de.yaams.extensions.rgssproject.database.DatabasePlugin;
import de.yaams.extensions.rgssproject.icon.RGSSIcons;
import de.yaams.extensions.rgssproject.map.MapPlugin;
import de.yaams.extensions.rgssproject.script.ScriptTab;
import de.yaams.extensions.rgssproject.type.RpgXpProjectType;
import de.yaams.extensions.rgssproject.wizard.WizardRpgXpVxLicenePage;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.Setting;
import de.yaams.maker.helper.SystemHelper;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.extensions.IExtension;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.YMessagesDialog;
import de.yaams.maker.helper.gui.YSettingHelper;
import de.yaams.maker.helper.gui.dock.DockLinkPanel;
import de.yaams.maker.helper.gui.form.FormLink;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.helper.gui.tabs.SplitActionListElement;
import de.yaams.maker.helper.helpcenter.HelpCenterManagement;
import de.yaams.maker.helper.wizard.WizardManagement;
import de.yaams.maker.programm.YaFrame;
import de.yaams.maker.programm.plugins.BasePlugin;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.ProjectManagement;
import de.yaams.maker.programm.tabs.OptionsTab;

/**
 * @author Praktikant
 * 
 */
public class RGSSProjectPlugin extends BasePlugin {

	/**
	 * 
	 */
	public RGSSProjectPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.plugins.core.BasePlugin#start()
	 */
	@Override
	public void start() {

		RGSSIcons.addIcons();

		RTP.init();

		// register type
		ProjectManagement.registerType(new RpgXpProjectType());

		// check licence
		if (!Setting.exist("licence.xp") || !Setting.exist("licence.vx")) {
			WizardManagement.addPage(new WizardRpgXpVxLicenePage());
		}

		// add tabs
		YaFrame.registerTab(new RGSSTabEvent());

		// add credits
		ExtentionManagement.add(OptionsTab.OPTIONS_INFO, new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {
				FormBuilder f = (FormBuilder) objects.get("form");
				f.addElement("thx.colorpicker", new FormLink("javagraphics for Color Picker",
						"http://javagraphics.blogspot.com/2007/04/jcolorchooser-making-alternative.html"));

			}
		});

		// add options
		ExtentionManagement.add(OptionsTab.EXADD, new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {
				ArrayList<BasisListElement> list = (ArrayList<BasisListElement>) objects.get("list");

				list.add(new SplitActionListElement(I18N.t("RGSS Project"), null, "rgssproject") {

					@Override
					protected Component getComponent(Project p) {
						final FormBuilder f = new FormBuilder("options.rgss");

						// add licences
						f.getHeader("basic").setTitle(I18N.t("Lizenzen")).setIcon("licence").setColumn(4);
						f.addElement("basic.xp", YSettingHelper.bool(null, I18N.t("Lizenz für den XP"), "licence.xp", false));
						f.addElement("basic.vx", YSettingHelper.bool(null, I18N.t("Lizenz für den VX"), "licence.vx", false));

						return f.getPanel(true);
					}
				});

			}
		});

		// add open in maker
		// TODO
		ExtentionManagement.add("rgssproject.hometag", new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {

				final Project p = (Project) objects.get("project");
				int rgss = RGSSProjectHelper.getRGSSVersion(p);

				// right typ?
				if (!SystemUtils.IS_OS_WINDOWS || rgss < 1 || rgss > 2) {
					return;
				}

				// licence?
				if (rgss == 1 && !Setting.get("licence.xp", false) || rgss == 2 && !Setting.get("licence.vx", false)) {
					return;
				}

				final DockLinkPanel link = (DockLinkPanel) objects.get("link");

				// add
				if (rgss == 1) {
					link.addLink(I18N.t("Im RPG Maker XP öffnen"), "rpgxp", new AE() {

						@Override
						public void run() {
							// which type?
							SystemHelper.viewFile(new File(p.getPath(), "Game.rxproj"));

						}
					});
				}

				// add2
				if (rgss == 2) {
					link.addLink(I18N.t("Im RPG Maker VX öffnen"), "rpgvx", new AE() {

						@Override
						public void run() {
							// which type?
							SystemHelper.viewFile(new File(p.getPath(), "Game.vxproj"));

						}
					});
				}

			}
		});

		// run another "plugins"
		TestPlay.addTestPlay();
		DatabasePlugin.start();
		GameOptions.addGameOptions();
		new MapPlugin();

		// add help
		HelpCenterManagement.register("scripting", I18N.t("Scripting"), "script", ScriptTab.class.getResource("Scripting.html"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.plugins.core.BasePlugin#useable(de.yaams.maker
	 * .helper.gui.YMessagesDialog)
	 */
	@Override
	public boolean useable(YMessagesDialog md) {
		return isVersionInstall(null, 0.6, 0.7, md) && isVersionInstall("jruby", 1.92, -1, md)
				&& isVersionInstall("basemap", 1.005, -1, md);
	}

}
