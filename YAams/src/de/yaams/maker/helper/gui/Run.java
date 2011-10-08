/**
 * yaams - de.yaams.boot.utils Run.java
 */
package de.yaams.maker.helper.gui;


/**
 * @author Administrator
 */
public abstract class Run implements Runnable {
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override public void run() {
		try {
			go();
		} catch (Throwable t) {
			YEx.warn("Can not run theard", t);
		}
		
	}
	
	/**
	 * Implemet the task
	 */
	public abstract void go();
	
	/**
	 * Killed it?
	 * 
	 * @return
	 */
	protected boolean stopped() {
		// cancel it? delete the half file
		return Thread.currentThread().isInterrupted();
	}
	
}
