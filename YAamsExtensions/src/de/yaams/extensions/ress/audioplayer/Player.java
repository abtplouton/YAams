/**
 * 
 */
package de.yaams.extensions.ress.audioplayer;

import java.io.File;

import de.yaams.maker.helper.gui.AE;

/**
 * @author abby
 * 
 */
public class Player {

	protected Thread thread;
	protected File file;
	protected Exception ex;
	protected BaseAudio audio;

	/**
	 * Code will called, when the event startes
	 */
	protected AE endCode, errorCode;

	/**
	 * 
	 */
	public Player(File file) {
		this.file = file;
	}

	/**
	 * Play the file
	 */
	public void start() {
		if (thread != null) {
			return;
		}

		// build it

		// midi?
		if (file.getName().endsWith(".mid") || file.getName().endsWith(".midi")) {
			audio = new MidiAudio(file, 100, 100, 1);
		}

		// get it
		audio = new PlayAudio(file, 100, 100, 1);
		audio.play(errorCode);

	}

	/**
	 * Stop it
	 */
	public void stop() {
		thread.interrupt();
		thread = null;
	}

	/**
	 * @return the ex
	 */
	public Exception getEx() {
		return ex;
	}

	/**
	 * @param endCode
	 *            the endCode to set
	 */
	public void setEndCode(AE endCode) {
		this.endCode = endCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(AE errorCode) {
		this.errorCode = errorCode;
	}
}
