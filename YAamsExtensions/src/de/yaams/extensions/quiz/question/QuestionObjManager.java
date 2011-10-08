/**
 * 
 */
package de.yaams.extensions.quiz.question;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.FormComboBox;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormTextArea;
import de.yaams.maker.helper.gui.form.FormTextField;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.programm.project.objects.simple.SimpleObjectManagement;

/**
 * @author abby
 * 
 */
public class QuestionObjManager extends SimpleObjectManagement {

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public QuestionObjManager() {
		super(I18N.t("Fragen"), I18N.t("Verwaltet alle Fragen des Quizes"), "question", "questions.xml", "quiz");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectManager#getGroup()
	 */
	@Override
	public String getGroup() {
		return "Questions";
	}

	@Override
	public BasisListElement createNewObject() {
		return new Question();
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
		if (!(selectedObject instanceof Question)) {
			throw new IllegalArgumentException(selectedObject + " is not a Question");
		}
		final Question q = (Question) selectedObject;

		// add basic
		f.addElement("basic.name", new FormTextArea(I18N.t("Frage"), q.getTitle()).addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				q.setTitle(form.getContentAsString());

			}
		}));

		// add questions
		for (int i = 0; i < 4; i++) {
			final int j = i;
			f.addElement("basic.question" + i, new FormTextField(I18N.t("Antwort {0}", i + 1), q.getQuestions().get(i))
					.addChangeListener(new FormElementChangeListener() {

						@Override
						public void stateChanged(FormElement form) {
							q.getQuestions().remove(j);
							q.getQuestions().add(j, form.getContentAsString());

						}
					}));
		}

		// add default
		f.addElement(
				"basic.right",
				new FormComboBox(I18N.t("Richtige Antwort"), new String[] { "0", "1", "2", "3" }, new String[] { "Antwort 1", "Antwort 2",
						"Antwort 3", "Antwort 4" }).selectField(Integer.toString(q.getRightElement())).addChangeListener(
						new FormElementChangeListener() {

							@Override
							public void stateChanged(FormElement form) {
								q.setRightElement(Integer.valueOf(form.getContentAsString()));

							}
						}));
	}

}
