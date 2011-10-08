/**
 * 
 */
package de.yaams.maker.helper.gui.tree;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.YEx;

/**
 * @author abt
 * 
 */
public class TreeHelper {
	
	private final HashMap<String, AE> actions;
	private final HashMap<String, YTreeNode> headers;
	private final HashMap<String, DefaultMutableTreeNode> headersNodes;
	private final DefaultMutableTreeNode root;
	private String defaultpath;
	
	/**
	 * Create a new YTree
	 */
	public TreeHelper() {
		headers = new HashMap<String, YTreeNode>();
		actions = new HashMap<String, AE>();
		headersNodes = new HashMap<String, DefaultMutableTreeNode>();
		root = TreeFactory.createTree();
		
	}
	
	/**
	 * Add a element to the tree
	 * 
	 * @param name
	 * @param icon
	 * @param a
	 */
	public void add(final String name, final String icon, final AE a) {
		add(name, new YTreeNode(name, icon, name), a);
	}
	
	/**
	 * Add a new category to the database
	 * 
	 * @param path
	 * @param node
	 */
	public void add(final String path, final YTreeNode node, final AE a) {
		if (has(path)) {
			YEx.info("Can not add " + node + " under path " + path + ", because it exist.",
					new IllegalArgumentException());
			return;
		}
		
		// check for null
		if (path == null || node == null) {
			YEx.info("Node " + node + " or path " + path + " is null", new NullPointerException());
			return;
		}
		
		// build the tree
		DefaultMutableTreeNode f = null;
		// has father?
		if (path.indexOf('.') == -1) {
			f = TreeFactory.addChild(root, node);
		} else {
			// find father
			final StringTokenizer s = new StringTokenizer(path, ".");
			final StringBuffer father = new StringBuffer("");
			String b = "";
			// create substring
			while (s.hasMoreElements()) {
				b = s.nextToken();
				if (s.hasMoreElements()) {
					// add split point or first entry?
					if (father.length() > 0) {
						father.append(".");
					}
					father.append(b);
				}
			}
			
			// exist father?
			if (headersNodes.containsKey(father.toString())) {
				f = TreeFactory.addChild(headersNodes.get(father.toString()), node);
			} else {
				YEx.info("Can not add treenode " + path + ", because father not exist.",
						new IllegalArgumentException());
				add(b, node, a);
				return;
			}
		}
		
		// set it
		node.setAction(path);
		
		// add it
		headers.put(path, node);
		
		// add it
		headersNodes.put(path, f);
		actions.put(path, a);
	}
	
	/**
	 * Look if this cat already register
	 * 
	 * @param path
	 * @return
	 */
	public boolean has(final String path) {
		return headers.containsKey(path);
	}
	
	/**
	 * @return the tree
	 */
	public JTree getTree() {
		
		final JTree t = TreeFactory.getTree(root, null);
		
		// Listen for when the selection changes.
		// t.addTreeSelectionListener(new TreeSelectionListener() {
		//
		// /**
		// * Active or deactiv the button
		// */
		// public void valueChanged(final TreeSelectionEvent e) {
		// try {
		// final DefaultMutableTreeNode node = (DefaultMutableTreeNode)
		// t.getLastSelectedPathComponent();
		// final YTreeNode y = (YTreeNode) node.getUserObject();
		// actions.get(y.getAction()).actionPerformed(null);
		// } catch (final Throwable t) {
		// YDialog.exception("Can not run tree rendern for " + e, t);
		// }
		// }
		// });
		
		// add double click elements
		t.addMouseListener(new MouseListener() {
			
			@Override public void mouseReleased(final MouseEvent e) {}
			
			@Override public void mousePressed(final MouseEvent e) {}
			
			@Override public void mouseExited(final MouseEvent e) {}
			
			@Override public void mouseEntered(final MouseEvent e) {}
			
			@Override public void mouseClicked(final MouseEvent e) {
				// show it
				if (e.getClickCount() == 2) {
					try {
						final DefaultMutableTreeNode node = (DefaultMutableTreeNode) t.getLastSelectedPathComponent();
						final YTreeNode y = (YTreeNode) node.getUserObject();
						actions.get(y.getAction()).actionPerformed(null);
					} catch (final Throwable t) {
						YEx.warn("Can not run tree action for " + e, t);
					}
				}
				
			}
		});
		
		if (defaultpath != null) {
			t.addSelectionPath(new TreePath(headers.get(defaultpath)));
		}
		
		return t;
	}
	
	/**
	 * @return the header node
	 */
	public YTreeNode getHeader(final String cat) {
		return headers.get(cat);
	}
	
	/**
	 * @param defaultpath
	 *            the defaultpath to set
	 */
	public void setDefaultpath(final String defaultpath) {
		this.defaultpath = defaultpath;
	}
}
