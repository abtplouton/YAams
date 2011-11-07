/**
 * 
 */
package de.yaams.extensions.diamant;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.SwingUtilities;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.RavenSkin;

import com.ezware.dialog.task.TaskDialog;

import de.yaams.extensions.diamant.graph.PWindow;
import de.yaams.extensions.diamant.graph.scene.menu.MenuMainScene;
import de.yaams.extensions.diamant.helper.YEx;

/**
 * @author abby
 * 
 */
public class RunDiamant {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TaskDialog s = new TaskDialog(null, "");
		s.setTitle("");
		s.setResizable(true);
		s.setInstruction("Schon gewusst? Heute ist Halloween.");
		s.setIcon(TaskDialog.StandardIcon.QUESTION);
		s.show();

		try {
			File path = null;
			// valid?
			if (args == null || args.length == 0) {
				path = new File(".").getAbsoluteFile();
			} else {
				// load project
				path = new File(args[0]);
			}

			// valid?
			if (!path.exists() || !path.isDirectory()) {
				throw new FileNotFoundException("No readable path " + path + " found.");

			}

			Project.project = new Project(path);

			// set look and feel
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					SubstanceLookAndFeel.setSkin(RavenSkin.class.getName());
					PWindow w = new PWindow();
					w.setActScene(new MenuMainScene());
					w.start();
				}
			});

		} catch (Throwable e) {
			YEx.error("Can not start game", e);
		}

	}

}
