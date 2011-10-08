/**
 * 
 */
package de.yaams.maker.helper.gui.tabs;

import java.awt.Component;

import javax.swing.JPanel;

import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.programm.project.Project;

/**
 * @author abt
 * 
 */
public abstract class SplitActionListElement extends BasisListElement {
	
	/**
	 * Create a new SplitActionListElement
	 * 
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public SplitActionListElement(final String title, final String desc, final String icon) {
		super(title, desc, icon);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.BasisAction#perform()
	 */
	public void show(final JPanel content, final Project p) {
		
		// build panel
		content.removeAll();
		
		content.add(getComponent(p));
		content.invalidate();
		content.revalidate();
		
	}
	
	/**
	 * Get the panel
	 * 
	 * @return
	 */
	protected abstract Component getComponent(final Project p);
	
}
