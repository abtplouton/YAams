/**
 * 
 */
package de.yaams.extensions.rgssproject.database.tabs;

import java.awt.Image;
import java.util.HashMap;

import org.jruby.RubyObject;

import de.yaams.extensions.rgssproject.RGSSProjectHelper;
import de.yaams.extensions.rgssproject.RTP;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.RGSS1Voc;
import de.yaams.extensions.rgssproject.database.form.FormDBComboBox;
import de.yaams.extensions.rgssproject.database.form.FormGraphEle;
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
public class ArmorTab extends GTab {
	private static final long serialVersionUID = -7224278425132595022L;

	protected HashMap<String, Image> iconcache;

	/**
	 * Create a new ItemTab
	 * 
	 * @param project
	 */
	public ArmorTab(final Project project) {
		super(project);
		iconcache = new HashMap<String, Image>();

		loadFile(Type.ARMOR);
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
		form.addElement("basic.image", new FormGraphEle(I18N.t("Bild"), project, RTP.ICON, act, "@icon_name", null));

		// sonst
		form.addHeader("usa", new FormHeader(I18N.t("Benutzung"), "info").setColumn(4));
		form.addElement("usa.price", RubyForm.getNumber(I18N.t("Preis"), act, "@price"));
		form.addElement("usa.type", RubyForm.getComboBoxNum("Type", new String[] { "0", "1", "2", "3" },
				new String[] { RGSS1Voc.shield(project), RGSS1Voc.helm(project), RGSS1Voc.armor(project), RGSS1Voc.acc(project) }, act,
				"@kind"));

		// values
		form.addHeader("value", new FormHeader(I18N.t("Werte"), "systemmonitor").setColumn(8));
		form.addElement("value.pdef", RubyForm.getNumber(RGSS1Voc.pdef(project), act, "@pdef"));
		form.addElement("value.mdef", RubyForm.getNumber(RGSS1Voc.mdef(project), act, "@mdef"));
		form.addElement("value.eva", RubyForm.getNumber("Evasion", act, "@eva"));
		form.addElement("value.str", RubyForm.getNumber(RGSS1Voc.str(project), act, "@str_plus"));
		form.addElement("value.dex", RubyForm.getNumber(RGSS1Voc.dex(project), act, "@dex_plus"));
		form.addElement("value.agi", RubyForm.getNumber(RGSS1Voc.agi(project), act, "@agi_plus"));
		form.addElement("value.int", RubyForm.getNumber(RGSS1Voc.inte(project), act, "@int_plus"));
		form.addElement("value.state", new FormDBComboBox("Auto State ID", project, Type.STATUS, act, "@auto_state_id", true));

		// build panel
		form.addHeader("resistance", new FormHeader(I18N.t("Resistenz gegen"), "armor").setColumn(4).setSorting(4));

		form.addElement("resistance.element",
				new FormDBList(project, Type.ELEMENT, act.getInstanceVariable("@guard_element_set"), I18N.t("Element Verteidigung.")));
		form.addElement("resistance.status",
				new FormDBList(project, Type.STATUS, act.getInstanceVariable("@guard_state_set"), I18N.t("Status Verteidigung.")));

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
		return "armor";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getTitle()
	 */
	@Override
	public String getTitle() {
		return I18N.t("Armors");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.genericLoader.GTab#createObject()
	 */
	@Override
	public RubyObject createObject() {
		return (RubyObject) RGSSProjectHelper.getInterpreter(project).runScriptlet("return RPG::Armor.new");
	}

}
