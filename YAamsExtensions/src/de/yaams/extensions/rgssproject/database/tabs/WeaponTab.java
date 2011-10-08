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
public class WeaponTab extends GTab {
	private static final long serialVersionUID = -7224278425132595022L;

	protected HashMap<String, Image> iconcache;

	/**
	 * Create a new ItemTab
	 * 
	 * @param project
	 */
	public WeaponTab(final Project project) {
		super(project);
		iconcache = new HashMap<String, Image>();

		loadFile(Type.WEAPON);
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

		form.addHeader("usa", new FormHeader(I18N.t("Benutzung"), "info"));
		form.addElement("usa.price", RubyForm.getNumber(I18N.t("Preis"), act, "@price"));

		// values
		form.addHeader("value", new FormHeader(I18N.t("Werte"), "systemmonitor").setColumn(8));
		form.addElement("value.atk", RubyForm.getNumber("Attack Power", act, "@atk"));
		form.addElement("value.pdef", RubyForm.getNumber("Phys Defense", act, "@pdef"));
		form.addElement("value.mdef", RubyForm.getNumber("Mag Defense", act, "@mdef"));
		form.addElement("value.str", RubyForm.getNumber(RGSS1Voc.str(project), act, "@str_plus"));
		form.addElement("value.dex", RubyForm.getNumber(RGSS1Voc.dex(project), act, "@dex_plus"));
		form.addElement("value.agi", RubyForm.getNumber(RGSS1Voc.agi(project), act, "@agi_plus"));
		form.addElement("value.int", RubyForm.getNumber(RGSS1Voc.inte(project), act, "@int_plus"));

		// animation
		form.addHeader("other", new FormHeader(I18N.t("Sonstiges"), "animation").setColumn(4));
		form.addElement("other.user", new FormDBComboBox("User Animation", project, Type.ANIMATION, act, "@animation1_id", true));
		form.addElement("other.target", new FormDBComboBox("Target Animation", project, Type.ANIMATION, act, "@animation2_id", true));

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
		return "weapon";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getTitle()
	 */
	@Override
	public String getTitle() {
		return I18N.t("Weapons");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.genericLoader.GTab#createObject()
	 */
	@Override
	public RubyObject createObject() {
		return (RubyObject) RGSSProjectHelper.getInterpreter(project).runScriptlet("return RPG::Weapon.new");
	}

}
