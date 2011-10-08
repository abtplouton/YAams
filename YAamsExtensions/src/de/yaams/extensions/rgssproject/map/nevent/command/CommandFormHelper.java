/**
 * 
 */
package de.yaams.extensions.rgssproject.map.nevent.command;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.extensions.rgssproject.map.form.FormSwitchVarSelector;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCode;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.FormComboBox;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormHelper;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;

/**
 * @author abt
 * 
 */
public class CommandFormHelper {

	/**
	 * Helpermethod to build a id chancer
	 * 
	 * @param e
	 * @param title
	 * @param ids
	 * @param titles
	 * @return
	 */
	public static FormComboBox buildCodeIdChancer(final EventCode e, String title, String[] ids, String[] titles) {
		return (FormComboBox) new FormComboBox(title, ids, titles).selectField(Integer.toString(e.getId())).addChangeListener(
				new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						e.setId(Integer.valueOf(form.getContentAsString()));

					}
				});
	}

	/**
	 * Helpermethod to build a id chancer
	 * 
	 * @param e
	 * @param title
	 * @param ids
	 * @param titles
	 * @return
	 */
	public static void addFrameSupport(FormBuilder f, final EventCode e, int startpam) {

		// add it
		f.addElement("basic.frames", RubyForm.getNumber(I18N.t("Frames"), e.getParameters(), startpam).setMinMax(0, 10000, 1));

	}

	/**
	 * Helpermethod to get for addFrameSupport the text
	 * 
	 * @param e
	 * @param title
	 * @param ids
	 * @param titles
	 * @return
	 */
	public static String getFrameText(final EventCode e, int startpam) {
		if (RubyHelper.toInt(e.getParameters().get(startpam)) == 0) {
			return "";
		}
		return I18N.t(" ({0}F)", RubyHelper.toInt(e.getParameters().get(startpam)));

	}

	/**
	 * Helpermethod to build the value selector with value or variable
	 * 
	 * @param e
	 * @param title
	 * @param ids
	 * @param titles
	 * @return
	 */
	public static void addOperatorSupport(FormBuilder f, final EventCode e, int startpam) {

		// add header
		f.addHeader("operator", new FormHeader(I18N.t("Operation"), "del_add"));

		// add plus/neg
		addOperatorSupport(f, e, startpam, "operator.", null, null);
	}

	/**
	 * Helpermethod to build the value selector with value or variable
	 * 
	 * @param e
	 * @param title
	 * @param ids
	 * @param titles
	 * @return
	 */
	public static void addOperatorSupport(FormBuilder f, final EventCode e, int startpam, String baseid, FormElement source,
			String selectToEnable) {

		// add plus/neg
		f.addElement(baseid + "op_neg", RubyForm.getComboBoxNum(I18N.t("Operation"), new String[] { "0", "1" }, new String[] { "+", "-" },
				e.getParameters(), startpam));

		// add amount
		FormComboBox c = RubyForm.getComboBoxNum(I18N.t("Amount"), new String[] { "0", "1" }, new String[] { "Wert", "Variable" },
				e.getParameters(), startpam + 1);

		f.addElement(baseid + "op_amount", source != null ? FormHelper.setEnabeldWhenRightElementSelect(source, c, selectToEnable) : c);
		f.addElement(baseid + "op_amountvalue",
				FormHelper.setEnabeldWhenRightElementSelect(c, RubyForm.getNumber(I18N.t("Wert"), e.getParameters(), startpam + 2), "0"));
		f.addElement(baseid + "op_amountvariable", FormHelper.setEnabeldWhenRightElementSelect(c, new FormSwitchVarSelector(e.getYecl()
				.getProject(), I18N.t("Variable"), e.getParameters(), startpam + 2, Type.VARIABLE), "1"));

		c.informListeners();
		if (source != null) {
			source.informListeners();
		}
	}

	/**
	 * Helpermethod to the the text info for an operator
	 * 
	 * @param f
	 * @param e
	 * @param startpam
	 * @return
	 */
	public static String getOperatorText(final EventCode e, int startpam) {
		// collect
		StringBuffer s = new StringBuffer("");
		s.append(RubyHelper.toInt(e.getParameters().get(startpam)) == 0 ? "+" : "-");
		s.append(RubyHelper.toInt(e.getParameters().get(startpam + 1)) == 0 ? RubyHelper.toInt(e.getParameters().get(startpam + 2))
				: RGSS1Helper.get(e.getYecl().getProject(), Type.VARIABLE).get(RubyHelper.toInt(e.getParameters().get(startpam + 2)))
						.getName());

		return s.toString();
	}
}
