/**
 * YAams - maker.plugins.core.scenes.core SceneSettingComboBoxRenderer.java
 */
package de.yaams.extensions.substance;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 * @author Administrator
 */
public class SubstanceComboBoxRenderer extends BasicComboBoxRenderer {
	private static final long serialVersionUID = -2151835880398053608L;

	/**
	 * @param list
	 * @param value
	 * @param index
	 * @param isSelected
	 * @param cellHasFocus
	 * @return translated text
	 */
	@Override
	public Component getListCellRendererComponent(final JList list, final Object value, int index, final boolean isSelected,
			final boolean cellHasFocus) {

		final JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		l.setIcon(null);
		if (index == -1) {
			index = list.getSelectedIndex();
		}
		// translate it
		try {
			l.setText(SubstancePlugin.classTitle[index]);
			// add example image
			if (index != 0) {
				l.setIcon(new ImageIcon(getClass().getResource("img/" + index + ".png")));
			}
		} catch (final Throwable t) {
			l.setText(t.toString());
			t.printStackTrace();
		}
		return l;
	}
}
