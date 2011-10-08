/**
 * 
 */
package de.yaams.maker.helper.gui.form;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

/**
 * @author Nebli
 * 
 */
public class FormCheckbox extends FormSaveElement {
	
	protected JCheckBox box;
	
	/**
	 * @param title
	 */
	public FormCheckbox(String title, boolean selected) {
		super("");
		box = new JCheckBox(title);
		element = box;
		box.setSelected(selected);
	}
	
	/**
	 * @return the element
	 */
	@Override public JComponent getElement(boolean withSaveFunction) {
		if (withSaveFunction) {
			box.addItemListener(new ItemListener() {
				
				@Override public void itemStateChanged(ItemEvent e) {
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
	@Override public String getContentAsString() {
		return Boolean.toString(box.isSelected());
	}
	
}
