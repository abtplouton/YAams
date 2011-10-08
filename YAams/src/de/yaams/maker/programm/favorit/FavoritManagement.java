/**
 * 
 */
package de.yaams.maker.programm.favorit;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import de.yaams.maker.helper.FileHelper;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.JavaHelper;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.extensions.IExtension;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.YDialog;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.YToolBar;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormTextField;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.icons.FormIcon;
import de.yaams.maker.helper.gui.icons.IconCache;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.helper.gui.tabs.SplitActionListElement;
import de.yaams.maker.programm.YAamsCore;
import de.yaams.maker.programm.YaFrame;
import de.yaams.maker.programm.environment.YLevel;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.tabs.BasicTabEvent;
import de.yaams.maker.programm.tabs.HomeTab;
import de.yaams.maker.programm.tabs.OptionsTab;
import de.yaams.maker.programm.tabs.TabEvent;
import de.yaams.maker.programm.tabs.YaTab;

/**
 * @author abby
 * 
 */
public class FavoritManagement {

	private static ArrayList<BasisListElement> favorit;
	private static ArrayList<String> lastTabs;

	/**
	 * 
	 */
	public FavoritManagement() {
		// TODO Auto-generated constructor stub
	}

	public synchronized static void init() {
		favorit = (ArrayList<BasisListElement>) FileHelper.loadXML(new File(YAamsCore.programPath, "favorit.xml"));
		// exist?
		if (favorit == null) {
			favorit = new ArrayList<BasisListElement>();
			add(new HomeTab());
		}

		lastTabs = (ArrayList<String>) FileHelper.loadXML(new File(YAamsCore.programPath, "lastTabs.xml"));
		// exist?
		if (lastTabs == null) {
			lastTabs = new ArrayList<String>();
		}

		// add it
		if (lastTabs.size() == 0) {
			lastTabs.add(BasicTabEvent.HOME);
		}

		// add save
		ExtentionManagement.add(ExtentionManagement.SAVE, new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {

				saveOpenTabs();
				// save favs
				FileHelper.saveXML(new File(YAamsCore.programPath, "favorit.xml"), favorit);

			}
		});

		// install page
		ExtentionManagement.add(OptionsTab.EXADD, new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {

				ArrayList<BasisListElement> ary = (ArrayList<BasisListElement>) objects.get("list");

				// add it
				ary.add(new SplitActionListElement(I18N.t("Tab-Favoriten"), null, "fav") {

					@Override
					protected Component getComponent(Project p) {
						// build list
						return new FavoritenList();
					}
				});

			}
		});
	}

	/**
	 * Add a tab to the favorit
	 * 
	 * @param tab
	 */
	public static void add(YaTab tab) {
		// add it
		favorit.add(new YFavorit(tab.getTitle(), tab.getIcon(), tab.getId()));
	}

	/**
	 * Add a tab to the favorit
	 * 
	 * @param tab
	 */
	public static boolean del(YFavorit fav) {
		// delete?
		if (YDialog.delete(fav.getTitle(), fav.getIcon())) {
			favorit.remove(fav);
			return true;
		}

		return false;

	}

	/**
	 * Config a fav
	 * 
	 * @param fav
	 */
	public static void config(final YFavorit fav) {
		// build config
		FormBuilder f = new FormBuilder("fav.config");
		f.addElement("basic.name", new FormTextField(I18N.t("Name"), fav.getTitle()).addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				fav.setTitle(form.getContentAsString());

			}
		}));
		f.addElement("basic.icon",
				new FormIcon(I18N.t("Icon"), fav.getIcon(), IconCache.games).addChangeListener(new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						fav.setIcon(form.getContentAsString());

					}
				}));
		// is dev?
		if (YLevel.IS_DEVELOPER) {
			f.addElement("basic.tab", new FormTextField(I18N.t("Tab"), fav.getTab()).addChangeListener(new FormElementChangeListener() {

				@Override
				public void stateChanged(FormElement form) {
					fav.setTab(form.getContentAsString());

				}
			}));
		}

		// show
		YDialog.showForm(I18N.t("Konfigure {0}", fav.getTitle()), "opts_" + fav.getIcon(), f);
	}

	/**
	 * @return the toolbar
	 */
	public static YToolBar getToolbar(final YaTab tab) {
		// start it?
		if (favorit == null) {
			init();
		}

		// create
		final YToolBar toolbar = new YToolBar();

		// add or del?
		boolean add = true;
		for (BasisListElement b : favorit) {

			// get it
			final YFavorit fav = (YFavorit) b;

			// check it
			if (fav.getTab() != null && fav.getTab().equals(tab.getId())) {
				add = false;

				// add remove
				toolbar.add(YFactory.tb(I18N.t("Tab aus den Favoriten löschen"), "fav_del", new AE() {

					@Override
					public void run() {
						// delete it
						if (del(fav)) {
							toolbar.getLeft().getComponent(0).setEnabled(false);
						}
					}
				}));

				break;
			}
		}

		if (add) {
			// add add
			toolbar.add(YFactory.tb(I18N.t("Tab als Favorit hinzufügen"), "fav_add", new AE() {

				@Override
				public void run() {
					add(tab);
					toolbar.getLeft().getComponent(0).setEnabled(false);
				}
			}));
		}

		// add config
		toolbar.add(YFactory.tb(I18N.t("Favoriten konfiguren"), "fav_opts", new AE() {

			@Override
			public void run() {
				YaFrame.open(TabEvent.buildParameter(HomeTab.OPTIONS, null, JavaHelper.createHashStringObj("select", "fav")));

			}
		}));

		toolbar.addSeparator();

		// add favoriten
		for (BasisListElement b : favorit) {

			// get it
			final YFavorit fav = (YFavorit) b;

			toolbar.add(YFactory.tb(fav.getTitle(), fav.getIcon(), new AE() {

				@Override
				public void run() {
					YaFrame.open(fav.getTab(), fav);

				}
			}));
		}

		toolbar.addSeparator();

		return toolbar;
	}

	/**
	 * Helpermethod to open the last saved tabs
	 */
	public static void openLastTabs() {
		try {
			// load the tabs
			for (String tabs : lastTabs) {
				if (YaFrame.existTab(tabs)) {
					YaFrame.open(tabs);
				}
			}
		} catch (Throwable t) {
			YEx.info("Can not open Tabs", t);
		}

	}

	/**
	 * Helpermethod to open the last saved tabs
	 */
	public static void saveOpenTabs() {
		// clear
		lastTabs.clear();

		// add it
		for (String s : YaFrame.get().getPane().getDocumentNames()) {
			lastTabs.add(s);
		}

		// save it
		FileHelper.saveXML(new File(YAamsCore.programPath, "lastTabs.xml"), lastTabs);
	}

	/**
	 * @return the favorit
	 */
	public static ArrayList<BasisListElement> getFavorit() {
		return favorit;
	}
}
