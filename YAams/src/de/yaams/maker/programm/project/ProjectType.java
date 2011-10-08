/**
 * 
 */
package de.yaams.maker.programm.project;

import java.io.File;
import java.util.ArrayList;

import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.programm.ress.RessInfoFile;
import de.yaams.maker.programm.tabs.YaTab;

/**
 * @author Praktikant
 * 
 */
public abstract class ProjectType {

	protected String type;

	/**
	 * 
	 * @param type
	 */
	public ProjectType(String type) {
		this.type = type;
	}

	/**
	 * Get the title
	 * 
	 * @return
	 */
	public abstract String getTitle();

	/**
	 * Get the icon
	 * 
	 * @return
	 */
	public abstract String getIcon();

	/**
	 * Can overwrite to save special project things
	 * 
	 * @param project
	 */
	public void save(Project project) {}

	/**
	 * Edit the form for the left view, eg add action buttons or another
	 * informations
	 * 
	 * @param form
	 */
	public abstract void leftForm(FormBuilder form, Project project);

	/**
	 * Open the maintab of the project
	 * 
	 * @param project
	 * @return TODO
	 */
	public abstract YaTab getHomeTab(Project project);

	/**
	 * Create Project a new project, eg extract Files
	 * 
	 * @param project
	 * @return successfull create?
	 */
	public abstract boolean createProject(Project project);

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Helpermethod to get all files for this folder, normally for ressources
	 * 
	 * @param project
	 * @param folder
	 * @return
	 */
	public abstract ArrayList<RessInfoFile> getFilesInFolder(Project project, String folder);

	/**
	 * Helpermethod to get a file, normally for ressources
	 * 
	 * @param project
	 * @param folder
	 * @return
	 */
	public abstract File getFile(Project project, String file);

}
