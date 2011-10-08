/**
 * 
 */
package de.yaams.core.helper.gui.form;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author Nebli
 * 
 */
public class FormTextArea extends FormSaveElement {

	protected JTextArea area;

	/**
	 * @param title
	 * @param element
	 */
	public FormTextArea(String title, String text) {
		super(title);

		area = new JTextArea(5, 20);
		area.setText(text);
		element = area;
	}

	/**
	 * @return the element
	 */
	@Override
	public JComponent getElement(boolean withSaveFunction) {
		if (withSaveFunction) {
			area.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void removeUpdate(DocumentEvent e) {
					// save it
					informListeners();
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					// save it
					informListeners();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {}
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
		return area.getText();
	}

}
