/**
 * 
 */
package de.yaams.core;

import java.io.File;
import java.util.HashMap;

import org.apache.commons.lang.SystemUtils;
import org.ini4j.Wini;

import de.yaams.core.helper.FileHelper;
import de.yaams.core.helper.I18N;
import de.yaams.core.helper.Log;
import de.yaams.core.helper.NetHelper;
import de.yaams.core.helper.SwingHelper;
import de.yaams.core.helper.SystemHelper;
import de.yaams.core.helper.WindowsRegistry;
import de.yaams.core.helper.gui.YActionDialog;
import de.yaams.core.helper.gui.YDialog;
import de.yaams.core.helper.gui.YEx;
import de.yaams.core.helper.gui.YProgressWindowRepeat;
import de.yaams.core.helper.integration.EditorIntegration;

/**
 * @author Praktikant
 * 
 */
public class RTP {

	public final static String TITLE = g("Titles"), WINDOWSKIN = g("Windowskins"), GAMEOVER = g("Gameovers"),
			TRANSITION = g("Transitions"), TILESET = g("Tilesets"), PANORAMA = g("Panoramas"), ICON = g("Icons"), FOG = g("Fogs"),
			CHARACTER = g("Characters"), BATTLERS = g("Battlers"), BATTLEBACK = g("Battlebacks"), AUTOTILE = g("Autotiles"),
			ANIMATION = g("Animations");

	public final static String BGM = a("BGM"), BGS = a("BGS"), ME = a("ME"), SE = a("SE");

	public static final HashMap<Integer, HashMap<String, File>> rtps = get();

	/**
	 * Check installed rtp & options
	 */
	protected static HashMap<Integer, HashMap<String, File>> get() {
		// read all rtps
		HashMap<Integer, HashMap<String, File>> rtp = new HashMap<Integer, HashMap<String, File>>();
		rtp.put(1, new HashMap<String, File>());
		rtp.put(2, new HashMap<String, File>());

		return rtp;
	}

	/**
	 * Helpermethod
	 * 
	 * @return
	 */
	private static String g(String folder) {
		return "Graphics" + File.separator + folder;
	}

	/**
	 * Helpermethod
	 * 
	 * @return
	 */
	private static String a(String folder) {
		return "Audio" + File.separator + folder;
	}

	/**
	 * Helpermethod to check the rtps from the project and check, if their
	 * exist, and will ask the user to install
	 * 
	 * @param p
	 */
	public static void readRTP(final RGSSGame p) {
		// has rtps?
		if (p.getObjects().containsKey("rtpFiles")) {
			return;
		}

		HashMap<String, File> rtpFiles = new HashMap<String, File>();
		// read game ini
		try {
			Wini ini = new Wini(p.getGameIniFile());

			// rgss1?
			if (p.getRgssVersion() == 1) {
				// read rtp
				for (String s : new String[] { "RTP1", "RTP2", "RTP3" }) {
					// exist?
					String rtp = ini.get("Game", s);
					if (rtp != null && rtp.length() > 0) {
						if (installRTP(rtp, 1)) {
							p.getObjects().put(s, rtp);
							rtpFiles.put(rtp, rtps.get(1).get(rtp));
						}
					} else {
						p.getObjects().put(s, "");
					}
				}
			}

			// rgss2?
			if (p.getRgssVersion() == 2) {
				// read rtp
				// exist?
				String rtp = ini.get("Game", "RTP");
				if (rtp != null && rtp.length() > 0) {
					if (installRTP(rtp, 2)) {
						p.getObjects().put("rtp", rtp);
						rtpFiles.put(rtp, rtps.get(2).get(rtp));
					}
				} else {
					p.getObjects().put("rtp", "");
				}
			}

		} catch (Throwable t) {
			YEx.info("Can not rad " + p.getGameIni() + " for rtp", t);
		}
		p.getObjects().put("rtpFiles", rtpFiles);
	}

	/**
	 * Helpermethod to get the filename in the yaams version
	 * 
	 * @param name
	 * @param rgssVersion
	 * @return
	 */
	protected static File getRTPFilename(String name, int rgssVersion) {
		return new File(YrgssCore.programPath, "rtp" + File.separator + rgssVersion + File.separator + name);
	}

	/**
	 * Helpermethod to install an rtp, if not exist
	 * 
	 * @param name
	 * @param rgssVersion
	 * @return true, exist, false otherwise
	 */
	protected static boolean installRTP(String name, int rgssVersion) {
		// exist?
		if (rtps.get(rgssVersion).containsKey(name)) {
			return true;
		}

		// can install
		File f = isRTPinstalledGui(name, rgssVersion, true);
		if (f != null) {
			rtps.get(rgssVersion).put(name, f);
			return true;
		}

		return false;
	}

	/**
	 * Helpermethod to check if exist, or ask the user to download/install it
	 * 
	 * @param name
	 * @param rgssVersion
	 * @return null, or File for the location
	 */
	protected static File isRTPinstalledGui(String name, int rgssVersion, boolean first) {
		// has it?
		File f = isRTPinstalled(name, rgssVersion);
		if (f != null) {

			// contains graphics.exe?
			if (new File(f, "Graphics.exe").exists()) {
				// fix for standard?
				if (name.equals("Standard")
						&& !new File(f, "Graphics" + File.separator + "Titles" + File.separator + "001-Title01.jpg").exists()) {
					if (YDialog
							.askUser(
									I18N.t("RTP {0} enthält unzulässige Dateien", name),
									"rtp.install.graphics",
									"rtp",
									I18N.t("Graphics.exe wird nicht unterstützt. Neuinstallation des RTPs wird empfohlen. Laden des Projektes trotzdem fortführen oder abbrechen?"),
									I18N.t("{0} laden"), I18N.CANCEL)) {
						return f;
					}

				}
				return f;
			}
		} else {

			int erg = 0;

			if (!first) {
				// ask for install
				YActionDialog y = new YActionDialog("rtp.install", I18N.t("RTP {0} wurde nicht gefunden.", name));
				y.addLink("setup_rtp", I18N.t("Installieren (empfohlen)"),
						I18N.t("Überprüft online, ob die Version existiert und installiert Sie."));
				y.addLink("folder_search", I18N.t("Ordner auswählen"), I18N.t("Ermöglicht selbst manuell den RTP Ordner auswählen"));
				y.addLink("cancel", I18N.CANCEL,
						I18N.t("Bindet kein RTP ein. Kann zu Nebeneffekten führen, wenn Ressourcen verwendet werden."));

				erg = y.show();
			}

			// get it
			if (erg == 0) {

				// ask online
				String url = NetHelper.getContentAsString("http://www.yaams.de/file/?typ=rtp&windows=" + SystemUtils.IS_OS_WINDOWS
						+ "&name=" + name + "&rgss=" + rgssVersion);

				// found?
				if (url == null) {
					return isRTPinstalledGui(name, rgssVersion, false);
				}

				// get file ending
				String[] s = url.split("/");

				// download
				File ex = new File(YrgssCore.tmpFolder, s[s.length - 1]);
				NetHelper.downloadFile(ex, url);

				// extract or run?
				if (url.endsWith(".zip")) {
					// create folder
					FileHelper.mkdirs(getRTPFilename(name, rgssVersion));
					// extract
					FileHelper.extractArchive(ex, getRTPFilename(name, rgssVersion));
				} else {
					final String rtpName = name;
					// inform user
					new SwingHelper(true) {

						@Override
						public void run() {
							YDialog.ok(
									I18N.t("RTP {0} wird nun installiert.", rtpName),
									I18N.t("Die Installation wird nun gestartet. YAams wird auf die Fertigstellung warten und ist für diese Zeit nicht benutzbar."),
									"setup_wait");

						}
					};

					YProgressWindowRepeat r = new YProgressWindowRepeat(I18N.t("Warten auf Fertigstellung der Installation vom RTP {0}",
							name), "rtp");
					// start
					SystemHelper.runExternal(new String[] { ex.getAbsolutePath() }, true);

					r.close();
				}

				return isRTPinstalledGui(name, rgssVersion, false);
			}

			// ask user
			if (erg == 1) {
				File rtpFolder = EditorIntegration.openDialog(false, true)[0];
				if (rtpFolder == null) {
					return isRTPinstalledGui(name, rgssVersion, false);
				} else {
					return rtpFolder;
				}
			}

			// get it
			return null;
		}

		return f;

	}

	/**
	 * Helpermethod to check if exist
	 * 
	 * @param name
	 * @param rgssVersion
	 * @return null, or File for the location
	 */
	protected static File isRTPinstalled(String name, int rgssVersion) {

		// exist in yaams?
		if (getRTPFilename(name, rgssVersion).exists()) {
			return getRTPFilename(name, rgssVersion);
		}

		// windows system?
		// supported?
		if (!SystemUtils.IS_OS_WINDOWS) {
			return null;

		}

		try {

			// has rtp?
			// 64b System fix?
			String path = WindowsRegistry.getKeySz(WindowsRegistry.HKEY_LOCAL_MACHINE, "SOFTWARE\\"
					+ (SystemUtils.OS_ARCH.contains("64") ? "Wow6432Node\\" : "") + "Enterbrain\\RGSS\\RTP", name);

			// add it
			return new File(path);
		} catch (Throwable t) {
			Log.ger.info("Can't find rtp " + name + " for rgss" + rgssVersion);
			t.printStackTrace();
		}
		return null;
	}

	/**
	 * Get all rtps of this project
	 * 
	 * @param p
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, File> getRTPs(RGSSGame p) {
		// run?
		if (!p.getObjects().containsKey("rtpFiles")) {
			readRTP(p);
		}

		// get it
		return (HashMap<String, File>) p.getObjects().get("rtpFiles");
	}
}
