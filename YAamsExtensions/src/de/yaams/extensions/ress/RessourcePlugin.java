/**
 * 
 */
package de.yaams.extensions.ress;

import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JPanel;

import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.extensions.IExtension;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.YMessagesDialog;
import de.yaams.maker.helper.gui.form.FormLink;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.icons.IconCache;
import de.yaams.maker.programm.plugins.core.BasePlugin;
import de.yaams.maker.programm.ress.FormRessElement;
import de.yaams.maker.programm.ress.RessPanel;
import de.yaams.maker.programm.ress.RessourceList;
import de.yaams.maker.programm.tabs.OptionsTab;

/**
 * @author Praktikant
 * 
 */
public class RessourcePlugin extends BasePlugin {

	/**
	 * 
	 */
	public RessourcePlugin() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.plugins.core.BasePlugin#start()
	 */
	@Override
	public void start() {
		// add icons
		for (String s : new String[] { "imageeditor" }) {
			IconCache.addPNG(s, getClass());
		}

		// add credits
		ExtentionManagement.add(OptionsTab.OPTIONS_INFO, new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {
				FormBuilder f = (FormBuilder) objects.get("form");
				f.addElement("thx.imageeditor", new FormLink("JH Labs for ImageEditor", "http://www.jhlabs.com/ie/index.html"));

			}
		});

		// add panel
		ExtentionManagement.add("ress.panel", new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {
				RessPanel ress = (RessPanel) objects.get("panel");
				RessourceList list = (RessourceList) objects.get("list");

				// build it
				PreviewPanel preview = new PreviewPanel();
				JPanel left = new JPanel(new GridLayout(1, 1));

				// add it
				ress.setList(new RessourceXList(list.getProject(), list.getFolder(), preview, left));

				// add element
				ress.removeAll();
				ress.add(YFactory.createHorizontPanel(left, ress.getList(), "ressX.panel"));
				ress.invalidate();
				ress.revalidate();

			}
		});

		// add preview
		ExtentionManagement.add("formelement.FormRessElement", new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {
				FormRessElement ress = (FormRessElement) objects.get("form");

				final PreviewIcon p = new PreviewIcon(ress);

				// add preview
				ress.setPreview(p);

			}
		});
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
		return true;
	}

}
