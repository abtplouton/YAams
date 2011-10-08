package de.yaams.extensions.ress.audioplayer;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * 
 */

/**
 * @author abby
 * 
 */
public class PlayAudio extends BaseAudio {

	protected SourceDataLine line;

	/**
	 * 
	 * @param file
	 * @param volume
	 * @param pitch
	 * @param repeat
	 */
	public PlayAudio(File file, int volume, int pitch, int repeat) {
		super(file, volume, pitch, repeat);
	}

	/**
	 * http://www.javazoom.net/vorbisspi/documents.html
	 * 
	 * @param filename
	 */
	@Override
	protected void playIt() {
		try {
			// Get AudioInputStream from given file.
			AudioInputStream in = AudioSystem.getAudioInputStream(file);
			AudioInputStream din = null;
			if (in != null) {
				AudioFormat baseFormat = in.getFormat();
				AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
						baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
				// Get AudioInputStream that will be decoded by underlying
				// VorbisSPI
				din = AudioSystem.getAudioInputStream(decodedFormat, in);
				// Play now !
				rawplay(decodedFormat, din);
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException {
		byte[] data = new byte[4096];
		line = getLine(targetFormat);
		if (line != null) {
			// Start
			line.start();
			int nBytesRead = 0;
			while (nBytesRead != -1) {
				nBytesRead = din.read(data, 0, data.length);
				if (nBytesRead != -1) {
					line.write(data, 0, nBytesRead);
				}
				// stop it?
				if (willStop) {
					break;
				}
			}
			// Stop
			line.drain();
			line.stop();
			line.close();
			din.close();
		}
		thread = null;
	}

	private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException {
		SourceDataLine res = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		res = (SourceDataLine) AudioSystem.getLine(info);
		res.open(audioFormat);
		return res;
	}

	/**
	 * @param volume
	 *            the volume to set
	 */
	@Override
	public void setVolume(int volume) {
		super.setVolume(volume);

		FloatControl gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
		// number between 0 and 1 (loudest)
		float dB = (float) (Math.log(volume / 100f) / Math.log(10.0) * 20.0);
		gainControl.setValue(dB);
	}
}
