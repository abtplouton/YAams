/**
 * 
 */
package de.yaams.extensions.notebook;

import de.yaams.maker.programm.project.objects.BasicObject;

/**
 * @author abby
 * 
 */
public class Note extends BasicObject {

	protected String content;

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public Note() {
		super("", null, "notes");
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

}
