/**
 * 
 */
package de.yaams.extensions.quiz;

import java.util.HashMap;

import de.yaams.extensions.quiz.question.QuestionObjManager;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.extensions.IExtension;
import de.yaams.maker.helper.gui.YMessagesDialog;
import de.yaams.maker.helper.gui.icons.IconCache;
import de.yaams.maker.programm.plugins.BasePlugin;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.ProjectManagement;

/**
 * @author Nebli
 * 
 */
public class QuizPlugin extends BasePlugin {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.core.helper.plugins.BasePlugin#useable(de.yaams.core.helper.
	 * gui.YMessagesDialog, int)
	 */
	@Override
	public boolean useable(YMessagesDialog md) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.core.helper.plugins.BasePlugin#start()
	 */
	@Override
	public void start() {
		// add icon
		IconCache.addPNG(QuizPlugin.class, "question", "quiz");

		// add project type
		ProjectManagement.registerType(new QuizProjectType());

		// add ex
		ExtentionManagement.add(Project.EXLOAD, new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {
				Project p = (Project) objects.get("project");

				// add it
				if ("quiz".equals(p.getTypeAsString()) && !p.getObjects().containsKey("quiz")) {
					p.addObjManager(new QuestionObjManager(), "quiz");
				}
			}
		});
	}

}
