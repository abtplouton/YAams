/**
 * 
 */
package de.yaams.extensions.ress;

import java.io.File;
import java.util.HashMap;

import javax.swing.JPanel;

import de.yaams.maker.helper.FileHelper;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormTextField;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.ress.RessInfoFile;
import de.yaams.maker.programm.ress.RessourceList;

/**
 * @author abt
 * 
 */
public class RessourceXList extends RessourceList {
	private static final long serialVersionUID = 6626562093124509075L;

	protected PreviewPanel preview;
	protected JPanel left;
	protected HashMap<String, String> data;
	protected File dataPath;

	/**
	 * Create a new RessiList
	 * 
	 * @param ary
	 */
	public RessourceXList(Project project, String folder, PreviewPanel preview, JPanel left) {
		super(project, folder);

		this.preview = preview;
		this.left = left;

		addToolbarButtons();

		// exist data?
		dataPath = new File(project.getPath(), folder + File.separator + "data.xml");
		data = (HashMap<String, String>) FileHelper.loadXML(dataPath);
		if (data == null) {
			data = new HashMap<String, String>();
		}

		updateLeft();
	}

	/**
	 * 
	 */
	private void addToolbarButtons() {

	}

	/**
	 * If the user select one element
	 */
	@Override
	protected void selected() {
		config();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#configIntern()
	 */
	@Override
	protected void configIntern() {
		updateLeft();
	}

	protected void updateLeft() {
		left.removeAll();

		// is something selected?
		if (getSelectedObject() == null) {
			return;
		}

		FormBuilder f = new FormBuilder("ressX.leftpanel");
		f.setCenter(preview);
		preview.setFile(getSelectedObject().getAbsolutePath());

		// add data?
		if (data != null) {

			String key = getSelectedFile().getName();

			addFormData(f, key, I18N.t("Autor"), "author");
			addFormData(f, key, I18N.t("Quelle"), "source");
			addFormData(f, key, I18N.t("Notizen"), "note");

			f.addButton("save", YFactory.b(I18N.t("Speichern"), "disk", new AE() {

				@Override
				public void run() {
					// save it
					FileHelper.saveXML(dataPath, data);

					// remove all modi
					for (RessInfoFile r : ary) {
						if (r.getData().containsKey("isModified"))
							r.getData().remove("isModified");
					}
				}
			}));
		}

		left.add(f.getPanel(true));
		left.invalidate();
		left.revalidate();
	}

	/**
	 * @param f
	 * @param key
	 */
	private void addFormData(FormBuilder f, String key, String title, String key2) {
		final String key3 = key + "." + key2;
		f.addElement("basic." + key2,
				new FormTextField(title, data.containsKey(key3) ? data.get(key3) : "").addChangeListener(new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						data.put(key3, form.getContentAsString());
						getSelectedObject().getData().put("isModified", true);
					}
				}));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.packandgo.helper.gui.list.YSimpleList#getModified(java.lang.
	 * Object)
	 */
	@Override
	public boolean isModified(final RessInfoFile o) {
		return o.getData().containsKey("isModified") && (Boolean) o.getData().get("isModified");
	}

}
