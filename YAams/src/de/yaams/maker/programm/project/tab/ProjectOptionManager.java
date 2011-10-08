/**
 * 
 */
package de.yaams.maker.programm.project.tab;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.programm.project.objects.SingleObjectManager;

/**
 * @author abby
 * 
 */
public class ProjectOptionManager extends SingleObjectManager {

	public static final String ID = "opts";

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public ProjectOptionManager() {
		super(I18N.t("Optionen"), I18N.t("Verwaltet die grundlegenen Projectoptionen"), "opts");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectManager#getTabId()
	 */
	@Override
	public String getTabUId() {
		return ProjectOptionsTab.ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectManager#getGroup()
	 */
	@Override
	public String getGroup() {
		return GRANOTHER;
	}

}
