package de.yaams.rgssplayer.core.render.graphic.core;

import java.util.LinkedList;

import de.yaams.rgssplayer.core.java.BaseGraphicElement;
import de.yaams.rgssplayer.core.java.Bitmap;

public abstract class IWindow {
	protected final LinkedList<BaseGraphicElement> sprites;
	protected int fps;

	public IWindow() {
		this.sprites = new LinkedList<BaseGraphicElement>();
	}

	public void registerSprite(BaseGraphicElement sprite) {
		int z = sprite.z() + (sprite.viewport() == null ? 0 : sprite.viewport().z());

		int i = 0;
		while (i < this.sprites.size()) {
			if (this.sprites.get(i).z() > z) {
				this.sprites.add(i, sprite);
				return;
			}
			i++;
		}

		this.sprites.add(this.sprites.size(), sprite);
	}

	public void clear() {
		for (BaseGraphicElement s : this.sprites) {
			s.dispose();
		}

		this.sprites.clear();
	}

	public void unregisterSprite(BaseGraphicElement sprite) {
		if (isRegisterSprited(sprite)) {
			this.sprites.remove(sprite);
		}
	}

	public boolean isRegisterSprited(BaseGraphicElement sprite) {
		return this.sprites.contains(sprite);
	}

	public LinkedList<BaseGraphicElement> getSprites() {
		return this.sprites;
	}

	public int getFPS() {
		return this.fps;
	}

	public void setFPS(int fps) {
		this.fps = fps;
	}

	public abstract int width();

	public abstract boolean isActive();

	public abstract int height();

	public abstract void freeze();

	public abstract void transition(int paramInt1, String paramString, int paramInt2);

	public abstract Bitmap snap_to_bitmap();

	public abstract void setFullscreen(boolean paramBoolean);

	public abstract boolean isFullscreen();

	public abstract void resize(int paramInt1, int paramInt2);
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.render.graphic.core.IWindow JD-Core Version: 0.6.0
 */