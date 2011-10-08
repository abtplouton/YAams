package de.yaams.rgssplayer.core.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import de.yaams.core.helper.I18N;
import de.yaams.core.helper.gui.YDialog;
import de.yaams.core.helper.gui.YEx;
import de.yaams.rgssplayer.core.render.graphic.core.IBitmap;
import de.yaams.rgssplayer.core.render.graphic.core.Render;

public class Bitmap {
	protected LinkedList<Sprite> sprites;
	public Font font = new Font();
	protected IBitmap data;
	protected Boolean isLoaded;

	public Bitmap(int width, int height) {
		this.data = Render.er().createBitmap(width, height, this);

		this.sprites = new LinkedList<Sprite>();
	}

	public Bitmap(String filename) {
		this.data = Render.er().createBitmap(searchImage(filename), this);

		this.sprites = new LinkedList<Sprite>();
	}

	protected File searchImage(String filename) {
		String[] end = { "", ".png", ".jpg", ".jpeg", ".bmp" };
		for (String s : end) {
			File f = new File(Yrgss.game.getPath(), filename + s);
			if (f.exists()) {
				return f;
			}
		}

		if (Yrgss.game.getRtp().size() > 0) {
			for (File rtp : Yrgss.game.getRtp().values()) {
				for (String s : end) {
					File f = new File(rtp, filename + s);
					if (f.exists()) {
						return f;
					}
				}
			}
		}
		YEx.info("IO image", new FileNotFoundException("Image " + new File(Yrgss.game.getPath(), filename).getAbsolutePath()
				+ " don't exist"));

		// get result
		Object o = YDialog.fileNotFound(new File(Yrgss.game.getPath(), filename + end[1]), filename);
		if (o instanceof File) {
			return (File) o;
		}
		if (o.equals(-2)) {
			return searchImage(filename);
		}

		return null;
	}

	public int width() {
		return isDisposed() ? 0 : this.data.width();
	}

	public int height() {
		return isDisposed() ? 0 : this.data.height();
	}

	public Rect rect() {
		return isDisposed() ? new Rect(0, 0, 0, 0) : new Rect(0, 0, width(), height());
	}

	public void blt(int x, int y, Bitmap src_bitmap, Rect src_rect) {
		blt(x, y, src_bitmap, src_rect, 255);
	}

	public void clear() {
		fill_rect(0, 0, width(), height(), new Color(0.0D, 0.0D, 0.0D, 0.0D));
	}

	public void clear_rect(Rect rect) {
		fill_rect(rect.x, rect.y, rect.x + rect.width, rect.y + rect.height, new Color(0.0D, 0.0D, 0.0D, 0.0D));
	}

	public void clear_rect(int x, int y, int width, int height) {
		fill_rect(x, y, x + width, y + height, new Color(0.0D, 0.0D, 0.0D, 0.0D));
	}

	public void blt(int x, int y, Bitmap src_bitmap, Rect src_rect, int opacity) {
		stretch_blt(new Rect(x, y, src_rect.width, src_rect.height), src_bitmap, src_rect, opacity);
	}

	public void stretch_blt(Rect dest_rect, Bitmap src_bitmap, Rect src_rect) {
		stretch_blt(dest_rect, src_bitmap, src_rect, 255);
	}

	public void stretch_blt(Rect dest_rect, Bitmap src_bitmap, Rect src_rect, int opacity) {
		this.data.stretch_blt(dest_rect, src_bitmap, src_rect, opacity);
		informSprites();
	}

	public void fill_rect(Rect rect, Color color) {
		fill_rect(rect.x, rect.y, rect.width, rect.height, color);
	}

	public void fill_rect(int x, int y, int width, int height, Color color) {
		this.data.fill_rect(x, y, width, height, color);
		informSprites();
	}

	public Color get_pixel(int x, int y) {
		return this.data.get_pixel(x, y);
	}

	public void set_pixel(int x, int y, Color color) {
		fill_rect(x, y, 1, 1, color);
	}

	public void draw_text(Rect rect, Object str) {
		draw_text(rect.x, rect.y, rect.width, rect.height, str, 0);
	}

	public void draw_text(Rect rect, Object str, int align) {
		draw_text(rect.x, rect.y, rect.width, rect.height, str, align);
	}

	public void draw_text(int x, int y, int width, int height, Object str) {
		draw_text(x, y, width, height, str, 0);
	}

	public void draw_text(int x, int y, int width, int height, Object str, int align) {
		this.data.draw_text(x, y, width, height, I18N.t(str.toString()), align);
		informSprites();
	}

	public Rect text_size(Object str) {
		return this.data.text_size(str.toString());
	}

	public void dispose() {
		if (!isDisposed()) {
			this.data.dispose();
			this.data = null;
		}
	}

	public boolean isDisposed() {
		return this.data == null;
	}

	public IBitmap getData() {
		return this.data;
	}

	public void setData(IBitmap data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Bitmap [font=" + this.font + ", data=" + this.data + ", isLoaded=" + this.isLoaded + "]";
	}

	public void addSprite(Sprite s) {
		this.sprites.add(s);
	}

	public void delSprite(Sprite s) {
		this.sprites.remove(s);
	}

	protected void informSprites() {
		int i = 0;
		for (int l = this.sprites.size(); i < l; i++) {
			this.sprites.get(i).setNeedUpdate(true);
		}
	}

	protected void hue_change(int hue) {
		int x = 0;
		for (int l = width(); x < l; x++) {
			int y = 0;
			for (int m = width(); y < m; y++) {
				Color c = get_pixel(x, y);

				if (c.alpha == 0.0D) {
					continue;
				}

				Color hc = Color.to_hsb(c.red, c.green, c.blue);
				double h = (hc.red + hue) % 360.0D;

				Color n = Color.hsb_to_rgb(h, hc.green, hc.blue);

				Color n2 = new Color(n.red, n.green, n.blue);
				n2.alpha = c.alpha;
				set_pixel(x, y, n2);
			}
		}
	}
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.java.Bitmap JD-Core Version: 0.6.0
 */