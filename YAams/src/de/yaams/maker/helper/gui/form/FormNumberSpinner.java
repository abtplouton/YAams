/**
 * 
 */
package de.yaams.maker.helper.gui.form;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * @author Nebli
 * 
 */
public class FormNumberSpinner extends FormSaveElement {

	protected JSpinner field;

	/**
	 * @param title
	 * @param element
	 */
	public FormNumberSpinner(final String title, long value) {
		super(title);

		field = new JSpinner();
		field.setValue((int) value);
		element = field;
	}

	/**
	 * @return the element
	 */
	@Override
	public JComponent getElement(final boolean withSaveFunction) {
		if (withSaveFunction) {
			field.getModel().addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(final ChangeEvent ce) {

					field.invalidate();
					field.revalidate();

					// save it
					informListeners();
				}
			});
		}
		return super.getElement(withSaveFunction);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.form.FormElement#getContentAsString()
	 */
	@Override
	public String getContentAsString() {
		return field.getValue().toString();
	}

	/**
	 * Helpermethod to set the range
	 * 
	 * @param min
	 * @param max
	 * @param step
	 */
	public FormNumberSpinner setMinMax(int min, int max, int step) {
		//set
		((SpinnerNumberModel) field.getModel()).setMinimum(min);
		((SpinnerNumberModel) field.getModel()).setMaximum(max);
		((SpinnerNumberModel) field.getModel()).setStepSize(step);

		//fix it?
		if (Long.valueOf(field.getValue().toString()) < min){
			field.setValue(min);
		}
		if (Long.valueOf(field.getValue().toString()) > max){
			field.setValue(max);
		}
		
		return this;
	}

}
