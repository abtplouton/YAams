/**
 * 
 */
package de.yaams.extensions.diamant.graph;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import de.yaams.extensions.diamant.Project;
import de.yaams.extensions.diamant.helper.Log;

/**
 * @author abby
 * 
 */
public class Plays {

	protected static Thread music;
	protected static File folder = new File(new File(Project.project.getFile(), "Ressources"), "Music");

	/**
	 * Play Background music
	 * 
	 * @param file
	 */
	public static void playMusic(final String file) {

		// stop it?
		if (music != null) {
			music.interrupt();
			music.suspend();
		}

		music = new Thread(new Runnable() {

			@Override
			public void run() {
				if (!file.equals("")) {
					play(file);
				}
				while (true) {
					play("theme" + Math.round(Math.random() * 7) + ".ogg");
				}

			}
		});
		music.start();
	}

	public static void playSound(final String file) {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				play(file);

			}
		});
		t.start();
	}

	protected static void play(String filename) {
		try {
			AudioInputStream in = AudioSystem.getAudioInputStream(new File(folder, filename));
			AudioInputStream din = null;
			AudioFormat baseFormat = in.getFormat();
			AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
					baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
			din = AudioSystem.getAudioInputStream(decodedFormat, in);
			// Play now.
			rawplay(decodedFormat, din);
			in.close();
		} catch (Exception e) {
			Log.ger.info("Cant play " + filename, e);
		}
	}

	private static void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException {
		byte[] data = new byte[4096];
		SourceDataLine line = getLine(targetFormat);
		if (line != null) {
			// Start
			line.start();
			int nBytesRead = 0;// , nBytesWritten = 0;
			while (nBytesRead != -1) {
				nBytesRead = din.read(data, 0, data.length);
				if (nBytesRead != -1) {
					// nBytesWritten =
					line.write(data, 0, nBytesRead);
				}
			}
			// Stop
			line.drain();
			line.stop();
			line.close();
			din.close();
		}
	}

	private static SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException {
		SourceDataLine res = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		res = (SourceDataLine) AudioSystem.getLine(info);
		res.open(audioFormat);
		return res;
	}
}
