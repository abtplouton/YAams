/**
 * 
 */
package de.yaams.rgssplayer.core.render.audio.spi;

import java.io.File;

import de.yaams.core.helper.gui.YEx;
import de.yaams.rgssplayer.core.render.audio.core.ISoundFile;

/**
 * @author abby
 * 
 */
public abstract class BaseAudio extends ISoundFile {

	protected Thread thread;
	protected boolean willStop;

	/**
	 * 
	 * @param file
	 * @param volume
	 * @param pitch
	 * @param repeat
	 */
	public BaseAudio(File file, int volume, int pitch, int repeat) {
		super(file, volume, pitch, repeat);
	}

	/**
	 * Start to play the selected file it
	 * 
	 * @param file
	 */
	@Override
	public void play() {
		willStop = false;
		// is running?
		if (thread != null) {
			return;
		}
		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (repeat > 0) {
					repeat--;
					try {
						playIt();
						setVolume(getVolume());
					} catch (Throwable t) {
						YEx.info("Can not play " + file, t);
					}
				}
			}
		});
		thread.start();
	}

	/**
	 * Overwrite to implement own play method
	 * 
	 * @param filename
	 */
	protected abstract void playIt() throws Throwable;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.rgssplayer.core.render.audio.core.ISoundFile#stop()
	 */
	@Override
	public void stop() {
		willStop = true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.rgssplayer.core.render.audio.core.ISoundFile#isPlaying()
	 */
	@Override
	public boolean isPlaying() {
		return thread != null;
	}
}
