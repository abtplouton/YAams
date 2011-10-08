/**
 * 
 */
package de.yaams.core.helper.gui.form;

import javax.swing.JComponent;

/**
 * @author abt
 * 
 */
public class FormCustom extends FormElement {

	/**
	 * Create a new FormCustom
	 * 
	 * @param title
	 */
	public FormCustom(String title, JComponent element) {
		super(title);
		this.element = element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.core.helper.gui.form.FormElement#getContentAsString()
	 */
	@Override
	public String getContentAsString() {
		return element.toString();
	}

}
