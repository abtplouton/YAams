/**
 * 
 */
package de.yaams.maker.programm.project.objects;

import java.util.ArrayList;

import de.yaams.maker.helper.gui.list.BasisListElement;

/**
 * @author abby
 * 
 */
public abstract class SingleObjectManager extends BasicObjectManager {

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public SingleObjectManager(String title, String desc, String icon) {
		super(title, desc, icon);
		objects = new ArrayList<BasisListElement>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectManager#loadObjects()
	 */
	@Override
	public void loadObjects() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectManager#saveObjects()
	 */
	@Override
	public void saveObjects() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectManager#getTabId(de
	 * .yaams.maker.programm.project.objects.BasicObject)
	 */
	@Override
	public String getTabId(BasicObject o) {
		return getTabId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectManager#createNewObject
	 * ()
	 */
	@Override
	public BasisListElement createNewObject() {
		return null;
	}

}
