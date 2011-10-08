package de.yaams.rgssplayer.core.java;

import de.yaams.core.helper.Log;

public class Tone implements Cloneable {
	public double red;
	public double green;
	public double blue;
	public double gray;

	public Tone(double red, double green, double blue) {
		set(red, green, blue);
		this.gray = 0.0D;
	}

	public Tone(double red, double green, double blue, double gray) {
		set(red, green, blue, gray);
	}

	public void set(double red, double green, double blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public void set(double red, double green, double blue, double gray) {
		set(red, green, blue);
		this.gray = gray;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Tone other = (Tone) obj;
		if (Double.doubleToLongBits(this.blue) != Double.doubleToLongBits(other.blue)) {
			return false;
		}
		if (Double.doubleToLongBits(this.gray) != Double.doubleToLongBits(other.gray)) {
			return false;
		}
		if (Double.doubleToLongBits(this.green) != Double.doubleToLongBits(other.green)) {
			return false;
		}
		return Double.doubleToLongBits(this.red) == Double.doubleToLongBits(other.red);
	}

	@Override
	public Tone clone() {
		try {
			return (Tone) super.clone();
		} catch (CloneNotSupportedException e) {
			Log.ger.warn(e);
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Tone [red=");
		builder.append(this.red);
		builder.append(", green=");
		builder.append(this.green);
		builder.append(", blue=");
		builder.append(this.blue);
		builder.append(", gray=");
		builder.append(this.gray);
		builder.append("]");
		return builder.toString();
	}
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.java.Tone JD-Core Version: 0.6.0
 */