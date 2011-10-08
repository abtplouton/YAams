/**
 * 
 */
package de.yaams.extensions.rgssproject.database.tabs;

import org.jruby.RubyObject;

import de.yaams.extensions.rgssproject.RGSSProjectHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.RGSS1Voc;
import de.yaams.extensions.rgssproject.database.form.FormDBComboBox;
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
public class StatusTab extends GTab {
	private static final long serialVersionUID = -7224278425132595022L;

	/**
	 * Create a new ItemTab
	 * 
	 * @param project
	 */
	public StatusTab(final Project project) {
		super(project);

		loadFile(Type.STATUS);
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
		form.addHeader("basic", new FormHeader(I18N.t("Grundlegenes"), getIcon() + "_info"));
		form.addElement("basic.name", RubyForm.getString(I18N.t("Name"), "@name", act));

		// animation
		form.addHeader("other", new FormHeader(I18N.t("Sonstiges"), "animation").setColumn(6));
		form.addElement("other.ani", new FormDBComboBox("Animation", project, Type.ANIMATION, act, "@animation_id", true));
		form.addElement("other.rat", RubyForm.getNumber(I18N.t("Rating"), act, "@rating").setMinMax(0, 10, 1));
		form.addElement("other.acc", RubyForm.getNumber(I18N.t("Accuracy"), act, "@hit_rate"));
		form.addElement("other.hp", RubyForm.getNumber(I18N.t("Max HP"), act, "@maxhp_rate"));
		form.addElement("other.sp", RubyForm.getNumber(I18N.t("Max SP"), act, "@maxsp_rate"));

		// values
		form.addHeader("feat", new FormHeader(I18N.t("Attribute"), "opts").setColumn(6));
		form.addElement(
				"feat.rest",
				RubyForm.getComboBoxNum("Restriction", new String[] { "0", "1", "2", "3", "4" }, new String[] { "None", "Can't use skills",
						"Always attack enemies", "Always attack allies", "All actions are disableld" }, act, "@restriction"));
		form.addElement("feat.unrest", RubyForm.getBoolean(I18N.t("Unresistable"), act, "@nonresistance"));
		form.addElement("feat.zerohp", RubyForm.getBoolean(I18N.t("Link with dead (HP 0)"), act, "@zero_hp"));
		form.addElement("feat.exp", RubyForm.getBoolean(I18N.t("Can't get exp"), act, "@cant_get_exp"));
		form.addElement("feat.eva", RubyForm.getBoolean(I18N.t("Evasion forbidden"), act, "@cant_evade"));
		form.addElement("feat.dam", RubyForm.getBoolean(I18N.t("Progressive Damage"), act, "@slip_damage"));

		// values
		form.addHeader("value", new FormHeader(I18N.t("Werte"), "systemmonitor").setColumn(8));
		form.addElement("value.str", RubyForm.getNumber(RGSS1Voc.str(project), act, "@str_rate"));
		form.addElement("value.dex", RubyForm.getNumber(RGSS1Voc.dex(project), act, "@dex_rate"));
		form.addElement("value.agi", RubyForm.getNumber(RGSS1Voc.agi(project), act, "@agi_rate"));
		form.addElement("value.int", RubyForm.getNumber(RGSS1Voc.inte(project), act, "@int_rate"));
		form.addElement("value.atk", RubyForm.getNumber("Attack Power", act, "@atk_rate"));
		form.addElement("value.pdef", RubyForm.getNumber("Phys Def", act, "@pdef_rate"));
		form.addElement("value.mdef", RubyForm.getNumber("Mag Def", act, "@mdef_rate"));
		form.addElement("value.eva", RubyForm.getNumber("Evasion", act, "@eva"));

		// build panel
		form.addHeader("rec", new FormHeader(I18N.t("Heilung"), "add").setColumn(4));
		form.addElement("rec.end", RubyForm.getBoolean(I18N.t("End After Battle"), act, "@battle_only"));
		form.addElement("rec.start",
				RubyForm.getNumber("Min. turn wait", act, "@hold_turn").setInfoTxt("How long must wait, to get a chance to recover"));
		form.addElement("rec.startrel", RubyForm.getNumber(I18N.t("% Chance to recover each turn"), act, "@auto_release_prob"));
		form.addElement(
				"rec.shockr",
				RubyForm.getNumber(I18N.t("% Chance to recover"), act, "@shock_release_prob").setInfoTxt(
						I18N.t("When damage is dealt, how big is the chance to recover?")));

		// build panel
		form.addHeader("result", new FormHeader(I18N.t("Setzen von Auswirkungen"), "status").setColumn(6).setSorting(4));

		form.addElement("result.element",
				new FormDBList(project, Type.ELEMENT, act.getInstanceVariable("@guard_element_set"), I18N.t("Attribute Resistance")));
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
		return "status";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getTitle()
	 */
	@Override
	public String getTitle() {
		return I18N.t("Status");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.genericLoader.GTab#createObject()
	 */
	@Override
	public RubyObject createObject() {
		return (RubyObject) RGSSProjectHelper.getInterpreter(project).runScriptlet("return RPG::State.new");
	}

}
