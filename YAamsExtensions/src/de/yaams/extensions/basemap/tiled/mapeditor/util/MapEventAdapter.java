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

package de.yaams.extensions.basemap.tiled.mapeditor.util;

import java.awt.Component;
import java.util.LinkedList;
import java.util.ListIterator;

public class MapEventAdapter {
	public static final int ME_MAPACTIVE = 1;
	public static final int ME_MAPINACTIVE = 2;

	private LinkedList<Component> listeners;

	public MapEventAdapter() {
		listeners = new LinkedList<Component>();
	}

	/**
	 * Adds a Component to the list of listeners of map events. Checks that the
	 * component is not already in the list.
	 * 
	 * @param obj
	 *            the listener to add
	 */
	public void addListener(Component obj) {
		/*
		 * Small sanity check - don't add it if it's already there. Really only
		 * useful to the removeListener() func, as LinkedList.remove() only
		 * removes the first instance of a given object.
		 */
		if (listeners.indexOf(obj) == -1) {
			listeners.add(obj);
		}
	}

	/**
	 * Removes a component from the list of listeners.
	 * 
	 * @param obj
	 *            the Component to remove
	 */
	public void removeListener(Component obj) {
		listeners.remove(obj);
	}

	/**
	 * Fires an event to notify all listeners.
	 * 
	 * @param type
	 *            the event type
	 */
	public void fireEvent(int type) {
		// TODO: the idea is to extend this to allow for a multitude of
		// different event types at some point...
		if (type == ME_MAPACTIVE) {
			enableEvent();
		} else if (type == ME_MAPINACTIVE) {
			disableEvent();
		}
	}

	private void enableEvent() {
		Component c;
		ListIterator<Component> li = listeners.listIterator();
		while (li.hasNext()) {
			c = li.next();
			c.setEnabled(true);
		}
	}

	private void disableEvent() {
		Component c;
		ListIterator<Component> li = listeners.listIterator();
		while (li.hasNext()) {
			c = li.next();
			c.setEnabled(false);
		}
	}
}
