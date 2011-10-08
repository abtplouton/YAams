package de.yaams.rgssplayer.core.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import de.yaams.core.helper.gui.YDialog;
import de.yaams.core.helper.gui.YEx;
import de.yaams.rgssplayer.core.render.audio.core.ISoundFile;
import de.yaams.rgssplayer.core.render.audio.core.SoundRender;

public class Audio {
	protected static LinkedList<ISoundFile> se = new LinkedList<ISoundFile>();
	protected static ISoundFile bgm;
	protected static ISoundFile bgs;
	protected static ISoundFile me;

	public static void bgm_play(String filename) {
		bgm_play(filename, 100, 100);
	}

	public static void bgm_play(String filename, int volume) {
		bgm_play(filename, volume, 100);
	}

	public static void bgm_play(String filename, int volume, int pitch) {
		bgm_stop();

		if (!Yrgss.musik || filename == null) {
			return;
		}

		bgm = SoundRender.er().create(filename, volume, pitch, -1);
		if (bgm != null) {
			bgm.play();
		}
	}

	public static File searchAudio(String filename) {
		File f = null;

		String[] end = { "", ".ogg", ".mp3", ".wav", ".mid", ".midi" };
		for (String s : end) {
			f = new File(Yrgss.game.getPath(), filename + s);
			if (f.exists()) {
				return f;
			}
		}

		if (Yrgss.game.getRtp().size() > 0) {
			for (File rtp : Yrgss.game.getRtp().values()) {
				for (String s : end) {
					f = new File(rtp, filename + s);
					if (f.exists()) {
						return f;
					}
				}
			}
		}
		YEx.info("IO audio", new FileNotFoundException("Audio " + new File(Yrgss.game.getPath(), filename).getAbsolutePath()
				+ " don't exist"));

		// get result
		Object o = YDialog.fileNotFound(new File(Yrgss.game.getPath(), filename + end[2]), filename);
		if (o instanceof File) {
			return (File) o;
		}
		if (o.equals(-2)) {
			return searchAudio(filename);
		}

		return null;
	}

	public static void bgm_stop() {
		if (bgm != null && bgm.isPlaying()) {
			bgm.stop();
		}
	}

	public static void bgm_fade(int time) {
		if (bgm != null && bgm.isPlaying()) {
			bgm.fadeOut(time);
		}
	}

	public static void bgs_play(String filename) {
		bgs_play(filename, 100, 100);
	}

	public static void bgs_play(String filename, int volume) {
		bgs_play(filename, volume, 100);
	}

	public static void bgs_play(String filename, int volume, int pitch) {
		bgs_stop();

		if (!Yrgss.sound || filename == null) {
			return;
		}

		bgs = SoundRender.er().create(filename, volume, pitch, -1);
		if (bgs != null) {
			bgs.play();
		}
	}

	public static void bgs_stop() {
		if (bgs != null && bgs.isPlaying()) {
			bgs.stop();
		}
	}

	public static void bgs_fade(int time) {
		if (bgs != null && bgs.isPlaying()) {
			bgs.fadeOut(time);
		}
	}

	public static void me_play(String filename) {
		me_play(filename, 100, 100);
	}

	public static void me_play(String filename, int volume) {
		me_play(filename, volume, 100);
	}

	public static void me_play(String filename, int volume, int pitch) {
		me_stop();

		if (!Yrgss.musik || filename == null) {
			return;
		}

		me = SoundRender.er().create(filename, volume, pitch, 1);
		if (me != null) {
			me.play();
		}
	}

	public static void me_stop() {
		if (me != null && me.isPlaying()) {
			me.stop();
		}
	}

	public static void me_fade(int time) {
		if (me != null && me.isPlaying()) {
			me.fadeOut(time);
		}
	}

	public static void se_play(String filename) {
		se_play(filename, 100, 100);
	}

	public static void se_play(String filename, int volume) {
		se_play(filename, volume, 100);
	}

	public static void se_play(String filename, int volume, int pitch) {
		if (!Yrgss.sound || filename == null) {
			return;
		}

		ISoundFile s = SoundRender.er().create(filename, volume, pitch, 1);
		if (s != null) {
			s.play();
			se.add(s);
		}
	}

	public static void se_stop() {
		for (ISoundFile s : se) {
			if (s.isPlaying()) {
				s.stop();
			}
		}

		se.clear();
	}
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.java.Audio JD-Core Version: 0.6.0
 */