/**
 * 
 */
package de.yaams.extensions.devtools;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.extensions.IExtension;
import de.yaams.maker.helper.gui.YMessagesDialog;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.icons.FormIcon;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.helper.gui.tabs.SplitActionListElement;
import de.yaams.maker.programm.environment.YLevel;
import de.yaams.maker.programm.plugins.core.BasePlugin;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.tabs.OptionsTab;

/**
 * @author Nebli
 * 
 */
public class DevToolsPlugin extends BasePlugin {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.core.helper.plugins.BasePlugin#useable(de.yaams.core.helper.
	 * gui.YMessagesDialog, int)
	 */
	@Override
	public boolean useable(YMessagesDialog md) {
		return isVersionInstall(null, 0.0053, -1, md);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.core.helper.plugins.BasePlugin#start()
	 */
	@Override
	public void start() {

		ExtentionManagement.add(OptionsTab.EXADD, new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {
				// right level?
				if (!YLevel.IS_DEVELOPER) {
					return;
				}

				ArrayList<BasisListElement> ary = (ArrayList<BasisListElement>) objects.get("list");

				// add setting
				ary.add(new SplitActionListElement(I18N.t("DevTools"), null, "opts") {

					@Override
					protected Component getComponent(final Project p) {
						FormBuilder f = new FormBuilder("options.dev");

						// add it
						f.addElement("basic.icon", new FormIcon(I18N.t("Icon"), "", null));

						return f.getPanel(true);
					}
				});
			}
		});
	}
}
