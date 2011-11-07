/**
 * 
 */
package de.yaams.maker.helper.gui.form;

import javax.swing.JComponent;

import org.apache.commons.lang.Validate;

/**
 * @author abby
 * 
 */
public class FormSwing extends FormElement {

	/**
	 * @param title
	 */
	public FormSwing(String title, JComponent comp) {
		super(title);

		Validate.notNull(comp, "Swing Element is null");

		element = comp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.gui.form.FormElement#getContentAsString()
	 */
	@Override
	public String getContentAsString() {
		return element.toString();
	}

}
