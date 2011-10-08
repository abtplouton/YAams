package de.yaams.core.helper.gui;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

public class YScrollPane extends JScrollPane {
	private static final long serialVersionUID = 805747432091480594L;

	public YScrollPane() {
		removeBorder();
	}

	public YScrollPane(Component arg0) {
		super(arg0);
		removeBorder();
	}

	public YScrollPane(int arg0, int arg1) {
		super(arg0, arg1);
		removeBorder();
	}

	public YScrollPane(Component arg0, int arg1, int arg2) {
		super(arg0, arg1, arg2);
		removeBorder();
	}

	protected void removeBorder() {
		setBorder(BorderFactory.createEmptyBorder());
	}
}