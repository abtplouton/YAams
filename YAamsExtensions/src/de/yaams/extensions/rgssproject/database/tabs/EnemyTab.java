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
import de.yaams.extensions.rgssproject.database.form.EnemyActionPanel;
import de.yaams.extensions.rgssproject.database.form.FormDBComboBox;
import de.yaams.extensions.rgssproject.database.form.FormGraphEle;
import de.yaams.extensions.rgssproject.database.form.FormTable;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.FormEmpty;
import de.yaams.maker.helper.gui.form.FormInfo;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;
import de.yaams.maker.programm.project.Project;

/**
 * @author abt
 * 
 */
public class EnemyTab extends GTab {
	private static final long serialVersionUID = -7224278425132595022L;

	protected HashMap<String, Image> iconcache;

	/**
	 * Create a new ItemTab
	 * 
	 * @param project
	 */
	public EnemyTab(final Project project) {
		super(project);
		iconcache = new HashMap<String, Image>();

		loadFile(Type.ENEMY);
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
		form.addElement("basic.battler", new FormGraphEle(I18N.t("Battler"), project, RTP.BATTLERS, act, "@battler_name", "@battler_hue"));

		// set skills
		form.addHeader("skill", new FormHeader(I18N.t("Skills"), "skill").setColumn(4));
		form.addElement("skill.hp", RubyForm.getNumber(I18N.t("Max HP"), act, "@maxhp"));
		form.addElement("skill.sp", RubyForm.getNumber(I18N.t("Max SP"), act, "@maxsp"));

		// add level

		// values
		form.addHeader("value", new FormHeader(I18N.t("Werte"), "systemmonitor").setColumn(8));
		form.addElement("value.str", RubyForm.getNumber(RGSS1Voc.str(project), act, "@str"));
		form.addElement("value.dex", RubyForm.getNumber(RGSS1Voc.dex(project), act, "@dex"));
		form.addElement("value.agi", RubyForm.getNumber(RGSS1Voc.agi(project), act, "@agi"));
		form.addElement("value.int", RubyForm.getNumber(RGSS1Voc.inte(project), act, "@int"));
		form.addElement("value.atk", RubyForm.getNumber("Attack Power", act, "@atk"));
		form.addElement("value.pdef", RubyForm.getNumber("Phys Def", act, "@pdef"));
		form.addElement("value.mdef", RubyForm.getNumber("Mag Def", act, "@mdef"));
		form.addElement("value.eva", RubyForm.getNumber("Evasion", act, "@eva"));

		// animation
		form.addHeader("other", new FormHeader(I18N.t("Sonstiges"), "animation").setColumn(4));
		form.addElement("other.user", new FormDBComboBox("User Animation", project, Type.ANIMATION, act, "@animation1_id", true));
		form.addElement("other.target", new FormDBComboBox("Target Animation", project, Type.ANIMATION, act, "@animation2_id", true));

		// drops
		form.addHeader("treasure", new FormHeader(I18N.t("Belohnung"), "armor_weapon").setColumn(8));
		form.addElement("treasure.gexp", RubyForm.getNumber("EXP", act, "@exp"));
		form.addElement("treasure.gold", RubyForm.getNumber("Gold", act, "@gold"));
		form.addElement("treasure.prob",
				RubyForm.getNumber("Chance(%)", act, "@treasure_prob").setMinMax(0, 100, 1).setInfoTxt("Chance to get this item"));

		form.addElement("treasure.get", new FormInfo("", I18N.t("Select of the 3 foldings, only once!")));
		form.addElement("treasure.geti", new FormDBComboBox("Item", project, Type.ITEM, act, "@item_id", true));
		form.addElement("treasure.getw", new FormDBComboBox("Weapon", project, Type.WEAPON, act, "@weapon_id", true));
		form.addElement("treasure.geta", new FormDBComboBox("Armor", project, Type.ARMOR, act, "@armor_id", true));

		// elements
		form.addHeader("ele", new FormHeader(RGSS1Helper.getName(Type.ELEMENT), RGSS1Helper.getIcon(Type.ELEMENT)).setCollapsed(true)
				.setColumn(4).setSorting(4));

		form.addElement("ele.0ele", new FormInfo("", I18N.t("Attribut Resistance")));
		form.addElement("ele.0stat", new FormInfo("", I18N.t("Status Resistance")));

		int e = RGSS1Helper.get(getProject(), Type.ELEMENT).size();
		IRubyObject eTable = act.getInstanceVariable("@element_ranks");
		int s = RGSS1Helper.get(getProject(), Type.STATUS).size();
		IRubyObject sTable = act.getInstanceVariable("@state_ranks");
		int l = e > s ? e : s;

		// run over all
		for (int i = 1; i < l; i++) {
			// add elements
			if (e > i) {
				form.addElement("ele." + i + "ele", new FormTable(RGSS1Helper.get(getProject(), Type.ELEMENT).get(i).getName(), eTable, i,
						0).setMinMax(0, 6, 1));
			} else {
				form.addElement("ele." + i + "ele", new FormEmpty());
			}

			// add status
			if (s > i) {
				form.addElement("ele." + i + "stat", new FormTable(RGSS1Helper.get(getProject(), Type.STATUS).get(i).getName(), sTable, i,
						0).setMinMax(0, 6, 1));
			} else {
				form.addElement("ele." + i + "stat", new FormEmpty());
			}
		}

		// build panel
		form.setCenter(new EnemyActionPanel(project, act.getInstanceVariable("@actions")));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.genericLoader.GTab#createObject()
	 */
	@Override
	public RubyObject createObject() {
		return (RubyObject) RGSSProjectHelper.getInterpreter(project).runScriptlet("return RPG::Enemy.new");
	}

}
