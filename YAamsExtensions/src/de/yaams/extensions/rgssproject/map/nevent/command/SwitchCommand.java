/**
 * 
 */
package de.yaams.extensions.rgssproject.map.nevent.command;

import javax.swing.JSpinner;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.extensions.rgssproject.map.form.FormSwitchVarSelector;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCode;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCommand;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.FormComboBox;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormHelper;
import de.yaams.maker.helper.gui.form.core.FormBuilder;

/**
 * @author abt
 * 
 */
public class SwitchCommand extends EventCommand {

	/**
	 * Create a new LabelCommand
	 */
	public SwitchCommand() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.extensions.map.nevent.core.EventCommand#getTitle(de.yaams.extensions
	 * .map.nevent.core.EventCode)
	 */
	@Override
	protected String getInternTitle(EventCode e) {
		// collect
		int s0 = RubyHelper.toInt(e.getParameters().get(0)), s1 = RubyHelper.toInt(e.getParameters().get(1));
		String r = RubyHelper.toInt(e.getParameters().get(2)) == 1 ? "true" : "false";

		// set
		if (s0 == s1) {
			return I18N.t("Switch {0} = {1}", s0, r);
		} else {
			return I18N.t("Switch {0}-{1} = {2}", s0, s1, r);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getIcon()
	 */
	@Override
	public String getIcon() {
		return "switch";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getStartParameter()
	 */
	@Override
	public String getStartParameter() {
		return "1,1,0";
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
		return I18N.t("Switch");
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
		// add type
		final FormComboBox c = new FormComboBox(I18N.t("Type"), new String[] { "0", "1" }, new String[] { "Single", "Multi" });
		c.selectField(RubyHelper.toInt(e.getParameters().get(0)) == RubyHelper.toInt(e.getParameters().get(1)) ? "0" : "1");

		f.addElement("basic.type", c.setSorting(-1));

		final FormSwitchVarSelector s2 = new FormSwitchVarSelector(e.getYecl().getProject(), I18N.t("bis"), e.getParameters(), 1,
				Type.SWITCH);

		// add switch
		f.addElement("basic.s1", new FormSwitchVarSelector(e.getYecl().getProject(), I18N.t("Setze"), e.getParameters(), 0, Type.SWITCH)
				.addChangeListener(new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						if (c.getContentAsString().equals("0")) {
							// RGSSHelper.setNum(e.getParameters(), 0,
							// c.getContentAsString());
							RubyHelper.setNum(e.getParameters(), 1, form.getContentAsString());
							((JSpinner) s2.getElement()).setValue(Integer.valueOf(form.getContentAsString()));
						}
					}
				}));

		// add 2. switch
		f.addElement("basic.s2", FormHelper.setEnabeldWhenRightElementSelect(c, s2, "1"));

		c.addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				if (c.getContentAsString().equals("0")) {
					RubyHelper.setNum(e.getParameters(), 1, (long) RubyHelper.toInt(e.getParameters().get(0)));
				}

			}
		});

		// add value
		f.addElement("basic.value",
				RubyForm.getComboBoxNum(I18N.t("="), new String[] { "0", "1" }, new String[] { "false", "true" }, e.getParameters(), 2));

		c.informListeners();
	}
}
