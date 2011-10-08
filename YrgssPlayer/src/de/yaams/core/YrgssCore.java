package de.yaams.core;

import java.io.File;
import java.util.HashMap;

import org.apache.commons.lang.SystemUtils;

import de.yaams.core.helper.FileHelper;
import de.yaams.core.helper.Log;
import de.yaams.core.helper.gui.YMessagesDialog;

public class YrgssCore {

	private static HashMap<String, String> setting;

	/**
	 * Version from the yaams script
	 */
	public final static String VERSION = "0.71";

	/**
	 * Name of yaams
	 */
	public final static String NAME = "Yrp";
	/**
	 * Name of yaams
	 */
	public final static String TITLE = "YAams RGSS Player 0.71";

	/**
	 * Where save the programm his options?
	 */
	public static File programPath = getFolder();

	private static File getFolder() {
		// special folder?
		File usb = FileHelper.fileRelativeExist("yaams.usb");
		if (usb != null && usb.exists() && usb.canRead()) {
			return new File(usb.getParentFile(), "yaams");
		}

		usb = FileHelper.fileRelativeExist("yaams.path");
		if (usb != null && usb.exists() && usb.canRead()) {
			return new File(FileHelper.readFile(usb));
		}

		// windows system?
		if (SystemUtils.IS_OS_WINDOWS) {
			return new File(System.getenv("APPDATA"), "YAams");
		} else if (SystemUtils.IS_OS_MAC_OSX) {
			return new File(new File(new File(SystemUtils.USER_HOME, "Library"), "Application Support"), "YAams");
		} else {
			return new File(SystemUtils.USER_HOME, ".YAams");
		}
	}

	/**
	 * In witch folder saves the temp?
	 */
	public static File tmpFolder;

	/**
	 * Init the system
	 * 
	 * @param errors
	 */
	@SuppressWarnings("unchecked")
	public static void init(YMessagesDialog errors) {

		// create it
		programPath = getFolder();
		FileHelper.checkPath(programPath, errors, true, true);
		Log.ger.info("UPath: " + programPath + " " + FileHelper.humanReadableByteCount(programPath.getFreeSpace(), false));

		// create it
		tmpFolder = new File(programPath, "tmp");
		FileHelper.checkPath(tmpFolder, errors, true, true);
		Log.ger.info("Tmp: " + tmpFolder);

		// delete tmp
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				FileHelper.deleteTree(tmpFolder);
			}
		}));

		// load settings
		setting = (HashMap<String, String>) FileHelper.loadXML(new File(programPath, "YAamsSettings.xml"));
		if (setting == null) {
			setting = new HashMap<String, String>();
		}

		log();
	}

	/**
	 * Log the main infos
	 */
	public static void log() {
		Log.ger.info(TITLE);
		Log.ger.info("Java: " + SystemUtils.JAVA_RUNTIME_NAME + " " + SystemUtils.JAVA_VERSION);
		Log.ger.info("Search OS: " + SystemUtils.OS_NAME + " " + SystemUtils.OS_VERSION + " " + SystemUtils.OS_ARCH);

	}

	/**
	 * @return the settings
	 */
	public static HashMap<String, String> getSetting() {
		return setting;
	}

	/**
	 * Save settings
	 */
	public static void save() {
		FileHelper.saveXML(new File(programPath, "YAamsSettings.xml"), setting);

		// ExtentionManagement.work("save", new HashMap<String, Object>());
	}

}
