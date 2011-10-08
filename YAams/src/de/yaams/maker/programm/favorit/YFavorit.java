/**
 * 
 */
package de.yaams.maker.programm.favorit;

import de.yaams.maker.helper.gui.list.BasisListElement;

/**
 * @author abby
 * 
 */
public class YFavorit extends BasisListElement {

	protected String tab;

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public YFavorit(String title, String icon, String tab) {
		super(title, null, icon);
		this.tab = tab;
	}

	/**
	 * @return the tab
	 */
	public String getTab() {
		return tab;
	}

	/**
	 * @param tab
	 *            the tab to set
	 */
	public void setTab(String tab) {
		this.tab = tab;
	}

}
