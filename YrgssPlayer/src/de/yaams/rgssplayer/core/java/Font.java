/*     */package de.yaams.rgssplayer.core.java;

/*     */
/*     */import java.awt.GraphicsEnvironment;

/*     */
/*     */public class Font
/*     */{
	/* 20 */public static String default_name = "Arial";
	/*     */
	/* 26 */public static int default_size = 22;
	/*     */
	/* 31 */public static boolean default_bold = false;
	/*     */
	/* 36 */public static boolean default_italic = false;
	/*     */
	/* 42 */public static boolean default_shadow = false;
	/*     */
	/* 48 */public static Color default_color = Color.WHITE;
	/*     */public String name;
	/*     */public int size;
	/*     */public boolean bold;
	/*     */public boolean italic;
	/*     */public boolean shadow;
	/*     */public Color color;

	/*     */
	/*     */public Font()
	/*     */{
		/* 59 */this(default_name, default_size);
		/*     */}

	/*     */
	/*     */public Font(String name)
	/*     */{
		/* 68 */this(name, default_size);
		/*     */}

	/*     */
	/*     */public Font(String name, int size)
	/*     */{
		/* 79 */this.name = name;
		/* 80 */this.size = size;
		/* 81 */this.bold = default_bold;
		/* 82 */this.italic = default_italic;
		/* 83 */this.color = default_color;
		/*     */}

	/*     */
	/*     */public static boolean isExist(String name)
	/*     */{
		/* 93 */java.awt.Font[] fonts =
		/* 94 */GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
		/*     */
		/* 96 */for (java.awt.Font f : fonts) {
			/* 97 */if (f.getName().equals(name))
				/* 98 */return true;
			/*     */}
		/* 100 */return false;
		/*     */}

	/*     */
	/*     */@Override
	public String toString()
	/*     */{
		/* 110 */StringBuilder builder = new StringBuilder();
		/* 111 */builder.append("Font [name=");
		/* 112 */builder.append(this.name);
		/* 113 */builder.append(", size=");
		/* 114 */builder.append(this.size);
		/* 115 */builder.append(", bold=");
		/* 116 */builder.append(this.bold);
		/* 117 */builder.append(", italic=");
		/* 118 */builder.append(this.italic);
		/* 119 */builder.append(", color=");
		/* 120 */builder.append(this.color);
		/* 121 */builder.append("]");
		/* 122 */return builder.toString();
		/*     */}
	/*     */}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.java.Font JD-Core Version: 0.6.0
 */