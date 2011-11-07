/**
 * 
 */
package de.yaams.extensions.diamant.maps;

import java.io.File;

import de.yaams.extensions.basemap.tiled.mapeditor.MapEditor;
import de.yaams.maker.helper.FileHelper;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.gui.bcb.BcbBuilder;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.ProjectTabEvent;
import de.yaams.maker.programm.tabs.TabEvent;

/**
 * @author abby
 * 
 */
public class CMapEditorTab extends MapEditor {

	private static final long serialVersionUID = -9200511404992594687L;
	public static final String ID = "cleverness.mapeditor";

	protected int id;
	protected String typ;

	/**
	 * @param p
	 */
	public CMapEditorTab(Project p, String typ, int id) {
		super(p);

		// create folder
		FileHelper.mkdirs(new File(p.getPath(), typ));

		this.typ = typ;
		this.id = id;
		try {
			loadMap(new CMapReader().readMap(p, new File(new File(p.getPath(), typ), id + ".cmap"), id, typ));
		} catch (Throwable t) {
			YEx.warn("Can not load Map (" + id + "):" + typ, t);
		}

		// build gui
		buildGui();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.tabs.YaTab#buildBcb(de.yaams.maker.helper.gui
	 * .bcb.BcbBuilder)
	 */
	@Override
	protected void buildBcb(BcbBuilder bcb) {
		// add map
		ProjectTabEvent.buildBcb(bcb, project, ID);

		bcb.addSeperator();

		// add all maps
		for (int i = 0, l = project.getObjects().get(typ).getObjects().size(); i < l; i++) {
			bcb.addElement(project.getObjects().get(typ).getObjects().get(i).getTitle(), "map", getId(project, i), i == id);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.tabs.YaTab#getId()
	 */
	@Override
	public String getId() {
		return getId(project, id);
	}

	/**
	 * Get it
	 * 
	 * @param p
	 * @param map
	 * @param event
	 * @return
	 */
	public static String getId(Project p, int map) {
		return TabEvent.buildParameter(ID, p, null, "map", Integer.toString(map));
	}

}
