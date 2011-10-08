/**
 * 
 */
package de.yaams.maker.programm.ress;

import java.io.File;
import java.util.HashMap;

import de.yaams.maker.programm.project.Project;

/**
 * @author abby
 * 
 */
public class RessInfoFile {

	protected boolean canDelete, canUse;
	protected String relativePath;
	protected File absolutePath;
	protected HashMap<String, Object> data;
	protected Project project;

	/**
	 * Create it
	 * 
	 * @param project
	 * @param relativePath
	 */
	public RessInfoFile(Project project, String relativePath) {
		canDelete = true;
		canUse = true;
		this.project = project;
		this.relativePath = relativePath;
		this.absolutePath = new File(project.getPath(), relativePath);
		data = new HashMap<String, Object>();
	}

	/**
	 * @return the canDelete
	 */
	public boolean isCanDelete() {
		return canDelete;
	}

	/**
	 * @param canDelete
	 *            the canDelete to set
	 */
	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

	/**
	 * @return the canUse
	 */
	public boolean isCanUse() {
		return canUse;
	}

	/**
	 * @param canUse
	 *            the canUse to set
	 */
	public void setCanUse(boolean canUse) {
		this.canUse = canUse;
	}

	/**
	 * @return the relativePath
	 */
	public String getRelativePath() {
		return relativePath;
	}

	/**
	 * @param relativePath
	 *            the relativePath to set
	 */
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	/**
	 * @return the absolutePath
	 */
	public File getAbsolutePath() {
		return absolutePath;
	}

	/**
	 * @param absolutePath
	 *            the absolutePath to set
	 */
	public void setAbsolutePath(File absolutePath) {
		this.absolutePath = absolutePath;
	}

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
	 * @return the data
	 */
	public HashMap<String, Object> getData() {
		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return absolutePath.getName();
	}

}
