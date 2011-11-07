/**
 * 
 */
package de.yaams.extensions.diamant.tileset;

import java.awt.image.BufferedImage;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormInfo;
import de.yaams.maker.helper.gui.form.FormTextField;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.programm.project.objects.simple.SimpleObjectManagement;
import de.yaams.maker.programm.ress.FormRessElement;
import de.yaams.maker.programm.ress.FormRessElement.Typ;
import de.yaams.maker.programm.ress.RessRess;

/**
 * @author abby
 * 
 */
public class TilesetObjManager extends SimpleObjectManagement {

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public TilesetObjManager() {
		super(I18N.t("Tileset"), I18N.t("Verwaltet alle Tilesets"), "tileset", "tileset.xml", "tileset");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectManager#getGroup()
	 */
	@Override
	public String getGroup() {
		return "Tileset";
	}

	@Override
	public BasisListElement createNewObject() {
		return new CTileset();
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
		if (!(selectedObject instanceof CTileset)) {
			throw new IllegalArgumentException(selectedObject + " is not a Tileset");
		}
		final CTileset t = (CTileset) selectedObject;

		f.getHeader("basic").setTitle(I18N.t("Allgemeines")).setIcon("info");

		// add basic
		f.addElement("basic.name", new FormTextField(I18N.t("Name"), t.getTitle()).addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				t.setTitle(form.getContentAsString());

			}
		}));

		// add graphic
		f.addElement(
				"basic.graphic",
				new FormRessElement(I18N.t("Grafik"), project, "Ressources", t.getGraphic(), Typ.ONLYNAME) {
				}.setInfoTxt(I18N.t("Nach dem Auswählen einer neuen Grafik, muss es neugeladen werden.")).addChangeListener(
						new FormElementChangeListener() {

							@Override
							public void stateChanged(FormElement form) {
								t.setGraphic(form.getContentAsString());

							}
						}));

		// add tileset
		f.addHeader("tileset", new FormHeader("Tileset", "tileset"));

		BufferedImage graphic = RessRess.getGraphic(project, "Ressources", t.getGraphic());

		// load tileset
		if (graphic == null) {
			f.addElement("tileset.error", new FormInfo("", I18N.t("Lade zuerst eine Grafik und dann diesen Abschnitt neu.")));
			return;
		}

		// set basics
		f.getHeader("tileset").setColumn(32);
		project.getCache().put(t.getGraphic(), graphic);

		// add tiles
		for (int i = 0, l = graphic.getWidth() / 32 * (graphic.getHeight() / 32); i < l; i++) {
			f.addElement("tileset." + i, new FormTileset(t, project, i).setSorting(i));
		}
	}

}
