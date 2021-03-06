/**
 * 
 */
package de.yaams.maker.helper.gui.tabs;

import java.util.ArrayList;

import javax.swing.JPanel;

import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.helper.gui.list.YActionList;
import de.yaams.maker.programm.project.Project;

/**
 * @author abt
 * 
 */
public class SplitActionList extends YActionList {
	private static final long serialVersionUID = -3909366254868019979L;

	protected JPanel content;
	protected Project p;
	protected ProjectSplitTab pst;

	/**
	 * Create a new SplitActionList
	 * 
	 * @param ary
	 */
	public SplitActionList(final ProjectSplitTab pst, final JPanel content, final Project p, final ArrayList<BasisListElement> ary) {
		super(ary);
		this.content = content;
		this.p = p;
		this.pst = pst;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#add()
	 */
	@Override
	public void add() {
		pst.add();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#configIntern()
	 */
	@Override
	protected void configIntern() {
		((SplitActionListElement) getSelectedObject()).show(content, p);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.packandgo.helper.gui.list.YSimpleList#getModified(java.lang.
	 * Object)
	 */
	@Override
	public boolean isModified(final BasisListElement o) {
		// is modifizied?
		if (!pst.isModified() && o.isModified()) {
			pst.setModified(true);
		}

		// return it
		return o.isModified();
	}

}
