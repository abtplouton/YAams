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
import de.yaams.maker.helper.gui.form.FormEmpty;
import de.yaams.maker.helper.gui.form.core.FormBuilder;

/**
 * @author abt
 * 
 */
public class PartyCommand extends EventCommand {

	/**
	 * Create a new LabelCommand
	 */
	public PartyCommand() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.extensions.map.nevent.core.EventCommand#getTitle(de.yaams.extensions
	 * .map.nevent.core.EventCode)
	 */
	@Override
	protected String getInternTitle(EventCode e) {
		int aid = RubyHelper.toInt(e.getParameters().get(0));
		// return e.getParameters().get(0).toString() +
		// e.getParameters().get(1).toString() +
		// e.getParameters().get(2).toString();
		return I18N.t("{0} {1} {2}", e.getParameters().get(1).toString().equals("0") ? "+" : "-",
				RGSS1Helper.get(e.getYecl().getProject(), Type.ACTOR).get(aid).getName(),
				e.getParameters().get(2).toString().equals("1") ? "(init)" : "");
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
		int aid = RubyHelper.toInt(e.getParameters().get(0));
		return RGSS1Helper.getIcon(e.getYecl().getProject(), Type.ACTOR, aid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getStartParameter()
	 */
	@Override
	public String getStartParameter() {
		return "1,0,0";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getName()
	 */
	@Override
	public String getName() {
		return I18N.t("Party anpassen");
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

		f.getHeader("basic").setColumn(4);

		f.addElement("basic.hero",
				new FormDBComboBox(RGSS1Helper.getName(Type.ACTOR), e.getYecl().getProject(), Type.ACTOR, e.getParameters(), 0, false));

		// add typ
		f.addElement("basic.add",
				RubyForm.getComboBoxNum("", new String[] { "0", "1" }, new String[] { "Füge hinzu", "Entferne" }, e.getParameters(), 1));
		f.addElement("basic.add2", new FormEmpty());

		// add typ
		f.addElement(
				"basic.typ",
				RubyForm.getBooleanAsInt(I18N.t("Im Startstatus (Init)"), e.getParameters(), 2)
						.setInfoTxt(
								I18N.t("Sollte der Held im Spielverlauf der Gruppe beigetreten sein und wieder erneut beitreten, kann man hier mit festlegen, ob er seine Erfahrungen, Werte behält, oder resetet werden soll.")));

	}
}
