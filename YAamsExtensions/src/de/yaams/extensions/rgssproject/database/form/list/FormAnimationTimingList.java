/**
 * 
 */
package de.yaams.extensions.rgssproject.database.form.list;

import org.jruby.RubyObject;
import org.jruby.runtime.builtin.IRubyObject;

import de.yaams.extensions.rgssproject.RGSSProjectHelper;
import de.yaams.extensions.rgssproject.RTP;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.form.FormColor;
import de.yaams.extensions.rgssproject.database.form.FormMusicEle;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.JavaHelper;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.programm.project.Project;

/**
 * @author abby
 * 
 */
public class FormAnimationTimingList extends FormRubyList {

	/**
	 * @param project
	 * @param ary
	 * @param title
	 * @param icon
	 * @param desc
	 */
	public FormAnimationTimingList(Project project, IRubyObject ary) {
		super(project, ary, "Timing", "wait", I18N.t("for the timing of an animation's SE and flash effects."));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.gui.form.FormList#getNewElement()
	 */
	@Override
	protected IRubyObject getNewElement() {
		return (RubyObject) RGSSProjectHelper.getInterpreter(project).runScriptlet("return RPG::Animation::Timing.new");
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
		f.addElement("basic.frame", RubyForm.getNumber(I18N.t("Frame"), r, "@frame"));
		f.addElement("basic.se", new FormMusicEle(I18N.t("Soundeffekt"), project, RTP.SE, r.getInstanceVariable("@se")));
		f.addElement(
				"basic.scope",
				RubyForm.getComboBoxNum(I18N.t("Flash Scope"), JavaHelper.numAry(3), new String[] { "None", "target", "screen",
						"delete target" }, r, "@flash_scope"));
		f.addElement("basic.color", new FormColor(I18N.t("Flash Color"), r.getInstanceVariable("@flash_color")));
		f.addElement("basic.duration", RubyForm.getNumber(I18N.t("Flash duration"), r, "@frame"));
		f.addElement("basic.effect", RubyForm.getComboBoxNum(I18N.t("Condition of the effect"), JavaHelper.numAry(3), new String[] { "None",
				"Hit", "Miss" }, r, "@flash_scope"));

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
		RubyObject se = (RubyObject) r.getInstanceVariable("@se");

		String play = se.getInstanceVariable("@name").toString().length() == 0 ? "" : I18N.t("Play {0}", se.getInstanceVariable("@name"));

		return I18N.t("{0}. {1}", r.getInstanceVariable("@frame").toString(), play);// RGSS1Helper.get(project,
																					// Type.ENEMY).get(RubyHelper.toInt(r.getInstanceVariable("@enemy_id"))).getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.gui.form.FormList#getIcon(java.lang.Object)
	 */
	@Override
	protected Object getIcon(IRubyObject value) {

		// new Throwable().printStackTrace();

		return RGSS1Helper.getIcon(Type.ANIMATION);
	}

}
