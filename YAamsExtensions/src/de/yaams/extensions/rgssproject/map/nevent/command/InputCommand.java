/**
 * 
 */
package de.yaams.extensions.rgssproject.map.nevent.command;

import org.jruby.RubyFixnum;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.extensions.rgssproject.map.form.FormSwitchVarSelector;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCode;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCommand;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormNumberSpinner;
import de.yaams.maker.helper.gui.form.core.FormBuilder;

/**
 * @author abt
 * 
 */
public class InputCommand extends EventCommand {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getIcon()
	 */
	@Override
	public String getIcon() {
		return "input";
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
		return I18N.t("Eingabe von Werten");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getGroup()
	 */
	@Override
	public String getGroup() {
		return I18N.t("Input");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getStartParameter()
	 */
	@Override
	public String getStartParameter() {
		return "1,6";
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

		// missing parameter?
		if (e.getParameters().size() < 2) {
			e.getParameters().add(RubyFixnum.five(e.getObject().getRuntime()));
		}

		// build panel
		final FormNumberSpinner n = RubyForm.getNumber(I18N.t("Ziffernanzahl"), e.getParameters(), 1).setMinMax(1, 8, 1);
		// add name
		f.addElement("basic.var", new FormSwitchVarSelector(e.getYecl().getProject(), I18N.t("Variable"), e.getParameters(), 0,
				Type.VARIABLE));

		// add type
		f.addElement(
				"basic.typ",
				CommandFormHelper.buildCodeIdChancer(e, I18N.t("Setze"), new String[] { "103", "105" }, new String[] { "Input Number",
						"Button Input" })).setSorting(-1).addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				n.setEnabled("103".equals(form.getContentAsString()));

			}
		});

		// add name
		f.addElement("basic.digit", n.setSorting(1));
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
		return I18N.t("{0} auf {1}", e.getId() == 103 ? "Input Number" : "Button Input",
				RGSS1Helper.get(e.getYecl().getProject(), Type.VARIABLE).get(RubyHelper.toInt(e.getParameters().get(0))).getName());
		// return I18N.t("{1}: {0}", RGSS1Helper.get(e.getYecl().getProject(),
		// Type.VARIABLE).get(0).getName(),
		// e.getId() == 103 ? "Input Number" : "Button Input");
	}

}
