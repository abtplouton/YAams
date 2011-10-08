/**
 * 
 */
package de.yaams.extensions.rgssproject.map.nevent.command;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCode;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCommand;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.JavaHelper;
import de.yaams.maker.helper.gui.form.FormComboBox;
import de.yaams.maker.helper.gui.form.FormHelper;
import de.yaams.maker.helper.gui.form.core.FormBuilder;

/**
 * @author abt
 * 
 */
public class WeatherCommand extends EventCommand {

	private static final String[] weath = new String[] { "Keins", "Regen", "Sturm", "Schnee" };
	private static final String[] weathIcons = new String[] { "weather", "weather-rain", "weather-tornado", "weather-snow" };

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getIcon()
	 */
	@Override
	public String getIcon() {
		return weathIcons[1];
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
		return weathIcons[RubyHelper.toInt(e.getParameters().get(0))];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getStartParameter()
	 */
	@Override
	public String getStartParameter() {
		return "1,5,20";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getName()
	 */
	@Override
	public String getName() {
		return I18N.t("Wetter");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getGroup()
	 */
	@Override
	public String getGroup() {
		return I18N.t("Map");
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

		// add time
		CommandFormHelper.addFrameSupport(f, e, 2);

		FormComboBox fc = RubyForm.getComboBoxNum(I18N.t(""), JavaHelper.numAry(4), weath, e.getParameters(), 0);

		// add type
		f.addElement("basic.type", fc);

		// add
		f.addElement(
				"basic.value",
				FormHelper.setEnabeldWhenNotRightElementSelect(fc,
						RubyForm.getNumber(I18N.t("Stärke"), e.getParameters(), 1).setMinMax(1, 10, 1), "0"));
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
		if (RubyHelper.toInt(e.getParameters().get(0)) == 0) {
			return I18N.t("{0}{1}", weath[RubyHelper.toInt(e.getParameters().get(0))], CommandFormHelper.getFrameText(e, 2));
		} else {
			return I18N.t("{0}:{1}{2}", weath[RubyHelper.toInt(e.getParameters().get(0))], RubyHelper.toInt(e.getParameters().get(1)),
					CommandFormHelper.getFrameText(e, 2));
		}
	}
}
