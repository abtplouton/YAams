/**
 * 
 */
package de.yaams.extensions.rgssproject.database.form;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jruby.RubyBasicObject;
import org.jruby.RubyObject;
import org.jruby.runtime.builtin.IRubyObject;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.ress.FormRessElement;
import de.yaams.maker.programm.ress.RessRess;

/**
 * @author abt
 * 
 */
public class FormMusicEle extends FormRessElement {

	protected JComponent subElements;
	protected String headerName;
	protected RubyObject object;

	/**
	 * Create a new FormRGSSRessElement
	 * 
	 * @param title
	 * @param project
	 * @param folder
	 * @param object
	 * @param id
	 * @param hueID
	 *            , if null, no hue support
	 */

	public FormMusicEle(String title, Project project, String folder, IRubyObject obj) {
		super("", project, folder, ((RubyBasicObject) obj).getInstanceVariable("@name").toString().length() == 0 ? null : RessRess
				.getRessFile(project, folder, ((RubyBasicObject) obj).getInstanceVariable("@name").toString().toString()).getName(),
				FormRessElement.Typ.ONLYNAME);

		headerName = title;
		this.object = (RubyObject) obj;
		if (object.getInstanceVariable("@name").toString().length() != 0) {
			selected = RessRess.getRessFile(project, folder, object.getInstanceVariable("@name").toString());
		}

		// add subelements
		FormBuilder f = new FormBuilder("music.ele");
		f.addElement("basic.vol", RubyForm.getNumber(I18N.t("Volume"), object, "@volume").setMinMax(0, 100, 1));
		f.addElement("basic.pitch", RubyForm.getNumber(I18N.t("Pitch"), object, "@pitch").setMinMax(50, 150, 1));

		f.setColumn(04);

		subElements = f.getPanel(true);
		// loadImage((String) graph.toJava(String.class));

		updateField();
	}

	/**
	 * Helpermethod to update the profil
	 */
	@Override
	protected void updateField() {
		// get string
		String s = selected == null ? null : getSelected().getName();

		// remove ending
		if (s != null) {
			if (s.lastIndexOf('.') >= 1) {
				s = s.substring(0, s.lastIndexOf('.'));
			}
		} else {
			s = "";
		}

		// set it
		if (object != null) {
			RubyHelper.setString(object.getInstanceVariable("@name"), s);

			field.setText(s);
		}
		updatePreview();
	}

	/**
	 * Get intern element, only for rendering
	 * 
	 * @return
	 */
	@Override
	protected JComponent getInternElement() {
		// build panel
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(BorderFactory.createTitledBorder(headerName));
		p.add(super.getInternElement(), BorderLayout.NORTH);
		p.add(subElements, BorderLayout.CENTER);
		return p;
	}

	/**
	 * Get image for preview
	 * 
	 * @return
	 */
	// @Override protected BufferedImage getImage() {
	// // has image?
	// if (getContentAsString() == null) {
	// return null;
	// }
	//
	// // load it
	// BufferedImage b = RessRess.getGraphic(project, folder,
	// getContentAsString());
	//
	// if (b == null) {
	// return (BufferedImage) IconCache.getImage("error", 32);
	// }
	//
	// // special type?
	// if (folder.equals(CHARACTER)) {
	// return b.getSubimage(0, 0, b.getWidth() / 4, b.getHeight() / 4);
	// }
	//
	// // special type?
	// if (folder.equals(TILESET)) {
	// BufferedImage i = new BufferedImage(64, 64,
	// BufferedImage.TYPE_4BYTE_ABGR);
	// i.getGraphics().drawImage(b.getSubimage(0, 0, b.getHeight() < 256 ?
	// b.getHeight() : 256, 256), 0, 0, 64,
	// 64, null);
	// return i;
	// }
	//
	// return b;
	// }
	/**
	 * En/Disable
	 * 
	 * @param value
	 * @return
	 */
	@Override
	public FormElement setEnabled(boolean value) {
		subElements.setEnabled(value);
		return super.setEnabled(value);
	}
}
