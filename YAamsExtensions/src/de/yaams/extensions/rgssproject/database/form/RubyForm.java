/**
 * 
 */
package de.yaams.extensions.rgssproject.database.form;

import org.jruby.RubyBoolean;
import org.jruby.RubyObject;
import org.jruby.runtime.builtin.IRubyObject;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.database.JavaTable;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.FormCheckbox;
import de.yaams.maker.helper.gui.form.FormComboBox;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormInfo;
import de.yaams.maker.helper.gui.form.FormNumberSpinner;
import de.yaams.maker.helper.gui.form.FormTextField;

/**
 * @author abt
 * 
 */
public class RubyForm {

	/**
	 * Add an String
	 * 
	 * @param name
	 * @param i
	 * @return
	 */
	public static FormTextField getString(final String name, final String id, final RubyObject object) {
		// return (FormTextField) new FormTextField(name,
		// object.getInstanceVariable(id).asJavaString())
		// .addChangeListener(new FormElementChangeListener() {
		//
		// @Override public void stateChanged(FormElement form) {
		// object.setInstanceVariable(id,
		// RubyString.newString(object.getRuntime(),
		// form.getContentAsString()));
		//
		// }
		// });
		return getString(name, object.getInstanceVariable(id));
	}

	/**
	 * Add an String
	 * 
	 * @param name
	 * @param i
	 * @return
	 */
	public static FormTextField getString(final String name, final IRubyObject irb) {
		return (FormTextField) new FormTextField(name, irb.asJavaString()).addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				RubyHelper.setString(irb, form.getContentAsString());
			}
		});
	}

	/**
	 * Add an String
	 * 
	 * @param name
	 * @param i
	 * @return
	 */
	public static FormNumberSpinner getNumber(final String name, final Object holder, final Object index) {
		return (FormNumberSpinner) new FormNumberSpinner(name, RubyHelper.toInt(holder, index))
				.addChangeListener(new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						RubyHelper.setNum(holder, index, form.getContentAsString());

					}
				});
	}

	/**
	 * Add an String
	 * 
	 * @param name
	 * @param i
	 * @return
	 */
	public static FormCheckbox getBooleanFlag(final String name, final RubyObject holder, final int index, final int flag) {
		final JavaTable table = new JavaTable(holder);

		return (FormCheckbox) new FormCheckbox(name, (table.get(index, 0, 0) & flag) == flag)
				.addChangeListener(new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						if (Boolean.parseBoolean(form.getContentAsString())) {
							table.set(index, 0, 0, table.get(index, 0, 0) | flag);
						} else {
							table.set(index, 0, 0, table.get(index, 0, 0));
						}

					}
				});
	}

	/**
	 * Add a boolean
	 * 
	 * @param name
	 * @param holder
	 * @param index
	 * @return
	 */
	public static FormCheckbox getBoolean(final String name, final Object holder, final Object index) {
		return (FormCheckbox) new FormCheckbox(name, ((RubyBoolean) RubyHelper.getObj(holder, index)).isTrue())
				.addChangeListener(new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						RubyHelper.setObj(holder, index,
								new RubyBoolean(RubyHelper.getRunTime(holder, index), Boolean.parseBoolean(form.getContentAsString())));

					}
				});

	}

	/**
	 * Build a ForumCheckbox for an int (values: 0/1 )
	 * 
	 * @param name
	 * @param object
	 * @return
	 */
	public static FormCheckbox getBooleanAsInt(final String name, final Object holder, final Object index) {
		return (FormCheckbox) new FormCheckbox(name, RubyHelper.toInt(holder, index) == 1)
				.addChangeListener(new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						RubyHelper.setNum(holder, index, Boolean.parseBoolean(form.getContentAsString()) ? 1 : 0);

					}
				});

	}

	/**
	 * Add an String
	 * 
	 * @param name
	 * @param i
	 * @return
	 */
	public static FormElement getError(final String name, IRubyObject irb) {
		String mess = irb.inspect().toString();
		return new FormInfo(name, "@" + irb.getMetaClass() + " " + (mess.length() > 100 ? mess.substring(0, 100) + "..." : mess))
				.setInfoTxt(I18N.t("Unbekannte Klasse"), "error");
	}

	/**
	 * Add an String
	 * 
	 * @param name
	 * @param i
	 * @return
	 */
	public static FormElement getError(final String name, final String id, final RubyObject object) {
		return getError(name, object.getInstanceVariable(id));
	}

	/**
	 * Add an String
	 * 
	 * @param name
	 * @param i
	 * @return
	 */
	public static FormComboBox getComboBoxNum(final String name, final String[] values, final String[] titles, final Object holder,
			final Object index) {

		return (FormComboBox) new FormComboBox(name, values, titles).selectField(RubyHelper.toString(holder, index)).addChangeListener(
				new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						// set
						RubyHelper.setNum(holder, index, form.getContentAsString());

					}
				});
	}

	/**
	 * Add an String
	 * 
	 * @param name
	 * @param i
	 * @return
	 */
	public static FormComboBox getComboBox(final String name, final String[] values, final String[] titles, final IRubyObject irb) {

		return (FormComboBox) new FormComboBox(name, values, titles).selectField(irb.toString()).addChangeListener(
				new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						// set
						RubyHelper.setString(irb, form.getContentAsString());

					}
				});
	}
}
