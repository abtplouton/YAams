/**
 * 
 */
package de.yaams.maker.helper;

import java.io.File;
import java.util.Date;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.programm.YAamsCore;

/**
 * @author Nebli
 * 
 */
public class Log {

	public static final File file = new File(YAamsCore.programPath, "log.log");
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
			logger.info("Date: " + new Date());
		} catch (Exception ex) {
			YEx.error("Can not generate log", ex);
		}
		return logger;
	}
}
