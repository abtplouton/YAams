/**
 * 
 */
package de.yaams.maker.helper.extensions;

import java.util.HashMap;

/**
 * @author abt
 * 
 */
public interface IExtension {
	
	/**
	 * Work/Run/Do things with this extension object
	 * 
	 * @param o
	 */
	public void work(HashMap<String, Object> objects);
}
