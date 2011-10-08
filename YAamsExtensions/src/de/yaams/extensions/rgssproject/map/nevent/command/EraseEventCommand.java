/**
 * 
 */
package de.yaams.extensions.rgssproject.map.nevent.command;

import de.yaams.extensions.rgssproject.map.nevent.core.EventCode;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCommand;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.core.FormBuilder;

/**
 * @author abt
 * 
 */
public class EraseEventCommand extends EventCommand {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getIcon()
	 */
	@Override
	public String getIcon() {
		return "event_del";
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
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getName()
	 */
	@Override
	public String getName() {
		return I18N.t("Lösche/Stoppe Event");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getGroup()
	 */
	@Override
	public String getGroup() {
		return I18N.t("Event");
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
	public void buildPanel(FormBuilder f, EventCode e) {
		// add type
		f.addElement(
				"basic.typ",
				CommandFormHelper.buildCodeIdChancer(e, I18N.t("Typ"), new String[] { "115", "116" }, new String[] { "Lösche Event",
						"Stoppe Event" }));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.extensions.map.nevent.core.EventCommand#getTitle(de.yaams.extensions
	 * .map.nevent.core.EventCode)
	 */
	@Override
	protected String getInternTitle(EventCode e) {
		return e.getId() == 115 ? I18N.t("Lösche Event") : I18N.t("Stoppe Event");
	}
}
