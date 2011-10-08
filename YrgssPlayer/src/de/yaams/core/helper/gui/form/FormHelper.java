/**
 * 
 */
package de.yaams.core.helper.gui.form;

/**
 * @author abt
 * 
 */
public class FormHelper {

	/**
	 * Helpermethod to en/disabeld an element, if in the form combobox the right
	 * element selected
	 * 
	 * @param source
	 * @param dest
	 * @param value
	 * @return
	 */
	public static FormElement setEnabeldWhenRightElementSelect(FormElement source, final FormElement dest, final String value) {
		source.addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				dest.setEnabled(value.equals(form.getContentAsString()));

			}
		});
		return dest;
	}

	/**
	 * Helpermethod to en/disabeld an element, if in the form combobox not the
	 * right element selected
	 * 
	 * @param source
	 * @param dest
	 * @param value
	 * @return
	 */
	public static FormElement setEnabeldWhenNotRightElementSelect(FormElement source, final FormElement dest, final String value) {
		source.addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				dest.setEnabled(!value.equals(form.getContentAsString()));

			}
		});
		return dest;
	}

}
