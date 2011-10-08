package de.yaams.core.helper.gui.icons;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.UIManager;

import org.apache.log4j.Level;
import org.apache.log4j.Priority;

import de.yaams.core.helper.I18N;
import de.yaams.core.helper.Log;
import de.yaams.core.helper.gui.YDialog;
import de.yaams.core.helper.gui.YEx;
import de.yaams.core.helper.gui.YMessagesDialog;

/**
 * @author abt
 * 
 */
public class IconCache {

	public static final int SIZE = 16;
	private final static HashMap<String, ImageIcon> icons = new HashMap<String, ImageIcon>();
	public static String[] games;

	public static void init(YMessagesDialog ymd) {

		// add standard icons
		final String[] icons = { "add", "open", "graph-opt", "ok", "dummy", "game", "del", "warn", "info", "yrgss", "debug", "opts",
				"close", "recall", "audio", "graphic", "monitor", "error", "help", "copy", "paste", "selectAll", "cut", "disk", "mail",
				"web", "cancel", "archive", "folder", "ruby", "yrgss" };
		for (final String s : icons) {
			addPNG("ico/" + s, IconCache.class);
		}

		// add look and feel icons
		loadSystemIcon("question", "OptionPane.questionIcon", ymd);
		loadSystemIcon("error", "OptionPane.errorIcon", ymd);
		loadSystemIcon("warn", "OptionPane.warningIcon", ymd);

	}

	/**
	 * Helpermethod
	 * 
	 * @param id
	 * @param name
	 * @param ymd
	 */
	protected static void loadSystemIcon(String id, String name, YMessagesDialog ymd) {
		try {
			Icon e = UIManager.getIcon(name);
			ImageIcon i = new ImageIcon(new BufferedImage(e.getIconWidth(), e.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR));
			e.paintIcon(new JLabel(""), i.getImage().getGraphics(), 0, 0);
			add(id, i);
		} catch (Throwable t) {
			ymd.add(I18N.t("Kann Systemicon {0} nicht laden.", id), Priority.INFO_INT);
			Log.ger.info("Can not load system icon " + name, t);
			addPNG("ico/" + id, IconCache.class);

		}
	}

	/**
	 * Add Ressources as ImageIcons to the Also search for xxx8.png and
	 * xxx32.png
	 * 
	 * @param name
	 * @param c
	 * @param infopath
	 */
	@SuppressWarnings("rawtypes")
	public static void addPNG(final String name, final Class c) {
		String nam = name;
		// check nam
		if (nam.contains("/")) {
			nam = name.substring(nam.indexOf("/") + 1);
		}

		add(nam, c, name + ".png");

		// exist special pixel version?
		final int[] pixels = { 8, 32, 64 };
		for (final int pixel : pixels) {
			if (c.getResource(name + pixel + ".png") != null) {
				add(nam, c, name + pixel + ".png");
			}
		}
	}

	/**
	 * Add Multiple Ressources as ImageIcons to the Also search for xxx8.png and
	 * xxx32.png, call addPNG(name,c)
	 * 
	 * @param class
	 * @param name
	 */
	@SuppressWarnings("rawtypes")
	public static void addPNG(final Class c, final String... name) {
		// add icon
		for (final String s : name) {
			IconCache.addPNG(s, c);
		}
	}

	/**
	 * Add Ressources as ImageIcons to the Also search for xxx8.png and
	 * xxx32.png
	 * 
	 * @param name
	 * @param c
	 * @param infopath
	 */
	public static void addPNG(final String name, final File dir) {
		add(name, new File(dir, name + ".png"));

		// exist special pixel version?
		final int[] pixels = { 8, 32, 64 };
		for (final int pixel : pixels) {
			final File f = new File(dir, name + pixel + ".png");
			if (f.exists()) {
				add(name, f);
			}
		}
	}

	/**
	 * Add Ressources as ImageIcons to the cache
	 * 
	 * @param name
	 * @param c
	 * @param path
	 */
	@SuppressWarnings("rawtypes")
	public static void add(final String name, final Class c, final String path) {

		try {
			add(name, new ImageIcon(c.getResource(path)));
		} catch (final Throwable t) {
			YEx.info("Can not load ico " + path + " as " + name + " from " + c.toString(), t);
		}
	}

	/**
	 * Add Files as ImageIcons to the cache
	 * 
	 * @param name
	 * @param image
	 */
	public static void add(final String name, final File image) {
		add(name, new ImageIcon(image.getAbsolutePath()));
	}

	/**
	 * Add Images as ImageIcons to the cache
	 * 
	 * @param name
	 * @param image
	 */
	public static void add(final String name, final Image image) {
		add(name, new ImageIcon(image));
	}

	/**
	 * Add ImageIcons to the cache
	 * 
	 * @param name
	 * @param imageicon
	 */
	public static void add(final String name, final ImageIcon imageicon) {
		try {
			final String n = getKey(name, imageicon.getIconWidth());
			icons.put(n, imageicon);
		} catch (final Throwable t) {
			YEx.info("Can not load ico " + imageicon + " as " + name, t);
		}
	}

	/**
	 * Get a ImageIcon with the standard size
	 * 
	 * @param name
	 * @param size
	 * @return the icon
	 */
	public static ImageIcon get(final String name) {
		return get(name, SIZE);
	}

	/**
	 * Run over all icons and put it in one ary filter differend size and comb
	 * icons
	 * 
	 * @return
	 */
	public static ArrayList<String> getAllUniqueIcons() {
		ArrayList<String> ary = new ArrayList<String>();

		for (String key : icons.keySet()) {
			// comb?
			if (key.indexOf("_") != -1) {
				continue;
			}

			ary.add(key);
		}

		return ary;
	}

	/**
	 * Get a ImageIcon
	 * 
	 * @param name
	 * @param size
	 * @return the icon
	 */
	public static ImageIcon get(final String name, final int size) {
		final String key = getKey(name, size);
		// exist?
		if (icons.containsKey(key)) {
			return icons.get(key);
		}

		// multiple icon?
		if (name != null && name.indexOf('_') != -1) {
			// find the 2 pices
			final int id = name.indexOf('_'), size2 = size / 2;
			final String first = name.substring(0, id), second = name.substring(id + 1, name.length());

			// exist their?
			if (exist(first) && exist(second)) {
				// create it
				final BufferedImage b = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
				b.getGraphics().drawImage(getImage(first, size), 0, 0, size, size, null);
				b.getGraphics().drawImage(getImage(second, size2), size2, size2, size2, size2, null);

				// add it in this size
				final ImageIcon i = new ImageIcon(b);
				icons.put(key, i);
				return i;
			}
		}

		// exist a bigger one?
		for (int nsize = size; nsize <= 512; nsize *= 2) {
			if (icons.containsKey(getKey(name, nsize))) {
				// create it
				final BufferedImage b = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
				b.getGraphics().drawImage(icons.get(getKey(name, nsize)).getImage(), 0, 0, size, size, null);
				// add it in this size
				final ImageIcon i = new ImageIcon(b);
				icons.put(key, i);
				return i;
			}
		}

		// exist a smaller one?
		for (int nsize = size; nsize >= 8; nsize /= 2) {
			if (icons.containsKey(getKey(name, nsize))) {
				// create it
				final BufferedImage b = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
				b.getGraphics().drawImage(icons.get(getKey(name, nsize)).getImage(), 0, 0, size, size, null);

				// add it in this size
				final ImageIcon i = new ImageIcon(b);
				icons.put(key, i);
				return i;
			}
		}

		// special size?
		if (size == 24 || size == 48) {
			final BufferedImage b = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
			b.getGraphics().drawImage(get(name, 32).getImage(), 0, 0, size, size, null);

			// add it in this size
			final ImageIcon i = new ImageIcon(b);
			icons.put(key, i);
			return i;
		}

		YDialog.ok("", "Don't found a icon for " + name + " (" + size + ")");
		YEx.info("Don't found a icon for " + name + " (" + size + ")", new IllegalArgumentException("Don't found a icon for " + name + " ("
				+ size + ")"));

		// dummy?
		if ("dummy".equals(name)) {
			// put in cache
			icons.put(key, null);
		} else {
			// put in cache
			icons.put(key, get("dummy", size));
		}

		// get it
		return "dummy".equals(name) ? null : get("dummy", size);
	}

	/**
	 * Get a Image with the standard size
	 * 
	 * @param name
	 * @param size
	 * @return the icon
	 */
	public static Image getImage(final String name) {
		return getImage(name, SIZE);
	}

	/**
	 * Get a Image
	 * 
	 * @param name
	 * @param size
	 * @return the icon
	 */
	public static Image getImage(final String name, final int size) {
		return get(name, size).getImage();
	}

	/**
	 * Look if the selected icon exist
	 * 
	 * @param name
	 * @return true, if it found or false, if it not found
	 */
	public static boolean exist(final String name) {
		return exist(name, SIZE);
	}

	/**
	 * Look if the selected icon with the spec. size exist
	 * 
	 * @param name
	 * @return true, if it found or false, if it not found
	 */
	public static boolean exist(final String name, final int size) {
		// multiple icon?
		if (name != null && name.indexOf('_') != -1) {
			// find the 2 pices
			final int id = name.indexOf('_'), size2 = size / 2;
			final String first = name.substring(0, id), second = name.substring(id + 1, name.length());
			// exist their?
			if (exist(first, size) && exist(second, size2)) {
				return true;
			}
		}

		// exist any size?
		if (name != null) {
			for (int nsize = 8; nsize <= 512; nsize *= 2) {
				if (icons.containsKey(getKey(name, nsize))) {
					return true;
				}
			}
		}

		// or exist default size?
		return icons.containsKey(getKey(name, size));
	}

	/**
	 * Helpermethod to transform the name & size to the key
	 * 
	 * @param name
	 * @param size
	 * @return
	 */
	private static String getKey(final String name, final int size) {
		return size == SIZE ? name : name + "_" + size;
	}

	/**
	 * Helpermethod to get read an image oder String
	 * 
	 * @param o
	 * @param size
	 */
	public static ImageIcon getS(final Object o, final int size) {
		// is image?
		if (o instanceof Image) {
			return new ImageIcon(((Image) o).getScaledInstance(size, size, Image.SCALE_FAST));
		}
		// is string?
		return get(o.toString(), size);
	}
}
