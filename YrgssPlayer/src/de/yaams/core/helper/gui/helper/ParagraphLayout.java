package de.yaams.core.helper.gui.helper;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

public class ParagraphLayout extends ConstraintLayout {
	public static final int TYPE_MASK = 3;
	public static final int STRETCH_H_MASK = 4;
	public static final int STRETCH_V_MASK = 8;
	public static final int NEW_PARAGRAPH_VALUE = 1;
	public static final int NEW_PARAGRAPH_TOP_VALUE = 2;
	public static final int NEW_LINE_VALUE = 3;
	public static final Integer NEW_PARAGRAPH = new Integer(1);
	public static final Integer NEW_PARAGRAPH_TOP = new Integer(2);
	public static final Integer NEW_LINE = new Integer(3);
	public static final Integer STRETCH_H = new Integer(4);
	public static final Integer STRETCH_V = new Integer(8);
	public static final Integer STRETCH_HV = new Integer(12);
	public static final Integer NEW_LINE_STRETCH_H = new Integer(7);
	public static final Integer NEW_LINE_STRETCH_V = new Integer(11);
	public static final Integer NEW_LINE_STRETCH_HV = new Integer(15);
	protected int hGapMajor;
	protected int vGapMajor;
	protected int hGapMinor;
	protected int vGapMinor;
	protected int rows;
	protected int colWidth1;
	protected int colWidth2;

	public ParagraphLayout() {
		this(10, 10, 12, 11, 4, 4);
	}

	public ParagraphLayout(int hMargin, int vMargin, int hGapMajor, int vGapMajor, int hGapMinor, int vGapMinor) {
		this.hMargin = hMargin;
		this.vMargin = vMargin;
		this.hGapMajor = hGapMajor;
		this.vGapMajor = vGapMajor;
		this.hGapMinor = hGapMinor;
		this.vGapMinor = vGapMinor;
	}

	@Override
	public void measureLayout(Container target, Dimension dimension, int type) {
		int count = target.getComponentCount();
		if (count > 0) {
			Insets insets = target.getInsets();
			Dimension size = target.getSize();
			int x = 0;
			int y = 0;
			int rowHeight = 0;
			int colWidth = 0;
			int numRows = 0;
			boolean lastWasParagraph = false;

			Dimension[] sizes = new Dimension[count];

			this.colWidth1 = (this.colWidth2 = 0);

			for (int i = 0; i < count; i++) {
				Component c = target.getComponent(i);
				if (includeComponent(c)) {
					Dimension d = getComponentSize(c, type);
					int w = d.width;
					int h = d.height;
					sizes[i] = d;
					Integer n = (Integer) getConstraint(c);

					if ((i == 0) || (n == NEW_PARAGRAPH) || (n == NEW_PARAGRAPH_TOP)) {
						if (i != 0)
							y += rowHeight + this.vGapMajor;
						this.colWidth1 = Math.max(this.colWidth1, w);
						colWidth = 0;
						rowHeight = 0;
						lastWasParagraph = true;
					} else if ((n == NEW_LINE) || (lastWasParagraph)) {
						x = 0;
						if ((!lastWasParagraph) && (i != 0))
							y += rowHeight + this.vGapMinor;
						colWidth = w;
						this.colWidth2 = Math.max(this.colWidth2, colWidth);
						if (!lastWasParagraph)
							rowHeight = 0;
						lastWasParagraph = false;
					} else {
						colWidth += w + this.hGapMinor;
						this.colWidth2 = Math.max(this.colWidth2, colWidth);
						lastWasParagraph = false;
					}
					rowHeight = Math.max(h, rowHeight);
				}

			}

			if (dimension != null) {
				dimension.width = (this.colWidth1 + this.hGapMajor + this.colWidth2);
				dimension.height = (y + rowHeight);
			} else {
				int spareHeight = size.height - (y + rowHeight) - insets.top - insets.bottom - 2 * this.vMargin;
				x = 0;
				y = 0;
				lastWasParagraph = false;
				int start = 0;
				int rowWidth = 0;
				Integer paragraphType = NEW_PARAGRAPH;
				boolean stretchV = false;

				boolean firstLine = true;
				for (int i = 0; i < count; i++) {
					Component c = target.getComponent(i);
					if (includeComponent(c)) {
						Dimension d = sizes[i];
						int w = d.width;
						int h = d.height;
						Integer n = (Integer) getConstraint(c);
						int nv = n != null ? n.intValue() : 0;

						if ((i == 0) || (n == NEW_PARAGRAPH) || (n == NEW_PARAGRAPH_TOP)) {
							if (i != 0)
								layoutRow(target, sizes, start, i - 1, y, rowWidth, rowHeight, firstLine, type, paragraphType);
							stretchV = false;
							paragraphType = n;
							start = i;
							firstLine = true;
							if (i != 0)
								y += rowHeight + this.vGapMajor;
							rowHeight = 0;
							rowWidth = this.colWidth1 + this.hGapMajor - this.hGapMinor;
							lastWasParagraph = true;
						} else if ((n == NEW_LINE) || (lastWasParagraph)) {
							if (!lastWasParagraph) {
								layoutRow(target, sizes, start, count - 1, y, rowWidth, rowHeight, firstLine, type, paragraphType);
								stretchV = false;
								start = i;
								firstLine = false;
								y += rowHeight + this.vGapMinor;
								rowHeight = 0;
							}
							rowWidth += sizes[i].width + this.hGapMinor;
							lastWasParagraph = false;
						} else {
							rowWidth += sizes[i].width + this.hGapMinor;
							lastWasParagraph = false;
						}
						if (((nv & 0x8) != 0) && (!stretchV)) {
							stretchV = true;
							h += spareHeight;
						}
						rowHeight = Math.max(h, rowHeight);
					}
				}
				layoutRow(target, sizes, start, count - 1, y, rowWidth, rowHeight, firstLine, type, paragraphType);
			}
		}
	}

	protected void layoutRow(Container target, Dimension[] sizes, int start, int end, int y, int rowWidth, int rowHeight,
			boolean paragraph, int type, Integer paragraphType) {
		int x = 0;
		Insets insets = target.getInsets();
		Dimension size = target.getSize();
		int spareWidth = size.width - rowWidth - insets.left - insets.right - 2 * this.hMargin;

		for (int i = start; i <= end; i++) {
			Component c = target.getComponent(i);
			if (includeComponent(c)) {
				Integer n = (Integer) getConstraint(c);
				int nv = n != null ? n.intValue() : 0;
				Dimension d = sizes[i];
				int w = d.width;
				int h = d.height;

				if ((nv & 0x4) != 0) {
					w += spareWidth;
					Dimension max = getComponentSize(c, 2);
					Dimension min = getComponentSize(c, 1);
					w = Math.max(min.width, Math.min(max.width, w));
				}
				if ((nv & 0x8) != 0) {
					h = rowHeight;
					Dimension max = getComponentSize(c, 2);
					Dimension min = getComponentSize(c, 1);
					h = Math.max(min.height, Math.min(max.height, h));
				}

				if (i == start) {
					if (paragraph)
						x = this.colWidth1 - w;
					else
						x = this.colWidth1 + this.hGapMajor;
				} else if ((paragraph) && (i == start + 1)) {
					x = this.colWidth1 + this.hGapMajor;
				}
				int yOffset = paragraphType == NEW_PARAGRAPH_TOP ? 0 : (rowHeight - h) / 2;
				if (target.getComponentOrientation().isLeftToRight())
					c.setBounds(insets.left + this.hMargin + x, insets.top + this.vMargin + y + yOffset, w, h);
				else
					c.setBounds(size.width - insets.right - insets.left - this.hMargin - x - w, insets.top + this.vMargin + y + yOffset, w,
							h);
				x += w + this.hGapMinor;
			}
		}
	}
}