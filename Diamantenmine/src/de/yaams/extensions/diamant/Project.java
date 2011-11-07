/**
 * 
 */
package de.yaams.extensions.diamant;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.Preferences;

import de.yaams.extensions.diamant.data.Level;
import de.yaams.extensions.diamant.graph.PWindow;
import de.yaams.extensions.diamant.graph.scene.GameScene;
import de.yaams.extensions.diamant.graph.scene.MessageScene;
import de.yaams.extensions.diamant.helper.FileHelper;
import de.yaams.extensions.diamant.maps.CMapInfo;
import de.yaams.extensions.diamant.tileset.BasisListElement;
import de.yaams.extensions.diamant.tileset.CTileset;

/**
 * @author abby
 * 
 */
public class Project {

	public static Project project;
	public static final String TUT = "tutorial", GAME = "map";

	protected File file;
	protected String title;

	protected ArrayList<CTileset> tileset;
	protected HashMap<String, ArrayList<CMapInfo>> maps;

	protected Preferences prefs;

	/**
	 * @param file
	 * @throws FileNotFoundException
	 */
	public Project(File file) throws FileNotFoundException {
		super();
		this.file = file;
		title = "Diamantenmine";

		// load tilesets
		tileset = (ArrayList<CTileset>) FileHelper.loadXML(new File(file, "tileset.xml"));
		if (tileset == null) {
			throw new FileNotFoundException("Can not read " + new File(file, "tileset.xml"));
		}

		maps = new HashMap<String, ArrayList<CMapInfo>>();

		// load tut maps
		maps.put(TUT, (ArrayList<CMapInfo>) FileHelper.loadXML(new File(file, TUT + ".xml")));
		if (maps.get(TUT) == null) {
			throw new FileNotFoundException("Can not read " + new File(file, TUT + ".xml"));
		}

		// load maps
		maps.put(GAME, (ArrayList<CMapInfo>) FileHelper.loadXML(new File(file, GAME + ".xml")));
		if (maps.get(GAME) == null) {
			throw new FileNotFoundException("Can not read " + new File(file, GAME + ".xml"));
		}

		prefs = Preferences.userNodeForPackage(RunDiamant.class);

	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get the name
	 * 
	 * @return
	 */
	public static String getName() {
		if (project == null) {
			return "Diament";
		}
		return project.getTitle();
	}

	/**
	 * @return the tileset
	 */
	public ArrayList<CTileset> getTileset() {
		return tileset;
	}

	/**
	 * @return the maps
	 */
	public HashMap<String, ArrayList<CMapInfo>> getMaps() {
		return maps;
	}

	/**
	 * Helpermethod to set the map
	 * 
	 * @param maptyp
	 * @param level
	 */
	public void setMapLevel(String maptyp, int level) {
		prefs.putInt(title + "." + maptyp, level);
	}

	/**
	 * Helpermethod to get the map
	 * 
	 * @param maptyp
	 * @param level
	 */
	public int getMapLevel(String maptyp) {
		return prefs.getInt(title + "." + maptyp, 0);
	}

	/**
	 * Start a new level
	 * 
	 * @param maptyp
	 * @param id
	 */
	public void startLevel(String maptyp, int id) {
		BasisListElement e = Project.project.getMaps().get(maptyp).get(id);
		PWindow.window.setActScene(new MessageScene(e.getTitle(), e.getDesc(), new GameScene(new Level(maptyp, id))));
	}
}
