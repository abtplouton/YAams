package de.yaams.rgssplayer.config;

import de.yaams.core.helper.AE;
import de.yaams.core.helper.I18N;
import de.yaams.core.helper.gui.YEx;
import de.yaams.core.helper.gui.form.FormButton;
import de.yaams.core.helper.gui.form.FormInfo;
import de.yaams.core.helper.gui.form.FormTextArea;
import de.yaams.core.helper.gui.form.core.FormBuilder;
import de.yaams.core.rb.RBRunTime;

public class ShellTab extends BaseTab {
	private static final long serialVersionUID = -1511668960746278912L;

	public ShellTab() {
		FormBuilder f = new FormBuilder("s");

		final FormTextArea txt = new FormTextArea("", "");

		f.addElement("basic.info",
				new FormInfo("", I18N.t("Ermöglicht das Ausführen von Code. Gebe dazu den Code unten ein und klicke auf ausführen."))
						.setSorting(-1));
		f.addElement("basic.code", txt);
		f.addElement("basic.button", new FormButton(I18N.t("Ausführen"), "ok", new AE() {

			@Override
			public void run() {
				try {
					RBRunTime.interpreter.runScriptlet(txt.getContentAsString());
				} catch (Throwable t) {
					YEx.warn("Can not run Code " + txt.getContentAsString(), t);
				}

			}
		}));

		add(f.getPanel(true));
	}

	@Override
	public String getTitle() {
		return I18N.t("Konsole");
	}

	@Override
	public String getIcon() {
		return "ruby";
	}
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.helper.gui.config.ConfigTab JD-Core Version: 0.6.0
 */