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
public class FormEnemyActionsList extends FormRubyList {

	/**
	 * @param project
	 * @param ary
	 * @param title
	 * @param icon
	 * @param desc
	 */
	public FormEnemyActionsList(Project project, IRubyObject ary) {
		super(project, ary, "Action", "dummy", I18N.t("The enemy's actions."));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.gui.form.FormList#getNewElement()
	 */
	@Override
	protected IRubyObject getNewElement() {
		return (RubyObject) RGSSProjectHelper.getInterpreter(project).runScriptlet("return RPG::Enemy::Action.new");
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

		// f.addHeader("kind", new FormHeader("Actiontyp", icon))
		// f.addElement(id, element)
		// kind
		// Type of action (0: basic, 1: skill).
		//
		// basic
		// When set to a [Basic] action, defines it further (0: attack, 1:
		// defend, 2: escape, 3: do nothing).
		//
		// skill_id
		// When set to a [Skill], the ID of that skill.
		//
		// condition_turn_a
		// condition_turn_b
		// a and b values specified in the [Turn] condition. To be input in the
		// form a + bx.
		//
		// When the turn is not specified as a condition, a = 0 and b = 1.
		//
		// condition_hp
		// Percentage specified in the [HP] condition.
		//
		// When HP is not specified as a condition, this value is set to 100.
		//
		// condition_level
		// Standard level specified in the [Level] condition.
		//
		// When the level is not specified as a condition, this value is set to
		// 1.
		//
		// condition_switch_id
		// Switch ID specified in the [Switch] condition.
		//
		// When the switch ID is not specified as a condition, this value is set
		// to 0. Consequently, it is essential to check whether this value is 0.
		//
		// rating
		// The action's rating (1..10).

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
