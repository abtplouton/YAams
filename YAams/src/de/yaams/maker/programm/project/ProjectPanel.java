/**
 * 
 */
package de.yaams.maker.programm.project;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import de.yaams.maker.helper.gui.YFactory;

/**
 * @author Praktikant
 * 
 */
public class ProjectPanel extends JPanel {

	private static final long serialVersionUID = 2137899531406951536L;

	protected ProjectList list;
	protected JPanel left;

	/**
	 * Create a new
	 */
	public ProjectPanel() {
		super(new GridLayout(1, 1));

		// build gui
		left = new JPanel(new GridLayout(1, 1));
		list = new ProjectList(this);

		add(YFactory.createHorizontPanel(left, list, "project.panel"));

	}

	/**
	 * Set the content of the left pane
	 * 
	 * @param j
	 */
	public void setLeft(JComponent j) {
		left.removeAll();
		left.add(j);
		left.invalidate();
		left.revalidate();
	}
}
