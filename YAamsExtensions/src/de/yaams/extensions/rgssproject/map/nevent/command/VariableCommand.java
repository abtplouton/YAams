/**
 * 
 */
package de.yaams.extensions.rgssproject.map.nevent.command;

import org.jruby.RubyFixnum;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.form.FormDBComboBox;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.extensions.rgssproject.map.form.FormSwitchVarSelector;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCode;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCommand;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.JavaHelper;
import de.yaams.maker.helper.gui.form.FormComboBox;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormEmpty;
import de.yaams.maker.helper.gui.form.FormHelper;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;

/**
 * @author abt
 * 
 */
public class VariableCommand extends EventCommand {

	protected static String[] tActor = new String[] { "Level", "EXP", "HP", "SP", "Max HP", "Max SP", "Strength", "Dexterity", "Agility",
			"Intelligence", "Attack Power", "Phys. Defense", "Mag. Defense", "Evasion" };
	protected static String[] tEnemy = new String[] { "HP", "SP", "Max HP", "Max SP", "Strength", "Dexterity", "Agility", "Intelligence",
			"Attack Power", "Phys. Defense", "Mag. Defense", "Evasion" };
	protected static String[] tSprite = new String[] { "X Tile", "Y Tile", "Face", "Screen X", "Screen Y", "Terrain" };
	protected static String[] tOther = new String[] { "Map ID", "Party Size", "Money", "Number of Step", "Play Time", "Timer in Secons",
			"Number of Saves" };
	protected static String[] oTit = new String[] { "=", "+", "-", "*", "/", "%" };

	/**
	 * Create a new LabelCommand
	 */
	public VariableCommand() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.extensions.map.nevent.core.EventCommand#getTitle(de.yaams.extensions
	 * .map.nevent.core.EventCode)
	 */
	@Override
	protected String getInternTitle(EventCode e) {

		// missing parameter?
		if (e.getParameters().size() < 6) {
			e.getParameters().add(RubyFixnum.one(e.getObject().getRuntime()));
		}

		// build it
		StringBuffer s = new StringBuffer();

		// single or multi?
		int v1 = RubyHelper.toInt(e.getParameters().get(0)), v2 = RubyHelper.toInt(e.getParameters().get(1));
		// exist?
		if (v1 == 0) {
			s.append("!0!");
		} else {
			s.append(RGSS1Helper.get(e.getYecl().getProject(), Type.VARIABLE).get(v1).getName());
		}
		if (v1 != v2) {
			s.append("-");
			// exist?
			if (v2 == 0) {
				s.append("!0!");
			} else {
				s.append(RGSS1Helper.get(e.getYecl().getProject(), Type.VARIABLE).get(v2).getName());
			}
		}

		// add operator
		s.append(oTit[RubyHelper.toInt(e.getParameters().get(2))]);

		// prepare
		v1 = RubyHelper.toInt(e.getParameters().get(4));
		v2 = RubyHelper.toInt(e.getParameters().get(5));

		// add value
		switch (RubyHelper.toInt(e.getParameters().get(3))) {
		case 0: // Value
			s.append(v1);
			break;
		case 1: // Variable
			// exist?
			if (v1 == 0) {
				s.append("!0!");
			} else {
				s.append(RGSS1Helper.get(e.getYecl().getProject(), Type.VARIABLE).get(v1).getName());
			}
			break;
		case 2: // Random
			s.append(I18N.t("Random:{0}-{1}", v1, v2));
			break;
		case 3: // Item
			// exist?
			if (v1 == 0) {
				s.append("!0!");
			} else {
				s.append(RGSS1Helper.get(e.getYecl().getProject(), Type.ITEM).get(v1).getName());
			}
			break;
		case 4: // Actor
			// exist?
			if (v1 == 0) {
				s.append("!0!");
			} else {
				s.append(I18N.t("{0} {1}", RGSS1Helper.get(e.getYecl().getProject(), Type.ACTOR).get(v1).getName(), tActor[v2]));
			}
			break;
		case 5: // Enemy
			s.append(I18N.t("{0} {1}", v1 + 1, tEnemy[v2]));
			break;
		case 6: // Spirit
			s.append(I18N.t("{0} {1}", v1, tSprite[v2]));
			break;
		case 7: // Other
			s.append(tOther[v1]);
			break;
		default:
			s.append("?");
			s.append(RubyHelper.toInt(e.getParameters().get(3)));
			break;
		}

		return s.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getIcon()
	 */
	@Override
	public String getIcon() {
		return "variable";
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
		return getIcon();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getStartParameter()
	 */
	@Override
	public String getStartParameter() {
		return "1,1,0,0,0,0,0";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getName()
	 */
	@Override
	public String getName() {
		return I18N.t("Variable");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getGroup()
	 */
	@Override
	public String getGroup() {
		return I18N.t("Control");
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
		// missing parameter?
		if (e.getParameters().size() < 6) {
			e.getParameters().add(RubyFixnum.one(e.getObject().getRuntime()));
		}

		// add type
		final FormComboBox c = new FormComboBox(I18N.t("Type"), new String[] { "0", "1" }, new String[] { "Single", "Multi" });
		c.selectField(RubyHelper.toInt(e.getParameters().get(0)) == RubyHelper.toInt(e.getParameters().get(1)) ? "0" : "1");

		f.getHeader("basic").setColumn(4);

		f.addElement("basic.type", c.setSorting(-1));
		f.addElement("basic.type2", new FormEmpty().setSorting(-1));

		// add switch
		f.addElement("basic.v1", new FormSwitchVarSelector(e.getYecl().getProject(), I18N.t("Setze"), e.getParameters(), 0, Type.VARIABLE)
				.addChangeListener(new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						if (c.getContentAsString().equals("0")) {
							RubyHelper.setNum(e.getParameters(), 1, c.getContentAsString());
						}
					}
				}));

		// add 2. switch
		f.addElement(
				"basic.v2",
				FormHelper.setEnabeldWhenRightElementSelect(c,
						new FormSwitchVarSelector(e.getYecl().getProject(), I18N.t("bis"), e.getParameters(), 1, Type.VARIABLE), "1"));

		c.addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				if (c.getContentAsString().equals("0")) {
					RubyHelper.setNum(e.getParameters(), 1, RubyHelper.toInt(e.getParameters().get(0)));
				}

			}
		});

		// add operator
		f.addElement("basic.operator", RubyForm.getComboBoxNum(I18N.t("Operator"), JavaHelper.numAry(5), oTit, e.getParameters(), 2)
				.setSorting(1));

		// add values

		f.addHeader("value", new FormHeader("Value", "variable").setColumn(4));

		// selector
		FormComboBox fcb = RubyForm.getComboBoxNum(I18N.t("Typ"), JavaHelper.numAry(7), new String[] { "Value", "Variable", "Random",
				RGSS1Helper.getName(Type.ITEM), "Actor", "Enemy", "Spirit", "Other" }, e.getParameters(), 3);
		f.addElement("value.atyp", fcb);
		f.addElement("value.atyp1", new FormEmpty());

		// value
		f.addElement("value.bvalue",
				FormHelper.setEnabeldWhenRightElementSelect(fcb, RubyForm.getNumber(I18N.t("Wert"), e.getParameters(), 4), "0"));

		// variable
		f.addElement(
				"value.bvar",
				FormHelper.setEnabeldWhenRightElementSelect(fcb,
						new FormSwitchVarSelector(e.getYecl().getProject(), I18N.t("Variable"), e.getParameters(), 4, Type.VARIABLE), "1"));

		// random
		f.addElement("value.crand",
				FormHelper.setEnabeldWhenRightElementSelect(fcb, RubyForm.getNumber(I18N.t("Random"), e.getParameters(), 4), "2"));
		f.addElement("value.crand2",
				FormHelper.setEnabeldWhenRightElementSelect(fcb, RubyForm.getNumber(I18N.t("-"), e.getParameters(), 5), "2"));

		// item
		f.addElement("value.ditem", FormHelper.setEnabeldWhenRightElementSelect(fcb, new FormDBComboBox(RGSS1Helper.getName(Type.ITEM), e
				.getYecl().getProject(), Type.ITEM, e.getParameters(), 4, false).setInfoTxt(I18N
				.t("Anzahl des Gegenstandes im Inventar der Party")), "3"));
		f.addElement("value.ditem2", new FormEmpty());

		// actor
		f.addElement("value.eactor", FormHelper.setEnabeldWhenRightElementSelect(fcb, new FormDBComboBox(I18N.t("Actor"), e.getYecl()
				.getProject(), Type.ACTOR, e.getParameters(), 4, false), "4"));
		f.addElement(
				"value.eactor2",
				FormHelper.setEnabeldWhenRightElementSelect(fcb,
						RubyForm.getComboBoxNum(I18N.t(""), JavaHelper.numAry(13), tActor, e.getParameters(), 5), "4"));

		// enemy
		f.addElement(
				"value.fenemy",
				FormHelper.setEnabeldWhenRightElementSelect(fcb,
						RubyForm.getComboBoxNum(I18N.t("Enemy"), JavaHelper.numAry(7), JavaHelper.numAry(7), e.getParameters(), 4), "5"));

		f.addElement(
				"value.fenemy2",
				FormHelper.setEnabeldWhenRightElementSelect(fcb,
						RubyForm.getComboBoxNum(I18N.t(""), JavaHelper.numAry(11), tEnemy, e.getParameters(), 4), "5")); // FUNF

		// enemy
		f.addElement("value.gsprite", RubyForm.getError(I18N.t("Sprite"), e.getParameters().get(4)));
		f.addElement(
				"value.gsprite2",
				FormHelper.setEnabeldWhenRightElementSelect(fcb,
						RubyForm.getComboBoxNum(I18N.t(""), JavaHelper.numAry(5), tSprite, e.getParameters(), 5), "6"));

		// other
		f.addElement(
				"value.hother",
				FormHelper.setEnabeldWhenRightElementSelect(fcb,
						RubyForm.getComboBoxNum(I18N.t("Other"), JavaHelper.numAry(6), tOther, e.getParameters(), 5), "7"));

		// disable wrong elements
		fcb.informListeners();
		c.informListeners();

	}
}
