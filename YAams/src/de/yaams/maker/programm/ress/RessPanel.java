/**
 * 
 */
package de.yaams.maker.programm.ress;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import de.yaams.maker.helper.JavaHelper;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.programm.project.Project;

/**
 * @author abt
 * 
 */
public class RessPanel extends JPanel {
	private static final long serialVersionUID = -8353825809907907598L;

	// private String path;
	private RessourceList list;

	/**
	 * Show the RessPanel
	 */
	public RessPanel(Project project, final String folder) {
		super(new BorderLayout());

		setPreferredSize(new Dimension(400, 300));

		// basiselemente bauen
		list = new RessourceList(project, folder);
		add(list);

		ExtentionManagement.work("ress.panel", JavaHelper.createHashStringObj("list", list, "panel", this));

	}

	/**
	 * @return the list
	 */
	public RessourceList getList() {
		return list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(RessourceList list) {
		this.list = list;
	}

}
