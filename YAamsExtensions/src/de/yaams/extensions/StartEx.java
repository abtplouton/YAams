/**
 * 
 */
package de.yaams.extensions;

import java.io.File;
import java.net.URISyntaxException;

import de.yaams.maker.Start;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.programm.plugins.PluginLoader;

/**
 * @author Nebli
 * 
 */
public class StartEx {

	/**
	 * @param args
	 * @throws URISyntaxException
	 */
	public static void main(final String[] args) throws URISyntaxException {

		try {
			PluginLoader.folder = new File(new File(new File(new File("src"), "de"), "yaams"), "extensions");
			// PluginManager.addFolder(new PluginFolder(, "Debug"));

			Start.main(args);
		} catch (final Throwable t) {
			YEx.lastExit("Can not run program", t);
		}

	}
}
