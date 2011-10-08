/**
 * 
 */
package de.yaams.extensions.quiz;

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
 * @author abby
 * 
 */
public class QuizProjectType extends ProjectType {

	/**
	 * @param type
	 */
	public QuizProjectType() {
		super("quiz");
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.project.ProjectType#getTitle()
	 */
	@Override
	public String getTitle() {
		return I18N.t("Quiz");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.project.ProjectType#getIcon()
	 */
	@Override
	public String getIcon() {
		return "quiz";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.ProjectType#leftForm(de.yaams.maker.helper
	 * .gui.form.core.FormBuilder, de.yaams.maker.programm.project.Project)
	 */
	@Override
	public void leftForm(FormBuilder form, Project project) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.ProjectType#getHomeTab(de.yaams.maker
	 * .programm.project.Project)
	 */
	@Override
	public YaTab getHomeTab(Project project) {
		return new ProjectHomeTab(project);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.ProjectType#createProject(de.yaams.maker
	 * .programm.project.Project)
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
	 * .maker.programm.project.Project, java.lang.String)
	 */
	@Override
	public ArrayList<RessInfoFile> getFilesInFolder(Project project, String folder) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.ProjectType#getFile(de.yaams.maker.programm
	 * .project.Project, java.lang.String)
	 */
	@Override
	public File getFile(Project project, String file) {
		// TODO Auto-generated method stub
		return null;
	}

}
