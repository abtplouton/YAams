/*    */package de.yaams.rgssplayer.core.java;

/*    */
/*    */public class Rect
/*    */{
	/*    */public int x;
	/*    */public int y;
	/*    */public int width;
	/*    */public int height;

	/*    */
	/*    */public Rect(Rect rect)
	/*    */{
		/* 22 */this(rect.x, rect.y, rect.width, rect.height);
		/*    */}

	/*    */
	/*    */public Rect(int x, int y, int width, int height)
	/*    */{
		/* 34 */set(x, y, width, height);
		/*    */}

	/*    */
	/*    */public void set(int x, int y, int width, int height)
	/*    */{
		/* 46 */this.x = x;
		/* 47 */this.y = y;
		/* 48 */this.width = width;
		/* 49 */this.height = height;
		/*    */}

	/*    */
	/*    */@Override
	public String toString()
	/*    */{
		/* 59 */return "Rect [x=" + this.x + ", y=" + this.y + ", width=" + this.width + ", height=" +
		/* 60 */this.height + "]";
		/*    */}
	/*    */}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.java.Rect JD-Core Version: 0.6.0
 */