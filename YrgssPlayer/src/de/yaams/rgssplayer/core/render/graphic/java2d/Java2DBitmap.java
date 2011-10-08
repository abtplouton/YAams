package de.yaams.rgssplayer.core.render.graphic.java2d;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import de.yaams.core.helper.gui.YEx;
import de.yaams.rgssplayer.core.java.Bitmap;
import de.yaams.rgssplayer.core.java.Rect;
import de.yaams.rgssplayer.core.render.graphic.core.IBitmap;

public class Java2DBitmap extends IBitmap {
	protected BufferedImage data;
	protected Bitmap container;

	public Java2DBitmap(int width, int height, Bitmap container) {
		this.data = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
				.createCompatibleImage(width, height, 3);

		this.container = container;
	}

	public Java2DBitmap(File filename, Bitmap container) {
		try {
			this.data = ImageIO.read(filename);
		} catch (Throwable t) {
			YEx.info("Can not load image " + filename, t);
		}
		this.container = container;
	}

	@Override
	public void stretch_blt(Rect dest_rect, Bitmap src_bitmap, Rect src_rect, int opacity) {
		this.data.getGraphics().drawImage(((Java2DBitmap) src_bitmap.getData()).getData(), dest_rect.x, dest_rect.y,
				dest_rect.x + dest_rect.width, dest_rect.y + dest_rect.height, src_rect.x, src_rect.y, src_rect.x + src_rect.width,
				src_rect.y + src_rect.height, null);
	}

	@Override
	public int width() {
		return this.data.getWidth();
	}

	@Override
	public int height() {
		return this.data.getHeight();
	}

	@Override
	public void fill_rect(int x, int y, int width, int height, de.yaams.rgssplayer.core.java.Color color) {
		Graphics2D g = (Graphics2D) this.data.getGraphics();
		g.setBackground(new java.awt.Color(0.0F, 0.0F, 0.0F, 0.0F));
		g.clearRect(x, y, width, height);

		if (color.alpha > 0.0D) {
			Composite c = null;
			if (color.alpha < 255.0D) {
				c = g.getComposite();
				g.setComposite(AlphaComposite.getInstance(3, (float) (color.alpha / 255.0D)));
			}

			this.data.getGraphics().setColor(Transfer.convertYtC(color));
			this.data.getGraphics().fillRect(x, y, width, height);

			if (c != null) {
				g.setComposite(c);
			}
		}
	}

	@Override
	public de.yaams.rgssplayer.core.java.Color get_pixel(int x, int y) {
		int c = this.data.getRGB(x, y);
		int alpha = (c & 0xFF000000) >> 32;
		int red = (c & 0xFF0000) >> 16;
		int green = (c & 0xFF00) >> 8;
		int blue = c & 0xFF;

		return new de.yaams.rgssplayer.core.java.Color(red, green, blue, alpha);
	}

	@Override
	public void draw_text(int x, int y, int width, int height, String str, int align) {
		if (this.container.font.color.alpha <= 0.0D || this.container.font.size < 6) {
			return;
		}

		Graphics2D g = (Graphics2D) this.data.getGraphics();
		g.setColor(Transfer.convertYtC(this.container.font.color));
		g.setFont(Transfer.convert(this.container.font));

		RenderingHints r = g.getRenderingHints();
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		if (this.container.font.shadow) {
			g.setColor(java.awt.Color.BLACK);
			drawText(x + 1, y + 1, width, height, str, align, g, this.container.font.color.alpha / 2.0D);
		}

		drawText(x, y, width, height, str, align, g, this.container.font.color.alpha);

		g.setRenderingHints(r);
	}

	protected void drawText(int x, int y, int width, int height, String str, int align, Graphics2D g, double alpha) {
		Composite c = null;
		if (alpha < 255.0D) {
			c = g.getComposite();
			g.setComposite(AlphaComposite.getInstance(3, (float) (alpha / 255.0D)));
		}

		Rect rect = text_size(str);

		height -= 3;
		width -= 6;

		if (align == 0) {
			g.drawString(str, x, y + height / 2 + rect.height / 2);
		}

		if (align == 1) {
			g.drawString(str, x + width / 2 - rect.width / 2, y + height / 2 + rect.height / 2);
		}

		if (align == 2) {
			g.drawString(str, x + width - rect.width - 1, y + height / 2 + rect.height / 2);
		}

		if (c != null) {
			g.setComposite(c);
		}
	}

	@Override
	public Rect text_size(String str) {
		Graphics2D g = (Graphics2D) this.data.getGraphics();

		g.setFont(Transfer.convert(this.container.font));

		Rectangle2D r = g.getFontMetrics().getStringBounds(str, g);

		return new Rect(0, 0, (int) r.getWidth(), (int) r.getHeight());
	}

	@Override
	public void hue_change(int hue) {}

	@Override
	public void dispose() {
		this.data = null;
	}

	public BufferedImage getData() {
		return this.data;
	}
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.render.graphic.java2d.Java2DBitmap JD-Core Version:
 * 0.6.0
 */