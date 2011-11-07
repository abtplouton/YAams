/**
 * 
 */
package de.yaams.extensions.diamant.tileset;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.objects.BasicObject;
import de.yaams.maker.programm.ress.RessRess;

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

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public CTileset() {
		super("", null, "tileset");

		types = new HashMap<Integer, HashMap<TYPE, Boolean>>();
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

	public BufferedImage getTilesetGraphic(Project p) {
		// exist in cache?
		if (!p.getCache().containsKey(getGraphic())) {
			BufferedImage graphic = RessRess.getGraphic(p, "Ressources", getGraphic());

			// add to cache basics
			p.getCache().put(getGraphic(), graphic);
		}

		return (BufferedImage) p.getCache().get(getGraphic());

	}

}
