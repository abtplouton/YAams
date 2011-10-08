/**
 * 
 */
package de.yaams.extensions.baseexport;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import de.yaams.extensions.baseexport.action.ExportActionList;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.bcb.BcbBuilder;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.tab.ProjectTab;
import de.yaams.maker.programm.tabs.TabEvent;

/**
 * @author Nebli
 * 
 */
public class ExportTab extends ProjectTab {
	private static final long serialVersionUID = -8849887394292456819L;

	public static final String ID = "project.export";

	/**
	 * @param project
	 */
	public ExportTab(final Project project) {
		super(project);

		buildGui();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getBottom()
	 */
	@Override
	public JComponent getBottom() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getIcon()
	 */
	@Override
	public String getIcon() {
		return "folder_arrow";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getTitle()
	 */
	@Override
	public String getTitle() {
		return I18N.t("Export");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getContent()
	 */
	@Override
	public JComponent getContent() {
		final JPanel container = new JPanel(new GridLayout(1, 1));
		return YFactory.createHorizontPanel(new ExportActionList(project, container), container, "export");
	}

	@Override
	protected void buildBcb(BcbBuilder bcb) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getId() {
		return TabEvent.buildParameter(ID, project, null);
	}

}
