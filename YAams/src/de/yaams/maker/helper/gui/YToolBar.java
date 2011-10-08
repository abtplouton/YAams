/**
 * 
 */
package de.yaams.maker.helper.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.plaf.UIResource;

/**
 * @author abt
 * 
 */
public class YToolBar extends JPanel implements UIResource {

	private static final long serialVersionUID = -3355452997816379866L;

	private JToolBar left, middle, right;

	/**
	 * Create a new YToolBar
	 */
	public YToolBar() {
		super(new BorderLayout());

	}

	/**
	 * Add a component to the left bar
	 * 
	 * @param j
	 */
	public void add(final JComponent j) {
		// add it
		if (left == null) {
			left = new JToolBar();
			left.setFloatable(false);
			add(left, BorderLayout.WEST);
		}

		left.add(j);
	}

	/**
	 * Add a component to the left bar
	 * 
	 * @param j
	 */
	public void addMiddle(final JComponent j) {
		// add it
		if (middle == null) {
			middle = new JToolBar();
			middle.setFloatable(false);
			middle.setLayout(new FlowLayout(FlowLayout.CENTER));
			final JPanel m = new JPanel(new FlowLayout(FlowLayout.CENTER));
			m.add(middle);
			add(m, BorderLayout.CENTER);
		}

		middle.add(j);
	}

	/**
	 * Add a component to the left bar
	 * 
	 * @param j
	 */
	public void addRight(final JComponent j) {
		// add it
		if (right == null) {
			right = new JToolBar();
			right.setFloatable(false);
			add(right, BorderLayout.EAST);
		}

		right.add(j);
	}

	/**
	 * Add a seperator left
	 */
	public void addSeparator() {
		if (left != null) {
			left.addSeparator();
		}
	}

	/**
	 * Add a seperator middle
	 */
	public void addMiddleSeparator() {
		if (middle != null) {
			middle.addSeparator();
		}
	}

	/**
	 * Add a seperator left
	 */
	public void addRightSeparator() {
		if (right != null) {
			right.addSeparator();
		}
	}

	/**
	 * Clear the bar
	 */
	@Override
	public void removeAll() {
		removeLeft();
		removeMiddle();
		removeRight();
	}

	/**
	 * Clear the bar
	 */
	public void removeLeft() {
		if (left != null) {
			left.removeAll();
		}
	}

	/**
	 * Clear the bar
	 */
	public void removeMiddle() {
		if (middle != null) {
			middle.removeAll();
		}
	}

	/**
	 * Clear the bar
	 */
	public void removeRight() {
		if (right != null) {
			right.removeAll();
		}
	}

	/**
	 * @return the left
	 */
	public JToolBar getLeft() {
		return left;
	}

	/**
	 * @return the middle
	 */
	public JToolBar getMiddle() {
		return middle;
	}

	/**
	 * @return the right
	 */
	public JToolBar getRight() {
		return right;
	}

}
