/**
 * 
 */
package de.yaams.extensions.rgssproject.database.form;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jruby.RubyObject;
import org.jruby.RubyString;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormNumberSpinner;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.ress.FormRessElement;
import de.yaams.maker.programm.ress.RessRess;

/**
 * @author abt
 * 
 */
public class FormGraphEle extends FormRessElement {

	protected RubyString graph;
	protected Object hueID, hueHolder;
	protected JPanel hue;

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

	public FormGraphEle(String title, Project project, String folder, RubyObject obj, String ID, String hueId) {
		this(title, project, folder, (RubyString) obj.getInstanceVariable(ID), obj, hueId);
	}

	/**
	 * Create a new FormRGSSRessElement
	 * 
	 * @param title
	 * @param project
	 * @param folder
	 * @param object
	 * @param id
	 * @param hueId
	 *            , if null, no hue support
	 */
	public FormGraphEle(String title, Project project, String folder, final RubyString graph, Object hueHolder, Object hueID) {
		super(title, project, folder, RubyHelper.toInt(graph.length()) == 0 ? null : RessRess
				.getRessFile(project, folder, graph.toString()).getName(), FormRessElement.Typ.ONLYNAME);

		this.graph = graph;
		this.hueHolder = hueHolder;
		this.hueID = hueID;
		if (RubyHelper.toInt(graph.length()) != 0) {
			selected = RessRess.getRessFile(project, folder, graph.toString());
		}

		// add hue?
		if (hueID != null) {
			FormNumberSpinner fns = RubyForm.getNumber(I18N.t("Hue"), hueHolder, hueID).setMinMax(0, 359, 1);

			// build addon
			hue = new JPanel(new BorderLayout());
			hue.add(BorderLayout.WEST, fns.getHeader());
			hue.add(BorderLayout.CENTER, fns.getElement());
		}
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
		if (graph != null) {
			RubyHelper.setString(graph, s);

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
		if (hue != null) {
			// build panel
			JPanel p = new JPanel(new BorderLayout());
			p.add(super.getInternElement(), BorderLayout.CENTER);
			p.add(hue, BorderLayout.EAST);
			return p;
		} else {
			return super.getInternElement();
		}
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
		if (hue != null) {
			hue.getComponent(1).setEnabled(value);
		}
		return super.setEnabled(value);
	}
}
