/**
 * 
 */
package de.yaams.maker.helper.gui;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

/**
 * @author abt
 * 
 */
public class YScrollPane extends JScrollPane {
	
	private static final long serialVersionUID = 805747432091480594L;
	
	/**
	 * Create a new YScrollPane
	 */
	public YScrollPane() {
		removeBorder();
	}
	
	/**
	 * Create a new YScrollPane
	 * 
	 * @param arg0
	 */
	public YScrollPane(final Component arg0) {
		super(arg0);
		removeBorder();
	}
	
	/**
	 * Create a new YScrollPane
	 * 
	 * @param arg0
	 * @param arg1
	 */
	public YScrollPane(final int arg0, final int arg1) {
		super(arg0, arg1);
		removeBorder();
	}
	
	/**
	 * Create a new YScrollPane
	 * 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public YScrollPane(final Component arg0, final int arg1, final int arg2) {
		super(arg0, arg1, arg2);
		removeBorder();
	}
	
	/**
	 * Helpermethod to remove the default border
	 */
	protected void removeBorder() {
		setBorder(BorderFactory.createEmptyBorder());
	}
	
}
