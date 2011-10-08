/**
 * 
 */
package de.yaams.extensions.cleverness.maps;

import java.util.ArrayList;

import javax.swing.JComboBox;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.form.FormComboBox;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormInfo;
import de.yaams.maker.helper.gui.form.FormTextArea;
import de.yaams.maker.helper.gui.form.FormTextField;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.programm.YaFrame;
import de.yaams.maker.programm.project.objects.simple.SimpleObjectManagement;
import de.yaams.maker.programm.tabs.TabEvent;

/**
 * @author abby
 * 
 */
public class MapsObjManager extends SimpleObjectManagement {

	protected String type;

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public MapsObjManager(String title, String type) {
		super(title, I18N.t("Verwaltet alle {0}", title), "map", type + ".xml", type);
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectManager#getGroup()
	 */
	@Override
	public String getGroup() {
		return I18N.t("Karte");
	}

	@Override
	public BasisListElement createNewObject() {
		return new CMapInfo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectTab#buildInternContent
	 * (de.yaams.maker.helper.gui.list.BasisListElement)
	 */
	@Override
	public void buildInternContent(BasisListElement selectedObject, FormBuilder f) {
		// right typ?
		if (!(selectedObject instanceof CMapInfo)) {
			throw new IllegalArgumentException(selectedObject + " is not a Map");
		}
		final CMapInfo q = (CMapInfo) selectedObject;

		// add basic
		f.addElement("basic.name", new FormTextField(I18N.t("Name"), q.getTitle()).addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				q.setTitle(form.getContentAsString());

			}
		}));

		// add basic
		f.addElement("basic.desc", new FormTextArea(I18N.t("Hilfetext"), q.getDesc()).addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				q.setDesc(form.getContentAsString());

			}
		}).setSorting(1));

		// add tileset
		ArrayList<BasisListElement> ble = project.getObjects().get("tileset").getObjects();
		int size = ble.size();
		String[] ary = new String[size];
		// convert
		for (int i = 0; i < size; i++) {
			ary[i] = ble.get(i).toString();
		}

		f.addElement("basic.tileset", size > 0 ? new FormComboBox(I18N.t("Tileset"), ary).selectField(ble.get(q.getTileset()).getTitle())
				.addChangeListener(new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						JComboBox j = (JComboBox) form.getElement();
						q.setTileset(j.getSelectedIndex());

					}
				}) : new FormInfo(I18N.t("Tileset"), I18N.t("Erstelle zuerst Tilesets")));

		// add button
		if (size > 0) {
			f.addButton("map", YFactory.b(I18N.t("Karte editieren"), "map_edit", new AE() {

				@Override
				public void run() {
					YaFrame.open(TabEvent.buildParameter(CMapEditorTab.ID, project, null, "typ", type, "map",
							Integer.toString(objects.indexOf(q))));

				}
			}, 32));
		}
	}

}
