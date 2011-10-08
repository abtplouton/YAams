/**
 * 
 */
package de.yaams.maker.helper.gui.form;

import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jidesoft.swing.AutoResizingTextArea;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.rightclick.YRightClickMenu;

/**
 * @author Nebli
 * 
 */
public class FormTextArea extends FormSaveElement {

	protected AutoResizingTextArea area;

	/**
	 * @param title
	 * @param element
	 */
	public FormTextArea(String title, String text) {
		super(title);

		area = new AutoResizingTextArea(2, 20);
		area.setText(text);

		YRightClickMenu.installRightClickMenu(area);
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

	/**
	 * Get intern element, only for rendering
	 * 
	 * @return
	 */
	@Override
	protected JComponent getInternElement() {
		return YFactory.createOverlayTextArea(area, I18N.t("Erstelle {0} hier.", header.getText()));
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
