/**
 * 
 */
package de.yaams.extensions.diamant.maps;

import de.yaams.maker.programm.project.objects.BasicObject;

/**
 * @author abby
 * 
 */
public class CMapInfo extends BasicObject {

	protected int tileset;

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public CMapInfo() {
		super("", null, "map");
	}

	/**
	 * @return the tileset
	 */
	public int getTileset() {
		return tileset;
	}

	/**
	 * @param tileset
	 *            the tileset to set
	 */
	public void setTileset(int tileset) {
		this.tileset = tileset;
	}

}
