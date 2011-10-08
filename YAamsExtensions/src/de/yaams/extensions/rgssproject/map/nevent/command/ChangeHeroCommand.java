/**
 * 
 */
package de.yaams.extensions.rgssproject.map.nevent.command;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.RGSS1Voc;
import de.yaams.extensions.rgssproject.database.form.FormDBComboBox;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCode;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCommand;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.JavaHelper;
import de.yaams.maker.helper.gui.form.FormComboBox;
import de.yaams.maker.helper.gui.form.FormHelper;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;
import de.yaams.maker.programm.project.Project;

/**
 * @author abt
 * 
 */
public class ChangeHeroCommand extends EventCommand {

	protected final String[] types = new String[] { "Parameters", "Skills", "Equipment", "Name", "Class", "Graphic" };

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.extensions.map.nevent.core.EventCommand#getTitle(de.yaams.extensions
	 * .map.nevent.core.EventCode)
	 */
	@Override
	protected String getInternTitle(EventCode e) {
		// check parameters
		addInt(e, 3, 0);
		addInt(e, 4, 0);
		addInt(e, 5, 0);

		// build basic text
		String erg = "";

		// parameters
		if (e.getId() == 317) {
			erg = CommandFormHelper.getOperatorText(e, 2)
					+ getParameters(e.getYecl().getProject())[RubyHelper.toInt(e.getParameters().get(1))];
		}

		// skills
		if (e.getId() == 318) {
			erg = new String[] { "+", "-" }[RubyHelper.toInt(e.getParameters().get(1))] + " "
					+ RGSS1Helper.get(e.getYecl().getProject(), Type.SKILL).get(RubyHelper.toInt(e.getParameters().get(2))).getName();
		}

		// equip
		if (e.getId() == 319) {
			if (RubyHelper.toInt(e.getParameters().get(2)) == 0) {
				erg = I18N.t("Nichts");

			} else if (RubyHelper.toInt(e.getParameters().get(1)) == 0) {
				erg = RGSS1Helper.get(e.getYecl().getProject(), Type.WEAPON).get(RubyHelper.toInt(e.getParameters().get(2))).getName();
			} else {
				erg = RGSS1Helper.get(e.getYecl().getProject(), Type.ARMOR).get(RubyHelper.toInt(e.getParameters().get(2))).getName();
			}
		}

		// name
		if (e.getId() == 320) {
			erg = e.getParameters().get(1).toString();
		}

		// class
		if (e.getId() == 321) {
			erg = RGSS1Helper.get(e.getYecl().getProject(), Type.CLASS).get(RubyHelper.toInt(e.getParameters().get(1))).getName();
		}

		// graphic
		if (e.getId() == 322) {
			erg = e.getParameters().get(1).toString() + " " + e.getParameters().get(2).toString();
		}

		// return
		// e.getObject().getInstanceVariable("@parameters").inspect().toString();
		return I18N.t("Setze {0} von {1} auf {2}", types[e.getId() - 317],
				RGSS1Helper.get(e.getYecl().getProject(), Type.ACTOR).get(RubyHelper.toInt(e.getParameters().get(0))).getName(), erg);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getIcon()
	 */
	@Override
	public String getIcon() {
		return "hero";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.extensions.map.nevent.core.EventCommand#getIcon(de.yaams.extensions
	 * .map.nevent.core.EventCode)
	 */
	@Override
	public Object getIcon(EventCode e) {
		// skills
		if (e.getId() == 318) {
			return RGSS1Helper.getIcon(e.getYecl().getProject(), Type.SKILL, RubyHelper.toInt(e.getParameters().get(0)));
		}

		// equip
		if (e.getId() == 319) {
			if (RubyHelper.toInt(e.getParameters().get(2)) == 0) {
				if (RubyHelper.toInt(e.getParameters().get(1)) == 0) {
					return RGSS1Helper.getIcon(Type.WEAPON);
				} else {
					return RGSS1Helper.getIcon(Type.ARMOR);
				}

			} else if (RubyHelper.toInt(e.getParameters().get(1)) == 0) {
				return RGSS1Helper.getIcon(e.getYecl().getProject(), Type.WEAPON, RubyHelper.toInt(e.getParameters().get(2)));
			} else {
				return RGSS1Helper.getIcon(e.getYecl().getProject(), Type.ARMOR, RubyHelper.toInt(e.getParameters().get(2)));
			}

		}

		// hero
		return RGSS1Helper.getIcon(e.getYecl().getProject(), Type.ACTOR, RubyHelper.toInt(e.getParameters().get(0)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getStartParameter()
	 */
	@Override
	public String getStartParameter() {
		return "1,0,0,0,0";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getName()
	 */
	@Override
	public String getName() {
		return I18N.t("Helden Eigenschaften anpassen");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getGroup()
	 */
	@Override
	public String getGroup() {
		return I18N.t("Party");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.extensions.map.nevent.core.EventCommand#buildPanel(de.yaams.
	 * core.helper.gui.form.core.FormBuilder,
	 * de.yaams.extensions.map.nevent.core.EventCode)
	 */
	@Override
	public void buildPanel(FormBuilder f, final EventCode e) {
		// check parameters
		addInt(e, 3, 0);
		addInt(e, 4, 0);
		addInt(e, 5, 0);

		// build panel
		Project p = e.getYecl().getProject();

		FormComboBox c = CommandFormHelper.buildCodeIdChancer(e, I18N.t("Setze"),
				new String[] { "317", "318", "319", "320", "321", "322" }, types);

		f.getHeader("basic").setColumn(4);
		f.addElement("basic.type", c);

		// add hero basis
		f.addElement("basic.hero", new FormDBComboBox(RGSS1Helper.getName(Type.ACTOR), p, Type.ACTOR, e.getParameters(), 0, false));

		// add parameters 317
		f.addHeader("parameters", new FormHeader(I18N.t("Parameters"), "parameter").setSorting(2));

		f.addElement("parameters.typ", FormHelper.setEnabeldWhenRightElementSelect(c,
				RubyForm.getComboBoxNum(I18N.t("Parameter"), JavaHelper.numAry(5), getParameters(p), e.getParameters(), 1).setSorting(-1),
				"317"));

		CommandFormHelper.addOperatorSupport(f, e, 2, "parameters.", c, "317");

		// add skill 318
		f.addHeader("skill", new FormHeader(RGSS1Helper.getName(Type.SKILL), RGSS1Helper.getIcon(Type.SKILL)).setColumn(4).setSorting(3));

		f.addElement(
				"skill.typ",
				FormHelper.setEnabeldWhenRightElementSelect(
						c,
						RubyForm.getComboBoxNum(RGSS1Helper.getName(Type.SKILL), JavaHelper.numAry(1),
								new String[] { "Vergesse", "Lerne" }, e.getParameters(), 1).setSorting(0), "318"));
		f.addElement(
				"skill.skill",
				FormHelper.setEnabeldWhenRightElementSelect(c,
						new FormDBComboBox("", p, Type.SKILL, e.getParameters(), 2, false).setSorting(1), "318"));

		// add equip 319
		f.addHeader("equip", new FormHeader(RGSS1Voc.equip(p), RGSS1Helper.getIcon(Type.WEAPON)).setSorting(4));

		FormComboBox b = RubyForm.getComboBoxNum(RGSS1Voc.equip(p), JavaHelper.numAry(4),
				new String[] { RGSS1Voc.weapon(p), RGSS1Voc.shield(p), RGSS1Voc.helm(p), RGSS1Voc.armor(p), RGSS1Voc.acc(p) },
				e.getParameters(), 1);

		f.addElement("equip.typ", FormHelper.setEnabeldWhenRightElementSelect(c, b.setSorting(-1), "319"));
		f.addElement("equip.weapon", FormHelper.setEnabeldWhenRightElementSelect(b, new FormDBComboBox(RGSS1Voc.weapon(p), p, Type.WEAPON,
				e.getParameters(), 2, false).setSorting(0), "0"));
		f.addElement("equip.shield", FormHelper.setEnabeldWhenRightElementSelect(b,
				new FormDBComboBox(RGSS1Voc.shield(p), p, Type.ARMOR, e.getParameters(), 2, false).setSorting(1), "1"));
		f.addElement("equip.helm", FormHelper.setEnabeldWhenRightElementSelect(b,
				new FormDBComboBox(RGSS1Voc.helm(p), p, Type.ARMOR, e.getParameters(), 2, false).setSorting(2), "2"));
		f.addElement("equip.armor", FormHelper.setEnabeldWhenRightElementSelect(b,
				new FormDBComboBox(RGSS1Voc.armor(p), p, Type.ARMOR, e.getParameters(), 2, false).setSorting(3), "3"));
		f.addElement("equip.acc", FormHelper.setEnabeldWhenRightElementSelect(b,
				new FormDBComboBox(RGSS1Voc.acc(p), p, Type.ARMOR, e.getParameters(), 2, false).setSorting(4), "4"));

		b.informListeners();

		// // add name 320
		// f.addHeader("name", new FormHeader(RGSS1Helper.getName(Type.CLASS),
		// RGSS1Helper.getIcon(Type.CLASS)).setSorting(5));
		// f.addElement("name.name",
		// FormHelper.setEnabeldWhenRightElementSelect(c,
		// RubyForm.getString("Name", e.getParameters().get(2)), "320"));

		// add class 321
		f.addHeader("class", new FormHeader(RGSS1Helper.getName(Type.CLASS), RGSS1Helper.getIcon(Type.CLASS)).setSorting(6));
		f.addElement(
				"class.class",
				FormHelper.setEnabeldWhenRightElementSelect(c,
						new FormDBComboBox(RGSS1Helper.getName(Type.CLASS), p, Type.CLASS, e.getParameters(), 1, false), "321"));
		//
		// // add graphic 322
		// f.addHeader("graphic", new FormHeader("Grafik",
		// "ress").setSorting(7));
		// f.addElement("graphic.graph",
		// FormHelper.setEnabeldWhenRightElementSelect(c, new
		// FormGraphEle("Character", p, RTP.CHARACTER,
		// (RubyString) e.getParameters().get(1), e.getParameters(), 2),
		// "322"));
		// f.addElement("graphic.battler",
		// FormHelper.setEnabeldWhenRightElementSelect(c, new
		// FormGraphEle("Battler", p, RTP.BATTLERS,
		// (RubyString) e.getParameters().get(3), e.getParameters(), 4),
		// "322"));

		// inform
		c.informListeners();
	}

	/**
	 * @param p
	 * @return
	 */
	protected String[] getParameters(Project p) {
		return new String[] { RGSS1Voc.hp(p), RGSS1Voc.sp(p), RGSS1Voc.str(p), RGSS1Voc.dex(p), RGSS1Voc.agi(p), RGSS1Voc.inte(p) };
	}
}
