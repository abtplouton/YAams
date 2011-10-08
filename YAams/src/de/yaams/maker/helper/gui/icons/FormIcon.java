/**
 * 
 */
package de.yaams.maker.helper.gui.icons;

import javax.swing.JLabel;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.YDialog;
import de.yaams.maker.helper.gui.form.FormTextFieldButton;

/**
 * @author Praktikant
 * 
 */
public class FormIcon extends FormTextFieldButton {

	protected String[] allowIcons;

	/**
	 * @param title
	 * @param content
	 */
	public FormIcon(String title, String content, String[] allowIcons) {
		super(title, content);
		this.allowIcons = allowIcons;

		preview = new JLabel();
		updatePreview();
		createRightButton("folder", I18N.t("Icon auswählen"));

		addValidator(new ValidatorIcon());
	}

	/**
	 * Helpermethod to run the button
	 */
	@Override
	protected void runRightButton() {
		IconBrowser i = new IconBrowser(allowIcons);

		// show it
		if (YDialog.show(I18N.t("Wähle das Icon"), "folder", i, true)) {
			// set it
			if (i.getSelectedIcon() != null) {
				field.setText(i.getSelectedIcon());
			}
		}
	};

	/**
	 * Helpermethod to run the button
	 */
	@Override
	protected void updatePreview() {
		((JLabel) preview).setIcon(IconCache.get(IconCache.exist(getContentAsString()) ? getContentAsString() : "dummy"));
	};
}
