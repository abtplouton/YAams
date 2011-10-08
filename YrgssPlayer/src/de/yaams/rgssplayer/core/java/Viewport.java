/*     */package de.yaams.rgssplayer.core.java;

/*     */
/*     */import java.util.Observable;

/*     */
/*     */public class Viewport extends Observable
/*     */{
	/*     */public Rect rect;
	/*     */public int ox;
	/*     */public int oy;
	/*     */protected int z;
	/*     */public boolean visible;
	/*     */public Color color;
	/*     */public Tone tone;

	/*     */
	/*     */public Viewport(int x, int y, int width, int height)
	/*     */{
		/* 62 */this(new Rect(x, y, width, height));
		/*     */}

	/*     */
	/*     */public Viewport(Rect rect)
	/*     */{
		/* 72 */this.rect = rect;
		/* 73 */this.visible = true;
		/*     */}

	/*     */
	/*     */public void dispose()
	/*     */{
		/*     */}

	/*     */
	/*     */public void flash(Color color, int duration)
	/*     */{
		/*     */}

	/*     */
	/*     */public void update()
	/*     */{
		/*     */}

	/*     */
	/*     */public boolean isDisposed()
	/*     */{
		/* 111 */return false;
		/*     */}

	/*     */
	/*     */@Override
	public String toString()
	/*     */{
		/* 121 */return "Viewport [rect=" + this.rect + ", ox=" + this.ox + ", oy=" + this.oy + ", z=" +
		/* 122 */this.z + ", visible=" + this.visible + ", color=" + this.color + ", tone=" +
		/* 123 */this.tone + "]";
		/*     */}

	/*     */
	/*     */public int z()
	/*     */{
		/* 135 */return this.z;
		/*     */}

	/*     */
	/*     */public void setZ(int z)
	/*     */{
		/* 148 */this.z = z;
		/* 149 */setChanged();
		/* 150 */notifyObservers();
		/*     */}
	/*     */}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.java.Viewport JD-Core Version: 0.6.0
 */