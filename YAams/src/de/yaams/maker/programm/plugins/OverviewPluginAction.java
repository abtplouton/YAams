/**
 * 
 */
package de.yaams.maker.programm.plugins;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.SystemHelper;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.Run;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.form.FormCustom;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.icons.IconCache;
import de.yaams.maker.programm.plugins.core.PluginInfo;
import de.yaams.maker.programm.plugins.core.PluginManager;

/**
 * @author Nebli
 * 
 */
public class OverviewPluginAction extends PluginAction {

	/**
	 * @param plugin
	 */
	public OverviewPluginAction() {
		super(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.BasisAction#perform()
	 */
	@Override
	public void show(JPanel content) {

		// build panel
		content.removeAll();

		// build panel
		final FormBuilder form = new FormBuilder("plugin.overview");

		final HashMap<String, HashMap<String, JButton>> buttons = new HashMap<String, HashMap<String, JButton>>();

		for (String id : PluginManager.getInfo().keySet()) {
			// get basics
			PluginInfo p = PluginManager.getInfo(id);
			buttons.put(id, p.getButtons());

			// build buttonpanel
			JPanel b = new JPanel(new FlowLayout(FlowLayout.RIGHT, 1, 1));
			for (String key : buttons.get(id).keySet()) {
				b.add(buttons.get(id).get(key));
			}

			// build basic
			JPanel e = new JPanel(new BorderLayout());
			JLabel l = new JLabel(p.getTitle());
			l.setIcon(IconCache.get(p.getIcon(), 32));
			e.add(l, BorderLayout.CENTER);
			e.add(p.getUpdateMessage(), BorderLayout.SOUTH);

			form.addElement("basic." + id, new FormCustom("", e));
			form.addElement("basic." + id + "b", new FormCustom("", b));
		}

		// add main buttons
		form.addButton("update", YFactory.b(I18N.t("Alle aktualisieren"), "update", new AE() {

			@Override
			public void run() {
				((JButton) form.getButtons().get("update")).setIcon(IconCache.get("restart"));
				((JButton) form.getButtons().get("update")).setText(I18N.t("Benötigt Neustart"));
				((JButton) form.getButtons().get("update")).setEnabled(false);

				// call all buttons
				SystemHelper.runThread(new Run() {

					@Override
					public void go() {
						for (String id : PluginManager.getInfo().keySet()) {
							if (PluginManager.getInfo(id).isUpdateAvaible()) {
								PluginManager.getInfo(id).updateInstall();
							}
						}
					}
				}, false);

			}
		}));

		// add main buttons
		form.addButton("active", YFactory.b(I18N.t("Alle aktivieren"), "on", null));

		// FormBuilder form = new FormBuilder();
		//
		// // edit Game.ini
		// try {
		// final Wini ini = new Wini(catalog);
		//
		// // add core update?
		// FormInfo h = new FormInfo("", YAamsCore.yaamsname);
		// h.getHeader().setIcon(IconCache.get("yaams", 32));
		// form.addElement(h);
		//
		// // update?
		// if (ini.get("core", "version").equals(YAamsCore.yaamsversion)) {
		// h = new FormInfo("",
		// I18N.t("You have installed {0}. The newest Version is {1}",
		// YAamsCore.yaamsversion, ini.get("core", "version")));
		// h.getHeader().setIcon(IconCache.get("warn"));
		// h.addSubElement(new FormButton(I18N.t("Update to {0}",
		// ini.get("core", "version")), "update", new AE() {
		//
		// @Override public void run() {
		// // call it
		// SystemHelper.openUrl("http://www.yaams.de/download.html");
		//
		// }
		// }));
		// form.addElement(h);
		// } else {
		// h = new FormInfo("",
		// I18N.t("You have the newest Version {0} installed.",
		// YAamsCore.yaamsversion));
		// h.getHeader().setIcon(IconCache.get("ok"));
		// form.addElement(h);
		// }
		//
		// // add all
		// for (final String id : ini.keySet()) {
		//
		// // wrong?
		// if ("core".equals(id)) {
		// continue;
		// }
		//
		// // show it
		// InfoPlugin i = new InfoPlugin(ini.get(id), id);
		// h = new FormInfo("", "<html><b>" + i.getTitle() + "</b><br>" +
		// i.getVersion() + "</html>");
		// h.getHeader().setIcon(IconCache.get("plugin", 32));
		// form.addElement(h);
		//
		// // create button
		// final FormButton b = new FormButton(I18N.t("Update to {0}",
		// i.getVersion()), "update", new AE() {
		//
		// @Override public void run() {
		// new Thread(new Runnable() {
		//
		// @Override public void run() {
		// ((JButton) ae.getSource()).setEnabled(false);
		// // download it
		// File file = new File(YAamsCore.tmpFolder, id + ".yex");
		// NetHelper.downloadFile(file, ini.get(id, "path"));
		//
		// // add it
		// ArrayList<String> mess = new ArrayList<String>();
		// PluginManager.getFolder().get(0).addNewYex(file);
		// YDialog.okMessages(I18N.t("Add Plugin"), "plugin", mess);
		// ((JButton) ae.getSource()).setText(I18N.t("Need Restart"));
		// ((JButton) ae.getSource()).setIcon(IconCache.get("restart"));
		//
		// }
		// }).start();
		//
		// }
		// });
		//
		// // installed?
		// if (PluginManager.isInstall(id)) {
		// // check version
		// if (PluginManager.getInfo(id).getVersion() != i.getVersion()) {
		// h = new FormInfo("",
		// I18N.t("You have installed {0}. The newest Version is {1}",
		// PluginManager
		// .getInfo(id).getVersion(), i.getVersion()));
		// h.getHeader().setIcon(IconCache.get("warn"));
		// h.addSubElement(b);
		// form.addElement(h);
		// } else {
		// h = new FormInfo("",
		// I18N.t("You have the newest Version {0} installed.",
		// i.getVersion()));
		// h.getHeader().setIcon(IconCache.get("ok"));
		// form.addElement(h);
		// }
		// } else {
		// h = new FormInfo("", I18N.t("You can install Version {0}.",
		// i.getVersion()));
		// h.getHeader().setIcon(IconCache.get("info"));
		//
		// b.set(I18N.t("Install {0}", i.getVersion()), "plugin_add");
		// h.addSubElement(b);
		// form.addElement(h);
		//
		// }
		// }
		//
		// // found nothing?
		// if (ini.keySet().size() <= 1) {
		// h = new FormInfo("",
		// I18N.t("There are no compatible plugins. Please check your Internet connection."));
		// h.getHeader().setIcon(IconCache.get("error", 32));
		// form.addElement(h);
		// }
		//
		// } catch (Throwable t) {
		// YException.info("Can not edit " + catalog, t);
		//
		// }

		// finish it
		form.getHeader("basic").setColumn(4);
		content.add(form.getPanel(true));
		content.invalidate();
		content.revalidate();

	}
}
