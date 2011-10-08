/**
 * 
 */
package de.yaams.extensions.rgssproject.script;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.programm.project.objects.SingleObjectManager;

/**
 * @author abby
 * 
 */
public class ScriptManager extends SingleObjectManager {

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public ScriptManager() {
		super("Scripts", null, "script");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectManager#getTabUId()
	 */
	@Override
	public String getTabUId() {
		return ScriptTab.ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectManager#getGroup()
	 */
	@Override
	public String getGroup() {
		return I18N.t("Erweitert");
	}

}
