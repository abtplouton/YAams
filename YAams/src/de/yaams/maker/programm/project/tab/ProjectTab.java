/**
 * 
 */
package de.yaams.maker.programm.project.tab;

import de.yaams.maker.helper.gui.bcb.BcbBuilder;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.objects.BasicObjectManager;
import de.yaams.maker.programm.tabs.BasicTabEvent;
import de.yaams.maker.programm.tabs.YaTab;

/**
 * @author Nebli
 * 
 */
public abstract class ProjectTab extends YaTab {
	private static final long serialVersionUID = 6425372096635752001L;

	protected Project project;

	/**
	 * @param project
	 */
	public ProjectTab(final Project project) {
		super();
		this.project = project;
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * Build the bar and add the main elements
	 * 
	 * @param bcb
	 *            , the bar
	 * @param project
	 * @param id
	 *            , the element of the selected tab/project id
	 */
	public static void buildBcb(BcbBuilder bcb, Project project, String id) {
		// add basics
		BasicTabEvent.buildBcb(bcb, project.getHash());

		bcb.addSeperator();

		// add elements
		for (String key : project.getObjects().keySet()) {
			// get it
			BasicObjectManager bom = project.getObjects().get(key);

			bcb.addElement(bom.getTitle(), bom.getIcon(), bom.getTabId(), key.equals(id));
		}
	}
}
