/**
 * 
 */
package de.yaams.maker.helper.gui.form;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.YFactory;

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
	protected JComponent getInternElement() {
		JPanel p = new JPanel(new BorderLayout());
		p.add(super.getInternElement(), BorderLayout.CENTER);
		// add select button
		if (rightButton != null) {
			p.add(rightButton, BorderLayout.EAST);
		}

		// add preview
		if (preview != null) {
			p.add(preview, BorderLayout.WEST);
		}

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
	protected void updatePreview() {
		if (preview != null) {
			preview.updateUI();
		}
	}

	/**
	 * @param preview
	 *            the preview to set
	 */
	public void setPreview(JComponent preview) {
		this.preview = preview;
	};

	/**
	 * En/Disable
	 * 
	 * @param value
	 * @return
	 */
	@Override
	public FormElement setEnabled(boolean value) {
		rightButton.setEnabled(value);
		return super.setEnabled(value);
	}
}
