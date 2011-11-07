/**
 * 
 */
package de.yaams.extensions.rgssproject.database.tabs;

import java.awt.Image;
import java.util.HashMap;

import org.jruby.RubyObject;
import org.jruby.runtime.builtin.IRubyObject;

import de.yaams.extensions.rgssproject.RGSSProjectHelper;
import de.yaams.extensions.rgssproject.RTP;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.RGSS1Voc;
import de.yaams.extensions.rgssproject.database.form.FormDBComboBox;
import de.yaams.extensions.rgssproject.database.form.FormGraphEle;
import de.yaams.extensions.rgssproject.database.form.FormTable;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.FormInfo;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;
import de.yaams.maker.programm.project.Project;

/**
 * @author abt
 * 
 */
public class ActorTab extends GTab {
	private static final long serialVersionUID = -7224278425132595022L;

	protected HashMap<String, Image> iconcache;

	/**
	 * Create a new ItemTab
	 * 
	 * @param project
	 */
	public ActorTab(final Project project) {
		super(project);
		iconcache = new HashMap<String, Image>();

		loadFile(Type.ACTOR);
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
		form.addElement("basic.class", new FormDBComboBox("Klasse", project, Type.CLASS, act, "@class_id", false));
		form.addElement("basic.image", new FormGraphEle(I18N.t("Bild"), project, RTP.CHARACTER, act, "@character_name", "@character_hue"));
		form.addElement("basic.battler", new FormGraphEle(I18N.t("Battler"), project, RTP.BATTLERS, act, "@battler_name", "@battler_hue"));

		// add level
		form.addHeader("skill", new FormHeader(RGSS1Helper.getName(Type.SKILL), RGSS1Helper.getIcon(Type.SKILL)).setColumn(8));
		form.addElement("skill.lvli", RubyForm.getNumber(I18N.t("Level"), act, "@initial_level").setMinMax(1, 99, 1));
		form.addElement("skill.lvlif", RubyForm.getNumber("-", act, "@final_level").setMinMax(1, 99, 1));
		form.addElement("skill.expb", RubyForm.getNumber("EXP-Basis", act, "@exp_basis"));
		form.addElement("skill.expi", RubyForm.getNumber("EXP-Inflation", act, "@exp_inflation"));

		// add items
		form.addHeader("item", new FormHeader(RGSS1Voc.item(project), "armor_weapon").setColumn(8));
		form.addElement("item.weapon", new FormDBComboBox(RGSS1Voc.weapon(project), project, Type.WEAPON, act, "@weapon_id", true));
		form.addElement("item.weaponfix", RubyForm.getBoolean(I18N.t("Fix"), act, "@weapon_fix"));
		form.addElement("item.armor1", new FormDBComboBox(RGSS1Voc.shield(project), project, Type.ARMOR, act, "@armor1_id", true));
		form.addElement("item.armor1fix", RubyForm.getBoolean(I18N.t("Fix"), act, "@armor1_fix"));
		form.addElement("item.armor2", new FormDBComboBox(RGSS1Voc.helm(project), project, Type.ARMOR, act, "@armor2_id", true));
		form.addElement("item.armor2fix", RubyForm.getBoolean(I18N.t("Fix"), act, "@armor2_fix"));
		form.addElement("item.armor3", new FormDBComboBox(RGSS1Voc.armor(project), project, Type.ARMOR, act, "@armor3_id", true));
		form.addElement("item.armor3fix", RubyForm.getBoolean(I18N.t("Fix"), act, "@armor3_fix"));
		form.addElement("item.armor4", new FormDBComboBox(RGSS1Voc.acc(project), project, Type.ARMOR, act, "@armor4_id", true));
		form.addElement("item.armor4fix", RubyForm.getBoolean(I18N.t("Fix"), act, "@armor4_fix"));

		// build parameters
		form.addHeader("parameters", new FormHeader(I18N.t("Parameters"), "error").setColumn(14).setCollapsed(true).setSorting(2));
		String hp = RGSS1Voc.hp(project), sp = RGSS1Voc.sp(project), str = RGSS1Voc.str(project), dex = RGSS1Voc.dex(project), agi = RGSS1Voc
				.agi(project), inte = RGSS1Voc.inte(project);
		IRubyObject table = act.getInstanceVariable("@parameters");

		for (int i = 1; i <= 99; i++) {
			form.addElement("parameters." + i, new FormInfo("", I18N.t("Level {0}", i)).setSorting(i));
			form.addElement("parameters." + i + "hp", new FormTable(hp, table, 0, i).setSorting(i));
			form.addElement("parameters." + i + "sp", new FormTable(sp, table, 1, i).setSorting(i));
			form.addElement("parameters." + i + "str", new FormTable(str, table, 2, i).setSorting(i));
			form.addElement("parameters." + i + "dex", new FormTable(dex, table, 3, i).setSorting(i));
			form.addElement("parameters." + i + "agi", new FormTable(agi, table, 4, i).setSorting(i));
			form.addElement("parameters." + i + "inte", new FormTable(inte, table, 5, i).setSorting(i));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.packandgo.helper.gui.list.YSimpleList#getDesc(java.lang.Object)
	 */
	@Override
	public String getDesc(final Integer o) {
		int id = Integer.valueOf(elements.get(o).getObject().getInstanceVariable("@class_id").toString());

		return RGSS1Helper.get(project, Type.CLASS).get(id).getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.genericLoader.GTab#createObject()
	 */
	@Override
	public RubyObject createObject() {
		return (RubyObject) RGSSProjectHelper.getInterpreter(project).runScriptlet("return RPG::Actor.new");
	}

}
