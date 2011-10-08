/**
 * 
 */
package de.yaams.maker.helper;

import javax.swing.SwingUtilities;

import de.yaams.maker.helper.gui.YEx;

/**
 * Helpclass to management swing calls from theards
 * 
 * @author Administrator
 * 
 */
public abstract class SwingHelper implements Runnable {

	/**
	 * Run invokeLater SwingWorker
	 */
	public SwingHelper() {

		// check thread
		if (!needWait()) {
			return;
		}

		try {
			SwingUtilities.invokeLater(this);
		} catch (Throwable t) {
			try {
				run();
			} catch (Throwable t2) {
				YEx.error("Can not run invokeLater SwingWorker ", t);
				YEx.error("Can not run alternative invokeLater SwingWorker ", t2);
			}
		}
	}

	/**
	 * Run invokeAndWait SwingWorker
	 */
	public SwingHelper(boolean wait) {

		// check thread
		if (!needWait()) {
			return;
		}

		try {
			SwingUtilities.invokeAndWait(this);
		} catch (Throwable t) {
			try {
				run();
			} catch (Throwable t2) {
				YEx.error("Can not run invokeAndWait SwingWorker ", t);
				YEx.error("Can not run alternative invokeAndWait SwingWorker ", t2);
			}
		}
	}

	/**
	 * Check if in the awt thread and so doesn't wait
	 * 
	 * @return
	 */
	protected boolean needWait() {
		// check thread
		if (Thread.currentThread().getName().startsWith("AWT-EventQueue")) {
			run();
			return false;
		}

		return true;
	}
}
