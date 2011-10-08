package de.yaams.rgssplayer.core.render.graphic.java2d;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import de.yaams.rgssplayer.core.java.Sprite;
import de.yaams.rgssplayer.core.java.Tone;
import de.yaams.rgssplayer.core.render.graphic.core.ISprite;

public class Java2DSprite extends ISprite {
	protected boolean needCache;
	protected BufferedImage cache;
	protected Tone toneClone;

	public Java2DSprite(Sprite container) {
		super(container);
	}

	@Override
	public void updateCache() {
		if (this.s.tone() == null || this.s.tone().equals(this.toneClone)) {
			this.needCache = false;
			this.s.setNeedUpdate(false);
			return;
		}
		this.toneClone = this.s.tone().clone();
		this.needCache = true;

		BufferedImage source = ((Java2DBitmap) this.s.bitmap().getData()).getData();

		int w = source.getWidth();
		int h = source.getHeight();

		if (this.cache != null && this.cache.getWidth() >= w && this.cache.getHeight() >= h) {
			Graphics2D g = (Graphics2D) this.cache.getGraphics();

			g.setBackground(new Color(0.0F, 0.0F, 0.0F, 0.0F));
			g.clearRect(0, 0, w, h);
		} else {
			this.cache = new BufferedImage(w, h, source.getType() == 0 ? 6 : source.getType());
		}

		int[] data = source.getRGB(0, 0, w, h, null, 0, source.getWidth());

		double gray = this.toneClone.gray / 255.0D;
		double gray1 = 1.0D - gray;

		int i = 0;
		for (int l = data.length; i < l; i++) {
			int c = data[i];

			int red = (int) (((c & 0xFF0000) >> 16) + this.toneClone.red);
			int green = (int) (((c & 0xFF00) >> 8) + this.toneClone.green);
			int blue = (int) ((c & 0xFF) + this.toneClone.blue);

			if (this.toneClone.gray > 0.0D) {
				int ges = red + green + blue;
				red = (int) (red * gray1 + ges / 3 * gray);
				green = (int) (green * gray1 + ges / 3 * gray);
				blue = (int) (blue * gray1 + ges / 3 * gray);
			}

			data[i] &= -16777216;

			if (red > 255) {
				data[i] = 16711680;
			} else if (red >= 0) {
				data[i] += red << 16;
			}

			if (green > 255) {
				data[i] = 65280;
			} else if (green >= 0) {
				data[i] += green << 8;
			}

			if (blue > 255) {
				data[i] = -16777216;
			} else if (blue >= 0) {
				data[i] += blue;
			}

		}

		this.cache.setRGB(0, 0, w, h, data, 0, source.getWidth());

		this.cache.flush();
		this.s.setNeedUpdate(false);
	}

	protected void renderSprite(Graphics2D g) {
		if (this.s.bitmap() == null || !this.s.visible || this.s.zoom_x <= 0.0F || this.s.zoom_y <= 0.0F || this.s.opacity() <= 0
				|| this.s.viewport() != null
				&& (!this.s.viewport().visible || this.s.viewport().rect.width <= 0 || this.s.viewport().rect.height <= 0)) {
			return;
		}

		if (this.s.isNeedUpdate()) {
			updateCache();
		}

		int x = this.s.x - this.s.ox;
		int y = this.s.y - this.s.oy;
		int width = (int) (this.s.bitmap().width() * this.s.zoom_x);
		int height = (int) (this.s.bitmap().width() * this.s.zoom_y);

		int sX = 0;
		int sY = 0;

		if (this.s.src_rect != null) {
			sX += this.s.src_rect.x;
			sY += this.s.src_rect.y;
			width = this.s.src_rect.width > this.s.bitmap().width() ? this.s.bitmap().width() : this.s.src_rect.width;
			height = this.s.src_rect.height > this.s.bitmap().height() ? this.s.bitmap().height() : this.s.src_rect.height;
		}

		if (this.s.viewport() != null) {
			int oX = x;
			int oY = y;
			x = x - this.s.viewport().ox + this.s.viewport().rect.x;
			y = y - this.s.viewport().oy + this.s.viewport().rect.y;

			width = x + width >= this.s.viewport().rect.x + this.s.viewport().rect.width ? this.s.viewport().rect.x
					+ this.s.viewport().rect.width - oX : width;
			height = y + height >= this.s.viewport().rect.y + this.s.viewport().rect.height ? this.s.viewport().rect.y
					+ this.s.viewport().rect.height - oY : height;
		}

		if (x + width < 0 || x > 640 || y + height < 0 || y > 640) {
			return;
		}

		Composite c = null;
		if (this.s.opacity() < 255) {
			c = g.getComposite();
			g.setComposite(AlphaComposite.getInstance(3, this.s.opacity() / 255.0F));
		}

		AffineTransform saveXform = null;
		if (this.s.angle % 360 != 0) {
			saveXform = g.getTransform();
			AffineTransform at = new AffineTransform();
			AffineTransform toCenterAt = new AffineTransform();
			at.rotate(Math.toRadians(this.s.angle % 360));
			toCenterAt.concatenate(at);
			toCenterAt.translate(-this.s.ox, -this.s.oy);
			g.transform(saveXform);
		}

		g.drawImage(this.needCache ? this.cache : ((Java2DBitmap) this.s.bitmap().getData()).getData(), x, y, x + width, y + height, sX,
				sY, sX + width, sY + height, null);

		if (c != null) {
			g.setComposite(c);
		}

		if (saveXform != null) {
			g.setTransform(saveXform);
		}
	}
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.render.graphic.java2d.Java2DSprite JD-Core Version:
 * 0.6.0
 */