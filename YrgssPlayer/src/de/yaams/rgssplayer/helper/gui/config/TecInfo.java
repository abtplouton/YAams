package de.yaams.rgssplayer.helper.gui.config;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.SystemUtils;

import de.yaams.core.YAamsCore;
import de.yaams.core.helper.Branding;
import de.yaams.core.helper.I18N;
import de.yaams.core.helper.Log;
import de.yaams.rgssplayer.core.java.Yrgss;
import de.yaams.rgssplayer.core.render.audio.core.SoundRender;
import de.yaams.rgssplayer.core.render.graphic.core.Render;

public class TecInfo extends BaseTab {
	private static final long serialVersionUID = -4052662243502342999L;
	DefaultTableModel model;

	public TecInfo() {
		this.model = new DefaultTableModel();

		this.model.setColumnIdentifiers(new Object[] { I18N.t("Name"), I18N.t("Value") });

		addV("Name", Yrgss.game.getName());
		addV("Path", Yrgss.game.getPath());
		addV("RGSS-Version", Integer.valueOf(Yrgss.game.getRgssVersion()));
		addV("Ruby Version", "1.9.2");
		addV("JRuby Version", "1.6.3");
		addV("YAams RGSS Player", Double.valueOf(YAamsCore.yaamsversion));
		addV("YAams Path", YAamsCore.programPath);
		addV("Game Author", Branding.get(Yrgss.game, "author", "?"));
		addV("Game Homepage", Branding.get(Yrgss.game, "homepage", "?"));
		addV("Graphic Renderer", Render.er().getName());
		addV("Sound Renderer", SoundRender.er().getName());
		addV("Width x Height", Render.getWindow().width() + " x " + Render.getWindow().height());
		addV("System", SystemUtils.OS_NAME + " " + SystemUtils.OS_VERSION);
		addV("Java", SystemUtils.JAVA_VENDOR + " " + SystemUtils.JAVA_VERSION);
		addV("Log", Log.file.getAbsoluteFile());

		add(new JScrollPane(new JTable(this.model)));
	}

	protected void addV(String name, Object value) {
		this.model.addRow(new Object[] { I18N.t(name), value });
	}

	@Override
	public String getTitle() {
		return I18N.t("System Info");
	}

	@Override
	public String getIcon() {
		return "info";
	}
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.helper.gui.config.TecInfo JD-Core Version: 0.6.0
 */