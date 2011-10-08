/**
 * 
 */
package de.yaams.extensions.rgssproject.map.nevent.command;

import de.yaams.extensions.rgssproject.map.nevent.core.EventCode;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCommand;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.core.FormBuilder;

/**
 * @author abt
 * 
 */
public class AudioStopCommand extends EventCommand {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getIcon()
	 */
	@Override
	public String getIcon() {
		return "audio";
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
		return I18N.t("Stoppe Musik");
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
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getStartParameter()
	 */
	@Override
	public String getStartParameter() {
		return "20";
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
	public void buildPanel(final FormBuilder f, EventCode e) {

		// add type
		f.addElement(
				"basic.me",
				CommandFormHelper.buildCodeIdChancer(e, "", new String[] { "242", "246", "247", "248", "251" }, new String[] { getTyp(242),
						getTyp(246), getTyp(247), getTyp(248), getTyp(251) })).addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				f.getElement("basic.frames").setEnabled("246".equals(form.getContentAsString()) || "242".equals(form.getContentAsString()));

			}
		});

		// add time
		CommandFormHelper.addFrameSupport(f, e, 0);

	}

	protected String getTyp(int id) {
		switch (id) {
		case 242:
			return I18N.t("Fade out BGM");
		case 246:
			return I18N.t("Fade out BGS");
		case 247:
			return I18N.t("Memorize BGM/BGS");
		case 248:
			return I18N.t("Restore BGM/BGS");
		case 251:
			return I18N.t("Stop SE");
		}

		return null;
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
		return getTyp(e.getId());
	}

}
