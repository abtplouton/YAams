/**
 * 
 */
package de.yaams.extensions.rgssproject.map;

import de.yaams.maker.helper.gui.YMessagesDialog;
import de.yaams.maker.programm.plugins.core.BasePlugin;

/**
 * @author abt
 * 
 */
public class MapStartPlugin extends BasePlugin {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.core.helper.plugins.BasePlugin#start()
	 */
	@Override
	public void start() {
		new MapPlugin();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.core.helper.plugins.BasePlugin#useable(de.yaams.core.helper.
	 * gui.YMessagesDialog, int)
	 */
	@Override
	public boolean useable(YMessagesDialog md) {
		return isMinVersionInstall("core", 0.004, md) && isMinVersionInstall("database", 0.013, md);
	}

}
