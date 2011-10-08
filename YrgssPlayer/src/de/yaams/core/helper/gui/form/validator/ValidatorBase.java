/**
 * 
 */
package de.yaams.core.helper.gui.form.validator;

import de.yaams.core.helper.gui.YMessagesDialog;
import de.yaams.core.helper.gui.form.FormElement;

/**
 * @author Praktikant
 * 
 */
public abstract class ValidatorBase {

	protected FormElement form;

	/**
	 * Check if the field valide, if not add a message
	 * 
	 * @return
	 */
	public abstract void isValide(YMessagesDialog y);

	/**
	 * @return the form
	 */
	public FormElement getForm() {
		return form;
	}

	/**
	 * @param form
	 *            the form to set
	 */
	public void setForm(FormElement form) {
		this.form = form;
	}

}
