/**
 * 
 */
package de.yaams.extensions.rgssproject.database.form;

import java.awt.Color;

import org.jruby.runtime.builtin.IRubyObject;

import com.bric.swing.ColorPicker;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.form.FormButton;
import de.yaams.maker.programm.YaFrame;

/**
 * @author abby
 * 
 */
public class FormColor extends FormButton {

	/**
	 * @param title
	 * @param icon
	 * @param e
	 */
	public FormColor(String title, final IRubyObject color) {
		super(title, null, new AE() {

			@Override
			public void run() {

				// create java color
				Color col = new Color(RubyHelper.toInt(color, "@red"), RubyHelper.toInt(color, "@green"), RubyHelper.toInt(color, "@blue"),
						RubyHelper.toInt(color, "@alpha"));

				// show it
				col = ColorPicker.showDialog(YaFrame.get(), col);

				// set it
				RubyHelper.setNum(col, "@red", col.getRed());
				RubyHelper.setNum(col, "@green", col.getGreen());
				RubyHelper.setNum(col, "@blue", col.getBlue());
				RubyHelper.setNum(col, "@alpha", col.getAlpha());

			}
		});
	}

}
