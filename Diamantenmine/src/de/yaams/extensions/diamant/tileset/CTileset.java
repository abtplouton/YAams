/**
 * 
 */
package de.yaams.extensions.diamant.tileset;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import de.yaams.extensions.diamant.data.CONST;
import de.yaams.extensions.diamant.graph.img.LoadFiles;

/**
 * @author abby
 * 
 */
public class CTileset extends BasicObject {

	public enum TYPE {
		AIR, BLOCK, CANEXPLODE, ISROUND, PLAYER, COIN, CANCOLLECT, FALLDOWN, CANMOVE, ISDOOR, CANDESTROY, DESTROYALLTIMES, DESTROYWILLGAMEOVER, ISCHEST
	}

	protected HashMap<Integer, HashMap<TYPE, Boolean>> types;
	protected String graphic;
	protected transient HashMap<Integer, BufferedImage> cache;

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public CTileset() {
		super("", null, "tileset");

		types = new HashMap<Integer, HashMap<TYPE, Boolean>>();
		cache = new HashMap<Integer, BufferedImage>();
	}

	/**
	 * @return the types
	 */
	public HashMap<Integer, HashMap<TYPE, Boolean>> getTypes() {
		return types;
	}

	/**
	 * @return the types
	 */
	public HashMap<TYPE, Boolean> getTypes(int id) {
		if (!types.containsKey(id)) {
			types.put(id, new HashMap<TYPE, Boolean>());
		}
		return types.get(id);
	}

	/**
	 * @return the types
	 */
	public boolean getTypes(int id, TYPE type) {
		return getTypes(id).containsKey(type) && getTypes(id).get(type);
	}

	/**
	 * @return the graphic
	 */
	public String getGraphic() {
		return graphic;
	}

	/**
	 * @param graphic
	 *            the graphic to set
	 */
	public void setGraphic(String graphic) {
		this.graphic = graphic;
	}

	/**
	 * Get it
	 * 
	 * @param p
	 * @return
	 */
	public BufferedImage getTilesetGraphic() {

		return LoadFiles.getImage(getGraphic());

	}

	/**
	 * Get it
	 * 
	 * @param p
	 * @return
	 */
	public BufferedImage getTilesetGraphic(int id) {

		// check it
		if (id < 0) {
			return null;
		}

		// has cache?
		if (cache == null) {
			cache = new HashMap<Integer, BufferedImage>();
		}

		// has in cache?
		if (!cache.containsKey(id)) {
			int w = LoadFiles.getImage(getGraphic()).getWidth();
			cache.put(
					id,
					LoadFiles.getImage(getGraphic()).getSubimage(id % (w / CONST.TILEW) * CONST.TILEW,
							id / (w / CONST.TILEW) * CONST.TILEH, CONST.TILEW, CONST.TILEH));
		}

		return cache.get(id);

	}

}
