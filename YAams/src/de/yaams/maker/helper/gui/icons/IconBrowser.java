/**
 * 
 */
package de.yaams.maker.helper.gui.icons;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.YScrollPane;
import de.yaams.maker.helper.gui.YToolBar;

/**
 * @author Praktikant
 * 
 */
public class IconBrowser extends JPanel {

	private static final long serialVersionUID = -4045610963978649542L;
	protected int size;
	protected JList list;

	/**
	 * 
	 */
	public IconBrowser(String[] icons) {
		super(new BorderLayout());

		size = 32;

		// build toolbar
		YToolBar y = new YToolBar();
		y.add(YFactory.b("16", null, new AE() {

			@Override
			public void run() {
				size = 16;
				repaint();

			}
		}));
		y.add(YFactory.b("32", null, new AE() {

			@Override
			public void run() {
				size = 32;
				repaint();

			}
		}));
		add(y, BorderLayout.NORTH);

		// build list
		list = new JList(icons == null ? IconCache.getAllUniqueIcons().toArray() : icons);
		list.setCellRenderer(new IconListCellRenderer());
		list.setLayoutOrientation(JList.VERTICAL_WRAP);
		add(new YScrollPane(list), BorderLayout.CENTER);

	}

	/**
	 * Get the selected icon or null
	 * 
	 * @return
	 */
	public String getSelectedIcon() {
		return list.getSelectedIndex() == -1 ? null : list.getSelectedValue().toString();
	}

	/**
	 * Renderer
	 * 
	 * @author Praktikant
	 * 
	 */
	class IconListCellRenderer implements ListCellRenderer {

		protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			renderer.setIcon(IconCache.get(renderer.getText(), size));
			renderer.setPreferredSize(new Dimension(size + size / 16, size + size / 16));
			renderer.setText("");
			return renderer;
		}
	}
}
