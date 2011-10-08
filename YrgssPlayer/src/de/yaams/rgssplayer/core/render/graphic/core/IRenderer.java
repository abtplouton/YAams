package de.yaams.rgssplayer.core.render.graphic.core;

import java.io.File;

import de.yaams.rgssplayer.core.java.Bitmap;
import de.yaams.rgssplayer.core.java.Sprite;

public abstract class IRenderer {
	public abstract void graphicsUpdate();

	public abstract void inputUpdate();

	public abstract IBitmap createBitmap(int paramInt1, int paramInt2, Bitmap paramBitmap);

	public abstract IBitmap createBitmap(File paramFile, Bitmap paramBitmap);

	public abstract ISprite createSprite(Sprite paramSprite);

	public abstract String getName();

	public abstract IWindow createWindow(int paramInt1, int paramInt2, boolean paramBoolean);
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.render.graphic.core.IRenderer JD-Core Version: 0.6.0
 */