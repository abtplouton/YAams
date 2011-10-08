/*    */package de.yaams.rgssplayer.core.render.graphic.core;

/*    */
/*    */import de.yaams.rgssplayer.core.java.Sprite;

/*    */
/*    */public abstract class ISprite
/*    */{
	/*    */protected Sprite s;

	/*    */
	/*    */public ISprite(Sprite s)
	/*    */{
		/* 21 */this.s = s;
		/*    */}

	/*    */
	/*    */public Sprite getSprite()
	/*    */{
		/* 28 */return this.s;
		/*    */}

	/*    */
	/*    */public void setSprite(Sprite s)
	/*    */{
		/* 36 */this.s = s;
		/*    */}

	/*    */
	/*    */public abstract void updateCache();
	/*    */}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.render.graphic.core.ISprite JD-Core Version: 0.6.0
 */