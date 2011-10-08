/**
 * 
 */
package de.yaams.extensions.rgssproject.map.nevent.command;

import de.yaams.extensions.rgssproject.RTP;
import de.yaams.extensions.rgssproject.database.form.FormMusicEle;
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
public class AudioPlayCommand extends EventCommand {

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
		return I18N.t("Spiele Musik");
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
		return "RPG::AudioFile.new()";
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

		// build panel
		final FormMusicEle m = new FormMusicEle(I18N.t("zu"), e.getYecl().getProject(), getTyp(e.getId()), e.getParameters().get(0));

		// add type
		f.addElement(
				"basic.me",
				CommandFormHelper.buildCodeIdChancer(e, I18N.t("Spiele"), new String[] { "249", "250", "245", "241" }, new String[] { "ME",
						"SE", "BGS", "BGM" })).addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				m.setFolder(getTyp(Integer.valueOf(form.getContentAsString())));

			}
		});

		// add name
		f.addElement("basic.name", m);
	}

	/**
	 * Get right folder
	 * 
	 * @param id
	 * @return
	 */
	protected String getTyp(int id) {
		switch (id) {
		case 249:
			return RTP.ME;
		case 250:
			return RTP.SE;
		case 245:
			return RTP.BGS;
		case 241:
			return RTP.BGM;
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
		return I18N.t("Spiele {1}: {0}", e.getParameters().get(0).getInstanceVariables().getInstanceVariable("@name"), getTyp(e.getId()));
	}

}
