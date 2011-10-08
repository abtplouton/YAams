package de.yaams.rgssplayer.core.java;

public class Color {
	public static Color WHITE = new Color(255.0D, 255.0D, 255.0D);
	public double red;
	public double green;
	public double blue;
	public double alpha;

	public Color(double red, double green, double blue) {
		setColor(red, green, blue);
		this.alpha = 255.0D;
	}

	public Color(double red, double green, double blue, double alpha) {
		setColor(red, green, blue, alpha);
	}

	public void setColor(double red, double green, double blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public void setColor(double red, double green, double blue, double alpha) {
		setColor(red, green, blue);
		this.alpha = alpha;
	}

	@Override
	public String toString() {
		return "Color [red=" + this.red + ", green=" + this.green + ", blue=" + this.blue + ", alpha=" + this.alpha + "]";
	}

	public static Color to_hsb(double r, double g, double b) {
		double max = Math.max(r, Math.max(g, b));

		double min = Math.min(r, Math.min(g, b));

		double h = 0.0D;
		if ((max == r) && (g >= b))
			h = 60.0D * (g - b) / (max - min);
		if ((max == r) && (g < b))
			h = 60.0D * (g - b) / (max - min) + 360.0D;
		if (max == g)
			h = 60.0D * (b - r) / (max - min) + 120.0D;
		if (max == b)
			h = 60.0D * (r - g) / (max - min) + 240.0D;
		if (max == min) {
			h = 0.0D;
		}
		double s = max == 0.0D ? 0.0D : 100.0D * (1.0D - min / max);

		double v = 100.0D * max / 255.0D;

		return new Color(h, s, v);
	}

	public static Color hsb_to_rgb(double hue, double sat, double bri) {
		hue %= 360.0D;

		sat = sat > 100.0D ? 1.0D : sat / 100.0D;
		bri = bri > 100.0D ? 1.0D : bri / 100.0D;

		int sector = (int) (hue / 60.0D);
		double f = (int) (hue / 60.0D - sector);
		double p = (int) (bri * (1.0D - sat));
		double q = (int) (bri * (1.0D - f * sat));
		double t = (int) (bri * (1.0D - (1.0D - f) * sat));

		double r = 0.0D;
		double g = 0.0D;
		double b = 0.0D;
		switch (sector) {
		case 0:
			r = bri;
			g = t;
			b = p;
			break;
		case 1:
			r = bri;
			g = t;
			b = p;
			break;
		case 2:
			r = q;
			g = bri;
			b = p;
			break;
		case 3:
			r = p;
			g = bri;
			b = t;
			break;
		case 4:
			r = p;
			g = q;
			b = bri;
			break;
		case 5:
			r = t;
			g = p;
			b = bri;
			break;
		case 6:
			r = bri;
			g = p;
			b = q;
		}

		return new Color(r * 255.0D, g * 255.0D, b * 255.0D);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.java.Color JD-Core Version: 0.6.0
 */