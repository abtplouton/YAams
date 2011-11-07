/**
 * 
 */
package de.yaams.maker.helper.gui.form;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.yaams.maker.helper.gui.rightclick.YRightClickMenuText;

/**
 * @author Nebli
 * 
 */
public class FormTextField extends FormSaveElement {
	
	protected JTextField field;
	
	/**
	 * @param title
	 * @param element
	 */
	public FormTextField(String title, String content) {
		super(title);
		
		field = new JTextField();
		YRightClickMenuText.install(field);
		element = field;
		field.setText(content);
	}
	
	/**
	 * @return the element
	 */
	@Override public JComponent getElement(boolean withSaveFunction) {
		if (withSaveFunction) {
			field.getDocument().addDocumentListener(new DocumentListener() {
				
				@Override public void removeUpdate(DocumentEvent e) {
					// save it
					informListeners();
				}
				
				@Override public void insertUpdate(DocumentEvent e) {
					// save it
					informListeners();
				}
				
				@Override public void changedUpdate(DocumentEvent e) {}
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
		return field.getText();
	}
	
}
