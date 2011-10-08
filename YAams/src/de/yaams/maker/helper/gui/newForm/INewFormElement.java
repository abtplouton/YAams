/**
 * 
 */
package de.yaams.maker.helper.gui.newForm;

/**
 * @author abt
 * 
 */
public interface INewFormElement {
	
	/**
	 * Get the name of the element
	 * 
	 * @return
	 */
	public String getTitle();
	
	/**
	 * Get the icon of the element
	 * 
	 * @return
	 */
	public String getIcon();
	
	/**
	 * Get the translated group of the element
	 * 
	 * @return
	 */
	public String getGroup();
	
	/**
	 * Get the id of the help topic
	 * 
	 * @return
	 */
	public String getHelpID();
	
	/**
	 * To the action, can be used by the correct implementation
	 * 
	 * @return
	 */
	public Object work(Object... o);
}
