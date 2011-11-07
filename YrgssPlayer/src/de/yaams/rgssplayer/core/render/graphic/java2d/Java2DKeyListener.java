package de.yaams.rgssplayer.core.render.graphic.java2d;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import de.yaams.rgssplayer.config.ConfigWindow;
import de.yaams.rgssplayer.core.java.YInput;
import de.yaams.rgssplayer.core.java.Yrgss;
import de.yaams.rgssplayer.core.render.graphic.core.Render;

public class Java2DKeyListener implements KeyListener {
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_F1) {
			new ConfigWindow();
			return;
		}

		if (e.getKeyCode() == 113) {
			JFrame j = ((Java2DWindow) Render.getWindow()).getWindow();
			if (j.getTitle().contains(" - ")) {
				j.setTitle(Yrgss.game.getName());
			} else {
				j.setTitle(Yrgss.game.getName() + " - " + Render.getWindow().getFPS() + " FPS");
			}
			return;
		}

		if (e.getKeyCode() == 115 || e.isAltDown() && e.getKeyCode() == 10) {
			Render.getWindow().setFullscreen(!Render.getWindow().isFullscreen());
			return;
		}

		if (e.getKeyCode() == 123) {
			Render.restart();
			return;
		}

		set(e, true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		set(e, false);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	protected void set(KeyEvent e, boolean type) {
		int key = keyTranslate(e.getKeyCode());
		if (key != -1) {
			if (type) {
				YInput.setKeyDown(key);
			} else {
				YInput.setKeyUp(key);
			}
		}
	}

	protected int keyTranslate(int id) {
		switch (id) {
		case 40:
		case 98:
			return YInput.DOWN;
		case 38:
		case 104:
			return YInput.UP;
		case 37:
		case 100:
			return YInput.LEFT;
		case 39:
		case 102:
			return YInput.RIGHT;
		case 90:
			return YInput.A;
		case 27:
		case 88:
		case 96:
			return YInput.B;
		case 10:
		case 32:
		case 67:
			return YInput.C;
		case 17:
			return YInput.CTRL;
		case 18:
			return YInput.ALT;
		case 16:
			return YInput.SHIFT;
		case 116:
			return YInput.F5;
		case 117:
			return YInput.F6;
		case 118:
			return YInput.F7;
		case 119:
			return YInput.F8;
		case 120:
			return YInput.F9;
		case 65:
			return YInput.X;
		case 83:
			return YInput.Y;
		case 68:
			return YInput.Z;
		case 33:
		case 81:
			return YInput.L;
		case 34:
		case 87:
			return YInput.R;
		}

		return -1;
	}
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.render.graphic.java2d.Java2DKeyListener JD-Core
 * Version: 0.6.0
 */