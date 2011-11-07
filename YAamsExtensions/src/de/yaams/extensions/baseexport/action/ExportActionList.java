/**
 * 
 */
package de.yaams.extensions.baseexport.action;

import java.util.ArrayList;

import javax.swing.JPanel;

import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.helper.gui.list.YBasisListElementArrayList;
import de.yaams.maker.programm.project.Project;

/**
 * @author Nebli
 * 
 */
public class ExportActionList extends YBasisListElementArrayList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1339492434184830922L;
	protected JPanel container;

	/**
	 * @param ary
	 */
	public ExportActionList(final Project p, final JPanel container) {
		super(buildList(p));
		this.container = container;
	}

	/**
	 * Helpermethod to build the list
	 * 
	 * @return
	 */
	private static ArrayList<BasisListElement> buildList(final Project p) {
		final ArrayList<BasisListElement> ary = new ArrayList<BasisListElement>();
		ary.add(new WindowsExportAction(p));
		ary.add(new MacExportAction(p));
		ary.add(new XBoxExportAction(p));
		ary.add(new JavaExportAction(p));
		return ary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#add()
	 */
	@Override
	public void add() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#info()
	 */
	@Override
	protected void info() {
	}

	/**
	 * Edit the object, add the name value
	 */
	@Override
	protected void configIntern() {
		container.removeAll();
		container.add(((ExportAction) getSelectedObject()).getPanel());
		container.invalidate();
		container.revalidate();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#oneClick()
	 */
	@Override
	protected void selected() {
		config();
	}

}
