/**
 * 
 */
package de.yaams.maker.programm.ress;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import javax.imageio.ImageIO;

import de.yaams.maker.helper.gui.YDialog;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.programm.project.Project;

/**
 * @author abt
 * 
 */
public class RessRess {

	protected final static HashMap<String, BufferedImage> cache = new HashMap<String, BufferedImage>();
	protected final static HashMap<String, File> linkCache = new HashMap<String, File>();

	/**
	 * Get the file for this ress
	 * 
	 * @param p
	 * @param folder
	 * @param name
	 * @param graphic
	 * @return file to the graphic or null
	 */
	public static File getRessFile(final Project project, final String folder, final String name) {
		// in the cache?
		File f = project.getType().getFile(project, folder + File.separator + name);

		// exist?
		if (f != null) {
			return f;
		}

		String key = project.getHash() + folder + name;

		// in cache
		if (linkCache.containsKey(key)) {
			return linkCache.get(key);
		}

		Object erg = YDialog.fileNotFound(new File(project.getPath(), folder + File.separator + name), folder + File.separator + name);
		// is file?
		if (erg instanceof File) {
			linkCache.put(key, (File) erg);
			return (File) erg;
		}

		// check again
		if (erg.equals(-2)) {
			return getRessFile(project, folder, name);
		}
		// use empty graphic
		linkCache.put(key, null);
		return null;
	}

	/**
	 * Show a Ress selector panel
	 * 
	 * @param p
	 * @param folder
	 * @param file
	 * @param graphic
	 * @return the selected string file or null
	 */
	public static File showRessSelector(Project project, String folder, String file) {
		File erg = new File(project.getPath(), folder + File.separator + file);

		// build panel
		RessPanel r = new RessPanel(project, folder);

		// add file
		if (file != null) {
			r.getList().setSelectedFile(file);
		}

		// show
		YDialog.show(folder, "ress", r, true);

		// return selected file
		if (r.getList().getList().getSelectedValue() != null) {
			erg = r.getList().getSelectedFile();
		}

		return erg;
	}

	/**
	 * Get the file for this ress
	 * 
	 * @param p
	 * @param folder
	 * @param name
	 * @param graphic
	 * @return null or the graphic
	 */
	public static BufferedImage getGraphic(final Project p, final String folder, final String name) {
		// exist name?
		if (name == null || name.length() == 0) {
			return null;
		}

		// load file & image
		final File f = getRessFile(p, folder, name);

		try {
			String key;
			// is empty? generate new
			if (f != null) {
				key = f.getAbsolutePath();
			} else {
				key = folder + name;
			}

			// in cache?
			if (!cache.containsKey(key)) {
				// read or create?
				if (f != null) {
					cache.put(key, ImageIO.read(f));
				} else {
					cache.put(key, new BufferedImage(500, 500, BufferedImage.TYPE_3BYTE_BGR));
				}
			}

			// get it
			return cache.get(key);
		} catch (final Throwable t) {
			YEx.warn("Can not load " + f, t);
		}
		return null;
	}

	/**
	 * Helpermethod to check, if the file end with one of the extentions
	 * 
	 * @param file
	 * @param ext
	 * @return
	 */
	public static boolean endWithExtention(File file, String[] ext) {

		// exist?
		if (!file.exists()) {
			return false;
		}

		// look in the project
		for (final String e : ext) {
			if (file.getName().endsWith(e)) {
				return true;
			}
		}
		return false;
	}
}
