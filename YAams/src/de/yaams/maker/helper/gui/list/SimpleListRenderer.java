package de.yaams.maker.helper.gui.list;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import de.yaams.maker.helper.Log;

public class SimpleListRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = -7524958285390435581L;

	protected YSimpleList<?> ysl;

	/**
	 * Create a new SimpleListRenderer
	 * 
	 * @param ysl
	 */
	public SimpleListRenderer(final YSimpleList<?> ysl) {
		super();
		this.ysl = ysl;
	}

	@Override
	public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected,
			final boolean cellHasFocus) {

		try {
			// can view?
			if (index < ysl.getOffSet() || ysl.getItemsPerSite() != -1 && index >= ysl.getOffSet() + ysl.getItemsPerSite()) {
				JLabel l = new JLabel("");
				l.setPreferredSize(new Dimension(20, 0));
				l.setEnabled(false);
				l.setVisible(false);
				return l;
			}

			// create
			final JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			return ysl.getListCellRendererComponent(label, list, value, index, isSelected, cellHasFocus);
		} catch (final Throwable t) {
			Log.ger.error("Can not getListCellRendererComponent", t);
			return new JLabel(t.getClass().getSimpleName() + ": " + t.getMessage());
		}
	}
}
