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

import java.awt.Dimension;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * The integer spinner is a variation on the JSpinner that is only to be used
 * for plain integer inputs. It offers some convenience constructors and
 * methods.
 * 
 * @version $Id$
 */
public class IntegerSpinner extends JSpinner {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6733633975932431098L;

	public IntegerSpinner() {
		super(new SpinnerNumberModel());
		setPreferredSize(new Dimension(60, getPreferredSize().height));
	}

	public IntegerSpinner(int val, int min, int max) {
		super(new SpinnerNumberModel(val, min, max, 1));
		setPreferredSize(new Dimension(60, getPreferredSize().height));
	}

	public IntegerSpinner(int val, int min) {
		this(val, min, Integer.MAX_VALUE);
	}

	public int intValue() {
		return ((Number) getValue()).intValue();
	}
}
