package de.yaams.extensions.ress.audioplayer;

import java.io.File;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;

/**
 * 
 */

/**
 * @author abby
 * 
 */
public class MidiAudio extends BaseAudio {

	protected Sequencer sequencer;
	protected Synthesizer synthesizer;

	/**
	 * @param file
	 */
	public MidiAudio(File file, int volume, int pitch, int repeat) {
		super(file, volume, pitch, repeat);
	}

	/**
	 * http://www.jsresources.org/examples/LoopingMidiPlayer15.java.html
	 * 
	 * @param filename
	 */
	@Override
	protected void playIt() throws Throwable {
		/*
		 * We read in the MIDI file to a Sequence object. This object is set at
		 * the Sequencer later.
		 */
		Sequence sequence = MidiSystem.getSequence(file);

		/*
		 * Now, we need a Sequencer to play the sequence. Here, we simply
		 * request the default sequencer without an implicitly connected
		 * synthesizer
		 */
		sequencer = MidiSystem.getSequencer(false);

		/*
		 * The Sequencer is still a dead object. We have to open() it to become
		 * live. This is necessary to allocate some ressources in the native
		 * part.
		 */
		sequencer.open();

		/*
		 * Next step is to tell the Sequencer which Sequence it has to play. In
		 * this case, we set it as the Sequence object created above.
		 */
		sequencer.setSequence(sequence);

		/*
		 * We try to get the default synthesizer, open() it and chain it to the
		 * sequencer with a Transmitter-Receiver pair.
		 */
		synthesizer = MidiSystem.getSynthesizer();
		synthesizer.open();
		Receiver synthReceiver = synthesizer.getReceiver();
		Transmitter seqTransmitter = sequencer.getTransmitter();
		seqTransmitter.setReceiver(synthReceiver);

		/*
		 * To free system resources, it is recommended to close the synthesizer
		 * and sequencer properly.
		 * 
		 * To accomplish this, we register a Listener to the Sequencer. It is
		 * called when there are "meta" events. Meta event 47 is end of track.
		 * 
		 * Thanks to Espen Riskedal for finding this trick.
		 */
		sequencer.addMetaEventListener(new MetaEventListener() {
			@Override
			public void meta(MetaMessage event) {
				if (event.getType() == 47) {
					sequencer.close();
					if (synthesizer != null) {
						synthesizer.close();
					}
					// System.exit(0);
				}
			}
		});

		/*
		 * Now, we can start over.
		 */
		sequencer.start();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.yrgss.core.render.audio.core.ISoundFile#stop()
	 */
	@Override
	public void stop() {
		if (sequencer != null) {
			sequencer.stop();
			sequencer = null;
		}

		if (thread != null) {
			thread.interrupt();
			thread = null;
		}
	}

	/**
	 * @param volume
	 *            the volume to set
	 */
	@Override
	public void setVolume(int volume) {
		super.setVolume(volume);
		if (sequencer != null) {
			MidiChannel[] channels = synthesizer.getChannels();

			// gain is a value between 0 and 1 (loudest)
			double gain = volume / 100D;
			for (int i = 0; i < channels.length; i++) {
				channels[i].controlChange(7, (int) (gain * 127.0));
			}
		}
	}
}
