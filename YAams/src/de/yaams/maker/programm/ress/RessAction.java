/**
 * 
 */
package de.yaams.maker.programm.ress;

import java.awt.Component;

import de.yaams.maker.helper.gui.tabs.SplitActionListElement;
import de.yaams.maker.programm.project.Project;

/**
 * @author abt
 * 
 */
public class RessAction extends SplitActionListElement {

	/**
	 * Create a new RessAction
	 * 
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public RessAction(String title, final String folder, String icon) {
		super(title, folder, icon);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.packandgo.helper.gui.tabs.SplitActionListElement#getComponent
	 * (de.yaams.packandgo.programm.project.Project)
	 */
	@Override
	protected Component getComponent(final Project project) {
		return new RessPanel(project, desc);
	}

}
