package de.yaams.rgssplayer.core.render.graphic.java2d;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;

import javax.swing.JFrame;

import org.apache.commons.lang.SystemUtils;

import de.yaams.core.helper.I18N;
import de.yaams.core.helper.gui.YDialog;
import de.yaams.core.helper.gui.YEx;
import de.yaams.core.helper.gui.icons.IconCache;
import de.yaams.rgssplayer.config.ConfigWindow;
import de.yaams.rgssplayer.core.java.Bitmap;
import de.yaams.rgssplayer.core.java.Yrgss;
import de.yaams.rgssplayer.core.render.graphic.core.IWindow;
import de.yaams.rgssplayer.core.render.graphic.core.Render;

public class Java2DWindow extends IWindow {
	protected JFrame window;
	protected Java2DArea area;
	protected DisplayMode oldDisplayMode;

	public Java2DWindow(int width, int height) {
		this.area = new Java2DArea(width, height);
		this.window = new JFrame(Yrgss.game.getName());
		this.window.setLayout(new GridLayout(1, 1));
		this.window.add(this.area);
		this.window.pack();
		this.window.setIconImage(IconCache.getImage(ConfigWindow.getIcon(), SystemUtils.IS_OS_WINDOWS_7 | SystemUtils.IS_OS_MAC_OSX ? 32
				: 16));
		this.window.setDefaultCloseOperation(3);

		this.window.addKeyListener(new Java2DKeyListener());

		this.window.setLocationRelativeTo(null);
		this.window.setVisible(true);
		setFPS(Yrgss.smoothmodus ? 40 : 20);
	}

	public Java2DArea getArea() {
		return this.area;
	}

	@Override
	public int width() {
		return this.area.getWidth();
	}

	@Override
	public int height() {
		return this.area.getHeight();
	}

	@Override
	public void transition(int duration, String filename, int vague) {
		this.area.transition(this.area.getTranstionIn(), ((Java2DBitmap) snap_to_bitmap().getData()).getData(), duration);
	}

	@Override
	public void freeze() {
		this.area.transition(((Java2DBitmap) snap_to_bitmap().getData()).getData(), null, 0);
	}

	@Override
	public Bitmap snap_to_bitmap() {
		Bitmap b = new Bitmap(Render.getWindow().width(), Render.getWindow().height());

		this.area.drawSprites(((Java2DBitmap) b.getData()).getData().getGraphics());

		return b;
	}

	@Override
	public void setFullscreen(boolean fullscreen) {
		if (!fullscreen) {
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
		} else {
			try {
				if (!GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().isFullScreenSupported()) {
					YDialog.ok(I18N.t("Dein System unterstützt kein Vollbildmodus"), "", "monitor_close");
					return;
				}

				GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
				this.oldDisplayMode = device.getDisplayMode();

				device.setFullScreenWindow(this.window);
				device.setDisplayMode(new DisplayMode(this.area.getWidth(), this.area.getHeight(), this.oldDisplayMode.getBitDepth(),
						this.oldDisplayMode.getRefreshRate()));
			} catch (Throwable t) {
				setFullscreen(false);
				YEx.info("Can not enter fullscreen", t);
			}
		}
	}

	@Override
	public boolean isFullscreen() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getFullScreenWindow() != null;
	}

	@Override
	public void resize(int width, int height) {
		this.area.setPreferredSize(new Dimension(width, height));
		this.window.pack();

		if (isFullscreen()) {
			setFullscreen(false);
			setFullscreen(true);
		}
	}

	@Override
	public boolean isActive() {
		return this.window.isActive();
	}

	public JFrame getWindow() {
		return this.window;
	}
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.render.graphic.java2d.Java2DWindow JD-Core Version:
 * 0.6.0
 */