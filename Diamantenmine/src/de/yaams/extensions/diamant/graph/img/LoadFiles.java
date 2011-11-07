/**
 * 
 */
package de.yaams.extensions.diamant.graph.img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import de.yaams.extensions.diamant.Project;
import de.yaams.extensions.diamant.helper.YEx;

/**
 * @author abby
 * 
 */
public class LoadFiles {

	protected static HashMap<String, BufferedImage> cache = new HashMap<String, BufferedImage>();

	/**
	 * 
	 */
	public static BufferedImage getImage(String name) {
		// in cache?
		if (cache.containsKey(name)) {
			return cache.get(name);
		}

		try {
			cache.put(name, ImageIO.read(new File(new File(Project.project.getFile(), "Ressources"), name)));
			return cache.get(name);
		} catch (IOException e) {
			YEx.info("Can not load " + name, e);
		}
		return null;
	}

	/**
	 * 
	 */
	public static BufferedImage getInternImage(String name) {
		// in cache?
		if (cache.containsKey(name)) {
			return cache.get(name);
		}
		try {
			cache.put(name, ImageIO.read(LoadFiles.class.getResource(name)));
			return cache.get(name);
		} catch (IOException e) {
			YEx.info("Can not load " + name, e);
		}
		return null;
	}

}
