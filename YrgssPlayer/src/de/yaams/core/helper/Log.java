/**
 * 
 */
package de.yaams.core.helper;

import java.io.File;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import de.yaams.core.YrgssCore;
import de.yaams.core.helper.gui.YEx;

/**
 * @author Nebli
 * 
 */
public class Log {

	public static final File file = new File(YrgssCore.programPath, "log.log");
	public static final Logger ger = getLogger();

	/**
	 * 
	 */
	private static Logger getLogger() {
		Logger logger = Logger.getRootLogger();
		try {
			PatternLayout layout = new PatternLayout("%d{HH:mm:ss} %-5p [%t] %m%n");
			ConsoleAppender consoleAppender = new ConsoleAppender(layout);
			logger.addAppender(consoleAppender);
			FileAppender fileAppender = new FileAppender(layout, file.getAbsolutePath(), false);
			logger.addAppender(fileAppender);
			logger.setLevel(Level.INFO);
			logger.info("Logginglevel: " + logger.getLevel());
		} catch (Exception ex) {
			YEx.error("Can not generate log", ex);
		}
		return logger;
	}
}
