/**
 * 
 */
package de.yaams.core.helper.integration;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * @author abt
 * 
 */
public class EditorBIntegration {

	/**
	 * Startcode
	 */
	public void start() {}

	/**
	 * Open the dialog to load an file, swing based
	 * 
	 * @return the file or null
	 */
	public File[] openDialog(final boolean multiple, final boolean folder, FileFilter filter) {

		// Create a file chooser
		final JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(multiple);
		fc.setFileSelectionMode(folder ? JFileChooser.DIRECTORIES_ONLY : JFileChooser.FILES_ONLY);
		if (filter != null) {
			fc.setFileFilter(filter);
		}

		// In response to a button click:
		final int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFiles();
		} else {
			return new File[] {};
		}
	}

	/**
	 * Open the dialog to load an file, swing based
	 * 
	 * @return the file or null
	 */
	public File saveDialog() {
		// Create a file chooser
		final JFileChooser fc = new JFileChooser();

		// In response to a button click:
		final int returnVal = fc.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile();
		} else {
			return null;
		}
	}

}
