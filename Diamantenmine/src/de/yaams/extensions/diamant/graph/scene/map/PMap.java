/**
 * 
 */
package de.yaams.extensions.diamant.graph.scene.map;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import de.yaams.extensions.diamant.data.CONST;
import de.yaams.extensions.diamant.data.Level;
import de.yaams.extensions.diamant.data.event.BaseEvent;
import de.yaams.extensions.diamant.graph.img.LoadFiles;
import de.yaams.extensions.diamant.graph.scene.GameScene;
import de.yaams.extensions.diamant.tileset.CTileset;
import de.yaams.extensions.diamant.tileset.CTileset.TYPE;

/**
 * @author abby
 * 
 */
public class PMap extends JComponent {

	private static final long serialVersionUID = 3055370326703262877L;

	protected Level level;
	protected BufferedImage mapCache, background;
	protected boolean mapCacheUpdate;
	protected GameScene scene;
	protected PBar bar;

	/**
	 * Create a new map
	 * 
	 * @param level
	 * @param scene
	 * @param bar
	 */
	public PMap(Level level, GameScene scene, PBar bar) {
		this.level = level;
		this.scene = scene;
		this.bar = bar;
		background = LoadFiles.getInternImage("map.jpg");
		setPreferredSize(new Dimension(CONST.GAME_WIDTH, CONST.GAME_HEIGHT - 32));
	}

	/**
	 * Draw it
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// paint background
		g.drawImage(background, 0, 0, getWidth(), getHeight(), null);

		// update map?
		if (mapCache == null || mapCacheUpdate) {
			updateMapCache();
		}
		g.drawImage(mapCache, 0, 0, null);

		// draw events
		for (BaseEvent e : level.getLevelEvent()) {
			e.paintComponent((Graphics2D) g);
		}
	};

	/**
	 * Update Map
	 */
	protected void updateMapCache() {
		if (mapCache == null) {
			mapCache = new BufferedImage(level.getWidth() * CONST.TILEW, level.getHeight() * CONST.TILEH, BufferedImage.TYPE_4BYTE_ABGR);
		}

		CTileset tileset = level.getTileset();

		// draw it
		// run over
		for (int x = 0; x < level.getWidth(); x++) {
			for (int y = 0; y < level.getHeight(); y++) {
				for (int i = 0, l = level.getLevelTiled().get(x).get(y).size(); i < l; i++) {
					int k = level.getLevelTiled().get(x).get(y).get(i);
					mapCache.getGraphics().drawImage(tileset.getTilesetGraphic(k), x * CONST.TILEW, y * CONST.TILEH, null);
				}

			}
		}
		mapCacheUpdate = false;
	}

	/**
	 * Get an event from this filed, if exist
	 * 
	 * @param x
	 * @param y
	 * @return null, if found nothing or the event
	 */
	public BaseEvent getEvent(int x, int y) {
		// check size
		if (!checkField(x, y)) {
			return null;
		}

		// check events
		for (BaseEvent e : level.getLevelEvent()) {
			if (e.onField(x, y)) {
				return e;
			}
		}

		return null;
	}

	/**
	 * Check if something on the field
	 * 
	 * @param x
	 * @param y
	 * @return true, field is ok, false field is blocked or outsite the game
	 */
	protected boolean checkField(int x, int y) {
		// check size
		if (x < 0 || x >= level.getWidth() || y < 0 || y >= level.getHeight()) {
			return false;
		}

		// check type
		if (level.getLevelTyp().containsKey(x + "x" + y)) {
			if (level.getLevelTyp().get(x + "x" + y) == TYPE.BLOCK) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Method to destroy alle events on the field, check also the eventtype,
	 * like chest or player
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public void destroyByExplode(int x, int y) {
		// check size
		if (getEvent(x, y) != null) {
			getEvent(x, y).destroyByExplode();
		}
	}

	/**
	 * Check if something on the field
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isFieldFree(int x, int y) {
		// check size
		if (!checkField(x, y)) {
			return false;
		}

		return getEvent(x, y) == null;
	}

	/**
	 * @return the level
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * @return the scene
	 */
	public GameScene getScene() {
		return scene;
	}

	/**
	 * Start method
	 */
	public void init() {
		// init event
		for (BaseEvent e : level.getLevelEvent()) {
			e.setMap(this);
			e.init();
		}

		// init bar
		bar.updateCoins();

	}

	public void update() {
		// draw events
		for (int i = 0, l = level.getLevelEvent().size(); i < l; i++) {
			if (i >= level.getLevelEvent().size()) {
				break;
			}
			level.getLevelEvent().get(i).update();
		}

		bar.update();
	}

	/**
	 * @return the bar
	 */
	public PBar getBar() {
		return bar;
	}

	/**
	 * @param coin
	 *            the coin to set
	 */
	public void addCoin() {
		level.setCoin(level.getCoin() + 1);
		bar.updateCoins();
		// has max coins?
		if (level.getCoin() == level.getCoinMax()) {
			// find door
			for (BaseEvent e : level.getLevelEvent()) {
				e.canWin();
			}
		}

	}

	public void close() {
		for (int i = 0, l = level.getLevelEvent().size(); i < l; i++) {
			if (i >= level.getLevelEvent().size()) {
				break;
			}
			level.getLevelEvent().get(i).destroy();
		}

	}
}
