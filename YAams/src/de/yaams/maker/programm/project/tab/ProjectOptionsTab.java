/**
 * 
 */
package de.yaams.maker.programm.project.tab;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.gui.bcb.BcbBuilder;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.helper.gui.tabs.ProjectSplitTab;
import de.yaams.maker.helper.gui.tabs.SplitActionListElement;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.tabs.BasicTabEvent;
import de.yaams.maker.programm.tabs.TabEvent;

/**
 * @author abt
 * 
 */
public class ProjectOptionsTab extends ProjectSplitTab {
	private static final long serialVersionUID = -8954633305994748914L;

	public static final String ID = "project.opts", EXADD = "project.options.add", EXCONFIG = "project.options.edit";

	/**
	 * Create a new ProjectOptionsTab
	 * 
	 * @param eles
	 * @param project
	 */
	public ProjectOptionsTab(Project project) {
		super(getEles(project), project);

		buildGui();
	}

	/**
	 * Helpermethod to collect all events
	 * 
	 * @return
	 */
	private static ArrayList<BasisListElement> getEles(Project project) {
		ArrayList<BasisListElement> eles = new ArrayList<BasisListElement>();

		// build objects
		HashMap<String, Object> objects = new HashMap<String, Object>();
		objects.put("project", project);
		objects.put("list", eles);

		// run extention
		ExtentionManagement.work(EXADD, objects);
		ExtentionManagement.work(EXCONFIG, objects);

		// found nothing?
		if (eles.size() == 0) {
			eles.add(new SplitActionListElement(I18N.t("No options found."), null, "opts_error") {

				@Override
				protected Component getComponent(Project p) {
					return new JLabel(I18N.t("No options found."));
				}
			});
		}

		return eles;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getIcon()
	 */
	@Override
	public String getIcon() {
		return "opts";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getTitle()
	 */
	@Override
	public String getTitle() {
		return I18N.t("Project Options");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.tabs.YaTab#getBcb(de.yaams.maker.helper.gui.bcb
	 * .BcbBuilder)
	 */
	@Override
	protected void buildBcb(BcbBuilder bcb) {
		BasicTabEvent.buildBcb(bcb, getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.tabs.YaTab#getId()
	 */
	@Override
	public String getId() {
		return TabEvent.buildParameter(ID, project, null);
	}

}
