/**
 * 
 */
package de.yaams.maker.helper.gui.list;

import java.util.ArrayList;

/**
 * @author Nebli
 * 
 */
public abstract class YBasisListElementArrayList extends YArrayList<BasisListElement> {
	private static final long serialVersionUID = 1478770453160852419L;
	
	/**
	 * @param ary
	 */
	public YBasisListElementArrayList(final ArrayList<BasisListElement> ary) {
		super(ary);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.packandgo.helper.gui.list.YSimpleList#getIcon(java.lang.Object)
	 */
	@Override public String getIcon(final BasisListElement o) {
		return o.getIcon();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.packandgo.helper.gui.list.YSimpleList#getDesc(java.lang.Object)
	 */
	@Override public String getDesc(final BasisListElement o) {
		return o.getDesc();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.packandgo.helper.gui.list.YSimpleList#getModified(java.lang.
	 * Object)
	 */
	@Override public boolean isModified(final BasisListElement o) {
		return o.isModified();
	}
	
}
