/**
 * 
 */
package de.yaams.maker.helper.gui.form;

import javax.swing.JLabel;

import de.yaams.maker.helper.gui.icons.IconCache;

/**
 * @author Nebli
 * 
 */
public class FormInfo extends FormElement {

	protected JLabel content;

	/**
	 * @param title
	 */
	public FormInfo(final String title, final Object content) {
		super(title);
		// add content
		if (content != null) {
			if (content instanceof JLabel) {
				this.content = (JLabel) content;
			} else {
				final String s = content.toString();
				this.content = new JLabel(s.length() > 1 && s.charAt(0) == '@' ? s.substring(1) : "<html>" + s + "</html>");
			}
		}
		element = this.content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.form.FormElement#getContentAsString()
	 */
	@Override
	public String getContentAsString() {
		return content != null ? content.getText() : null;
	}

	/**
	 * @return the content
	 */
	public JLabel getContent() {
		return content;
	}

	/**
	 * Set Spezific Icon for the header label
	 * 
	 * @param icon
	 * @param size
	 * @return
	 */
	public FormInfo setIcon(String icon, int size) {
		header.setIcon(IconCache.get(icon, size));

		return this;
	}

}
