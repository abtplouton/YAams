/**
 * 
 */
package de.yaams.extensions.rgssproject.type;

import de.yaams.maker.programm.project.Project;

/**
 * @author Praktikant
 * 
 */
public class RpgXpProjectType extends RGSSProjectType {

	/**
	 * @param type
	 */
	public RpgXpProjectType() {
		super("rpgxp");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.ProjectType#createProject(java.io.File)
	 */
	@Override
	public boolean createProject(Project p) {
		// can create?
		if (extractTemplate(p, "xp_english")) {
			readGameIniToData(p);
			return true;
		}
		return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.project.ProjectType#getTitle()
	 */
	@Override
	public String getTitle() {
		return "RPG Maker XP - English";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.project.ProjectType#getIcon()
	 */
	@Override
	public String getIcon() {
		return "rpgxp";
	}

}
