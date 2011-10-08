package de.yaams.rgssplayer.core.render.graphic.java2d;

import java.awt.image.BufferedImage;
import java.io.File;

import de.yaams.rgssplayer.core.java.Bitmap;
import de.yaams.rgssplayer.core.java.Sprite;
import de.yaams.rgssplayer.core.render.graphic.core.IBitmap;
import de.yaams.rgssplayer.core.render.graphic.core.IRenderer;
import de.yaams.rgssplayer.core.render.graphic.core.ISprite;
import de.yaams.rgssplayer.core.render.graphic.core.IWindow;
import de.yaams.rgssplayer.core.render.graphic.core.Render;

public class Java2D extends IRenderer {
	protected static BufferedImage transition;

	@Override
	public void inputUpdate() {}

	@Override
	public void graphicsUpdate() {
		// Java2DWindow w = (Java2DWindow) Render.getWindow();
		((Java2DWindow) Render.getWindow()).getArea().repaint();
	}

	@Override
	public IBitmap createBitmap(int width, int height, Bitmap container) {
		return new Java2DBitmap(width, height, container);
	}

	@Override
	public IBitmap createBitmap(File filename, Bitmap container) {
		return new Java2DBitmap(filename, container);
	}

	@Override
	public ISprite createSprite(Sprite container) {
		return new Java2DSprite(container);
	}

	@Override
	public String getName() {
		return "Java2D Softrenderer";
	}

	@Override
	public IWindow createWindow(int width, int height, boolean fullscreen) {
		return new Java2DWindow(width, height);
	}
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.render.graphic.java2d.Java2D JD-Core Version: 0.6.0
 */