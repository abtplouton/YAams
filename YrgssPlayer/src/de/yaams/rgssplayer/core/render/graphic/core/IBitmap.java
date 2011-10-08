package de.yaams.rgssplayer.core.render.graphic.core;

import de.yaams.rgssplayer.core.java.Bitmap;
import de.yaams.rgssplayer.core.java.Color;
import de.yaams.rgssplayer.core.java.Rect;

public abstract class IBitmap {
	public abstract void stretch_blt(Rect paramRect1, Bitmap paramBitmap, Rect paramRect2, int paramInt);

	public abstract int width();

	public abstract int height();

	public abstract void fill_rect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor);

	public abstract Color get_pixel(int paramInt1, int paramInt2);

	public abstract void draw_text(int paramInt1, int paramInt2, int paramInt3, int paramInt4, String paramString, int paramInt5);

	public abstract Rect text_size(String paramString);

	public abstract void hue_change(int paramInt);

	public abstract void dispose();
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.render.graphic.core.IBitmap JD-Core Version: 0.6.0
 */