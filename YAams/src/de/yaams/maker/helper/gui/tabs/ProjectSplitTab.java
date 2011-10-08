/**
 * 
 */
package de.yaams.maker.helper.gui.tabs;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.commons.lang.Validate;

import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.tab.ProjectTab;

/**
 * @author abt
 * 
 */
public abstract class ProjectSplitTab extends ProjectTab {

	private static final long serialVersionUID = 1511368201990510263L;

	protected SplitActionList list;
	protected JPanel psContent;

	/**
	 * Create a new ProjectSplitTab
	 * 
	 * @param project
	 */
	public ProjectSplitTab(final ArrayList<BasisListElement> eles, final Project project) {
		super(project);

		psContent = new JPanel(new GridLayout(1, 1));

		list = new SplitActionList(this, psContent, project, eles);
	}

	/**
	 * Search for the right action and selected it, based on the iconname
	 * 
	 * @param icon
	 */
	public void selectActionByIcon(String icon) {
		// check param
		Validate.notEmpty(icon, "Icon is null");

		// run over aray
		int id = 0;
		for (BasisListElement b : list.getAry()) {
			if (icon.equals(b.getIcon())) {
				list.getList().setSelectedIndex(id);
				break;
			}
			id++;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#add()
	 */
	protected void add() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getContent()
	 */
	@Override
	public JComponent getContent() {
		return YFactory.createHorizontPanel(list, psContent, "ps." + getIcon());
	}

}
