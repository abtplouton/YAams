package de.yaams.rgssplayer.config;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import de.yaams.core.helper.Branding;
import de.yaams.core.helper.I18N;
import de.yaams.core.helper.gui.YEx;
import de.yaams.core.helper.gui.YHeader;
import de.yaams.core.helper.gui.icons.IconCache;
import de.yaams.rgssplayer.core.java.Yrgss;

public class ConfigWindow {
	private final ArrayList<BaseTab> tabs;

	public ConfigWindow() {
		JFrame f = new JFrame(I18N.t("Options"));
		f.setLayout(new BorderLayout());
		f.add(new YHeader(Yrgss.game.getName(), getIcon()), BorderLayout.NORTH);

		this.tabs = new ArrayList<BaseTab>();
		this.tabs.add(new GameInfo());
		this.tabs.add(new ConfigTab());
		this.tabs.add(new TecInfo());
		this.tabs.add(new FeedbackTab());
		// add debug info?
		if (Yrgss.debug) {
			this.tabs.add(new ShellTab());
		}

		JTabbedPane tab = new JTabbedPane();
		for (BaseTab t : this.tabs) {
			try {
				tab.addTab(t.getTitle(), IconCache.get(t.getIcon()), t);
			} catch (Throwable th) {
				YEx.info("Can not create tab " + t, th);
			}
		}
		f.add(tab, "Center");

		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	public static String getIcon() {
		return Branding.get(Yrgss.game, "icon", "yrgss");
	}
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.helper.gui.config.ConfigWindow JD-Core Version: 0.6.0
 */