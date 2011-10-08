package com.ezware.dialog.task;

import java.awt.Graphics;

import javax.swing.JComponent;

public interface ICommandLinkPainter {

	void intialize(JComponent source);

	void paint(Graphics g, JComponent source);

}
