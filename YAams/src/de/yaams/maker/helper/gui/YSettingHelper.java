/**
 * 
 */
package de.yaams.maker.helper.gui;

import de.yaams.maker.helper.Setting;
import de.yaams.maker.helper.gui.form.FormCheckbox;
import de.yaams.maker.helper.gui.form.FormComboBox;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormNumberSpinner;
import de.yaams.maker.helper.gui.form.FormTextArea;
import de.yaams.maker.helper.gui.form.FormTextField;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.ProjectSett;

/**
 * @author abt
 * 
 */
public class YSettingHelper {

	/**
	 * Create Setting checkbox
	 * 
	 * @param p
	 * @param title
	 * @param id
	 * @param standard
	 * @return
	 */
	public static FormCheckbox bool(final Project p, String title, final String id, boolean standard) {
		return (FormCheckbox) new FormCheckbox(title, p == null ? Setting.get(id, standard) : ProjectSett.get(p, id, standard))
				.addChangeListener(new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						if (p == null) {
							Setting.set(id, form.getContentAsString());
						} else {
							ProjectSett.set(p, id, form.getContentAsString());
						}

					}
				});
	}

	/**
	 * Create Setting number field
	 * 
	 * @param p
	 * @param title
	 * @param id
	 * @param standard
	 * @return
	 */
	public static FormNumberSpinner numb(final Project p, String title, final String id, int standard) {
		return (FormNumberSpinner) new FormNumberSpinner(title, p == null ? Setting.get(id, standard) : ProjectSett.get(p, id, standard))
				.addChangeListener(new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						if (p == null) {
							Setting.set(id, form.getContentAsString());
						} else {
							ProjectSett.set(p, id, form.getContentAsString());
						}

					}
				});
	}

	/**
	 * Create Setting number string
	 * 
	 * @param p
	 * @param title
	 * @param id
	 * @param standard
	 * @return
	 */
	public static FormTextField text(final Project p, String title, final String id, String standard) {
		return (FormTextField) new FormTextField(title, p == null ? Setting.get(id, standard) : ProjectSett.get(p, id, standard))
				.addChangeListener(new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						if (p == null) {
							Setting.set(id, form.getContentAsString());
						} else {
							ProjectSett.set(p, id, form.getContentAsString());
						}

					}
				});
	}

	/**
	 * Create Setting number string
	 * 
	 * @param p
	 * @param title
	 * @param id
	 * @param standard
	 * @return
	 */
	public static FormTextArea area(final Project p, String title, final String id, String standard) {
		return (FormTextArea) new FormTextArea(title, p == null ? Setting.get(id, standard) : ProjectSett.get(p, id, standard))
				.addChangeListener(new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						if (p == null) {
							Setting.set(id, form.getContentAsString());
						} else {
							ProjectSett.set(p, id, form.getContentAsString());
						}

					}
				});
	}

	/**
	 * Create Setting number string
	 * 
	 * @param p
	 * @param title
	 * @param id
	 * @param standard
	 * @return
	 */
	public static FormComboBox combo(final Project p, String title, final String id, String standard, final String[] valueId,
			String[] valueTitles) {
		return (FormComboBox) new FormComboBox(title, valueId, valueTitles).selectField(
				p == null ? Setting.get(id, standard) : ProjectSett.get(p, id, standard)).addChangeListener(
				new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						if (p == null) {
							Setting.set(id, form.getContentAsString());
						} else {
							ProjectSett.set(p, id, form.getContentAsString());
						}

					}
				});
	}
}
