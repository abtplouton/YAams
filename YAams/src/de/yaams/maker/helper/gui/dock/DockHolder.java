/**
 * 
 */
package de.yaams.maker.helper.gui.dock;

import java.awt.GridLayout;

import javax.swing.JPanel;

/**
 * @author Praktikant
 * 
 */
public class DockHolder extends JPanel {
	private static final long serialVersionUID = 7565211940822541822L;

	protected String id;

	/**
	 * Create a new
	 * 
	 * @param id
	 * @param width
	 * @param height
	 */
	public DockHolder(String id, int width, int height) {
		super(new GridLayout(height, width, 5, 5));
		this.id = id;
	}

	/**
	 * Add it
	 * 
	 * @param d
	 */
	public void add(DockBasePanel d) {
		super.add(d);
		d.setDock(this);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

}
