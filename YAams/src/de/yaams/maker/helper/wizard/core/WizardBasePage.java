/**
 * 
 */
package de.yaams.maker.helper.wizard.core;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.wizard.WizardManagement;

/**
 * @author Praktikant
 * 
 */
public abstract class WizardBasePage {

	protected BufferedImage cimg;

	/**
	 * 
	 */
	public WizardBasePage() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Get the img for the left view
	 * 
	 * @return
	 */
	public BufferedImage getImage() {
		if (cimg == null)
			try {
				cimg = ImageIO.read(WizardManagement.class.getResource("img/" + getImgName()));
			} catch (Throwable t) {
				YEx.info("Can not load Image " + getImgName() + " for wizard", t);
			}
		return cimg;
	}

	/**
	 * Get the name of the image for the left pane
	 * 
	 * @return
	 */
	protected abstract String getImgName();

	/**
	 * Get the icon for the header in the main panel
	 * 
	 * @return
	 */
	protected abstract String getIcon();

	/**
	 * Get the name of this page
	 * 
	 * @return
	 */
	public abstract String getTitle();

	/**
	 * Get the main panel
	 * 
	 * @return
	 */
	public abstract JComponent getForm();

	/**
	 * Set the settings, when the wizard is finish
	 */
	public abstract void finish();

}
