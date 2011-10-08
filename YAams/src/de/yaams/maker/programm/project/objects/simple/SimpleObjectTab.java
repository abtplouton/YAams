/**
 * 
 */
package de.yaams.maker.programm.project.objects.simple;

import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.objects.BasicObjectTab;

/**
 * @author abby
 * 
 */
public class SimpleObjectTab extends BasicObjectTab {

	private static final long serialVersionUID = -7425917622862399932L;

	public final static String ID = "simple.object";

	/**
	 * @param bom
	 * @param project
	 */
	public SimpleObjectTab(Project project, String ObjID, int index) {
		super(project.getObjects().get(ObjID), project, ObjID);

		// add list selection
		if (index != -1) {
			list.getList().setSelectedIndex(index);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectTab#buildInternContent
	 * (de.yaams.maker.helper.gui.list.BasisListElement)
	 */
	@Override
	public void buildInternContent(final BasisListElement selectedObject) {

		// create panel
		FormBuilder f = new FormBuilder("simple.obj." + uid);
		((SimpleObjectManagement) bom).buildInternContent(selectedObject, f);

		// add modification
		f.addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				selectedObject.setModified(true);
				setModified(true);

			}
		});

		// set form
		panel.removeAll();
		panel.add(f.getPanel(true));
		panel.invalidate();
		panel.revalidate();
	}

}
