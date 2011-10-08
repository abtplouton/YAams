/**
 * 
 */
package de.yaams.maker.programm.project.objects;

import java.util.ArrayList;

import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.tabs.TabEvent;

/**
 * @author abby
 * 
 */
public abstract class BasicObjectManager extends BasisListElement {

	protected Project project;
	protected ArrayList<BasisListElement> objects;

	public static final String GRANOTHER = "Sonstiges";

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public BasicObjectManager(String title, String desc, String icon) {
		super(title, desc, icon);
	}

	/**
	 * Load the objects
	 */
	public abstract void loadObjects();

	/**
	 * Save it
	 */
	public abstract void saveObjects();

	/**
	 * Get the tab id to open the tab to edit the objects
	 * 
	 * @return
	 */
	public String getTabId() {
		return TabEvent.buildParameter(getTabUId(), project, null);
	}

	/**
	 * Get the tab id to open the tab to edit the objects
	 * 
	 * @return
	 */
	public abstract String getTabUId();

	/**
	 * Get the tab id to open the tab to edit this object
	 * 
	 * @return
	 */
	public abstract String getTabId(BasicObject o);

	/**
	 * Get the groupname of this objects
	 * 
	 * @return
	 */
	public abstract String getGroup();

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @param project
	 *            the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * @return the objects
	 */
	public ArrayList<BasisListElement> getObjects() {
		return objects;
	}

	/**
	 * Create a new object
	 * 
	 * @return
	 */
	public abstract BasisListElement createNewObject();
}
