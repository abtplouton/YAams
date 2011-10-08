/**
 * 
 */
package de.yaams.extensions.rgssproject;

import de.yaams.extensions.rgssproject.tab.RGSSRessourceTab;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.programm.project.objects.SingleObjectManager;

/**
 * @author abby
 * 
 */
public class RessourceObjectManager extends SingleObjectManager {

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public RessourceObjectManager() {
		super(I18N.t("Ressourcen"), I18N.t("Verwaltet, Sortiert, Fügt hinzu & Löscht Ressourcen, wie Grafiken und Sounds"), "ress");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectManager#getTabUId()
	 */
	@Override
	public String getTabUId() {
		return RGSSRessourceTab.ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectManager#getGroup()
	 */
	@Override
	public String getGroup() {
		return I18N.t("Verwaltung");
	}

}
