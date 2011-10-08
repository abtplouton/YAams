/**
 * 
 */
package de.yaams.extensions.baseexport.action;

import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;

import org.apache.commons.lang.SystemUtils;

import de.yaams.maker.helper.FileHelper;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.NetHelper;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.Run;
import de.yaams.maker.helper.gui.YDialog;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.YMessagesDialog;
import de.yaams.maker.helper.gui.YSettingHelper;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormFileSelectField;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.programm.YAamsCore;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.ProjectSett;

/**
 * @author Nebli
 * 
 */
public abstract class ExportAction extends BasisListElement {

	protected Project project;
	protected Thread exportThread;
	protected File path;
	protected File tmpPath;

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public ExportAction(String title, String icon, Project project) {
		super(title, null, icon);
		this.project = project;

		path = new File(ProjectSett.get(project, "export." + icon + ".path",
				new File(SystemUtils.USER_HOME, icon + ".zip").getAbsolutePath()));
	}

	/**
	 * Export the game
	 */
	public abstract void export();

	/**
	 * Export the game
	 */
	public void exportCheck(YMessagesDialog errors) {

		// save last changed
		project.save();

		// create tmp path
		tmpPath = new File(YAamsCore.tmpFolder, icon);
		FileHelper.deleteTree(tmpPath);
		FileHelper.checkPath(tmpPath, errors, true, true);

		// exportpath ok?
		FileHelper.checkPath(path, errors, false, true);
	}

	/**
	 * Get the panel to display
	 * 
	 * @return
	 */
	public abstract JComponent getPanel();

	/**
	 * Build main export panel
	 * 
	 * @return
	 */
	protected FormBuilder getMainPanel() {
		FormBuilder f = new FormBuilder("export." + getIcon());
		f.addHeader("basic", new FormHeader(I18N.t("Generelle Exportoptionen"), "export"));

		// add path
		f.addElement("basic.path", new FormFileSelectField(I18N.t("Pfad"), path, JFileChooser.SAVE_DIALOG, false, true)
				.addChangeListener(new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						path = new File(form.getContentAsString());
						ProjectSett.set(project, "export." + icon + ".path", form.getContentAsString());

					}
				}));

		// add net version
		f.addElement(
				"basic.net",
				YSettingHelper.bool(project,
						I18N.t("Use optimated version. (Game is smaller, but need for the first run internet access.)"), "export." + icon
								+ ".netversion", true));

		// add button
		f.addButton("export", YFactory.b(I18N.t("Start Export"), icon + "_arrow", new AE() {

			@Override
			public void run() {
				exportThread = new Thread(new Run() {

					@Override
					public void go() {
						exportThread();

					}
				});
				exportThread.start();

			}
		}));

		return f;
	}

	/**
	 * Helpermethod to add the right jar
	 */
	protected void addJar(File basepath) {
		// which version?
		boolean net = ProjectSett.get(project, "export." + icon + ".netversion", true);
		String name = net ? "YAamsRGSSNetPlayer.jar" : "YAamsRGSSPlayer.jar";
		File file = new File(YAamsCore.programPath, name);

		// exist?
		if (!file.exists()) {
			// download it!
			NetHelper.downloadFile(file,
					NetHelper.getContentAsString("http://www.yaams.de/files/index.php?typ=yrgss&action=" + (net ? "net" : "normal")));
		}

		// add it
		FileHelper.copy(file, new File(basepath, "start.jar"));
	}

	/**
	 * Helpermethod to export it
	 * 
	 * @param thread
	 */
	protected void exportThread() {
		// check it
		YMessagesDialog errors = new YMessagesDialog(I18N.t("Kann wahrscheinlich {0} nicht richtig exportieren", project.getTitle()),
				"export." + getIcon());
		exportCheck(errors);

		// check it
		if (!errors.setFooter(I18N.t("Export trotzdem wagen?")).showQuestion()) {
			return;
		}

		// do it
		export();

		// pack it
		FileHelper.packZip(tmpPath, path);

		// delete it
		FileHelper.deleteTree(tmpPath);

		// show it
		YDialog.ok(I18N.t("Export successful"), I18N.t("{0} was successful exported to  {1} ", project.getTitle(), path), "arrow_ok");
	}
}
