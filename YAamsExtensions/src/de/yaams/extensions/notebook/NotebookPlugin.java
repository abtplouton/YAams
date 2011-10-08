/**
 * 
 */
package de.yaams.extensions.notebook;

import java.util.HashMap;

import de.yaams.extensions.notebook.icons.NIcons;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.extensions.IExtension;
import de.yaams.maker.helper.gui.YMessagesDialog;
import de.yaams.maker.helper.gui.icons.IconCache;
import de.yaams.maker.programm.plugins.core.BasePlugin;
import de.yaams.maker.programm.project.Project;

/**
 * @author Nebli
 * 
 */
public class NotebookPlugin extends BasePlugin {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.core.helper.plugins.BasePlugin#useable(de.yaams.core.helper.
	 * gui.YMessagesDialog, int)
	 */
	@Override
	public boolean useable(YMessagesDialog md) {
		return isVersionInstall(null, 0.005, 0.006, md);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.core.helper.plugins.BasePlugin#start()
	 */
	@Override
	public void start() {
		// add icon
		IconCache.addPNG(NIcons.class, "notes", "bold", "italic", "underline", "leaf-black", "leaf-blue", "leaf-red", "leaf-yellow",
				"leaf-green", "redo", "undo");

		// add ex
		ExtentionManagement.add(Project.EXLOAD, new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {
				Project p = (Project) objects.get("project");

				// add it
				if (!p.getObjects().containsKey("notes")) {
					p.addObjManager(new NoteObjManager(), "notes");
				}
			}
		});
	}

}
