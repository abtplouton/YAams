/**
 * 
 */
package de.yaams.core.helper.integration;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.apache.commons.lang.SystemUtils;

/**
 * Management the intragration for the system, if yaams need an "hack" for
 * system, it will added here
 * 
 * @author abt
 * 
 */
public class EditorIntegration {

	private static EditorBIntegration integration = getInt();

	/**
	 * Set yaams special for the platform
	 */
	public static EditorBIntegration getInt() {

		EditorBIntegration integration;

		// is a mac platform?
		if (SystemUtils.IS_OS_MAC_OSX) {
			integration = new EditorMacIntegration();
		} else if (SystemUtils.IS_OS_WINDOWS) {
			integration = new EditorWindowIntegration();
		} else {
			integration = new EditorBIntegration();
		}
		integration.start();

		return integration;
	}

	/**
	 * Open the dialog to load an file, swing based
	 * 
	 * @return the file or null
	 */
	public static File[] openDialog(final boolean multiple, final boolean folder) {
		return integration.openDialog(multiple, folder, null);
	}

	/**
	 * Open the dialog to load an file, swing based with file filter
	 * 
	 * @return the file or null
	 */
	public static File[] openDialog(final boolean multiple, final boolean folder, FileFilter filter) {
		return integration.openDialog(multiple, folder, filter);
	}

	/**
	 * Open the dialog to save an file, swing based
	 * 
	 * @return the file or null
	 */
	public static File saveDialog() {
		return integration.saveDialog();
	}
}
