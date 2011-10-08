package de.yaams.rgssplayer.core.java;

import java.util.Observable;
import java.util.Observer;

import de.yaams.rgssplayer.core.render.graphic.core.Render;

public class BaseGraphicElement implements Observer {
	protected Bitmap bitmap;
	protected Viewport viewport;
	public int ox;
	public int oy;
	public boolean visible;
	public float zoom_x;
	public float zoom_y;
	protected int opacity;
	protected int z;
	public int blend_type;
	public Color color;
	protected Tone tone;

	public BaseGraphicElement() {
		this(null);
	}

	public BaseGraphicElement(Viewport viewport) {
		setViewport(viewport);
		this.opacity = 255;
		this.color = Color.WHITE;

		Render.getWindow().registerSprite(this);
	}

	public int z() {
		return this.z;
	}

	public void setZ(int z) {
		this.z = z;

		Render.getWindow().unregisterSprite(this);
		Render.getWindow().registerSprite(this);
	}

	public Bitmap bitmap() {
		return this.bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		setZ(this.z);
	}

	public Viewport viewport() {
		return this.viewport;
	}

	public void setViewport(Viewport viewport) {
		this.viewport = viewport;
		if (viewport != null) {
			viewport.addObserver(this);
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [viewport=" + this.viewport + ", ox=" + this.ox + ", oy=" + this.oy + ", visible="
				+ this.visible + ", zoom_x=" + this.zoom_x + ", zoom_y=" + this.zoom_y + ", opacity=" + this.opacity + ", z=" + this.z
				+ ", blend_type=" + this.blend_type + ", color=" + this.color + ", tone=" + this.tone + "]";
	}

	public void dispose() {
		if (!isDisposed()) {
			if (this.bitmap != null && !this.bitmap.isDisposed()) {
				this.bitmap.dispose();
				this.bitmap = null;
			}

			Render.getWindow().unregisterSprite(this);
		}
	}

	public boolean isDisposed() {
		return !Render.getWindow().isRegisterSprited(this);
	}

	public int opacity() {
		return this.opacity;
	}

	public Tone tone() {
		return this.tone;
	}
}