package de.yaams.rgssplayer.core.java;

import de.yaams.core.helper.gui.YEx;
import de.yaams.rgssplayer.core.render.graphic.core.Render;

public class YGraphics {
	protected static long lastUpdate = System.currentTimeMillis();
	protected static Thread lastUpdateChecker;
	protected static boolean inTranstion;
	public static boolean restart = false;
	public static int frame_count;

	public static void update() {
		if (restart) {
			Thread.currentThread().interrupt();
			restart = false;
			return;
		}

		Render.er().graphicsUpdate();
		frame_count += 1;
		lastUpdate = System.currentTimeMillis();
		try {
			Thread.sleep(1000 / frame_rate());
		} catch (InterruptedException e) {
			YEx.info("Can not sleep in graphics update", e);
		}

		if (restart) {
			Thread.currentThread().interrupt();
			restart = false;
			return;
		}

		if (!Yrgss.active && !Render.getWindow().isActive()) {
			inTranstion = true;
			while (!Render.getWindow().isActive()) {
				try {
					Thread.sleep(250L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			lastUpdate = System.currentTimeMillis();
			inTranstion = false;
		}
	}

	public static void freeze() {
		Render.getWindow().freeze();
	}

	public static int width() {
		return Render.getWindow().width();
	}

	public static int height() {
		return Render.getWindow().height();
	}

	public static void frame_reset() {}

	public static void transition() {
		transition(8, null, 40);
	}

	public static void transition(int duration) {
		transition(duration, null, 40);
	}

	public static void transition(int duration, String filename) {
		transition(duration, filename, 40);
	}

	public static void transition(int duration, String filename, int vague) {
		if (Yrgss.game.getRgssVersion() == 1 && lastUpdateChecker != null) {
			lastUpdateChecker.interrupt();
		}
		inTranstion = true;
		Render.getWindow().transition(duration, filename, vague);
		lastUpdate = System.currentTimeMillis();
		inTranstion = false;
	}

	public static void resize_screen(int width, int height) {
		Render.getWindow().resize(width, height);
	}

	protected static Thread getThread() {
		if (Yrgss.game.getRgssVersion() != 1) {
			return null;
		}
		return new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (!YGraphics.inTranstion && System.currentTimeMillis() > YGraphics.lastUpdate + 10000L) {
						YEx.info("", new InterruptedException(Yrgss.game.getName() + " is hanging since 10s"));
					}
					try {
						Thread.sleep(1000L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public static void wait(int duration) {
		for (int i = 0; i < duration; i++) {
			update();
		}
	}

	public static int frame_rate() {
		return Render.getWindow().getFPS();
	}
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.java.YGraphics JD-Core Version: 0.6.0
 */