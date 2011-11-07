/**
 * 
 */
package de.yaams.extensions.diamant.graph.scene.menu;

/**
 * @author abby
 * 
 */
public abstract class MenuEntry {

	protected String name;

	/**
	 * @param name
	 */
	public MenuEntry(String name) {
		super();
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public abstract void run();
}
