package de.yaams.rgssplayer.core.java;

import de.yaams.rgssplayer.core.render.graphic.core.Render;

public class YInput {
	public static int DOWN = 2;
	public static int LEFT = 4;
	public static int RIGHT = 6;
	public static int UP = 8;

	public static int A = 11;
	public static int B = 12;
	public static int C = 13;
	public static int X = 14;
	public static int Y = 15;
	public static int Z = 16;
	public static int L = 17;
	public static int R = 18;

	public static int SHIFT = 21;
	public static int CTRL = 22;
	public static int ALT = 23;

	public static int F5 = 25;
	public static int F6 = 26;
	public static int F7 = 27;
	public static int F8 = 28;
	public static int F9 = 29;

	private static final Boolean[] keyDown = new Boolean[F9 + 1];

	private static final Boolean[] keyCanSetToUsed = new Boolean[F9 + 1];

	private static final Boolean[] keyUsed = new Boolean[F9 + 1];
	// private static final int repeatWaitTime = 250;
	private static final long[] keyTime = new long[F9 + 1];

	public static void update() {
		for (int i = keyCanSetToUsed.length - 1; i >= 0; i--) {
			if (keyCanSetToUsed[i].booleanValue()) {
				keyCanSetToUsed[i] = Boolean.valueOf(false);
				keyUsed[i] = Boolean.valueOf(true);
			}
		}

		Render.er().inputUpdate();
	}

	public static boolean isRepeated(int num) {
		if (keyDown[num] == null) {
			return false;
		}

		if (keyDown[num].booleanValue() && keyTime[num] + 250L <= System.currentTimeMillis()) {
			keyTime[num] = System.currentTimeMillis();
			return true;
		}

		return false;
	}

	public static boolean isPressed(int num) {
		if (keyDown[num] == null) {
			return false;
		}

		return keyDown[num].booleanValue();
	}

	public static boolean isTrigger(int num) {
		if (keyDown[num] == null) {
			return false;
		}

		if (keyDown[num].booleanValue() && (keyUsed[num] == null || !keyUsed[num].booleanValue())) {
			keyCanSetToUsed[num] = Boolean.valueOf(true);
			return true;
		}

		return false;
	}

	public static int dir4() {
		if (isPressed(DOWN)) {
			return 2;
		}
		if (isPressed(LEFT)) {
			return 4;
		}
		if (isPressed(UP)) {
			return 8;
		}
		if (isPressed(RIGHT)) {
			return 6;
		}
		return 0;
	}

	public static int dir8() {
		if (isPressed(DOWN) && isPressed(LEFT)) {
			return 1;
		}
		if (isPressed(LEFT) && isPressed(UP)) {
			return 7;
		}
		if (isPressed(UP) && isPressed(RIGHT)) {
			return 9;
		}
		if (isPressed(RIGHT) && isPressed(DOWN)) {
			return 3;
		}
		return dir4();
	}

	public static void setKeyDown(int code) {
		keyDown[code] = Boolean.valueOf(true);
	}

	public static void setKeyUp(int code) {
		keyDown[code] = Boolean.valueOf(false);
		keyUsed[code] = Boolean.valueOf(false);
	}
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.java.YInput JD-Core Version: 0.6.0
 */