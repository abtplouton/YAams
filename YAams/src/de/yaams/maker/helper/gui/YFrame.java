/**
 * 
 */
package de.yaams.maker.helper.gui;

import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Window;
import java.util.ArrayList;

import javax.swing.JFrame;

import de.yaams.maker.helper.gui.icons.IconCache;

/**
 * @author abby
 * 
 */
public class YFrame extends JFrame {

	private static final long serialVersionUID = 3212308585176119742L;

	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public YFrame(String arg0) throws HeadlessException {
		super(arg0);

		// add icon
		setIcons(this);
	}

	/**
	 * Helpermethod to the icon
	 * 
	 * @param dia
	 */
	public static void setIcons(Window w) {
		// add icon
		ArrayList<Image> icons = new ArrayList<Image>();
		icons.add(IconCache.get("yaams", 16).getImage());
		icons.add(IconCache.get("yaams", 32).getImage());
		icons.add(IconCache.get("yaams", 64).getImage());
		icons.add(IconCache.get("yaams", 128).getImage());
		w.setIconImages(icons);
	}
}
