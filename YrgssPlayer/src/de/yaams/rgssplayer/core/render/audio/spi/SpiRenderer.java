/**
 * 
 */
package de.yaams.rgssplayer.core.render.audio.spi;

import java.io.File;

import org.apache.commons.lang.Validate;

import de.yaams.rgssplayer.core.java.Audio;
import de.yaams.rgssplayer.core.render.audio.core.ISoundFile;
import de.yaams.rgssplayer.core.render.audio.core.ISoundRenderer;

/**
 * @author abby
 * 
 */
public class SpiRenderer extends ISoundRenderer {

	/**
	 * 
	 */
	public SpiRenderer() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.rgssplayer.core.render.audio.core.ISoundRenderer#getName()
	 */
	@Override
	public String getName() {
		return "Java SPI Renderer";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.rgssplayer.core.render.audio.core.ISoundRenderer#create(java
	 * .lang.String, int, int, int)
	 */
	@Override
	public ISoundFile create(String name, int volume, int pitch, int repeat) {
		// check param
		Validate.notNull(name, "Audiofile is null");
		Validate.isTrue(volume >= 0 & volume <= 100, "Volume is wrong: " + volume, volume);
		Validate.isTrue(pitch >= 50 & pitch <= 150, "Pitch is wrong: " + pitch, pitch);

		// build it
		File file = Audio.searchAudio(name);

		// midi?
		if (file.getName().endsWith(".mid") || file.getName().endsWith(".midi")) {
			return new MidiAudio(file, volume, pitch, repeat);
		}

		// get it
		return new PlayAudio(file, volume, pitch, repeat);
	}

}
