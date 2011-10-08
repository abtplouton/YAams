package de.yaams.rgssplayer.core.java;

import de.yaams.rgssplayer.core.render.graphic.core.ISprite;
import de.yaams.rgssplayer.core.render.graphic.core.Render;

public class Sprite extends BaseGraphicElement {
	protected boolean needUpdate;
	protected ISprite sprite;
	public int x;
	public int y;
	public Rect src_rect;
	public int angle;
	public boolean mirror;
	public int bush_depth;

	public Sprite() {
		this(null);
	}

	public Sprite(Viewport viewport) {
		super(viewport);
		this.visible = true;
		this.src_rect = new Rect(0, 0, 0, 0);
		this.zoom_x = 1.0F;
		this.zoom_y = 1.0F;
		this.sprite = Render.er().createSprite(this);
	}

	public void update() {}

	@Override
	public void setBitmap(Bitmap bitmap) {
		if (this.bitmap != null) {
			this.bitmap.delSprite(this);
		}
		this.bitmap = bitmap;

		int w = bitmap == null ? 0 : bitmap.width();
		int h = bitmap == null ? 0 : bitmap.height();

		if (bitmap != null) {
			bitmap.addSprite(this);
		}

		if (this.src_rect == null) {
			this.src_rect = new Rect(0, 0, w, h);
		} else {
			this.src_rect.set(0, 0, w, h);
		}
	}

	public int width() {
		return this.src_rect == null ? 0 : this.src_rect.width;
	}

	public int height() {
		return this.src_rect == null ? 0 : this.src_rect.height;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Sprite [x=");
		builder.append(this.x);
		builder.append(", y=");
		builder.append(this.y);
		builder.append(", src_rect=");
		builder.append(this.src_rect);
		builder.append(", angle=");
		builder.append(this.angle);
		builder.append(", mirror=");
		builder.append(this.mirror);
		builder.append(", bush_depth=");
		builder.append(this.bush_depth);
		builder.append(", viewport=");
		builder.append(this.viewport);
		builder.append(", ox=");
		builder.append(this.ox);
		builder.append(", oy=");
		builder.append(this.oy);
		builder.append(", visible=");
		builder.append(this.visible);
		builder.append(", zoom_x=");
		builder.append(this.zoom_x);
		builder.append(", zoom_y=");
		builder.append(this.zoom_y);
		builder.append(", opacity=");
		builder.append(this.opacity);
		builder.append(", z=");
		builder.append(this.z);
		builder.append(", blend_type=");
		builder.append(this.blend_type);
		builder.append(", color=");
		builder.append(this.color);
		builder.append(", tone=");
		builder.append(this.tone);
		builder.append("]");
		return builder.toString();
	}

	public boolean isNeedUpdate() {
		return this.needUpdate;
	}

	public void setNeedUpdate(boolean needUpdate) {
		this.needUpdate = needUpdate;
	}

	public ISprite getSprite() {
		return this.sprite;
	}

	public void setOpacity(int opacity) {
		this.opacity = opacity;
		this.needUpdate = true;
	}

	public void setTone(Tone tone) {
		this.tone = tone;
		this.needUpdate = true;
	}
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.java.Sprite JD-Core Version: 0.6.0
 */