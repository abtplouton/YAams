/**
 * 
 */
package de.yaams.maker.helper.gui.form;

import java.io.File;

import javax.swing.JFileChooser;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.validator.ValidatorFile;
import de.yaams.maker.helper.gui.form.validator.ValidatorNotEmpty;
import de.yaams.maker.helper.integration.EditorIntegration;

/**
 * @author Nebli
 * 
 */
public class FormFileSelectField extends FormTextFieldButton {

	protected int typ;
	protected boolean dir;

	/**
	 * 
	 * @param title
	 * @param file
	 * @param typ
	 *            , JFileChooser.OPEN_DIALOG or JFileChooser.SAVE_DIALOG
	 * @param dir
	 *            , select dir?
	 * @param checkWrite
	 */
	public FormFileSelectField(String title, File file, int typ, boolean dir, boolean checkWrite) {
		super(title, file != null ? file.getAbsolutePath() : "");
		this.typ = typ;
		this.dir = dir;

		createRightButton("folder", I18N.t("Datei auswählen"));

		// add it
		addValidator(new ValidatorNotEmpty()).addValidator(new ValidatorFile(dir, checkWrite));
	}

	/**
	 * Helpermethod to run the button
	 */
	protected void runRightButton() {
		File select;
		if (typ == JFileChooser.OPEN_DIALOG) {
			File[] f = EditorIntegration.openDialog(false, dir);
			select = f.length == 0 ? null : f[0];
		} else {
			select = EditorIntegration.saveDialog();
		}
		if (select != null) {
			field.setText(select.getAbsolutePath());
		}
	};

}
