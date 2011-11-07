/**
 * 
 */
package de.yaams.maker.helper.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.jdesktop.swingx.JXHeader;

import de.yaams.maker.helper.gui.icons.IconCache;

/**
 * @author abt
 * 
 */
public class YHeader extends JXHeader {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5538804683438095238L;

	/**
	 * @param title
	 * @param description
	 * @param icon
	 */
	@Deprecated
	public YHeader(final String title) {
		this(title, "yrgss");
	}

	/**
	 * @param title
	 * @param description
	 * @param icon
	 */
	public YHeader(final String title, final Object icon) {
		super(title, "", icon == null ? null : IconCache.getS(icon, IconCache.SIZE * 2));

		// some gui chance
		setTitleFont(getTitleFont().deriveFont(22f));
		setIconPosition(IconPosition.LEFT);

		// double click, and the component will be hidden
		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(final MouseEvent e) {
			}

			@Override
			public void mousePressed(final MouseEvent e) {
			}

			@Override
			public void mouseExited(final MouseEvent e) {
			}

			@Override
			public void mouseEntered(final MouseEvent e) {
			}

			@Override
			public void mouseClicked(final MouseEvent e) {
				// double click? dont show it
				if (e.getClickCount() == 2) {
					setVisible(false);
				}

			}
		});
	}

	/**
	 * @param title
	 * @param icon
	 */
	public void setText(final String title, final String icon) {
		if (title != null) {
			setTitle(title);
		}

		if (icon != null) {
			setIcon(IconCache.get(icon, IconCache.SIZE * 2));
		}
	}

}
