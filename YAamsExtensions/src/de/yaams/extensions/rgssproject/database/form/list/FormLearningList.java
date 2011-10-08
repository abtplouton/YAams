/**
 * 
 */
package de.yaams.extensions.rgssproject.database.form.list;

import org.jruby.RubyObject;
import org.jruby.runtime.builtin.IRubyObject;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.RGSSProjectHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.form.FormDBComboBox;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.programm.project.Project;

/**
 * @author abby
 * 
 */
public class FormLearningList extends FormRubyList {

	/**
	 * @param project
	 * @param ary
	 * @param title
	 * @param icon
	 * @param desc
	 */
	public FormLearningList(Project project, IRubyObject ary) {
		super(project, ary, RGSS1Helper.getName(Type.SKILL), RGSS1Helper.getIcon(Type.SKILL), I18N
				.t("Gebe hier an, mit welchem Level mal welche Skills lernt."));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.gui.form.FormList#getNewElement()
	 */
	@Override
	protected IRubyObject getNewElement() {
		return (RubyObject) RGSSProjectHelper.getInterpreter(project).runScriptlet("return RPG::Class::Learning.new");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.helper.gui.form.FormList#configForm(de.yaams.maker.helper
	 * .gui.form.core.FormBuilder, java.lang.Object)
	 */
	@Override
	protected void configForm(FormBuilder f, IRubyObject element) {
		RubyObject r = (RubyObject) element;

		// set it
		f.addElement("basic.level", RubyForm.getNumber(I18N.t("Ab Level"), r, "@level").setMinMax(0, 99, 1));
		f.addElement("basic.skill", new FormDBComboBox(title, project, Type.SKILL, r, "@skill_id", false));

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
		RubyObject r = (RubyObject) value;

		return I18N.t("Ab Lv {0} lernt {1}", r.getInstanceVariable("@level"),
				RGSS1Helper.get(project, Type.SKILL).get(RubyHelper.toInt(r.getInstanceVariable("@skill_id"))).getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.gui.form.FormList#getIcon(java.lang.Object)
	 */
	@Override
	protected Object getIcon(IRubyObject value) {
		RubyObject r = (RubyObject) value;

		// new Throwable().printStackTrace();

		return RGSS1Helper.getIcon(project, Type.SKILL, RubyHelper.toInt(r.getInstanceVariable("@skill_id")));
	}

}
