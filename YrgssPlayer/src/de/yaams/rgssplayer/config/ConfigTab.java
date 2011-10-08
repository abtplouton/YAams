package de.yaams.rgssplayer.config;

import de.yaams.core.helper.I18N;
import de.yaams.core.helper.gui.form.FormCheckbox;
import de.yaams.core.helper.gui.form.FormElement;
import de.yaams.core.helper.gui.form.FormElementChangeListener;
import de.yaams.core.helper.gui.form.core.FormBuilder;
import de.yaams.core.helper.gui.form.core.FormHeader;
import de.yaams.core.rb.RBRunTime;
import de.yaams.rgssplayer.core.java.Audio;
import de.yaams.rgssplayer.core.java.Yrgss;
import de.yaams.rgssplayer.core.render.graphic.core.Render;

public class ConfigTab extends BaseTab {
	private static final long serialVersionUID = -1511668960746278912L;

	public ConfigTab() {
		FormBuilder f = new FormBuilder("s");
		f.getHeader("basic").setTitle(I18N.t("System")).setIcon("opts").setSorting(-1);

		f.addElement("basic.smooth",
				new FormCheckbox(I18N.t("Smooth-Modus"), Yrgss.smoothmodus).addChangeListener(new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						Yrgss.smoothmodus = Boolean.valueOf(form.getContentAsString());
						Render.getWindow().setFPS(Yrgss.smoothmodus ? 40 : 20);

					}
				}));

		f.addElement("basic.debug", new FormCheckbox(I18N.t("Debug-Modus"), Yrgss.debug).addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				Yrgss.debug = Boolean.valueOf(form.getContentAsString());
				RBRunTime.interpreter.runScriptlet("$debug =" + Yrgss.debug);

			}
		}));

		f.addHeader("audio", new FormHeader(I18N.t("Audio"), "audio"));

		f.addElement("audio.sound", new FormCheckbox(I18N.t("Sound"), Yrgss.sound).addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				Yrgss.sound = Boolean.valueOf(form.getContentAsString());
				if (!Yrgss.sound) {
					Audio.bgs_stop();
					Audio.se_stop();
				}

			}
		}));

		f.addElement("audio.music", new FormCheckbox(I18N.t("Music"), Yrgss.musik).addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				Yrgss.musik = Boolean.valueOf(form.getContentAsString());
				if (!Yrgss.musik) {
					Audio.bgm_stop();
					Audio.me_stop();
				}

			}
		}));

		f.addHeader("another", new FormHeader(I18N.t("Another"), "graphic").setSorting(1));

		f.addElement("another.fullscreen",
				new FormCheckbox(I18N.t("Fullscreen Modus"), Yrgss.fullscreen).addChangeListener(new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						Yrgss.fullscreen = Boolean.valueOf(form.getContentAsString());
						Render.getWindow().setFullscreen(Yrgss.fullscreen);

					}
				}));

		f.addElement("another.active",
				new FormCheckbox(I18N.t("If the program not active, update the game also. Otherwise the game will paused"), Yrgss.active)
						.addChangeListener(new FormElementChangeListener() {

							@Override
							public void stateChanged(FormElement form) {
								Yrgss.active = Boolean.valueOf(form.getContentAsString());

							}
						}));

		f.addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				Yrgss.saveSettings();

			}
		});

		add(f.getPanel(false));
	}

	@Override
	public String getTitle() {
		return I18N.t("Options");
	}

	@Override
	public String getIcon() {
		return "opts";
	}
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.helper.gui.config.ConfigTab JD-Core Version: 0.6.0
 */