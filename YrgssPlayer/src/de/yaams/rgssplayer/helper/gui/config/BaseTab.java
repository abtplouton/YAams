package de.yaams.rgssplayer.helper.gui.config;

import java.awt.GridLayout;

import javax.swing.JPanel;

public abstract class BaseTab extends JPanel {
	private static final long serialVersionUID = -2508472875975851555L;

	public BaseTab() {
		super(new GridLayout(1, 1));
	}

	public abstract String getTitle();

	public abstract String getIcon();
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.helper.gui.config.BaseTab JD-Core Version: 0.6.0
 */