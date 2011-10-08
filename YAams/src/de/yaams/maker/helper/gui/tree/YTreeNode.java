/**
 * 
 */
package de.yaams.maker.helper.gui.tree;

import javax.swing.Icon;

import de.yaams.maker.helper.gui.icons.IconCache;

/**
 * @author abt
 * 
 */
public class YTreeNode {
	
	private String action, translation;
	private final String icoName;
	private Icon ico;
	
	/**
	 * Create a new TreeNode
	 * 
	 * @param action
	 * @param ico
	 */
	public YTreeNode(final String action, final String ico, final String translation) {
		super();
		this.action = action;
		this.ico = IconCache.get(ico);
		icoName = ico;
		this.translation = translation;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return action;
	}
	
	/**
	 * @return the title
	 */
	public String getAction() {
		return action;
	}
	
	/**
	 * @return the title
	 */
	public String getTranslation() {
		return translation;
	}
	
	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(final String title) {
		action = title;
	}
	
	/**
	 * @return the ico
	 */
	public Icon getIco() {
		return ico;
	}
	
	/**
	 * @param ico
	 *            the ico to set
	 */
	public void setIco(final Icon ico) {
		this.ico = ico;
	}
	
	/**
	 * @param translation
	 *            the translation to set
	 */
	public void setTranslation(final String translation) {
		this.translation = translation;
	}
	
	/**
	 * @return the icoName
	 */
	public String getIcoName() {
		return icoName;
	}
	
}
