/**
 * 
 */
package de.yaams.extensions.diamant.graph.scene;

import java.awt.BorderLayout;

import de.yaams.extensions.diamant.data.Level;
import de.yaams.extensions.diamant.graph.scene.map.PBar;
import de.yaams.extensions.diamant.graph.scene.map.PMap;

/**
 * @author abby
 * 
 */
public class GameScene extends BaseScene {
	private static final long serialVersionUID = 4300923477656866580L;

	protected PMap map;
	protected PBar bar;
	protected Level level;

	/**
	 * Create a new scene
	 * 
	 * @param level
	 */
	public GameScene(Level level) {
		setLayout(new BorderLayout());
		this.level = level;
	}

	@Override
	public void init() {

		bar = new PBar();
		map = new PMap(level, this, bar);
		bar.setMap(map);

		add(map, BorderLayout.CENTER);
		add(bar, BorderLayout.SOUTH);

		map.init();
	}

	@Override
	public void update() {
		if (map != null) {
			map.update();
		}

	}

	/**
	 * @return the level
	 */
	public Level getLevel() {
		return level;
	}

	@Override
	public void close() {
		map.close();

	}

}
