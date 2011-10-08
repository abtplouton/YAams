/**
 * 
 */
package de.yaams.maker.helper.gui.list;

/**
 * @author Nebli
 * 
 */
public abstract class BasisAction extends BasisListElement {

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public BasisAction(String title, String desc, String icon) {
		super(title, desc, icon);
	}

	/**
	 * Perform the action
	 */
	public abstract void perform();

}
