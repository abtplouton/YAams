/**
 * 
 */
package de.yaams.core.helper.gui.form;

/**
 * @author abt
 * 
 */
public interface FormElementChangeListener {

	/**
	 * This method will call, when a chance in the form element is detect
	 * 
	 * @param form
	 */
	public abstract void stateChanged(FormElement form);
}
