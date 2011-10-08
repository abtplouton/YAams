/**
 * 
 */
package de.yaams.extensions.rgssproject.database.form;

import org.jruby.RubyArray;
import org.jruby.RubyObject;
import org.jruby.runtime.builtin.IRubyObject;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormNumberSpinner;

/**
 * @author abby
 * 
 */
public class FormTable extends FormNumberSpinner {

	protected String title;
	protected RubyObject table;
	protected int x, y, pos;

	/**
	 * 
	 * @param title
	 * @param table
	 * @param X
	 *            , cellID
	 * @param Y
	 *            , cellID
	 */
	public FormTable(String title, IRubyObject table, int x, int y) {
		super(title, 0);

		if (!(table instanceof RubyObject)) {
			throw new IllegalArgumentException("table is not instance of RubyObject");
		}
		this.table = (RubyObject) table;
		this.x = x;
		this.y = y;
		// calculate the pos of the id
		pos = x + y * RubyHelper.toInt(this.table.getInstanceVariable("@xsize"));

		// get value
		RubyArray ary = (RubyArray) this.table.getInstanceVariable("@data");

		field.setValue(RubyHelper.toInt(ary, pos));

		// set value
		addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				// set value
				RubyArray ary = (RubyArray) FormTable.this.table.getInstanceVariable("@data");
				RubyHelper.setNum(ary, pos, Long.valueOf(getContentAsString()));
			}
		});
	}
}
