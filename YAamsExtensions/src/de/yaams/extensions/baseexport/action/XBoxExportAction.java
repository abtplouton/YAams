/**
 * 
 */
package de.yaams.extensions.baseexport.action;

import javax.swing.JComponent;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.SystemHelper;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.programm.project.Project;

/**
 * @author Nebli
 * 
 */
public class XBoxExportAction extends ExportAction {

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 * @param project
	 */
	public XBoxExportAction(Project project) {
		super(I18N.t("XBox"), "xbox", project);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.project.export.ExportAction#export()
	 */
	@Override
	public void export() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.project.export.ExportAction#getPanel()
	 */
	@Override
	public JComponent getPanel() {
		FormBuilder f = new FormBuilder("export.xbox");
		f.addButton("xbox", YFactory.b(I18N.t("Please use GeeX"), "info", new AE() {

			@Override
			public void run() {
				SystemHelper.openUrl("http://www.geexpowered.com/");
			}
		}));

		return f.getPanel(true);
	}
}
