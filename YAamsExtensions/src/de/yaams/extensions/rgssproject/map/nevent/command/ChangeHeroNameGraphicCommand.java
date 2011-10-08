/**
 * 
 */
package de.yaams.extensions.rgssproject.map.nevent.command;

import org.jruby.RubyString;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.RTP;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.RGSS1Voc;
import de.yaams.extensions.rgssproject.database.form.FormDBComboBox;
import de.yaams.extensions.rgssproject.database.form.FormGraphEle;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCode;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCommand;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.FormComboBox;
import de.yaams.maker.helper.gui.form.FormHelper;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;
import de.yaams.maker.programm.project.Project;

/**
 * @author abt
 * 
 */
public class ChangeHeroNameGraphicCommand extends EventCommand {

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
		addString(e, 2, "");

		// build basic text
		String erg = "";

		// name
		if (e.getId() == 320) {
			erg = e.getParameters().get(1).toString();
		}

		// graphic
		else {
			erg = e.getParameters().get(1).toString() + " " + e.getParameters().get(2).toString();
		}

		// return
		// e.getObject().getInstanceVariable("@parameters").inspect().toString();
		return I18N.t("Setze {0} von {1} auf {2}", e.getId() == 320 ? "Name" : "Grafik",
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
		return "\"\",\"\"";
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
		addString(e, 2, "");

		// build panel
		Project p = e.getYecl().getProject();

		FormComboBox c = CommandFormHelper.buildCodeIdChancer(e, I18N.t("Setze"), new String[] { "320", "322" }, new String[] { "Name",
				"Grafik" });

		f.getHeader("basic").setColumn(4);
		f.addElement("basic.type", c);

		// add hero basis
		f.addElement("basic.hero", new FormDBComboBox(RGSS1Helper.getName(Type.ACTOR), p, Type.ACTOR, e.getParameters(), 0, false));

		// add name 320
		f.addHeader("name", new FormHeader(RGSS1Helper.getName(Type.CLASS), RGSS1Helper.getIcon(Type.CLASS)).setSorting(5));
		f.addElement("name.name",
				FormHelper.setEnabeldWhenRightElementSelect(c, RubyForm.getString("Name", e.getParameters().get(2)), "320"));

		// add graphic 322
		f.addHeader("graphic", new FormHeader("Grafik", "ress").setSorting(7));
		f.addElement("graphic.graph", FormHelper.setEnabeldWhenRightElementSelect(c, new FormGraphEle("Character", p, RTP.CHARACTER,
				(RubyString) e.getParameters().get(1), e.getParameters(), 2), "322"));
		f.addElement("graphic.battler", FormHelper.setEnabeldWhenRightElementSelect(c, new FormGraphEle("Battler", p, RTP.BATTLERS,
				(RubyString) e.getParameters().get(3), e.getParameters(), 4), "322"));

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
