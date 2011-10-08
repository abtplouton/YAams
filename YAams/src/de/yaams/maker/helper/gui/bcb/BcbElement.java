/**
 * 
 */
package de.yaams.maker.helper.gui.bcb;

import javax.swing.tree.DefaultMutableTreeNode;

import de.yaams.maker.helper.gui.AE;

/**
 * @author abby
 * 
 */
public class BcbElement extends DefaultMutableTreeNode {

	private static final long serialVersionUID = -8982881290568011877L;

	protected String title, icon;
	protected AE action;

	/**
	 * 
	 */
	public BcbElement(String title, String icon, AE action) {
		super(title);
		this.title = title;
		this.icon = icon;
		this.action = action;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @return the action
	 */
	public AE getAction() {
		return action;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

}
