/**
 * 
 */
package de.yaams.extensions.quiz.question;

import java.util.ArrayList;

import de.yaams.maker.programm.project.objects.BasicObject;

/**
 * @author abby
 * 
 */
public class Question extends BasicObject {

	protected ArrayList<String> questions;
	protected int rightElement;

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public Question() {
		super("", null, "question");
		questions = new ArrayList<String>(4);
		questions.add("");
		questions.add("");
		questions.add("");
		questions.add("");
	}

	/**
	 * @return the questions
	 */
	public ArrayList<String> getQuestions() {
		return questions;
	}

	/**
	 * @return the rightElement
	 */
	public int getRightElement() {
		return rightElement;
	}

	/**
	 * @param rightElement
	 *            the rightElement to set
	 */
	public void setRightElement(int rightElement) {
		this.rightElement = rightElement;
	}

}
