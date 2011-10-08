/**
 * 
 */
package de.yaams.extensions.jruby;

import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jidesoft.editor.CodeEditor;

import de.yaams.maker.helper.gui.form.FormSaveElement;

/**
 * @author Nebli
 * 
 */
public class FormCode extends FormSaveElement {

	protected CodeEditor code;

	/**
	 * @param title
	 * @param element
	 */
	public FormCode(String title, String text) {
		super(title);

		code = JRubyPlugin.getCodeEditor(text);

		element = code;
	}

	/**
	 * @return the element
	 */
	@Override
	public JComponent getElement(boolean withSaveFunction) {
		if (withSaveFunction) {
			code.getDocument().addDocumentListener(new DocumentListener() {

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
		return code.getText();
	}

}
