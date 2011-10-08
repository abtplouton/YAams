/**
 * 
 */
package de.yaams.core.helper.integration;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.ezware.dialog.task.TaskDialogs;

import de.yaams.core.helper.I18N;

/**
 * @author abt
 * 
 */
public class EditorMacIntegration extends EditorBIntegration {

	/**
	 * Create a new AppleIntegration
	 */
	// @Override
	// public void start() {
	// Application s = Application.getApplication();
	// s.setEnabledAboutMenu(true);
	// s.setEnabledPreferencesMenu(true);
	// s.setDockIconImage(IconCache.getImage("yaams", 128));
	// s.addApplicationListener(new ApplicationListener() {
	//
	// @Override
	// public void handleReOpenApplication(ApplicationEvent arg0) {
	// }
	//
	// @Override
	// public void handleQuit(ApplicationEvent arg0) {
	// System.exit(0);
	//
	// }
	//
	// @Override
	// public void handlePrintFile(ApplicationEvent arg0) {
	// Sounds.buzzer();
	//
	// }
	//
	// // show the pref menu
	// @Override
	// public void handlePreferences(ApplicationEvent arg0) {
	// // can open?
	// if (YaFrame.is()) {
	// // TODO YAFrame.openSetting();
	// } else {
	// Sounds.buzzer();
	// }
	//
	// }
	//
	// @Override
	// public void handleOpenFile(ApplicationEvent arg0) {
	// Sounds.buzzer();
	//
	// }
	//
	// @Override
	// public void handleOpenApplication(ApplicationEvent arg0) {
	// }
	//
	// // show about tab
	// @Override
	// public void handleAbout(ApplicationEvent arg0) {
	// // can open?
	// if (YaFrame.is()) {
	// YaFrame.openAbout();
	// } else {
	// Sounds.buzzer();
	// }
	//
	// }
	// });
	// }

	/**
	 * Open the dialog to load an file, swing based
	 * 
	 * @return the file or null
	 */
	@Override
	public File[] openDialog(final boolean multiple, final boolean folder, FileFilter filter) {

		// folder selector?
		if (folder) {
			return super.openDialog(multiple, folder, filter);
		}

		final FileDialog d = new FileDialog(new Frame(), "", FileDialog.LOAD);
		d.setVisible(true);
		if (d.getFile() != null) {
			File f = new File(d.getDirectory(), d.getFile());
			// is typ ok?
			if (folder && !f.isDirectory()) {
				f = f.getParentFile();
			}

			// file filter?
			if (filter != null && !filter.accept(f)) {
				TaskDialogs.inform(null, I18N.t("File {0} has wrong format", f.getName()), I18N.t(
						"Please select in the file dialog only a file, who match {0}. {1} doesn't do this.", filter.getDescription(),
						f.getAbsoluteFile()));
			}

			return new File[] { f };
		}
		return new File[] {};
	}

	/**
	 * Open the dialog to load an file, swing based
	 * 
	 * @return the file or null
	 */
	@Override
	public File saveDialog() {
		final FileDialog d = new FileDialog(new Frame(), "", FileDialog.SAVE);
		d.setVisible(true);
		if (d.getFile() != null) {
			return new File(d.getDirectory(), d.getFile());
		}
		return null;
	}

}
