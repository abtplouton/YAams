/**
 * 
 */
package de.yaams.extensions.diamant.tileset;

/**
 * @author Nebli
 * 
 */
public class BasisListElement {

	protected String title, desc, icon;
	protected boolean modified;

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public BasisListElement(final String title, final String desc, final String icon) {
		super();
		this.title = title;
		this.desc = desc;
		this.icon = icon;
		modified = false;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return title;
	}

	/**
	 * @return the modified
	 */
	public boolean isModified() {
		return modified;
	}

	/**
	 * @param modified
	 *            the modified to set
	 */
	public void setModified(final boolean modified) {
		this.modified = modified;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

}
