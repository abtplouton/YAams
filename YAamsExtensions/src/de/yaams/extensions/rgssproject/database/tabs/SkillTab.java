/**
 * 
 */
package de.yaams.extensions.rgssproject.database.tabs;

import java.awt.Image;
import java.util.HashMap;

import org.jruby.RubyObject;

import de.yaams.extensions.rgssproject.RGSSProjectHelper;
import de.yaams.extensions.rgssproject.RTP;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.form.FormDBComboBox;
import de.yaams.extensions.rgssproject.database.form.FormGraphEle;
import de.yaams.extensions.rgssproject.database.form.FormMusicEle;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.extensions.rgssproject.database.form.list.FormDBList;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;
import de.yaams.maker.programm.project.Project;

/**
 * @author abt
 * 
 */
public class SkillTab extends GTab {
	private static final long serialVersionUID = -7224278425132595022L;

	protected HashMap<String, Image> iconcache;

	/**
	 * Create a new ItemTab
	 * 
	 * @param project
	 */
	public SkillTab(final Project project) {
		super(project);
		iconcache = new HashMap<String, Image>();

		loadFile(Type.SKILL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.genericLoader.GTab#getInternContent(int)
	 */
	@Override
	public void buildForm(FormBuilder form, int id) {
		// prepare
		RubyObject act = elements.get(id).getObject();

		// build basics
		form.addHeader("basic", new FormHeader(I18N.t("Grundlegenes"), getIcon() + "_info").setColumn(4));
		form.addElement("basic.name", RubyForm.getString("Name", "@name", act));
		form.addElement("basic.desc", RubyForm.getString(I18N.t("Beschreibung"), "@description", act));
		form.addElement("basic.image", new FormGraphEle(I18N.t("Image"), project, RTP.ICON, act, "@icon_name", null));

		// sonst
		form.addHeader("usa", new FormHeader(I18N.t("Benutzung"), "info").setColumn(6));
		form.addElement("usa.sp", RubyForm.getNumber("SP Cost", act, "@sp_cost"));

		form.addElement("usa.target", RubyForm
				.getComboBoxNum("Target", new String[] { "0", "1", "2", "3", "4", "5", "6", "7" }, new String[] { "None", "one enemy",
						"all enemies", "one ally", "all allies", "1 ally--HP 0", "all allies--HP 0", "the user" }, act, "@scope"));
		form.addElement(
				"usa.usa",
				RubyForm.getComboBoxNum("Usability", new String[] { "0", "1", "2", "3" }, new String[] { "always", "only in battle",
						"only from the menu", "never" }, act, "@occasion"));

		// values
		form.addHeader("value", new FormHeader(I18N.t("Werte"), "systemmonitor").setColumn(8));
		form.addElement("value.pow", RubyForm.getNumber("Power", act, "@power"));
		form.addElement("value.atk", RubyForm.getNumber("Attack", act, "@atk_f"));
		form.addElement("value.eva", RubyForm.getNumber("Evasion", act, "@eva_f"));
		form.addElement("value.str", RubyForm.getNumber("Strength", act, "@str_f"));
		form.addElement("value.dex", RubyForm.getNumber("Dexterity", act, "@dex_f"));
		form.addElement("value.agi", RubyForm.getNumber("Agility", act, "@agi_f"));
		form.addElement("value.mag", RubyForm.getNumber("Magic", act, "@int_f"));
		form.addElement("value.acc", RubyForm.getNumber("Accurary", act, "@hit"));
		form.addElement("value.phy", RubyForm.getNumber("Phys Def", act, "@pdef_f"));
		form.addElement("value.mdef", RubyForm.getNumber("Mag Def", act, "@mdef_f"));
		form.addElement("value.var", RubyForm.getNumber("Variance", act, "@variance"));

		// animation
		form.addHeader("other", new FormHeader(I18N.t("Sonstiges"), "animation").setColumn(4));
		form.addElement("other.user", new FormDBComboBox("User Animation", project, Type.ANIMATION, act, "@animation1_id", true));
		form.addElement("other.target", new FormDBComboBox("Target Animation", project, Type.ANIMATION, act, "@animation2_id", true));
		form.addElement("other.se", new FormMusicEle("Menu Use SE", project, RTP.SE, act.getInstanceVariable("@menu_se")));
		form.addElement("other.ce",
				new FormDBComboBox("Common Event", project, RGSS1Helper.Type.COMMONEVENT, act, "@common_event_id", true));

		// build panel
		form.addHeader("result", new FormHeader(I18N.t("Setzen von Auswirkungen"), "skill").setColumn(6).setSorting(4));

		form.addElement("result.element",
				new FormDBList(project, Type.ELEMENT, act.getInstanceVariable("@element_set"), I18N.t("Setzen von Elementen")));
		form.addElement("result.statusAdd",
				new FormDBList(project, Type.STATUS, act.getInstanceVariable("@plus_state_set"), I18N.t("Hinzufügen des Status.")));
		form.addElement("result.statusRemove",
				new FormDBList(project, Type.STATUS, act.getInstanceVariable("@minus_state_set"), I18N.t("Entfernen des Status.")));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.genericLoader.GTab#getText(java.lang.Integer)
	 */
	@Override
	public Object getText(final Integer value) {
		return elements.get(value).getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getIcon()
	 */
	@Override
	public String getIcon() {
		return "skill";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getTitle()
	 */
	@Override
	public String getTitle() {
		return I18N.t("Skills");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.genericLoader.GTab#createObject()
	 */
	@Override
	public RubyObject createObject() {
		return (RubyObject) RGSSProjectHelper.getInterpreter(project).runScriptlet("return RPG::Skill.new");
	}

}
