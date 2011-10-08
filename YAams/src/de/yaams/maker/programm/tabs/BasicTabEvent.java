/**
 * 
 */
package de.yaams.maker.programm.tabs;

import java.util.HashMap;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.JavaHelper;
import de.yaams.maker.helper.gui.bcb.BcbBuilder;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.programm.YaFrame;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.ProjectManagement;
import de.yaams.maker.programm.project.ProjectTabEvent;

/**
 * @author abby
 * 
 */
public class BasicTabEvent extends TabEvent {

	public static final String HOME = "home";

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.tabs.TabEvent#getTab(java.lang.String)
	 */
	@Override
	public YaTab getTab(String id, Project p, HashMap<String, String> parameters, HashMap<String, String> arguments) {

		if (HomeTab.OPTIONS.equals(id)) {
			return new OptionsTab();
		}

		if (HOME.equals(id)) {
			return new HomeTab();
		}

		return null;
	}

	/**
	 * Open Plugins
	 */
	public static void openPlugin() {
		YaFrame.open(TabEvent.buildParameter(HomeTab.OPTIONS, null, JavaHelper.createHashStringObj("select", "plugin")));
	}

	/**
	 * Open Feedback
	 */
	public static void openFeedback() {
		YaFrame.open(TabEvent.buildParameter(HomeTab.OPTIONS, null, JavaHelper.createHashStringObj("select", "mail_web")));
	}

	/**
	 * Build the bar and add the main elements
	 * 
	 * @param bcb
	 *            , the bar
	 * @param id
	 *            , the element of the selected tab/project id
	 */
	public static void buildBcb(BcbBuilder bcb, String id) {
		// add basic
		bcb.addElement(I18N.t("Willkommen"), "home", HOME, true);
		bcb.addSeperator();

		// add next step?
		if (HOME.equals(id)) {
			return;
		}

		// add basic
		bcb.addElement(I18N.t("Options"), "opts", HomeTab.OPTIONS, HomeTab.OPTIONS.equals(id));

		// add project?
		for (BasisListElement p : ProjectManagement.getProjects()) {
			bcb.addElement(p.getTitle(), p.getIcon(), TabEvent.buildParameter(ProjectTabEvent.HOME, (Project) p, null), ((Project) p)
					.getHash().equals(id));

		}
	}
}
