/**
 * 
 */
package de.yaams.extensions.cleverness.tileset;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import de.yaams.extensions.cleverness.tileset.CTileset.TYPE;
import de.yaams.maker.helper.gui.form.FormComboBox;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.programm.project.Project;

/**
 * @author abby
 * 
 */
public class FormTileset extends FormComboBox {

	/**
	 * Create a new FormTileset
	 * 
	 * @param tileset
	 * @param index
	 */
	public FormTileset(final CTileset tileset, Project project, final int index) {
		super(Integer.toString(index), getValues(), getTitle());

		BufferedImage buff = (BufferedImage) project.getCache().get(tileset.getGraphic());

		// add display
		header.setIcon(new ImageIcon(buff.getSubimage(index % (buff.getWidth() / 32) * 32, index / (buff.getWidth() / 32) * 32, 32, 32)));

		// load it
		selectField(tileset.getTypes().containsKey(index) ? tileset.getTypes().get(index).toString() : CTileset.TYPE.AIR.toString());

		// add save
		addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				tileset.getTypes().put(index, CTileset.TYPE.valueOf(form.getContentAsString()));

			}
		});
	}

	/**
	 * @return
	 */
	protected static String[] getValues() {
		// create it
		TYPE[] value = CTileset.TYPE.values();
		String[] values = new String[value.length];

		for (int i = value.length - 1; i >= 0; i--) {
			values[i] = value[i].toString();
		}

		return values;
	}

	/**
	 * @return
	 */
	protected static String[] getTitle() {
		// create it
		TYPE[] value = CTileset.TYPE.values();
		String[] values = new String[value.length];

		for (int i = value.length - 1; i >= 0; i--) {
			values[i] = value[i].toString();
		}

		return values;
	}

}
