/**
 * 
 */
package de.yaams.extensions.rgssproject.map.nevent.command;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.form.FormDBComboBox;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCode;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCommand;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.FormComboBox;
import de.yaams.maker.helper.gui.form.FormHelper;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;

/**
 * @author abt
 * 
 */
public class PartyValueCommand extends EventCommand {

	protected final String[] types = new String[] { "HP", "SP", "EXP", "Level" };

	/**
	 * Create a new LabelCommand
	 */
	public PartyValueCommand() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.extensions.map.nevent.core.EventCommand#getTitle(de.yaams.extensions
	 * .map.nevent.core.EventCode)
	 */
	@Override
	protected String getInternTitle(EventCode e) {
		addBoolean(e, 5, false);

		// return
		// e.getObject().getInstanceVariable("@parameters").inspect().toString();
		return I18N.t("{0} {1} {2}",
				RubyHelper.toInt(e.getParameters().get(0)) == 0 ? "Ganze Party" : RGSS1Helper.get(e.getYecl().getProject(), Type.ACTOR)
						.get(RubyHelper.toInt(e.getParameters().get(0))).getName(), CommandFormHelper.getOperatorText(e, 1),
				types[e.getId() >= 315 ? e.getId() - 313 : e.getId() - 311]);

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
		return getIcon();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getStartParameter()
	 */
	@Override
	public String getStartParameter() {
		return "1,0,0,1, false";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getName()
	 */
	@Override
	public String getName() {
		return I18N.t("Helden-Werte anpassen");
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

		// missing parameter?
		addBoolean(e, 5, false);

		// build panel

		FormComboBox c = CommandFormHelper.buildCodeIdChancer(e, I18N.t("Setze"), new String[] { "311", "312", "315", "316" }, types);

		// add operator
		CommandFormHelper.addOperatorSupport(f, e, 1);

		f.addElement("basic.type", c);

		// add hero
		f.addElement("basic.hero",
				new FormDBComboBox(RGSS1Helper.getName(Type.ACTOR), e.getYecl().getProject(), Type.ACTOR, e.getParameters(), 0, true));

		// add special elements
		f.addHeader("special", new FormHeader("Spezial", "dummy"));

		f.addElement(
				"special.hp",
				FormHelper.setEnabeldWhenRightElementSelect(c,
						RubyForm.getBoolean(I18N.t("Wenn HP, Sterben erlaubt"), e.getParameters(), 4), "311"));

	}
}
