/**
 * 
 */
package de.yaams.maker.programm.project.empty;

import java.io.File;
import java.util.ArrayList;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.ProjectType;
import de.yaams.maker.programm.project.tab.ProjectHomeTab;
import de.yaams.maker.programm.ress.RessInfoFile;
import de.yaams.maker.programm.tabs.YaTab;

/**
 * @author Praktikant
 * 
 */
public class EmptyProjectType extends ProjectType {

	/**
	 * @param type
	 */
	public EmptyProjectType() {
		super("empty");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.project.ProjectType#getTitle()
	 */
	@Override
	public String getTitle() {
		return I18N.t("!Missing Type!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.project.ProjectType#getIcon()
	 */
	@Override
	public String getIcon() {
		return "error";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.ProjectType#leftForm(de.yaams.maker.helper
	 * .gui.form.core.FormBuilder, de.yaams.maker.programm.project.Project)
	 */
	@Override
	public void leftForm(FormBuilder form, Project project) {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.ProjectType#openTab(de.yaams.maker.programm
	 * .project.Project)
	 */
	@Override
	public YaTab getHomeTab(Project project) {
		return new ProjectHomeTab(project);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.ProjectType#createProject(java.io.File)
	 */
	@Override
	public boolean createProject(Project project) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.ProjectType#getFilesInFolder(de.yaams
	 * .maker.programm .project.Project, java.io.File)
	 */
	@Override
	public ArrayList<RessInfoFile> getFilesInFolder(Project project, String folder) {
		return new ArrayList<RessInfoFile>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.ProjectType#getFilesInFolder(de.yaams
	 * .maker.programm .project.Project, java.io.File)
	 */
	@Override
	public File getFile(Project project, String folder) {
		return null;
	}

}
