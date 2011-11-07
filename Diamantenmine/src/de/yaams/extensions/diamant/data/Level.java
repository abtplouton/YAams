/**
 * 
 */
package de.yaams.extensions.diamant.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import de.yaams.extensions.diamant.Project;
import de.yaams.extensions.diamant.data.event.BaseEvent;
import de.yaams.extensions.diamant.data.event.LoadEvent;
import de.yaams.extensions.diamant.data.event.PlayerEvent;
import de.yaams.extensions.diamant.graph.PWindow;
import de.yaams.extensions.diamant.graph.scene.MessageScene;
import de.yaams.extensions.diamant.graph.scene.menu.MenuMainScene;
import de.yaams.extensions.diamant.helper.FileHelper;
import de.yaams.extensions.diamant.maps.CMap;
import de.yaams.extensions.diamant.tileset.CTileset;
import de.yaams.extensions.diamant.tileset.CTileset.TYPE;

/**
 * @author abby
 * 
 */
public class Level {

	protected ArrayList<ArrayList<ArrayList<Integer>>> levelTiled;
	protected HashMap<String, TYPE> levelTyp;
	protected ArrayList<BaseEvent> levelEvent;
	protected int width, height;

	protected int coin, coinMax;
	protected String mess;
	protected CMap map;
	protected int id;
	protected String maptyp, title;
	protected CTileset tileset;

	/**
	 * 
	 */
	public Level(String maptyp, int id) {
		levelEvent = new ArrayList<BaseEvent>();
		setLevelSize(20, 15);
		mess = Project.project.getMaps().get(maptyp).get(id).getDesc();

		this.maptyp = maptyp;
		this.id = id;

		map = (CMap) FileHelper.loadXML(new File(new File(Project.project.getFile(), maptyp), id + ".cmap"));
		tileset = Project.project.getTileset().get(Project.project.getMaps().get(maptyp).get(id).getTileset());
		title = Project.project.getMaps().get(maptyp).get(id).getTitle();

		// build level
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				for (int z = 0; z < map.getLayer(); z++) {
					int tid = map.get(x, y, z);
					// is empty?
					if (tid == -1) {
					} else
					// add player
					if (tileset.getTypes(tid, TYPE.PLAYER)) {
						addEvent(new PlayerEvent(x, y, tileset, tid));
						// add map
					} else if (tileset.getTypes(tid, TYPE.AIR)) {
						setLevelTile(x, y, TYPE.AIR, tid);
					} else if (tileset.getTypes(tid, TYPE.BLOCK)) {
						setLevelTile(x, y, TYPE.BLOCK, tid);
					} else {
						// add other
						addEvent(new LoadEvent(x, y, tileset, tid));
						if (tileset.getTypes(tid, TYPE.COIN)) {
							setCoinMax(getCoinMax() + 1);
						}
					}
				}
			}
		}
	}

	/**
	 * Add a new event
	 * 
	 * @param event
	 */
	protected void addEvent(BaseEvent event) {
		levelEvent.add(event);
	}

	/**
	 * Set the size of this level
	 * 
	 * @param w
	 * @param h
	 */
	protected void setLevelSize(int w, int h) {
		width = w;
		height = h;

		// build it
		levelTiled = new ArrayList<ArrayList<ArrayList<Integer>>>();
		levelTyp = new HashMap<String, TYPE>();

		// run over
		for (int i = 0; i < w; i++) {
			levelTiled.add(new ArrayList<ArrayList<Integer>>());
			for (int j = 0; j < h; j++) {
				levelTiled.get(i).add(new ArrayList<Integer>());
			}
		}
	}

	/**
	 * Set one tile
	 * 
	 * @param x
	 * @param y
	 * @param typ
	 * @param tile
	 */
	public void setLevelTile(int x, int y, TYPE typ, int... tile) {
		for (int i : tile) {
			levelTiled.get(x).get(y).add(i);
		}
		levelTyp.put(x + "x" + y, typ);

	}

	/**
	 * Set more tiles
	 * 
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @param typ
	 * @param tile
	 */
	public void setLevelTile(int x1, int x2, int y1, int y2, TYPE typ, int... tile) {

		// set it
		for (int i = x1; i <= x2; i++) {
			for (int j = y1; j <= y2; j++) {
				setLevelTile(i, j, typ, tile);
			}
		}

	}

	/**
	 * @return the levelTiled
	 */
	public ArrayList<ArrayList<ArrayList<Integer>>> getLevelTiled() {
		return levelTiled;
	}

	/**
	 * @return the levelEvent
	 */
	public ArrayList<BaseEvent> getLevelEvent() {
		return levelEvent;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return the levelTyp
	 */
	public HashMap<String, TYPE> getLevelTyp() {
		return levelTyp;
	}

	/**
	 * @return the coin
	 */
	public int getCoin() {
		return coin;
	}

	/**
	 * @return the coinMax
	 */
	public int getCoinMax() {
		return coinMax;
	}

	/**
	 * @param coinMax
	 *            the coinMax to set
	 */
	public void setCoinMax(int coinMax) {
		this.coinMax = coinMax;
	}

	/**
	 * This Method will be call, if the player win the game
	 */
	public void win() {

		// exist next level?
		if (Project.project.getMaps().get(maptyp).size() > id + 1) {
			Project.project.startLevel(maptyp, id + 1);

			// save it
			if (Project.project.getMapLevel(maptyp) < id + 1) {
				Project.project.setMapLevel(maptyp, id + 1);
			}
		} else {
			PWindow.window.setActScene(new MessageScene("Gewonnen", "", new MenuMainScene()));
		}
	}

	/**
	 * @param coin
	 *            the coin to set
	 */
	public void setCoin(int coin) {
		this.coin = coin;
	}

	/**
	 * @return the mess
	 */
	public String getMess() {
		return mess;
	}

	/**
	 * @return the map
	 */
	public CMap getMap() {
		return map;
	}

	/**
	 * @return the tileset
	 */
	public CTileset getTileset() {
		return tileset;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the maptyp
	 */
	public String getMaptyp() {
		return maptyp;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
}
