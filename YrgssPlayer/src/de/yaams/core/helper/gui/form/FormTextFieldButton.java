/**
 * 
 */
package de.yaams.core.helper.gui.form;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.yaams.core.helper.AE;
import de.yaams.core.helper.I18N;
import de.yaams.core.helper.gui.YFactory;

/**
 * @author Nebli
 * 
 */
public class FormTextFieldButton extends FormTextField {

	protected JLabel rightButton;
	protected JComponent preview;

	/**
	 * Create a new FormTextField
	 * 
	 * @param title
	 * @param content
	 */
	public FormTextFieldButton(String title, String content) {
		super(title, content);

		addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				// reload icon preview while typing
				updatePreview();

			}
		});
	}

	/**
	 * @return the element
	 */
	@Override
	public JComponent getElement(boolean withSaveFunction) {
		JPanel p = new JPanel(new BorderLayout());
		p.add(super.getElement(withSaveFunction), BorderLayout.CENTER);
		// add select button
		if (rightButton != null)
			p.add(rightButton, BorderLayout.EAST);

		// add preview
		if (preview != null)
			p.add(preview, BorderLayout.WEST);

		return p;
	}

	/**
	 * Create the button
	 * 
	 * @param icon
	 * @param title
	 */
	protected void createRightButton(String icon, String title) {
		rightButton = YFactory.tb(I18N.t("Datei auswählen"), "folder", new AE() {

			@Override
			public void run() {
				runRightButton();
				updatePreview();
			}
		});
	}

	/**
	 * Helpermethod to run the button
	 */
	protected void runRightButton() {};

	/**
	 * Helpermethod to run the button
	 */
	protected void updatePreview() {}

	/**
	 * @param preview
	 *            the preview to set
	 */
	public void setPreview(JComponent preview) {
		this.preview = preview;
	};
}
