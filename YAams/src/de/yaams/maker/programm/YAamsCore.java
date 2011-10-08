package de.yaams.maker.programm;

import java.io.File;
import java.util.HashMap;

import org.apache.commons.lang.SystemUtils;

import de.yaams.maker.helper.FileHelper;
import de.yaams.maker.helper.Log;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.gui.YMessagesDialog;

public class YAamsCore {

	private static HashMap<String, String> setting;

	/**
	 * Is this a beta version?
	 */
	public final static boolean BETA = false;

	/**
	 * Version from the yaams script
	 */
	public final static Double VERSION = 0.0053;

	/**
	 * Name of yaams
	 */
	public final static String NAME = "YAams";
	/**
	 * Name + Version of yaams
	 */
	public final static String TITLE = "YAams 0.0053 (Qilin)";

	/**
	 * Where save the programm his options?
	 */
	public final static File programPath = getFolder();

	/**
	 * In witch folder saves the temp?
	 */
	public final static File tmpFolder = new File(programPath, "tmp");

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
	 * Init the system
	 * 
	 * @param errors
	 */
	@SuppressWarnings("unchecked")
	public static void init(YMessagesDialog errors) {

		// create it
		// programPath = getFolder();
		FileHelper.checkPath(programPath, errors, true, true);
		Log.ger.info("UPath: " + programPath + " " + FileHelper.humanReadableByteCount(programPath.getFreeSpace(), false));

		// create it
		// tmpFolder = new File(programPath, "tmp");
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

		ExtentionManagement.work(ExtentionManagement.SAVE, new HashMap<String, Object>());
	}

}
