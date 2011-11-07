/**
 * 
 */
package de.yaams.maker.helper.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdesktop.swingx.JXTitledPanel;
import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.simple.extend.XhtmlNamespaceHandler;

import com.jidesoft.swing.DefaultOverlayable;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.Setting;
import de.yaams.maker.helper.SwingHelper;
import de.yaams.maker.helper.SystemHelper;
import de.yaams.maker.helper.gui.icons.IconCache;
import de.yaams.maker.programm.YaFrame;

/**
 * @author abt
 * 
 */
public class YFactory {

	/**
	 * Create a swing element to display a url
	 * 
	 * @param url
	 * @return
	 */
	public static JPanel getBrowserPanel(final String url) {
		final JPanel p = new JPanel(new GridLayout(1, 1));
		p.add(new JLabel("Loading " + url));

		SystemHelper.runThread(new Run() {

			@Override
			public void go() {
				// Create a JPanel subclass to render the page
				final XHTMLPanel panel = new XHTMLPanel();

				// Set the XHTML document to render. We use the simplest form
				// of the API call, which uses a File reference. There
				// are a variety of overloads for setDocument().
				try {
					panel.setDocument(url);
				} catch (Throwable t) {
					// build stack
					StringBuffer sb = new StringBuffer("<html><h2>Can not display ");
					sb.append(url);
					sb.append("</h2><h4>");
					sb.append(t.toString());
					sb.append("</h4>");

					// add stack
					for (StackTraceElement ste : t.getStackTrace()) {
						sb.append(ste.toString());
						sb.append("<br />");
					}

					sb.append("</html>");

					panel.setDocumentFromString(sb.toString(), null, new XhtmlNamespaceHandler());
				}

				// Put our panel in a scrolling pane. You can use
				// a regular JScrollPane here, or our FSScrollPane.
				// FSScrollPane is already set up to move the correct
				// amount when scrolling 1 line or 1 page
				new SwingHelper() {

					@Override
					public void run() {
						FSScrollPane f = new FSScrollPane(panel);
						p.removeAll();
						p.add(f);
						p.invalidate();
						p.revalidate();

					}
				};
			}
		}, false);

		return p;

	}

	/**
	 * Create a SplitPane with the option to save the divider
	 * 
	 * @param c
	 * @param d
	 * @param id
	 *            , comp id to save the settings
	 * @return
	 */
	public static JSplitPane createVerticalPanel(final JComponent c, final JComponent d, final String id) {
		return createVerticalPanel(c, d, id, 400);
	}

	/**
	 * Create a SplitPane with the option to save the divider
	 * 
	 * @param c
	 * @param d
	 * @param id
	 * @param hegiht
	 * @return
	 */
	public static JSplitPane createVerticalPanel(final JComponent c, final JComponent d, final String id, int height) {
		// create
		final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, c, d);
		splitPane.setOneTouchExpandable(true);
		splitPane.setBorder(null);
		splitPane.setDividerLocation(Setting.get("divider.vert." + id, height));

		// Add save option
		c.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentResized(ComponentEvent e) {
				Setting.set("divider.vert." + id, splitPane.getDividerLocation());

			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});

		return splitPane;
	}

	/**
	 * Create a SplitPane with the option to save the divider
	 * 
	 * @param c
	 * @param d
	 * @param id
	 *            , comp id to save the settings
	 * @return
	 */
	public static JSplitPane createHorizontPanel(final JComponent c, final JComponent d, final String id) {
		return createHorizontPanel(c, d, id, 150);
	}

	/**
	 * Create a SplitPane with the option to save the divider
	 * 
	 * @param c
	 * @param d
	 * @param id
	 * @return
	 */
	public static JSplitPane createHorizontPanel(final JComponent c, final JComponent d, final String id, int width) {
		// visible fix
		c.setBorder(BorderFactory.createEmptyBorder());
		d.setBorder(BorderFactory.createEmptyBorder());

		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, c, d);
		splitPane.setOneTouchExpandable(true);
		splitPane.setBorder(BorderFactory.createEmptyBorder());

		splitPane.setDividerLocation(Setting.get("divider.horizont." + id, width));

		// Add save option
		c.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentResized(ComponentEvent e) {
				Setting.set("divider.horizont." + id, splitPane.getDividerLocation());

			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});

		return splitPane;
	}

	/**
	 * Create a Button
	 * 
	 * @param name
	 * @param icon
	 * @param l
	 * @return
	 */
	public static JButton b(final String text, final Object icon, final AE l) {
		return b(text, icon, l, IconCache.SIZE);
	}

	/**
	 * Create a Button
	 * 
	 * @param name
	 * @param icon
	 * @param l
	 * @return
	 */
	public static JButton b(final String text, final Object icon, final AE l, final int size) {
		// load button
		final JButton b = icon == null ? new JButton(text) : new JButton(text, IconCache.getS(icon, size));

		// install tooltip
		YFactory.installTooltip(b, text, icon);

		b.setActionCommand(text);
		b.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent me) {
				YaFrame.setTooltip(text, icon);

			}
		});

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
	public static JLabel tb(final String text, final Object icon, final AE e) {
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
	public static JLabel tb(final String text, final Object icon, final AE ae, final int size) {
		// create button
		// JideButton b = new JideButton(IconCache.get(icon,size));
		// b.addActionListener(ae);
		// b.setButtonStyle(JideButton.TOOLBAR_STYLE);
		//
		// // install tooltip
		// YFactory.installTooltip(b, text, icon);

		final JLabel l = new JLabel(IconCache.getS(icon, size));

		// install tooltip
		YFactory.installTooltip(l, text, icon);

		l.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		l.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (l.isEnabled()) {
					ae.actionPerformed(null);
				}

			}

			@Override
			public void mouseExited(MouseEvent e) {
				l.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
				// l.setBorder(BorderFactory.

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if (l.isEnabled()) {
					l.setBorder(BorderFactory.createEtchedBorder());
				}

			}
		});

		return l;
	}

	/**
	 * Helpmethod to add the toolbar buttons
	 * 
	 * @param text
	 *            & translate it
	 * @param icon
	 * @param e
	 */
	public static JButton tb(Action action, final String text, final Object icon, final int size) {
		JButton b = new JButton(action);
		b.setText("");
		b.setIcon(IconCache.getS(icon, size));
		installTooltip(b, text, icon);

		return b;
	}

	/**
	 * Install tooltip
	 * 
	 * @param ele
	 * @param tooltip
	 */
	public static JComponent installTooltip(JComponent ele, final String tooltip, final Object icon) {
		// install tooltip
		ele.setToolTipText(tooltip);
		ele.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent arg0) {
				YaFrame.setTooltip(tooltip, icon);

			}
		});
		return ele;
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

	/**
	 * Add a header
	 * 
	 * @param title
	 * @param icon
	 * @param comp
	 * @return
	 */
	public static JXTitledPanel addHeader(final String title, final String icon, final JComponent comp) {

		// visible fix
		comp.setBorder(BorderFactory.createEmptyBorder());

		final JXTitledPanel j = new JXTitledPanel(title, comp);
		if (icon != null) {
			j.setLeftDecoration(new JLabel(IconCache.get(icon)));
		}
		j.setBorder(BorderFactory.createEmptyBorder());

		return j;
	}

	/**
	 * Add the the toolbar 4 Zoom Buttons
	 * 
	 * @param bar
	 * @param zoom
	 * @return
	 */
	public static YToolBar installZoomlevel(YToolBar bar, final IZoom zoom) {
		// set toolbar
		bar.add(YFactory.tb(I18N.t("Ins Fenster einfassen"), "zoom-actual", new AE() {

			@Override
			public void run() { // set to fill
				Dimension view = zoom.getViewDimension();
				Dimension obj = zoom.getObjectDimension();

				double zoomX = (float) view.getWidth() / obj.getWidth();
				double zoomY = (float) view.getHeight() / obj.getHeight();

				zoomX = zoomX < zoomY ? zoomX : zoomY;

				zoom.setZoomLevel(zoomX);

			}
		}));
		bar.add(YFactory.tb(I18N.t("100% Ansicht"), "zoom-100", new AE() {

			@Override
			public void run() { // set to 100
				zoom.setZoomLevel(1);

			}
		}));

		bar.addSeparator();

		bar.add(YFactory.tb(I18N.t("+10% Zoom"), "zoom-in", new AE() {

			@Override
			public void run() { // fill size?
				zoom.setZoomLevel(zoom.getZoomLevel() + 0.1);

			}
		}));
		bar.add(YFactory.tb(I18N.t("-10% Zoom"), "zoom-out", new AE() {

			@Override
			public void run() { // fill size?
				zoom.setZoomLevel(zoom.getZoomLevel() - 0.1);

			}
		}));

		return bar;
	}

	/**
	 * Overlay text area
	 * 
	 * @param area
	 * @param mess
	 */
	public static DefaultOverlayable createOverlayTextArea(final JTextArea area, String mess) {
		// build
		JLabel overlayLabel = new JLabel(mess);
		overlayLabel.setForeground(SystemColor.textInactiveText);

		final DefaultOverlayable overlayTextArea = new DefaultOverlayable(new JScrollPane(area));
		area.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (area.getDocument().getLength() > 0) {
					overlayTextArea.setOverlayVisible(false);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (area.getDocument().getLength() == 0) {
					overlayTextArea.setOverlayVisible(true);
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});
		area.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				overlayTextArea.setOverlayVisible(false);
			}

			@Override
			public void focusLost(FocusEvent e) {
				overlayTextArea.setOverlayVisible(area.getDocument().getLength() == 0);
			}
		});
		overlayTextArea.addOverlayComponent(overlayLabel);

		// has text?
		overlayTextArea.setOverlayVisible(area.getDocument().getLength() == 0);

		// get it
		return overlayTextArea;
	}
}
