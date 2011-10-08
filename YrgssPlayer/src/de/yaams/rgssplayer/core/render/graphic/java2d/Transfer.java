/*    */package de.yaams.rgssplayer.core.render.graphic.java2d;

/*    */
/*    */import de.yaams.rgssplayer.core.java.Tone;

/*    */
/*    */public class Transfer
/*    */{
	/*    */public static de.yaams.rgssplayer.core.java.Color convertCtY(java.awt.Color color)
	/*    */{
		/* 23 */return new de.yaams.rgssplayer.core.java.Color(color.getRed(), color.getGreen(), color.getBlue(),
		/* 24 */color.getAlpha());
		/*    */}

	/*    */
	/*    */public static java.awt.Color convertYtC(de.yaams.rgssplayer.core.java.Color color)
	/*    */{
		/* 34 */return new java.awt.Color((int) color.red, (int) color.green,
		/* 35 */(int) color.blue, (int) color.alpha);
		/*    */}

	/*    */
	/*    */public static java.awt.Color convertTtC(Tone tone)
	/*    */{
		/* 46 */return new java.awt.Color((int) tone.red, (int) tone.green,
		/* 47 */(int) tone.blue, (int) tone.gray);
		/*    */}

	/*    */
	/*    */public static java.awt.Font convert(de.yaams.rgssplayer.core.java.Font font)
	/*    */{
		/* 58 */return new java.awt.Font(font.name, (font.bold ? 1 :
		/* 59 */0) | (font.italic ? 2 :
		/* 60 */0), font.size - 2);
		/*    */}

	/*    */
	/*    */public static de.yaams.rgssplayer.core.java.Font convert(java.awt.Font font)
	/*    */{
		/* 70 */de.yaams.rgssplayer.core.java.Font f = new de.yaams.rgssplayer.core.java.Font(font.getName(), font.getSize() + 2);
		/* 71 */f.bold = font.isBold();
		/* 72 */f.italic = font.isItalic();
		/* 73 */return f;
		/*    */}
	/*    */}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.render.graphic.java2d.Transfer JD-Core Version:
 * 0.6.0
 */