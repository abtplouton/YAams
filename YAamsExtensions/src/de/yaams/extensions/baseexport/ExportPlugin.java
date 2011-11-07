/**
 * 
 */
package de.yaams.extensions.baseexport;

import de.yaams.maker.helper.gui.YMessagesDialog;
import de.yaams.maker.helper.gui.icons.IconCache;
import de.yaams.maker.programm.YaFrame;
import de.yaams.maker.programm.plugins.BasePlugin;

/**
 * @author Nebli
 * 
 */
public class ExportPlugin extends BasePlugin {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.core.helper.plugins.BasePlugin#useable(de.yaams.core.helper.
	 * gui.YMessagesDialog, int)
	 */
	@Override
	public boolean useable(YMessagesDialog md) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.core.helper.plugins.BasePlugin#start()
	 */
	@Override
	public void start() {
		// add standard icons
		IconCache.addPNG(ExportPlugin.class, "mac", "windows", "xbox", "export");

		// add tab
		YaFrame.registerTab(new ExportTabEvent());

		// add it
		// ExtentionManagement.add("rgssproject.hometag", new IExtension() {
		//
		// @Override
		// public void work(HashMap<String, Object> objects) {
		// // final Project project = (Project) objects.get("project");
		// final DockLinkPanel link = (DockLinkPanel) objects.get("link");
		//
		// // add
		// link.addLink(I18N.t("Test Game"), "monitor_opts", new AE() {
		//
		// @Override
		// public void run() {
		// // runProject(project);
		//
		// }
		// });
		//
		// link.addLink(I18N.t("Export"), "export", new AE() {
		//
		// @Override
		// public void run() {
		// YaFrame.open(ExportTab.ID);
		//
		// }
		// });
		//
		// }
		// });

	}

}
