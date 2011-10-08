/**
 * 
 */
package de.yaams.extensions.rgssproject.map.nevent.command;

import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCode;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCommand;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.FormInfo;
import de.yaams.maker.helper.gui.form.core.FormBuilder;

/**
 * @author abt
 * 
 */
public class ErrorCommand extends EventCommand {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getIcon()
	 */
	@Override
	public String getIcon() {
		return "error";
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
		return I18N.t("Error");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getGroup()
	 */
	@Override
	public String getGroup() {
		return I18N.t("Sonstiges");
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
		f.addElement("basic.code", RubyForm.getError(I18N.t("ID"), "@code", e.getObject()));
		f.addElement("basic.para", RubyForm.getError(I18N.t("Parameter"), "@parameters", e.getObject()));
		f.addElement("basic.indent", RubyForm.getError(I18N.t("Indent"), "@indent", e.getObject()));
		f.addElement("basic.znotsupported", new FormInfo("", I18N.t("Dieser Befehl wird noch nicht unterstützt.")).setIcon("error", 32));
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
		return e.getId() + ": " + e.getObject().getInstanceVariable("@parameters").inspect().toString();
	}

}
