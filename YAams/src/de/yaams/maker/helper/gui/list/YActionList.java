/**
 * 
 */
package de.yaams.maker.helper.gui.list;

import java.util.ArrayList;

/**
 * @author Nebli
 * 
 */
public abstract class YActionList extends YBasisListElementArrayList {
	private static final long serialVersionUID = -5255156642930356829L;

	/**
	 * @param ary
	 */
	public YActionList(final ArrayList<BasisListElement> ary) {
		super(ary);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#add()
	 */
	@Override
	protected void add() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#info()
	 */
	@Override
	protected void info() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#configIntern()
	 */
	@Override
	protected void configIntern() {
		((BasisAction) getSelectedObject()).perform();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#oneClick()
	 */
	@Override
	protected void selected() {
		config();
	}

}
