/**
 * 
 */
package de.yaams.maker.programm.ress;

import java.io.File;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.FormTextFieldButton;
import de.yaams.maker.programm.project.Project;

/**
 * @author abt
 * 
 */
public abstract class FormRessElement extends FormTextFieldButton {

	public static enum Typ {
		ABSOLUTE, RELATIVE, ONLYNAME
	}

	protected Typ typ;
	protected Project project;
	protected String folder;
	protected File selected;

	/**
	 * Create a new FormRessElement
	 * 
	 * @param title
	 * @param project
	 * @param folder
	 * @param file
	 * @param typ
	 */
	public FormRessElement(String title, Project project, String folder, String file, FormRessElement.Typ typ) {
		super(title, "");

		this.typ = typ;
		this.folder = folder;
		this.project = project;
		selected = file == null ? null : new File(project.getPath(), folder + File.separator + file);

		createRightButton("folder_ress", I18N.t("Wähle die Ressource aus"));
		updateField();
	}

	/**
	 * Helpermethod to update the profil
	 */
	protected void updateField() {
		String erg = "";
		// check type
		if (selected != null) {
			// set full path
			if (typ == Typ.ABSOLUTE) {
				erg = selected.getAbsolutePath();
			}

			// set path for programm
			if (typ == Typ.RELATIVE) {
				String[] s = selected.getAbsolutePath().toString().split(project.getPath().getAbsolutePath());
				erg = s[1];
			}

			// set only filename
			if (typ == Typ.ONLYNAME) {
				erg = selected.getName();
			}
		}
		field.setText(erg);
		updatePreview();
	}

	/**
	 * Helpermethod to run the button
	 */
	@Override
	protected void runRightButton() {
		File select = RessRess.showRessSelector(this.project, this.folder, field.getText());

		// save
		if (select != null) {
			selected = select;
			updateField();
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.core.helper.gui.form.FormElement#getContentAsString()
	 */
	@Override
	public String getContentAsString() {
		return selected == null ? null : super.getContentAsString();
	}

	/**
	 * @return the selected
	 */
	public File getSelected() {
		return selected;
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @return the folder
	 */
	public String getFolder() {
		return folder;
	}

	/**
	 * @param folder
	 *            the folder to set
	 */
	public void setFolder(String folder) {
		this.folder = folder;
	}
}
