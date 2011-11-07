/**
 * 
 */
package de.yaams.extensions.diamant.data.event;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import de.yaams.extensions.diamant.data.CONST;
import de.yaams.extensions.diamant.graph.Plays;
import de.yaams.extensions.diamant.graph.img.LoadFiles;
import de.yaams.extensions.diamant.graph.scene.menu.GameMenuEndScene;
import de.yaams.extensions.diamant.tileset.CTileset;

/**
 * @author abby
 * 
 */
public class PlayerEvent extends BaseEvent {

	protected KeyListener key;

	protected BufferedImage imgRoh;

	protected int step;

	/**
	 * @param x
	 * @param y
	 */
	public PlayerEvent(int x, int y, CTileset tileset, int id) {
		super(x, y, tileset);

		imgRoh = LoadFiles.getInternImage("player.png");
		setImg(0, 1);

		destryAllTime = true;
		canGameOver = true;
		canDestroy = true;
	}

	/**
	 * Helpermethod to the set image
	 * 
	 * @param row
	 * @param step
	 */
	protected void setImg(int row, int step) {
		img = imgRoh.getSubimage(step * 32, row * 48, 32, 48);
	}

	/**
	 * Draw the image
	 * 
	 * @param g
	 */
	@Override
	public void paintComponent(Graphics2D g) {
		// check it
		if (img == null) {
			throw new IllegalArgumentException("img is missing");
		}

		// draw it
		g.drawImage(img, oX + CONST.TILEW / 2 - img.getWidth() / 2, oY + CONST.TILEH - img.getHeight(), null);

		// draw it
		// g.setColor(Color.white);
		// g.drawString(x + "/" + y, oX, oY + 20);
	}

	@Override
	public void init() {
		// add key listener
		key = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {

			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent k) {
				// run escape?
				if (k.getKeyCode() == KeyEvent.VK_ESCAPE) {
					map.getScene().getWindow().setActScene(new GameMenuEndScene("Hauptmenü", map.getScene()));
					return;
				}

				// is moving?
				if (isMoving) {
					return;
				}

				// check dir?
				if (k.getKeyCode() == KeyEvent.VK_DOWN) {
					// move himself?
					movePlayer(0, +1);
					setImg(0, 1);
				}

				// check dir?
				if (k.getKeyCode() == KeyEvent.VK_UP) {
					// move himself?
					movePlayer(0, -1);
					setImg(3, 1);
				}

				// check dir?
				if (k.getKeyCode() == KeyEvent.VK_RIGHT) {
					// move himself?
					movePlayer(1, 0);
					setImg(2, 1);
				}

				// check dir?
				if (k.getKeyCode() == KeyEvent.VK_LEFT) {
					// move himself?
					movePlayer(-1, 0);
					setImg(1, 1);
				}

			}
		};

		map.getScene().getWindow().getContent().addKeyListener(key);
	}

	/**
	 * Finish the event
	 */
	@Override
	public void destroy() {
		super.destroy();
		map.getScene().getWindow().getContent().removeKeyListener(key);

	}

	/**
	 * Move Player, also check if something to do, like collect or move
	 * 
	 * @param dX
	 * @param dY
	 */
	private void movePlayer(int dX, int dY) {
		// can move?
		if (map.isFieldFree(x + dX, y + dY)) {
			moveTo(x + dX, y + dY, CONST.PLAYERSPEED);
			return;
		}

		// can collect?
		BaseEvent e = map.getEvent(x + dX, y + dY);
		if (e == null || e.isMoving()) {
			return;
		}

		// collect it
		if (e.canCollect()) {
			// is coin?
			if (e.isCoin()) {
				map.addCoin();
				Plays.playSound("gold.ogg");
			}

			moveTo(x + dX, y + dY, CONST.PLAYERSPEED);
			e.destroy();
			return;

		}

		// move it?
		if (dY == 0 && e.canMove() && map.isFieldFree(x + dX * 2, y)) {
			moveTo(x + dX, y, CONST.MOVESPEED);
			e.moveTo(x + dX * 2, y, CONST.MOVESPEED);
		}
	}
}
