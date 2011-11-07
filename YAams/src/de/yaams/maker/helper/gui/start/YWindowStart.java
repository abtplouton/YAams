/**
 * 
 */
package de.yaams.maker.helper.gui.start;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.net.URL;

import javax.imageio.ImageIO;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.JavaHelper;
import de.yaams.maker.helper.Setting;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.gui.YFrame;
import de.yaams.maker.programm.YAamsCore;
import de.yaams.maker.programm.YaFrame;

/**
 * @author abt
 * 
 */
public final class YWindowStart extends Dialog {

	private static final long serialVersionUID = 1828459853652531037L;

	private String note;
	private IconPanel icon;

	/**
	 * Create a new YProgressWindowRepeat
	 * 
	 * @param bar
	 */
	public YWindowStart() {
		super(YaFrame.get(), false);
		note = "";

		YFrame.setIcons(this);

		// ask for image
		URL url = this.getClass().getResource(
				(Setting.get("substance.startlogo", "default").equals("default") ? "Gargoyle" : Setting.get("substance.startlogo",
						"default")) + ".jpg");
		ExtentionManagement.work("startwindow", JavaHelper.createHashStringObj("url", url));

		try {

			// load img
			icon = new IconPanel(ImageIO.read(url));
			setLayout(new BorderLayout());
			add(icon, BorderLayout.WEST);

		} catch (Throwable t) {
			YEx.info("Can not load Image " + url, t);
		}

		// build it
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setUndecorated(true);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Show it
	 * 
	 * @param title
	 */
	public void setNote(final String title) {
		note = title;
		icon.repaint();
	}

	/**
	 * Close it
	 */
	public void close() {

		setVisible(false);
		dispose();
	}

	class IconPanel extends Panel {

		private static final long serialVersionUID = 5075697963386354117L;

		protected Image image;

		public IconPanel(Image image) {
			this.image = image;
			setPreferredSize(new Dimension(958, 512));
		}

		@Override
		public void paint(Graphics g) {
			g.drawImage(image, 0, 0, null);
			g.setColor(Color.white);
			g.setFont(getFont().deriveFont(18f));
			g.drawString(note, 10, 498);
			g.setFont(getFont().deriveFont(25f).deriveFont(Font.BOLD));
			g.drawString(I18N.t("{0} wird gestartet...", YAamsCore.TITLE), 10, 472);
		}
	}
}
