/**
 * 
 */
package de.yaams.maker.helper.gui.dock;

import javax.swing.JLabel;

/**
 * @author Praktikant
 * 
 */
public class DockInfoPanel extends DockBasePanel {

	private static final long serialVersionUID = -1449348494813362726L;

	/**
	 * Build it
	 */
	public DockInfoPanel(String title, String cont) {
		JLabel c = new JLabel("<html>" + cont);
		content.add(c);
		buildGui(title, "help", false);
	}
}
