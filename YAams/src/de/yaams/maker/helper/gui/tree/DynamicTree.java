package de.yaams.maker.helper.gui.tree;

/*
 * Copyright (c) 1995 - 2008 Sun Microsystems, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

//package components;

/*
 * This code is based on an example provided by Richard Stanford, 
 * a tutorial reader.
 */

import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class DynamicTree extends JPanel {
	private static final long serialVersionUID = -2832950701763984294L;
	
	protected DefaultMutableTreeNode rootNode;
	protected DefaultTreeModel treeModel;
	protected JTree tree;
	private final Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	public DynamicTree(final String parent, final TreeSelectionListener l, final TreeModelListener t) {
		super(new GridLayout(1, 0));
		
		rootNode = new DefaultMutableTreeNode(new YTreeNode("root", "yaams", parent));
		treeModel = new DefaultTreeModel(rootNode);
		treeModel.addTreeModelListener(t);
		tree = new JTree(treeModel);
		// tree.setEditable(false);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		// tree.setShowsRootHandles(true);
		tree.setRootVisible(false);
		// tree.setRolloverEnabled(true);
		// tree.addHighlighter(new
		// ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, null, Color.BLUE));
		
		// Set the icon for leaf nodes.
		tree.setCellRenderer(new YCellRenderer());
		
		// Listen for when the selection changes.
		tree.addTreeSelectionListener(l);
		
		// expand it
		TreeFactory.expandAll(tree, true);
		
		// new TreeWrapper(tree);
		add(new JScrollPane(tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
	}
	
	/** Remove all nodes except the root node. */
	public void clear() {
		rootNode.removeAllChildren();
		treeModel.reload();
	}
	
	/** Remove the currently selected node. */
	public void removeCurrentNode() {
		final TreePath currentSelection = tree.getSelectionPath();
		if (currentSelection != null) {
			final DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) currentSelection.getLastPathComponent();
			final MutableTreeNode parent = (MutableTreeNode) currentNode.getParent();
			if (parent != null) {
				treeModel.removeNodeFromParent(currentNode);
				return;
			}
		}
		
		// Either there was no selection, or the root was selected.
		toolkit.beep();
	}
	
	/** Add child to the currently selected node. */
	public DefaultMutableTreeNode addObject(final YTreeNode child) {
		DefaultMutableTreeNode parentNode = null;
		final TreePath parentPath = tree.getSelectionPath();
		
		if (parentPath == null)
			parentNode = rootNode;
		else
			parentNode = (DefaultMutableTreeNode) parentPath.getPathComponent(parentPath.getPathCount() - 2);
		
		return addObject(parentNode, child, true);
	}
	
	public DefaultMutableTreeNode addObject(final DefaultMutableTreeNode parent, final YTreeNode child) {
		return addObject(parent, child, false);
	}
	
	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, final YTreeNode child,
			final boolean shouldBeVisible) {
		final DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
		
		if (parent == null)
			parent = rootNode;
		
		// It is key to invoke this on the TreeModel, and NOT
		// DefaultMutableTreeNode
		treeModel.insertNodeInto(childNode, parent, parent.getChildCount());
		
		// Make sure the user can see the lovely new node.
		if (shouldBeVisible)
			tree.scrollPathToVisible(new TreePath(childNode.getPath()));
		return childNode;
	}
	
	/**
	 * @return the tree
	 */
	public JTree getTree() {
		return tree;
	}
}
