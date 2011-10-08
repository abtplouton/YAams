/**
 * 
 */
package de.yaams.maker.helper.gui;

import java.awt.Dimension;

/**
 * Helperclass for YFactory.installZoom
 * 
 * @author Praktikant
 *
 */
public interface IZoom {

	/**
	 * Get the act zoom level
	 * @return
	 */
	public double getZoomLevel();
	
	/**
	 * Set the act zoom level
	 * @param d
	 * @return
	 */
	public void setZoomLevel(double d);

	/**
	 * Get the act dimension of the view
	 * @return
	 */
	public Dimension getViewDimension();

	/**
	 * Get the act dimension of the object
	 * @return
	 */
	public Dimension getObjectDimension();
}
