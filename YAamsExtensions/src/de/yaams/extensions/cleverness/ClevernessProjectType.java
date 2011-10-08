/**
 * 
 */
package de.yaams.extensions.cleverness;

import java.io.File;
import java.util.ArrayList;

import de.yaams.maker.helper.FileHelper;
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
public class ClevernessProjectType extends ProjectType {

	/**
	 * @param type
	 */
	public ClevernessProjectType() {
		super("cleverness");
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
	 * de.yaams.maker.programm.project.ProjectType#openHomeTab(de.yaams.maker
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
	 * de.yaams.maker.programm.project.ProjectType#getFilesInFolder(de.yaams
	 * .maker.programm .project.Project, java.io.File)
	 */
	@Override
	public ArrayList<RessInfoFile> getFilesInFolder(Project project, String folder) {
		ArrayList<RessInfoFile> res = new ArrayList<RessInfoFile>();
		File fold = new File(project.getPath(), folder);

		// list folder
		if (fold.canRead() && fold.isDirectory()) {
			for (File f : fold.listFiles()) {
				// right file?
				if (!f.canRead() || !f.isFile() || f.isHidden()) {
					continue;
				}
				// look in the project
				res.add(new RessInfoFile(project, folder + File.separator + f.getName()));
			}
		}

		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.ProjectType#getFilesInFolder(de.yaams
	 * .maker.programm .project.Project, java.io.File)
	 */
	@Override
	public File getFile(Project project, String file) {
		File path = new File(project.getPath(), file);

		// right file?
		if (path.canRead() && path.isFile()) {
			return path;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.ProjectType#createProject(java.io.File)
	 */
	@Override
	public boolean createProject(Project p) {

		// create folders
		FileHelper.mkdirs(new File(p.getPath(), "Ressources"));

		return true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.project.ProjectType#getTitle()
	 */
	@Override
	public String getTitle() {
		return I18N.t("Cleverness");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.project.ProjectType#getIcon()
	 */
	@Override
	public String getIcon() {
		return "cleverness";
	}
}
