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
public class FormMemberList extends FormRubyList {

	/**
	 * @param project
	 * @param ary
	 * @param title
	 * @param icon
	 * @param desc
	 */
	public FormMemberList(Project project, IRubyObject ary) {
		super(project, ary, RGSS1Helper.getName(Type.ENEMY), RGSS1Helper.getIcon(Type.ENEMY), I18N
				.t("Feinde die in dieser Truppe enthalten sind."));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.gui.form.FormList#getNewElement()
	 */
	@Override
	protected IRubyObject getNewElement() {
		return (RubyObject) RGSSProjectHelper.getInterpreter(project).runScriptlet("return RPG::Troop::Member.new");
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
		f.addElement("basic.x", RubyForm.getNumber(I18N.t("X-Pos"), r, "@x"));
		f.addElement("basic.y", RubyForm.getNumber(I18N.t("Y-Pos"), r, "@y"));
		f.addElement("basic.hidden", RubyForm.getBoolean(I18N.t("Appear Midway"), r, "@hidden"));
		f.addElement("basic.immortal", RubyForm.getBoolean(I18N.t("Immortal"), r, "@immortal"));
		f.addElement("basic.enemy", new FormDBComboBox(title, project, Type.ENEMY, r, "@enemy_id", false));

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

		return RGSS1Helper.get(project, Type.ENEMY).get(RubyHelper.toInt(r.getInstanceVariable("@enemy_id"))).getName();
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

		return RGSS1Helper.getIcon(project, Type.ENEMY, RubyHelper.toInt(r.getInstanceVariable("@enemy_id")));
	}

}
