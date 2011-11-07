/**
 * 
 */
package de.yaams.extensions.diamant.tileset;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import de.yaams.extensions.diamant.tileset.CTileset.TYPE;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.YDialog;
import de.yaams.maker.helper.gui.form.FormButton;
import de.yaams.maker.helper.gui.form.FormCheckbox;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.programm.project.Project;

/**
 * @author abby
 * 
 */
public class FormTileset extends FormButton {

	protected CTileset tileset;
	protected int index;

	/**
	 * Create a new FormTileset
	 * 
	 * @param tileset
	 * @param index
	 */
	public FormTileset(final CTileset tileset, Project project, final int index) {
		super("", null, new AE() {

			@Override
			public void run() {
				// build form
				FormBuilder f = new FormBuilder("diamant.tile.detail");

				// add all
				for (TYPE t : CTileset.TYPE.values()) {
					final TYPE t2 = t;
					f.addElement("basic." + t,
							new FormCheckbox(t.toString(), tileset.getTypes(index, t)).addChangeListener(new FormElementChangeListener() {

								@Override
								public void stateChanged(FormElement form) {
									// save it
									tileset.getTypes(index).put(t2, Boolean.valueOf(form.getContentAsString()));
								}
							}));
				}

				YDialog.showForm(I18N.t("Konfigurie"), "opts_tileset", f);

			}
		});

		// add vars
		this.tileset = tileset;
		this.index = index;

		BufferedImage buff = (BufferedImage) project.getCache().get(tileset.getGraphic());

		// add display
		button.setIcon(new ImageIcon(buff.getSubimage(index % (buff.getWidth() / 32) * 32, index / (buff.getWidth() / 32) * 32, 32, 32)));
	}
}
