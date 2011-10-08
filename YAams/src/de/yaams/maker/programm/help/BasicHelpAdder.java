/**
 * 
 */
package de.yaams.maker.programm.help;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.helpcenter.HelpCenterManagement;
import de.yaams.maker.programm.YAamsCore;
import de.yaams.maker.programm.project.Project;

/**
 * @author abt
 * 
 */
public class BasicHelpAdder {
	
	/**
	 * Create a new BasicHelpAdder
	 */
	public BasicHelpAdder() {
		HelpCenterManagement.register("welcome", I18N.t("Welcome to {0}", YAamsCore.NAME), "yaams",
				BasicHelpAdder.class.getResource("Welcome.html"));
		HelpCenterManagement.register("project_overview", I18N.t("Project Management"), "project",
				BasicHelpAdder.class.getResource("Project.html"));
		HelpCenterManagement.register("plugins", I18N.t("Plugins"), "plugin",
				BasicHelpAdder.class.getResource("Plugin.html"));
		HelpCenterManagement.register("project.empty", I18N.t("Empty Project"), "project",
				Project.class.getResource("project_empty.html"));
		HelpCenterManagement.register("project.new", I18N.t("Create a new project"), "add",
				Project.class.getResource("project_new.html"));
		
	}
	
}
