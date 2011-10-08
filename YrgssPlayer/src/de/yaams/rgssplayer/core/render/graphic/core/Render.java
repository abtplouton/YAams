package de.yaams.rgssplayer.core.render.graphic.core;

import de.yaams.core.helper.Log;
import de.yaams.core.helper.gui.YEx;
import de.yaams.rgssplayer.core.rb.RBLocater;

public class Render {
	protected static IRenderer renderer;
	protected static IWindow window;

	public static void set(IRenderer renderer) {
		Log.ger.info("Renderer: " + renderer.getName());
		Render.renderer = renderer;
		window = renderer.createWindow(640, 480, false);
	}

	public static IRenderer er() {
		return renderer;
	}

	public static IWindow getWindow() {
		return window;
	}

	public static void restart() {
		de.yaams.rgssplayer.core.java.YGraphics.restart = true;
		try {
			Thread.sleep(250L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		window.clear();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					RBLocater.loadScripts();
				} catch (Throwable t) {
					YEx.info("Can not load game scripts", t);
				}
			}
		}).start();
	}
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.render.graphic.core.Render JD-Core Version: 0.6.0
 */