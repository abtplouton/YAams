/**
 * 
 */
package de.yaams.core.helper.gui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import de.yaams.core.helper.AE;
import de.yaams.core.helper.gui.icons.IconCache;

/**
 * @author abt
 * 
 */
public class YFactory {

	/**
	 * Create a Button
	 * 
	 * @param name
	 * @param i
	 * @param l
	 * @return
	 */
	public static JButton b(final String text, final String i, final AE l) {
		return b(text, i, l, 16);
	}

	/**
	 * Create a Button
	 * 
	 * @param name
	 * @param icon
	 * @param l
	 * @return
	 */
	public static JButton b(final String text, final String icon, final AE l, final int size) {
		// load button
		final JButton b = icon == null ? new JButton(text) : new JButton(text, IconCache.get(icon, size));

		b.setActionCommand(text);

		// add action?
		if (l == null) {
			b.setEnabled(false);
		} else {
			b.addActionListener(l);
		}

		return b;
	}

	/**
	 * Helpmethod to add the toolbar buttons
	 * 
	 * @param text
	 *            & translate it
	 * @param icon
	 * @param e
	 */
	public static JLabel tb(final String text, final String icon, final AE e) {
		return tb(text, icon, e, 16);
	}

	/**
	 * Helpmethod to add the toolbar buttons
	 * 
	 * @param text
	 *            & translate it
	 * @param icon
	 * @param e
	 */
	public static JLabel tb(final String text, final String icon, final AE ae, final int size) {
		// create button
		// JideButton b = new JideButton(IconCache.get(icon,size));
		// b.addActionListener(ae);
		// b.setButtonStyle(JideButton.TOOLBAR_STYLE);
		//
		// // install tooltip
		// YFactory.installTooltip(b, text, icon);

		final JLabel l = new JLabel(IconCache.get(icon, size));

		l.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		l.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (l.isEnabled())
					ae.actionPerformed(null);

			}

			@Override
			public void mouseExited(MouseEvent e) {
				l.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
				// l.setBorder(BorderFactory.

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if (l.isEnabled())
					l.setBorder(BorderFactory.createEtchedBorder());

			}
		});

		return l;
	}

	/**
	 * Helpmethod to add the menu buttons
	 * 
	 * @param text
	 *            & translate it
	 * @param icon
	 * @param e
	 */
	public static JMenuItem mb(final String text, final String icon, final AE e) {
		final JMenuItem b = new JMenuItem(text, IconCache.get(icon));
		if (e != null) {
			b.addActionListener(e);
		}
		return b;
	}

	/**
	 * Helpmethod to add the menu
	 * 
	 * @param text
	 *            & translate it
	 * @param icon
	 * @param e
	 */
	public static JMenu m(final String text, final String icon, final String selectedIcon, final AE ae) {
		final JMenu b = new JMenu(text == null ? "" : text);
		// set name
		if (text == null) {
			b.setIconTextGap(0);
		}
		// set icon
		if (icon != null) {
			b.setIcon(IconCache.get(icon));
		}
		// set selectedIcon
		if (selectedIcon != null) {
			b.setSelectedIcon(IconCache.get(selectedIcon));
			b.setPressedIcon(IconCache.get(selectedIcon));
		}
		// add actionlistener
		if (ae != null) {
			b.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(final MouseEvent e) {
					ae.actionPerformed(null);

				}
			});
		}

		return b;
	}

	/**
	 * Create a horizontal box with the right layout
	 * 
	 * @param box
	 * @return
	 */
	public static JScrollPane createVerticalBox(final JPanel box, boolean border) {
		box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));

		// set layout
		final JPanel northbox = new JPanel(new BorderLayout());
		northbox.add(BorderLayout.NORTH, box);

		final JScrollPane j = new JScrollPane(northbox);
		j.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		// add border?
		if (!border) {
			j.setBorder(BorderFactory.createEmptyBorder());
		}
		return j;
	}
}
