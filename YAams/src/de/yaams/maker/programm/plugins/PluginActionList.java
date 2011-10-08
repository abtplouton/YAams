/**
 * 
 */
package de.yaams.maker.programm.plugins;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.YDialog;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.helper.gui.list.YActionList;
import de.yaams.maker.helper.integration.EditorIntegration;
import de.yaams.maker.programm.plugins.core.PluginManager;

/**
 * @author Nebli
 * 
 */
public class PluginActionList extends YActionList {
	private static final long serialVersionUID = -2366666789457336459L;

	protected JPanel container;

	/**
	 * @param ary
	 */
	public PluginActionList(final JPanel container) {
		super(buildList());
		this.container = container;
		open = true;
		delete = true;
		buildToolbar(I18N.t("Plugin"), "plugin");
	}

	/**
	 * Delete the selected element, overwrite it to implements your own methods
	 * 
	 * @return
	 */
	@Override
	public void del() {
		// can delete?
		if (!canDelete()) {
			return;
		}

		// delete it
		if (PluginManager.uninstall(((PluginAction) getSelectedObject()).getPlugin().getId())) {
			super.delete();
		}

	}

	/**
	 * Helpermethod to build the list
	 * 
	 * @return
	 */
	private static ArrayList<BasisListElement> buildList() {
		final ArrayList<BasisListElement> ary = new ArrayList<BasisListElement>();
		ary.add(new OverviewPluginAction());
		for (final String s : PluginManager.getInfo().keySet()) {
			ary.add(new PluginAction(PluginManager.getInfo().get(s)));
		}
		return ary;
	}

	/**
	 * Edit the object, add the name value
	 */
	@Override
	protected void configIntern() {
		((PluginAction) getSelectedObject()).show(container);

	}

	/**
	 * This method will call, if the user will import a new element, normally
	 * this method call add(T) with the special element.
	 */
	@Override
	protected void open() {

		// Add files
		for (File f : EditorIntegration.openDialog(true, false, new FileFilter() {

			@Override
			public String getDescription() {
				return "*.yex";
			}

			@Override
			public boolean accept(final File f) {
				return f.getName().endsWith(".yex") || f.isDirectory();
			}
		})) {
			PluginManager.getFolder().get(0).addNewYex(f);
		}

		YDialog.ok(I18N.t("Add Plugins"), "plugin", I18N.t("Bitte neustarten, damit die Plugins geladen werden."));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#oneClick()
	 */
	@Override
	protected void selected() {
		config();
	}

}
