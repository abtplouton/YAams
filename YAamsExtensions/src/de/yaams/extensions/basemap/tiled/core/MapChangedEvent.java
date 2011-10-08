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

package de.yaams.extensions.basemap.tiled.core;

import java.util.EventObject;

/**
 * @version $Id$
 */
public class MapChangedEvent extends EventObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -958810147253176691L;

	public MapChangedEvent(Map map) {
		super(map);
	}

	public Map getMap() {
		return (Map) getSource();
	}
}
