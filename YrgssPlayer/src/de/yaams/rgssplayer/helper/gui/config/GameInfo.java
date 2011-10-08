package de.yaams.rgssplayer.helper.gui.config;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import de.yaams.core.helper.Branding;
import de.yaams.rgssplayer.core.java.Yrgss;

public class GameInfo extends BaseTab {
	private static final long serialVersionUID = 3946611004093014239L;

	public GameInfo() {
		setLayout(new BorderLayout());
		add(new JLabel("<html><b>" + Yrgss.game.getName() + "</b>"), "North");
		add(new JLabel("<html>" + Branding.get(Yrgss.game, "desc", "")), "North");
		add(new JLabel("<html><b>" + Branding.get(Yrgss.game, "homepage", "http://www.yaams.de") + "</b>"), "South");
	}

	@Override
	public String getTitle() {
		return Yrgss.game.getName();
	}

	@Override
	public String getIcon() {
		return ConfigWindow.getIcon();
	}
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.helper.gui.config.GameInfo JD-Core Version: 0.6.0
 */