/**
 * 
 */
package de.yaams.core.helper.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import de.yaams.core.helper.gui.icons.IconCache;

/**
 * @author abt
 * 
 */
public class YProgressWindowRepeat extends Dialog {

	private static final long serialVersionUID = 1828459853652531037L;

	protected Label label, note;
	protected IconPanel icon;
	protected Panel panel;

	/**
	 * Create a new YProgressWindowRepeat
	 * 
	 * @param bar
	 */
	public YProgressWindowRepeat(String title, String icon) {
		super(null, true);
		label = new Label(title + "    ");
		this.note = new Label("Loading ...");
		this.icon = new IconPanel(icon);
		this.icon.repaint();
		this.icon.invalidate();

		// build content
		panel = new Panel(new BorderLayout());
		panel.add(label, BorderLayout.NORTH);
		panel.add(this.note, BorderLayout.CENTER);

		// add close
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				if (JOptionPane.showConfirmDialog(YProgressWindowRepeat.this, "Break Progress?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					close();
				}

			}
		});

		// build it
		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
		add(this.icon, BorderLayout.WEST);

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
	@Override
	public void setTitle(final String title) {
		label.setText(title);
	}

	/**
	 * Show it
	 * 
	 * @param title
	 */
	public void setNote(final String title) {
		note.setText(title);
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

		public IconPanel(String icon) {
			image = IconCache.get(icon, 32).getImage();
			setPreferredSize(new Dimension(40, 40));
		}

		@Override
		public void paint(Graphics g) {
			g.drawImage(image, 4, 4, null);
		}
	}
}
