/**
 * 
 */
package de.yaams.extensions.diamant.graph.scene;

import java.awt.Dimension;

import javax.swing.JPanel;

import de.yaams.extensions.diamant.data.CONST;
import de.yaams.extensions.diamant.graph.PWindow;

/**
 * @author abby
 * 
 */
public abstract class BaseScene extends JPanel {

	private static final long serialVersionUID = 4791368917632376703L;
	protected PWindow window;

	/**
	 * 
	 */
	public BaseScene() {
		setPreferredSize(new Dimension(CONST.GAME_WIDTH, CONST.GAME_HEIGHT - 32));
	}

	/**
	 * @param window
	 *            the window to set
	 */
	public void setWindow(PWindow window) {
		this.window = window;
	}

	/**
	 * @return the window
	 */
	public PWindow getWindow() {
		return window;
	}

	/**
	 * Helpermethod to start it
	 */
	public abstract void init();

	/**
	 * Helpermethod to update it
	 */
	public abstract void update();

	/**
	 * Helpermethod to close it, eg tidy up, remove something
	 */
	public abstract void close();

}
