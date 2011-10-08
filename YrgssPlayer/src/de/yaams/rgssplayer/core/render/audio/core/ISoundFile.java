package de.yaams.rgssplayer.core.render.audio.core;

import java.io.File;

public abstract class ISoundFile {
	protected Thread fadeOut;
	protected File file;
	protected int volume;
	protected int pitch;
	protected int repeat;

	public ISoundFile(File file, int volume, int pitch, int repeat) {
		this.file = file;
		this.volume = volume;
		this.pitch = pitch;
		this.repeat = repeat;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public int getPitch() {
		return pitch;
	}

	public void setPitch(int pitch) {
		this.pitch = pitch;
	}

	public abstract void play();

	public abstract void stop();

	public abstract boolean isPlaying();

	public int getRepeat() {
		return repeat;
	}

	public void fadeOut(final int time) {
		fadeOut = new Thread(new Runnable() {
			@Override
			public void run() {
				int w = time / ISoundFile.this.getVolume();
				for (int i = ISoundFile.this.getVolume(); i >= 0; i--) {
					try {
						Thread.sleep(w);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					ISoundFile.this.setVolume(i);
				}
				ISoundFile.this.stop();
			}

		});
		fadeOut.start();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ISoundFile [file=");
		builder.append(file);
		builder.append(", volume=");
		builder.append(volume);
		builder.append(", pitch=");
		builder.append(pitch);
		builder.append(", playing=");
		builder.append(isPlaying());
		builder.append(", repeat=");
		builder.append(repeat);
		builder.append("]");
		return builder.toString();
	}

}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.render.audio.core.ISoundFile JD-Core Version: 0.6.0
 */