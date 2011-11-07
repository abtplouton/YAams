/**
 * 
 */
package de.yaams.extensions.diamant.maps;

import java.util.HashMap;

/**
 * @author abby
 * 
 */
public class CMap {

	protected int width, height, layer;
	protected HashMap<String, Integer> data;

	/**
	 * Create a new map
	 */
	public CMap() {
		width = 20;
		height = 15;
		layer = 2;
		data = new HashMap<String, Integer>();
	}

	/**
	 * Set a new value
	 * 
	 * @param width
	 * @param height
	 * @param layer
	 * @param value
	 */
	public void set(int width, int height, int layer, int value) {
		data.put(getKey(width, height, layer), value);
	}

	/**
	 * Get a value
	 * 
	 * @param width
	 * @param height
	 * @param layer
	 * @return
	 */
	public int get(int width, int height, int layer) {
		// exist?
		if (data.containsKey(getKey(width, height, layer))) {
			return data.get(getKey(width, height, layer));
		}
		return -1;
	}

	/**
	 * Helpermethod
	 * 
	 * @param width
	 * @param height
	 * @param layer
	 * @return
	 */
	protected String getKey(int width, int height, int layer) {
		return layer + "o" + width + "x" + height;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the layer
	 */
	public int getLayer() {
		return layer;
	}

	/**
	 * @param layer
	 *            the layer to set
	 */
	public void setLayer(int layer) {
		this.layer = layer;
	}
}
