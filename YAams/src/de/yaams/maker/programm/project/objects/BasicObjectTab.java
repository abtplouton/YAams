/**
 * 
 */
package de.yaams.maker.programm.project.objects;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.bcb.BcbBuilder;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.ProjectTabEvent;
import de.yaams.maker.programm.project.tab.ProjectTab;

/**
 * @author abby
 * 
 */
public abstract class BasicObjectTab extends ProjectTab {

	private static final long serialVersionUID = 2224192035641585426L;

	protected String uid;
	protected BasicObjectManager bom;
	protected BasicObjectList list;
	protected JPanel panel;

	/**
	 * @param eles
	 * @param project
	 */
	public BasicObjectTab(BasicObjectManager bom, Project project, String uid) {
		super(project);
		this.bom = bom;
		this.uid = uid;

		// build it
		buildGui();
		addSaveButton();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.tabs.YaTab#buildBcb(de.yaams.maker.helper.gui
	 * .bcb.BcbBuilder)
	 */
	@Override
	protected void buildBcb(BcbBuilder bcb) {
		ProjectTabEvent.buildBcb(bcb, project, bom.getTabUId());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.tabs.YaTab#getIcon()
	 */
	@Override
	public String getIcon() {
		return bom.getIcon();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.tabs.YaTab#getTitle()
	 */
	@Override
	public String getTitle() {
		return bom.getTitle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.tabs.YaTab#getId()
	 */
	@Override
	public String getId() {
		return uid;
	}

	/**
	 * Create the content for the selected object
	 * 
	 * @param selectedObject
	 */
	public abstract void buildInternContent(BasisListElement selectedObject);

	/**
	 * Build the content
	 */
	@Override
	public JComponent getContent() {
		panel = new JPanel(new GridLayout(1, 1));
		list = new BasicObjectList(bom, this);

		return YFactory.createHorizontPanel(list, panel, "basic.object.tab." + uid);
	}

	/**
	 * Save chanced, overwrite it to implement it, and call addSaveTab() to add
	 * the support button
	 */
	@Override
	protected void saveIntern() {
		bom.saveObjects();
	}

}
