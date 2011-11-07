/**
 * 
 */
package de.yaams.extensions.diamant.data.event;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;

import de.yaams.extensions.diamant.data.CONST;
import de.yaams.extensions.diamant.graph.PWindow;
import de.yaams.extensions.diamant.graph.Plays;
import de.yaams.extensions.diamant.graph.scene.map.PMap;
import de.yaams.extensions.diamant.graph.scene.menu.GameMenuEndScene;
import de.yaams.extensions.diamant.helper.YEx;
import de.yaams.extensions.diamant.tileset.CTileset;

/**
 * @author abby
 * 
 */
public class BaseEvent {

	protected int x, y, oX, oY;
	protected double Nx, Ny, NoX, NoY;

	protected BufferedImage img;
	protected PMap map;
	protected boolean isMoving, canMove, canCollect, fallDown, isRound, isCoin, door, canExplode, canDestroy, destryAllTime, canGameOver,
			isChest;

	protected double speed;

	protected CTileset tileset;

	/**
	 * Create a new event
	 * 
	 * @param x
	 * @param y
	 */
	public BaseEvent(int x, int y, CTileset tileset) {
		setPos(x, y);
		isMoving = false;
		this.tileset = tileset;
	}

	/**
	 * Start an event
	 */
	public void init() {

	}

	/**
	 * Finish the event
	 */
	public void destroy() {
		map.getLevel().getLevelEvent().remove(this);

		// finish the game?
		if (door && canCollect) {
			map.getLevel().win();
			Plays.playMusic("win.ogg");
		}

	}

	/**
	 * Draw the image
	 * 
	 * @param g
	 */
	public void paintComponent(Graphics2D g) {
		// check it
		if (img == null) {
			throw new IllegalArgumentException("img is missing");
		}

		// draw it
		g.drawImage(img, oX, oY, null);

		// draw it
		// g.setColor(Color.white);
		// g.drawString(x + "/" + y, oX, oY + 20);
	}

	/**
	 * Update the logic of the event
	 */
	public void update() {

		// move it?
		if (oX != NoX || oY != NoY) {
			// move hori?
			if (oX != NoX) {

				// move it
				oX += NoX > oX ? speed : -speed;

				// set new pos
				if (NoX > oX ? NoX - oX < speed : oX - NoX < speed) {
					x = (int) Nx;
					oX = (int) NoX;
				} else {
					return;
				}

			}

			// move vert?
			if (oY != NoY) {
				// move it
				oY += NoY > oY ? speed : -speed;

				// set new pos
				if (NoY == oY) {
					y = (int) Ny;
				}
				if (NoY > oY ? NoY - oY < speed : oY - NoY < speed) {
					y = (int) Ny;
					oY = (int) NoY;
				}

				// is bomb and explode?
				if (oY == NoY && !map.isFieldFree(x, y + 1)) {
					if (canExplode) {
						// field is full, explode
						destroyByExplode();
					}
					if (!canExplode && !(this instanceof PlayerEvent) && map.getEvent(x, y + 1) != null
							&& map.getEvent(x, y + 1).isDestryAllTime()) {
						// field is full, explode
						map.getEvent(x, y + 1).destroyByExplode();
					}
				}
			}

			// finish?
			if (oX == NoX && oY == NoY) {
				isMoving = false;
			}
		}

		// check it
		if (isMoving) {
			return;
		}

		// move it down?
		if (fallDown && map.isFieldFree(x, y + 1)) {
			moveTo(x, y + 1, CONST.FALLSPEED);
			return;
		}

		if (isRound) {
			// stand on player?
			if (map.getEvent(x, y + 1) instanceof PlayerEvent) {
				return;
			}

			// move right?
			if (map.isFieldFree(x + 1, y + 1) && map.isFieldFree(x + 1, y)) {
				moveTo(x + 1, y + 1, CONST.FALLSPEED);
				return;
			}
			// move left?
			if (map.isFieldFree(x - 1, y + 1) && map.isFieldFree(x - 1, y)) {
				moveTo(x - 1, y + 1, CONST.FALLSPEED);
				return;
			}
		}
	}

	/**
	 * Move the object animated
	 * 
	 * @param x
	 * @param y
	 */
	protected void moveTo(int x, int y, double speed) {
		Nx = x;
		Ny = y;
		NoX = Nx * CONST.TILEW;
		NoY = Ny * CONST.TILEH;
		isMoving = true;
		this.speed = speed;
	}

	/**
	 * Set a new postion
	 * 
	 * @param x
	 * @param y
	 */
	protected void setPos(int x, int y) {
		this.x = (int) (Nx = x);
		this.y = (int) (Ny = y);
		oX = (int) (NoX = x * CONST.TILEW);
		oY = (int) (NoY = y * CONST.TILEH);
	}

	/**
	 * @param map
	 *            the map to set
	 */
	public void setMap(PMap map) {
		this.map = map;
	}

	/**
	 * Check if the event on this field
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean onField(int x, int y) {
		return x == this.x && y == this.y || x == Nx && y == Ny;
	}

	/**
	 * @return the isMoving
	 */
	public boolean isMoving() {
		return isMoving;
	}

	/**
	 * If this event remove un
	 */
	public void destroyByExplode() {
		// is chest?
		if (isChest) {
			// is coin?
			if (isCoin) {
				// change image
				img = tileset.getTilesetGraphic(993);

				canCollect = true;
			}

			Plays.playSound("open-chest.wav");
			isChest = false;
		} else {
			// can destroy?
			if (canDestroy) {
				destroy();
			}
		}

		// is bomb?
		if (canExplode) {
			Plays.playSound("explode.wav");
			map.destroyByExplode(x - 1, y - 1);
			map.destroyByExplode(x, y - 1);
			map.destroyByExplode(x + 1, y - 1);
			map.destroyByExplode(x - 1, y + 1);
			map.destroyByExplode(x, y + 1);
			map.destroyByExplode(x + 1, y + 1);
			map.destroyByExplode(x - 1, y);
			map.destroyByExplode(x + 1, y);
		}

		// can lose?
		if (canGameOver) {
			// set look and feel
			try {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {

						Plays.playMusic("gameover.wav");
						PWindow.window.setActScene(new GameMenuEndScene("Level verloren!", map.getScene()));

					}
				});
			} catch (Throwable e) {
				YEx.error("Can not change scene", e);
			}
		}
	}

	/**
	 * @return the canMove
	 */
	public boolean canMove() {
		return canMove;
	}

	/**
	 * @return the canCollect
	 */
	public boolean canCollect() {
		return canCollect;
	}

	/**
	 * @return the fallDown
	 */
	public boolean isFallDown() {
		return fallDown;
	}

	/**
	 * @return the isRound
	 */
	public boolean isRound() {
		return isRound;
	}

	/**
	 * @return the isCoin
	 */
	public boolean isCoin() {
		return isCoin;
	}

	/**
	 * This method will call, if all coins are collected
	 */
	public void canWin() {
		if (door) {
			canCollect = true;
			Plays.playSound("win_level.wav");
		}

	}

	/**
	 * @return the destryAllTime
	 */
	public boolean isDestryAllTime() {
		return destryAllTime;
	}
}
