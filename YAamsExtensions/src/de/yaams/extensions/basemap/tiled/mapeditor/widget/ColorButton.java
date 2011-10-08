/*
 * Tiled Map Editor, (c) 2004-2006
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * Adam Turk <aturk@biggeruniverse.com> Bjorn Lindeijer <bjorn@lindeijer.nl>
 */

package de.yaams.extensions.basemap.tiled.mapeditor.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * A button with an associated color. The color is displayed as the background
 * of the button and clicking the button spawns a color chooser dialog to allow
 * changing the associated color.
 */
public class ColorButton extends JButton implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3912085341846333778L;

	public ColorButton(Color initialColor) {
		setBackground(initialColor);
		addActionListener(this);
	}

	public ColorButton() {
		this(Color.white);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(40, 15);
	}

	/**
	 * Sets the new color of this button.
	 */
	public void setColor(Color color) {
		setBackground(color);
	}

	/**
	 * Gets the color of this button.
	 */
	public Color getColor() {
		return getBackground();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// Spawn a color chooser dialog
	}
}
