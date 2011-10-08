/**
 * 
 */
package de.yaams.extensions.rgssproject.database;

import java.util.HashMap;

import org.jruby.RubyObject;

/**
 * @author abt
 * 
 */
public class SystemGObject {

	protected boolean modified;
	protected RubyObject object;

	/**
	 * Addional data
	 */
	protected HashMap<String, Object> tmpData;

	/**
	 * Create a new AddionalGInfo
	 * 
	 * @param object
	 */
	public SystemGObject(RubyObject object) {
		super();
		this.object = object;
		tmpData = new HashMap<String, Object>();
	}

	/**
	 * @return the modified
	 */
	public boolean isModified() {
		return modified;
	}

	/**
	 * @param modified
	 *            the modified to set
	 */
	public void setModified(boolean modified) {
		this.modified = modified;
	}

	/**
	 * @return the object
	 */
	public RubyObject getObject() {
		return object;
	}

	/**
	 * @param object
	 *            the object to set
	 */
	public void setObject(RubyObject object) {
		this.object = object;
	}

	/**
	 * Get the name, if exist
	 * 
	 * @return
	 */
	public String getName() {
		return (String) object.getInstanceVariable("@name").toJava(String.class);
	}

	/**
	 * @return the tmpData
	 */
	public HashMap<String, Object> getTmpData() {
		return tmpData;
	}

}
