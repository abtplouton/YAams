/**
 * 
 */
package de.yaams.extensions.rgssproject.map.nevent.core;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author abt
 * 
 */
public class EventCommandManagement {

	protected static HashMap<Integer, EventCommand> codes = new HashMap<Integer, EventCommand>();
	protected static ArrayList<EventCommand> commands = new ArrayList<EventCommand>();

	/**
	 * Register a event command
	 * 
	 * @param id
	 * @param ybe
	 */
	public static void register(int id, EventCommand ybe, boolean newCommand) {
		ybe.setId(id);
		codes.put(id, ybe);
		// add to list?
		if (newCommand) {
			commands.add(ybe);
		}
	}

	/**
	 * Register a event command
	 * 
	 * @param id
	 * @param ybe
	 */
	public static void registerM(EventCommand ybe, int... id) {
		// add all
		for (int i = 0, l = id.length; i < l; i++) {
			register(id[i], ybe, i == 0);
		}
	}

	/**
	 * Get the command for this code
	 * 
	 * @param id
	 */
	public static EventCommand get(int id) {
		if (!codes.containsKey(id)) {
			return codes.get(-1);
		} else {
			return codes.get(id);
		}
	}

	/**
	 * @return the commands
	 */
	public static ArrayList<EventCommand> getCommands() {
		return commands;
	}
}
