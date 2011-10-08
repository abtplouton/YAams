/**
 * 
 */
package de.yaams.maker.helper.integration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.lang.SystemUtils;

import com.jidesoft.swing.FolderChooser;

import de.yaams.maker.helper.Setting;

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
		// what kind of folder?
		if (folder) {
			FolderChooser fc = new FolderChooser();
			fc.setNavigationFieldVisible(true);
			// fc.setRecentListVisible(true);
			fc.setRecentList(Arrays.asList(Setting
					.get("integration.folderlist", new String[] { SystemUtils.USER_HOME })));
			fc.setAvailableButtons(FolderChooser.BUTTON_ALL);
			fc.setMultiSelectionEnabled(multiple);
			fc.setDialogTitle("");
			
			// show it
			int result = fc.showOpenDialog(null);
			if (result == FolderChooser.APPROVE_OPTION) {
				// collect & save
				List<String> s = fc.getRecentList();
				s.add(0, fc.getSelectedFile().getAbsolutePath());
				if (s.size() >= 10) {
					s.remove(10);
				}
				Setting.set("integration.folderlist", s);
				
				// get
				return fc.getSelectedFiles();
			} else {
				return new File[] {};
			}
		} else {
			
			// Create a file chooser
			final JFileChooser fc = new JFileChooser();
			fc.setMultiSelectionEnabled(multiple);
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
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
