/**
 * 
 */
package de.yaams.extensions.diamant.graph.scene.map;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import de.yaams.extensions.diamant.data.CONST;
import de.yaams.extensions.diamant.graph.img.LoadFiles;

/**
 * @author abby
 * 
 */
public class PBar extends JPanel {

	private static final long serialVersionUID = 4616887575981982115L;

	protected JLabel coins, time;
	protected PMap map;
	protected long startTime, lastSetTime, actTime;

	/**
	 * @param gameScene
	 * @param map
	 * 
	 */
	public PBar() {
		super(new GridLayout(1, 2));
		setPreferredSize(new Dimension(CONST.GAME_WIDTH, 32));

		startTime = System.currentTimeMillis();

		// build coins
		coins = new JLabel();
		coins.setIcon(new ImageIcon(LoadFiles.getInternImage("point.png")));
		coins.setBorder(new EmptyBorder(0, 10, 0, 0));
		add(coins);

		// build time
		time = new JLabel();
		time.setBorder(new EmptyBorder(0, 0, 0, 10));
		time.setHorizontalAlignment(JLabel.RIGHT);
		time.setIcon(new ImageIcon(LoadFiles.getInternImage("time.png")));
		add(time);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(LoadFiles.getInternImage("MainMenuBack.jpg"), 0, 0, getWidth(), getHeight(), null);
		// super.paintComponent(g);
	}

	/**
	 * Update the Coins view
	 */
	public void updateCoins() {
		coins.setText(map.getLevel().getCoin() + " / " + map.getLevel().getCoinMax());

	}

	/**
	 * @return the map
	 */
	public PMap getMap() {
		return map;
	}

	/**
	 * @param map
	 *            the map to set
	 */
	public void setMap(PMap map) {
		this.map = map;
	}

	/**
	 * Update it
	 */
	public void update() {
		// chance it?
		actTime = System.currentTimeMillis();
		if (actTime - lastSetTime > 1000) {
			lastSetTime = actTime;

			// set it
			long temp = (actTime - startTime) / 1000;
			time.setText(String.format("%d:%02d", temp / 60, temp % 60));
		}

	}

}
