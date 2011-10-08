/**
 * 
 */
package de.yaams.extensions.rgssproject.map;

import org.jruby.RubyObject;

import de.yaams.extensions.basemap.tiled.mapeditor.MapEditor;
import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.map.event.EventsEditorTab;
import de.yaams.extensions.rgssproject.map.rxdata.RXDataReader;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.gui.bcb.BcbBuilder;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.tabs.TabEvent;

/**
 * @author abby
 * 
 */
public class MapEditorTab extends MapEditor {

	private static final long serialVersionUID = -9200511404992594687L;
	public static final String ID = "rgssproject.mapeditor";

	protected int mid;
	protected RubyObject map;

	/**
	 * @param p
	 */
	public MapEditorTab(Project p, RubyObject map) {
		super(p);
		this.mid = RubyHelper.toInt(map.getInstanceVariable("@id"));
		this.map = map;
		try {
			loadMap(new RXDataReader().readMap(p, mid));
		} catch (Throwable t) {
			YEx.warn("Can not load Map (" + mid + "):" + map, t);
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
		EventsEditorTab.buildMapBcB(bcb, project, mid);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.tabs.YaTab#getId()
	 */
	@Override
	public String getId() {
		return getId(project, mid);
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
