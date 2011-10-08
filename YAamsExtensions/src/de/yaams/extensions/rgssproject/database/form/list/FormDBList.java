/**
 * 
 */
package de.yaams.extensions.rgssproject.database.form.list;

import org.jruby.RubyFixnum;
import org.jruby.runtime.builtin.IRubyObject;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.RGSSProjectHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.form.FormDBComboBox;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.programm.project.Project;

/**
 * @author abby
 * 
 */
public class FormDBList extends FormRubyList {

	protected Type type;

	/**
	 * Create a new FormDBList
	 * 
	 * @param project
	 * @param type
	 * @param ary
	 */
	public FormDBList(Project project, final Type type, IRubyObject ary, final String desc) {
		super(project, ary, RGSS1Helper.getName(type), RGSS1Helper.getIcon(type), desc);

		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.gui.form.FormList#getNewElement()
	 */
	@Override
	protected IRubyObject getNewElement() {
		RubyFixnum r = RubyFixnum.one(RGSSProjectHelper.getInterpreter(project).getRuntime());
		return r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.helper.gui.form.FormList#configForm(de.yaams.maker.helper
	 * .gui.form.core.FormBuilder)
	 */
	@Override
	protected void configForm(FormBuilder f, IRubyObject element) {
		f.addElement("basic.type", new FormDBComboBox(title, project, type, list.getAry(), list.getList().getSelectedIndex(), false)
				.addChangeListener(new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						saveToAry();

					}
				}));
	}

	/**
	 * Helpermethod to get the text for the list element/title, normally
	 * toString.
	 * 
	 * @param value
	 * @return
	 */
	@Override
	public Object getText(final Object value) {
		IRubyObject r = (IRubyObject) value;

		return RGSS1Helper.get(project, type).get(RubyHelper.toInt(r)).getName();
	}

	/**
	 * Get the desc
	 * 
	 * @param f
	 */
	@Override
	protected String getDesc(IRubyObject r) {

		return RGSS1Helper.getDesc(project, type, RubyHelper.toInt(r));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.gui.form.FormList#getIcon(java.lang.Object)
	 */
	@Override
	protected Object getIcon(IRubyObject r) {

		return RGSS1Helper.getIcon(project, type, RubyHelper.toInt(r));
	}

}
