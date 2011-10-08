/**
 * 
 */
package de.yaams.extensions.rgssproject.map.nevent.command;

import org.jruby.RubyString;

import de.yaams.extensions.rgssproject.RTP;
import de.yaams.extensions.rgssproject.database.form.FormGraphEle;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCode;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCommand;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.FormHelper;
import de.yaams.maker.helper.gui.form.core.FormBuilder;

/**
 * @author abt
 * 
 */
public class TransitionCommand extends EventCommand {

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
		addString(e, 0, "");

		// get it
		return e.getId() == 221 ? I18N.t("Bereite Transition vor") : I18N
				.t("Führe Transition {0} aus", e.getParameters().get(0).toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getIcon()
	 */
	@Override
	public String getIcon() {
		return "transition";
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
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getName()
	 */
	@Override
	public String getName() {
		return I18N.t("Transition");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getGroup()
	 */
	@Override
	public String getGroup() {
		return I18N.t("System");
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
		addString(e, 0, "");

		// add type
		f.addElement(
				"basic.typ",
				CommandFormHelper.buildCodeIdChancer(e, I18N.t("Typ"), new String[] { "221", "222" }, new String[] { "Vorbereiten",
						"Transition" }));

		// add name
		f.addElement("basic.name", FormHelper.setEnabeldWhenRightElementSelect(f.getElement("basic.typ"), new FormGraphEle(
				I18N.t("Grafik"), e.getYecl().getProject(), RTP.TRANSITION, (RubyString) e.getParameters().get(0), 1, null).setSorting(1),
				"222"));

		f.getElement("basic.typ").informListeners();

	}

}
