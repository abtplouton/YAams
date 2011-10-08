package de.yaams.maker.helper.gui.tree;

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXTree;

import de.yaams.maker.helper.I18N;

public class TreeFactory {

	/**
	 * Start to create a tree
	 * 
	 * @return
	 */
	public static DefaultMutableTreeNode createTree() {
		return new DefaultMutableTreeNode(new YTreeNode("homepage", "home", I18N.t("Willkommen")));
	}

	/**
	 * Add Nodes to the tree
	 * 
	 * @param top
	 * @param l
	 * @return
	 */
	public static DefaultMutableTreeNode addChild(final DefaultMutableTreeNode top, final YTreeNode t) {
		final DefaultMutableTreeNode child = new DefaultMutableTreeNode(t);
		top.add(child);
		return child;
	}

	/**
	 * Add multible Nodes to the tree
	 * 
	 * @param top
	 * @param t
	 */
	public static void addChild(final DefaultMutableTreeNode top, final YTreeNode[] t) {
		for (int i = 0, l = t.length; i < l; i++)
			addChild(top, t[i]);
	}

	/**
	 * Finish the Building of the tree
	 * 
	 * @param top
	 * @param l
	 * @return
	 */
	public static JTree getTree(final DefaultMutableTreeNode top, final TreeSelectionListener l) {

		// Create a tree that allows one selection at a time.
		final JTree tree = new JXTree(top);
		// tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		// tree.setRootVisible(false);
		// tree.setRolloverEnabled(true);
		// tree.addHighlighter(new
		// ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, null, Color.BLUE));

		// Set the icon for leaf nodes.
		tree.setCellRenderer(new YCellRenderer());

		// Listen for when the selection changes.
		tree.addTreeSelectionListener(l);

		// expand it
		// expandAll(tree, true);

		// Create the scroll pane and add the tree to it.
		return tree;
	}

	/**
	 * If expand is true, expands all nodes in the tree. Otherwise, collapses
	 * all nodes in the tree.
	 * 
	 * @param tree
	 * @author http://forum.byte-welt.net/showpost.php?s=0
	 *         ae40f898d8b8df6940ad13b24fe3a28&p=7384&postcount=2
	 * @param expand
	 */
	public static void expandAll(final JTree tree, final boolean expand) {
		final TreeNode root = (TreeNode) tree.getModel().getRoot();
		// Traverse tree from root
		expandAll(tree, new TreePath(root), expand);
	}

	/**
	 * Private helpermethod
	 * 
	 * @param tree
	 * @param parent
	 * @param expand
	 */
	private static void expandAll(final JTree tree, final TreePath parent, final boolean expand) {
		// Traverse children
		final TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0)
			for (final Enumeration<?> e = node.children(); e.hasMoreElements();) {
				final TreeNode n = (TreeNode) e.nextElement();
				final TreePath path = parent.pathByAddingChild(n);
				expandAll(tree, path, expand);
			}
		// Expansion or collapse must be done bottom-up
		if (expand)
			tree.expandPath(parent);
		else
			tree.collapsePath(parent);
	}
}
