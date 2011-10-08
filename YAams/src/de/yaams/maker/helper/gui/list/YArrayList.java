/**
 * 
 */
package de.yaams.maker.helper.gui.list;

import java.util.ArrayList;

import de.yaams.maker.helper.gui.YEx;

/**
 * @author Nebli
 * 
 */
public abstract class YArrayList<T> extends YSimpleList<T> {

	private static final long serialVersionUID = 4844448959768557046L;

	protected ArrayList<T> ary;

	/**
	 * @param ary
	 */
	public YArrayList(final ArrayList<T> ary) {
		super();
		this.ary = ary;

		buildGui();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#getObjectSize()
	 */
	@Override
	public int getObjectSize() {
		return ary.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#getObject(int)
	 */
	@Override
	public T getObject(final int id) {
		try {
			return ary.get(id);
		} catch (final Throwable t) {
			YEx.info("No element for " + id + " found.", t);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#delObject(int)
	 */
	@Override
	public void delObject(final int id) {
		ary.remove(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.packandgo.helper.gui.list.YSimpleList#addObject(java.lang.Object
	 * )
	 */
	@Override
	public void addObject(final T object, int id) {
		ary.add(id, object);

	}

	/**
	 * @return the ary
	 */
	public ArrayList<T> getAry() {
		return ary;
	}

}
