/**
 * 
 */
package de.yaams.extensions.baseexport.action;

import java.io.File;
import java.io.InputStream;

import javax.swing.JComponent;

import org.apache.commons.lang.Validate;

import de.yaams.maker.helper.FileHelper;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.YSettingHelper;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;
import de.yaams.maker.programm.YAamsCore;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.ProjectSett;

/**
 * @author Nebli
 * 
 */
public class MacExportAction extends ExportAction {

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 * @param project
	 */
	public MacExportAction(Project project) {
		super(I18N.t("Mac OS X"), "mac", project);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.project.export.ExportAction#export()
	 */
	@Override
	public void export() {

		// extract archive
		File arc = new File(YAamsCore.tmpFolder, "mac.zip");
		InputStream i = getClass().getResourceAsStream("mac.zip");

		// check it
		Validate.notNull(i, "mac.zip is empty");

		// do it
		FileHelper.extractFromJar(i, arc);
		FileHelper.extractArchive(arc, tmpPath);
		FileHelper.deleteFile(arc);

		// add game
		arc = new File(tmpPath, "yrgss.app" + File.separator + "Contents" + File.separator + "Resources" + File.separator + "Java");
		FileHelper.copyTree(project.getPath(), arc);

		// add yrgss
		addJar(arc);

		// rename it
		Validate.isTrue(
				!new File(tmpPath, "yrgss.app").renameTo(new File(tmpPath, ProjectSett.get(project, "export." + icon + ".appname",
						project.getTitle())
						+ ".app")), "move of yrgss.app failed");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.project.export.ExportAction#getPanel()
	 */
	@Override
	public JComponent getPanel() {
		FormBuilder f = getMainPanel();

		f.addHeader("mac", new FormHeader(I18N.t("Mac spezifisch"), "mac"));
		// add path
		f.addElement("mac.name", YSettingHelper.text(project, I18N.t("App-Name"), "export." + icon + ".appname", project.getTitle()));

		return f.getPanel(true);
	}

}
