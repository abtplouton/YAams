/**
 * 
 */
package de.yaams.maker.programm.project.objects.simple;

import java.io.File;
import java.util.ArrayList;

import de.yaams.maker.helper.FileHelper;
import de.yaams.maker.helper.JavaHelper;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.programm.project.objects.BasicObject;
import de.yaams.maker.programm.project.objects.BasicObjectManager;
import de.yaams.maker.programm.tabs.TabEvent;

/**
 * @author abby
 * 
 */
public abstract class SimpleObjectManagement extends BasicObjectManager {

	protected String file, ObjID;

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public SimpleObjectManagement(String title, String desc, String icon, String file, String ObjID) {
		super(title, desc, icon);
		this.file = file;
		this.ObjID = ObjID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectManager#loadObjects()
	 */
	@Override
	public void loadObjects() {
		objects = (ArrayList<BasisListElement>) FileHelper.loadXML(new File(project.getPath(), file));
		// exist?
		if (objects == null) {
			objects = new ArrayList<BasisListElement>();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectManager#saveObjects()
	 */
	@Override
	public void saveObjects() {
		// remove status
		for (BasisListElement b : objects) {
			b.setModified(false);
		}

		// save
		FileHelper.saveXML(new File(project.getPath(), file), objects);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectManager#getTabUId()
	 */
	@Override
	public String getTabUId() {
		return SimpleObjectTab.ID;
	}

	/**
	 * Get the tab id to open the tab to edit the objects
	 * 
	 * @return
	 */
	@Override
	public String getTabId() {
		return TabEvent.buildParameter(getTabUId(), project, null, "obj", ObjID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectManager#getTabId(de
	 * .yaams.maker.programm.project.objects.BasicObject)
	 */
	@Override
	public String getTabId(BasicObject o) {
		return TabEvent.buildParameter(getTabUId(), project, JavaHelper.createHashString("selected", Integer.toString(objects.indexOf(o))),
				"obj", ObjID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectTab#buildInternContent
	 * (de.yaams.maker.helper.gui.list.BasisListElement)
	 */
	public abstract void buildInternContent(BasisListElement selectedObject, FormBuilder f);

}
