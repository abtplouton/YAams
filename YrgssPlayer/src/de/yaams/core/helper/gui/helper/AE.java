/**
 * yaams - de.yaams.core.utils AL.java
 */
package de.yaams.core.helper.gui.helper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import de.yaams.core.helper.gui.YEx;

/**
 * @author Administrator
 */
public abstract class AE implements ActionListener {

	protected ActionEvent ae;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		try {
			this.ae = ae;
			run();
		} catch (Throwable t) {
			YEx.warn("Can not perform button operation", t);
		}
	}

	/**
	 * Run the action
	 */
	abstract public void run();
}
