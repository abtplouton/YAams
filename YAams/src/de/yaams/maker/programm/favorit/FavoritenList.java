/**
 * 
 */
package de.yaams.maker.programm.favorit;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.YDialog;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.list.YBasisListElementArrayList;

/**
 * @author abby
 * 
 */
public class FavoritenList extends YBasisListElementArrayList {

	private static final long serialVersionUID = 648095255596023688L;

	/**
	 * @param ary
	 */
	public FavoritenList() {
		super(FavoritManagement.getFavorit());

		config = true;
		delete = true;
		buildToolbar(I18N.t("Tab-Favorit"), "fav");

		toolbar.addRight(YFactory.tb(I18N.t("Info"), "help", new AE() {

			@Override
			public void run() {
				YDialog.ok(
						"Tab-Favoriten",
						I18N.t("Hier können die Tab-Favoriten verwaltet werden. Das Hinzufügen ist nur über den speziellen Tab möglich. Die Veränderungen werden erst beim Neuaufrufen des Tabs übernommen."),
						"fav");

			}
		}));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.gui.list.YSimpleList#add()
	 */
	@Override
	protected void add() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.gui.list.YSimpleList#info()
	 */
	@Override
	protected void info() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.gui.list.YSimpleList#configIntern()
	 */
	@Override
	protected void configIntern() {
		FavoritManagement.config((YFavorit) getSelectedObject());
	}

}
