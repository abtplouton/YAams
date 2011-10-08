package de.yaams.maker.helper.gui.tree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class YCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = -2458729445254861568L;
	
	/**
	 * Render the selected node
	 * 
	 * @param tree
	 * @param value
	 * @param sel
	 * @param expanded
	 * @param leaf
	 * @param row
	 * @param hasFocus
	 * @return
	 */
	@Override public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel,
			final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
		
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		
		// draw it
		try {
			final YTreeNode t = (YTreeNode) ((DefaultMutableTreeNode) value).getUserObject();
			setText(t.getTranslation());
			// add icon?
			if (t.getIco() != null) {
				setIcon(t.getIco());
			}
		} catch (final Throwable t) {
			setText(t.getClass().getSimpleName() + ": " + t.getMessage());
			t.printStackTrace();
		}
		
		return this;
	}
	
}