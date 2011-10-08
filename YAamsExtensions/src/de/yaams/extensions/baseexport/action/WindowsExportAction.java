/**
 * 
 */
package de.yaams.extensions.baseexport.action;

import java.io.File;

import javax.swing.JComponent;

import de.yaams.maker.helper.FileHelper;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.programm.project.Project;

/**
 * @author Nebli
 * 
 */
public class WindowsExportAction extends ExportAction {

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 * @param project
	 */
	public WindowsExportAction(Project project) {
		super(I18N.t("Windows"), "windows", project);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.project.export.ExportAction#export()
	 */
	@Override
	public void export() {

		// extract archive
		FileHelper.extractFromJar(WindowsExportAction.class.getResourceAsStream("start.exe"), new File(tmpPath, "start.exe"));

		// add game
		FileHelper.copyTree(project.getPath(), tmpPath);

		// add yrgss
		addJar(tmpPath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.project.export.ExportAction#getPanel()
	 */
	@Override
	public JComponent getPanel() {
		FormBuilder f = getMainPanel();
		return f.getPanel(true);
	}

}
