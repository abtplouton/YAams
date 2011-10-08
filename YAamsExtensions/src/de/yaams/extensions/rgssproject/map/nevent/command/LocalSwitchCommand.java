/**
 * 
 */
package de.yaams.extensions.rgssproject.map.nevent.command;

import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCode;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCommand;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.core.FormBuilder;

/**
 * @author abt
 * 
 */
public class LocalSwitchCommand extends EventCommand {

	protected static final String[] lswitch = { "A", "B", "C", "D" };

	/**
	 * Create a new LabelCommand
	 */
	public LocalSwitchCommand() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.extensions.map.nevent.core.EventCommand#getTitle(de.yaams.extensions
	 * .map.nevent.core.EventCode)
	 */
	@Override
	protected String getInternTitle(EventCode e) {
		return I18N.t("Local Switch {0} = {1}", e.getParameters().get(0).toString(),
				e.getParameters().get(1).toString().equals("1") ? "true" : "false");
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
		return "1,0,\"A\",0";
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
		return I18N.t("Local Switch");
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

		f.getHeader("basic").setColumn(4);

		// add type
		f.addElement("basic.localswitch", RubyForm.getComboBox(I18N.t(""), lswitch, lswitch, e.getParameters().get(0)));

		// add value
		f.addElement("basic.value",
				RubyForm.getComboBoxNum(I18N.t("="), new String[] { "0", "1" }, new String[] { "false", "true" }, e.getParameters(), 1));

	}

}
