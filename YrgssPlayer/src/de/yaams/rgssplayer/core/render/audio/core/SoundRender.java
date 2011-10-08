package de.yaams.rgssplayer.core.render.audio.core;

import de.yaams.core.helper.Log;

public class SoundRender {
	protected static ISoundRenderer renderer;

	public static ISoundRenderer er() {
		return renderer;
	}

	public static void set(ISoundRenderer renderer) {
		Log.ger.info("Sound Renderer: " + renderer.getName());
		SoundRender.renderer = renderer;
	}
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.render.audio.core.SoundRender JD-Core Version: 0.6.0
 */