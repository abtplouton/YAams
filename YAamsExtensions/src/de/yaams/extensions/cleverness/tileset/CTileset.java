/**
 * 
 */
package de.yaams.extensions.cleverness.tileset;

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
		AIR, DIRT, BLOCK, BOMB
	}

	protected HashMap<Integer, TYPE> types;
	protected String graphic;

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public CTileset() {
		super("", null, "tileset");

		types = new HashMap<Integer, TYPE>();
	}

	/**
	 * @return the types
	 */
	public HashMap<Integer, TYPE> getTypes() {
		return types;
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
