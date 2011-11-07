/**
 * 
 */
package de.yaams.extensions.diamant.data.event;

import de.yaams.extensions.diamant.tileset.CTileset;
import de.yaams.extensions.diamant.tileset.CTileset.TYPE;

/**
 * @author abby
 * 
 */
public class LoadEvent extends BaseEvent {

	/**
	 * @param x
	 * @param y
	 */
	public LoadEvent(int x, int y, CTileset tileset, int id) {
		super(x, y, tileset);

		// build settings
		if (tileset.getTypes(id, TYPE.COIN)) {
			isCoin = true;
		}
		if (tileset.getTypes(id, TYPE.CANCOLLECT)) {
			canCollect = true;
		}
		if (tileset.getTypes(id, TYPE.FALLDOWN)) {
			fallDown = true;
		}
		if (tileset.getTypes(id, TYPE.ISROUND)) {
			isRound = true;
		}
		if (tileset.getTypes(id, TYPE.CANMOVE)) {
			canMove = true;
		}
		if (tileset.getTypes(id, TYPE.ISDOOR)) {
			door = true;
		}
		if (tileset.getTypes(id, TYPE.CANEXPLODE)) {
			canExplode = true;
		}
		if (tileset.getTypes(id, TYPE.CANDESTROY)) {
			canDestroy = true;
		}
		if (tileset.getTypes(id, TYPE.DESTROYALLTIMES)) {
			destryAllTime = true;
		}
		if (tileset.getTypes(id, TYPE.DESTROYWILLGAMEOVER)) {
			canGameOver = true;
		}
		if (tileset.getTypes(id, TYPE.ISCHEST)) {
			isChest = true;
		}

		// set img
		img = tileset.getTilesetGraphic(id);
	}

}
