/**
 * 
 */
package de.yaams.extensions.notebook;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormTextField;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.programm.project.objects.simple.SimpleObjectManagement;

/**
 * @author abby
 * 
 */
public class NoteObjManager extends SimpleObjectManagement {

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public NoteObjManager() {
		super(I18N.t("Notizen"), I18N.t("Speichert Notizen zum Projekt"), "notes", "notes.xml", "notes");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectManager#getGroup()
	 */
	@Override
	public String getGroup() {
		return I18N.t("Notizen");
	}

	@Override
	public BasisListElement createNewObject() {
		return new Note();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectTab#buildInternContent
	 * (de.yaams.maker.helper.gui.list.BasisListElement)
	 */
	@Override
	public void buildInternContent(BasisListElement selectedObject, FormBuilder f) {
		// right typ?
		if (!(selectedObject instanceof Note)) {
			throw new IllegalArgumentException(selectedObject + " is not a Note");
		}

		final Note q = (Note) selectedObject;

		// add basic
		f.addElement("basic.name", new FormTextField(I18N.t("Name"), q.getTitle()).addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				q.setTitle(form.getContentAsString());

			}
		}));

		// add Text
		final FormattedTextPanel t = new FormattedTextPanel(q.getContent());
		t.getDoc().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				// save it
				q.setContent(t.getTextPane().getText());
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				// save it
				q.setContent(t.getTextPane().getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
		});

		f.setCenter(t);
	}

}
