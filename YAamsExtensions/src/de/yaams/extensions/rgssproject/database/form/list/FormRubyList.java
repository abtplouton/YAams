/**
 * 
 */
package de.yaams.extensions.rgssproject.database.form.list;

import java.util.ArrayList;

import org.jruby.RubyArray;
import org.jruby.RubyObject;
import org.jruby.runtime.builtin.IRubyObject;

import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.YDialog;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormList;
import de.yaams.maker.programm.project.Project;

/**
 * @author abby
 * 
 */
public abstract class FormRubyList extends FormList<IRubyObject> {

	protected RubyArray ary;
	protected Project project;
	protected String icon;

	/**
	 * Create a new FormDBList
	 * 
	 * @param project
	 * @param ID
	 * @param obj
	 */
	public FormRubyList(Project project, String ID, RubyObject obj, String title, String icon, String desc) {
		this(project, obj.getInstanceVariable(ID), title, icon, desc);
	}

	/**
	 * Create a new FormDBList
	 * 
	 * @param project
	 * @param ary
	 */
	public FormRubyList(Project project, IRubyObject ary, final String title, final String icon, final String desc) {
		this.project = project;
		this.icon = icon;
		this.ary = (RubyArray) ary;

		// build array
		// load vars
		IRubyObject[] jAry = this.ary.toJavaArray();
		ArrayList<IRubyObject> nAry = new ArrayList<IRubyObject>();

		// add vars
		for (IRubyObject i : jAry) {
			// build Object
			nAry.add(i);
		}

		// build it
		buildGui(title, icon, nAry, true, false, true);

		// add help
		list.getToolbar().addRight(YFactory.tb(desc, "help", new AE() {

			@Override
			public void run() {
				YDialog.ok(title, desc, "help_" + icon);

			}
		}));

		// add listener
		// save it
		addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				saveToAry();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.gui.form.FormList#getIcon(java.lang.Object)
	 */
	@Override
	protected Object getIcon(IRubyObject element) {
		return icon;
	}

	/**
	 * Save it
	 */
	protected void saveToAry() {
		// empty rubyarry
		ary.clear();

		// add values
		for (IRubyObject d : list.getAry()) {
			ary.add(d);
		}

	}

}
