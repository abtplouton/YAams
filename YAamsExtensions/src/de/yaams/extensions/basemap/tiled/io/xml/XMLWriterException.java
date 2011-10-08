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

package de.yaams.extensions.basemap.tiled.io.xml;

/**
 * @version $Id$
 */
public class XMLWriterException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2026395105362077655L;

	public XMLWriterException(String error) {
		super(error);
	}
}
