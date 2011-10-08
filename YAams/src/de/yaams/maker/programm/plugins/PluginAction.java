/**
 * 
 */
package de.yaams.maker.programm.plugins;

import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JPanel;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.list.BasisAction;
import de.yaams.maker.programm.plugins.core.PluginInfo;

/**
 * @author Nebli
 * 
 */
public class PluginAction extends BasisAction {

	protected PluginInfo plugin;

	// protected IEditorPlugin ePlugin;

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public PluginAction(PluginInfo plugin) {
		super(plugin == null ? I18N.t("Overview") : plugin.getTitle(), plugin == null ? I18N.t("Management all plugins") : plugin
				.getUpdateMessage().getText(), plugin == null ? "plugin" : plugin.getIcon());
		this.plugin = plugin;
		if (plugin != null) {
			// ePlugin = (IEditorPlugin) plugin.getStartedPluginClass();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.BasisAction#perform()
	 */
	@Override
	public void perform() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.BasisAction#perform()
	 */
	public void show(JPanel content) {

		// build panel
		content.removeAll();

		// add basics
		FormBuilder form = new FormBuilder("plugin.action");
		form.setCenter(YFactory.getBrowserPanel(plugin.getOnlineElement("info", "http://packages.yaams.de/system/pluginNoInfo.html")));

		// add buttons
		HashMap<String, JButton> but = plugin.getButtons();
		for (String id : but.keySet()) {
			form.addButton(id, but.get(id));
		}

		// FormInfo h = new FormInfo("", "<html><b>" + plugin.getTitle() +
		// "</b><br>" + plugin.getVersion() + "</html>");
		// h.getHeader().setIcon(IconCache.get(icon, 64));
		// form.addElement(h);
		// form.addElement(new FormTitle(I18N.t("Options"), "plugin_opts"));
		// form.addElement(new
		// FormCheckbox(I18N.t("Disable Plugin (Need Restart)"),
		// PluginManager.isDisabled(plugin
		// .getID())).addChangeListener(new FormElementChangeListener() {
		//
		// @Override public void stateChanged(FormElement e) {
		// // box ist selected or not?
		// if (!((JCheckBox) e.getElement()).isSelected()) {
		// PluginManager.enable(plugin.getID());
		// } else {
		// PluginManager.disable(plugin.getID());
		// }
		//
		// }
		// }));

		// add plugin thinks
		// ePlugin.addConfigPanel(form);

		// add buttons
		// if (plugin.getLink() != null) {
		// form.addButton(YFactory.b(I18N.t("Homepage"), "web", new AE() {
		//
		// @Override public void run() {
		// SystemHelper.openUrl(plugin.getLink());
		//
		// }
		// }));
		// }

		content.add(form.getPanel(true));
		content.invalidate();
		content.revalidate();

	}

	/**
	 * @return the plugin
	 */
	public PluginInfo getPlugin() {
		return plugin;
	}

}
