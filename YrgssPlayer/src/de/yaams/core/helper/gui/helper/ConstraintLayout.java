package de.yaams.core.helper.gui.helper;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.Hashtable;

public class ConstraintLayout implements LayoutManager2 {
	protected static final int PREFERRED = 0;
	protected static final int MINIMUM = 1;
	protected static final int MAXIMUM = 2;
	protected int hMargin = 0;
	protected int vMargin = 0;
	private Hashtable constraints;
	protected boolean includeInvisible = false;

	@Override
	public void addLayoutComponent(String constraint, Component c) {
		setConstraint(c, constraint);
	}

	@Override
	public void addLayoutComponent(Component c, Object constraint) {
		setConstraint(c, constraint);
	}

	@Override
	public void removeLayoutComponent(Component c) {
		if (this.constraints != null)
			this.constraints.remove(c);
	}

	public void setConstraint(Component c, Object constraint) {
		if (constraint != null) {
			if (this.constraints == null)
				this.constraints = new Hashtable();
			this.constraints.put(c, constraint);
		} else if (this.constraints != null) {
			this.constraints.remove(c);
		}
	}

	public Object getConstraint(Component c) {
		if (this.constraints != null)
			return this.constraints.get(c);
		return null;
	}

	public void setIncludeInvisible(boolean includeInvisible) {
		this.includeInvisible = includeInvisible;
	}

	public boolean getIncludeInvisible() {
		return this.includeInvisible;
	}

	protected boolean includeComponent(Component c) {
		return (this.includeInvisible) || (c.isVisible());
	}

	@Override
	public Dimension minimumLayoutSize(Container target) {
		return calcLayoutSize(target, 1);
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		return calcLayoutSize(target, 2);
	}

	@Override
	public Dimension preferredLayoutSize(Container target) {
		return calcLayoutSize(target, 0);
	}

	public Dimension calcLayoutSize(Container target, int type) {
		Dimension dim = new Dimension(0, 0);
		measureLayout(target, dim, type);
		Insets insets = target.getInsets();
		dim.width += insets.left + insets.right + 2 * this.hMargin;
		dim.height += insets.top + insets.bottom + 2 * this.vMargin;
		return dim;
	}

	@Override
	public void invalidateLayout(Container target) {}

	@Override
	public float getLayoutAlignmentX(Container parent) {
		return 0.5F;
	}

	@Override
	public float getLayoutAlignmentY(Container parent) {
		return 0.5F;
	}

	@Override
	public void layoutContainer(Container target) {
		measureLayout(target, null, 0);
	}

	public void measureLayout(Container target, Dimension dimension, int type) {}

	protected Dimension getComponentSize(Component c, int type) {
		if (type == 1)
			return c.getMinimumSize();
		if (type == 2)
			return c.getMaximumSize();
		return c.getPreferredSize();
	}
}