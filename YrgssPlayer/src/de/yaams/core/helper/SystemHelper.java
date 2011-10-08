/**
 * 
 */
package de.yaams.core.helper;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.apache.log4j.Level;
import org.apache.log4j.Priority;

import de.yaams.core.YrgssCore;
import de.yaams.core.helper.gui.YDialog;
import de.yaams.core.helper.gui.YEx;
import de.yaams.core.helper.gui.YMessagesDialog;
import de.yaams.core.helper.gui.YProgressWindowRepeat;

/**
 * @author Nebli
 * 
 */
public class SystemHelper {

	/**
	 * Check the basics, if the system support the action for this path
	 * 
	 * @param a
	 * @param path
	 * @return true, support it/user select ignore, false, don't support it
	 */
	public static boolean basics(final Action a, final String path) {
		try {
			YMessagesDialog errors = new YMessagesDialog(I18N.t("Aufruf von {0} nicht möglich", path), "action" + a.toString());

			// supported?
			if (!Desktop.isDesktopSupported()) {
				errors.add(I18N.t("Java Desktop Funktion wird vom System nicht untersützt."), Priority.INFO_INT);
			}

			final Desktop desktop = Desktop.getDesktop();

			// supported?
			if (!desktop.isSupported(a)) {
				errors.add(I18N.t("{0}-Aktion wird nicht unterstützt.", a), Priority.INFO_INT);
			}

			// abort?
			return errors.showOk();

		} catch (final Throwable t) {
			YEx.info("Can not run system action " + a + " for " + path, t);
		}
		return true;
	}

	/**
	 * Open the url with the default browser
	 * 
	 * @param url
	 */
	public static void openUrl(final String url) {
		if (!basics(Action.BROWSE, url)) {
			return;
		}

		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (final Throwable t) {
			YEx.info("Can not open url " + url, t);
		}
	}

	/**
	 * Open the file with the default viewer
	 * 
	 * @param file
	 */
	public static void viewFile(final File file) {
		if (!basics(Action.OPEN, file.getAbsolutePath())) {
			return;
		}

		try {
			Desktop.getDesktop().open(file);
		} catch (final Throwable t) {
			YEx.info("Can not view " + file, t);
		}
	}

	/**
	 * Open the file with the default viewer
	 * 
	 * @param file
	 */
	public static void editFile(final File file) {
		if (!basics(Action.EDIT, file.getAbsolutePath())) {
			return;
		}

		try {
			Desktop.getDesktop().open(file);
		} catch (final Throwable t) {
			YEx.info("Can not edit " + file, t);
		}
	}

	/**
	 * Run as thread
	 * 
	 * @param run
	 */
	public static void runThread(Run run, boolean wait) {
		Thread t = new Thread(run);
		t.start();

		// wait for the thread?
		if (wait) {
			while (!t.isInterrupted() && t.isAlive()) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Run a script external
	 * 
	 * @param code
	 * @param wait
	 */
	public static void runExternal(final String[] command, final boolean wait) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// run it
				try {
					Log.ger.info("Run external: " + Arrays.toString(command));
					if (!wait) {
						Runtime.getRuntime().exec(command);
						return;
					}

					// run it
					Process p = Runtime.getRuntime().exec(command);

					// look at
					String erg = FileHelper.inputStreamToString(p.getInputStream());
					erg += FileHelper.inputStreamToString(p.getErrorStream());
					if (erg != null && erg.length() > 0) {
						YDialog.ok(Arrays.toString(command), "Exitcode:" + p.exitValue() + " " + erg, p.exitValue() == 0 ? "info" : "error");
					}
				} catch (final Throwable t) {
					YEx.info("Can not run external command " + Arrays.toString(command), t);
				}
			}
		}).start();
	}

	/**
	 * Helpermethod for system.exit
	 */
	public static void exit(int level) {
		// log it
		Log.ger.info("Close Programm", new Throwable());

		// do it
		System.exit(level);
	}

	/**
	 * Send feeback to yaams
	 * 
	 * @param data
	 */
	public static void sendData(String type, String title, final HashMap<String, String> data) {
		YProgressWindowRepeat y = new YProgressWindowRepeat(I18N.t("Send Data to yaams.de"), "web");

		try {
			File cap = new File(YrgssCore.tmpFolder, "cap.jpg");
			if (cap.exists()) {
				FileHelper.deleteFile(cap);
			}
			// create screenshot
			try {
				BufferedImage bufferedImage = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
				ImageIO.write(bufferedImage, "jpg", cap);
			} catch (Throwable t) {
				Log.ger.info("Can not create capture " + cap, t);
				FileHelper.deleteFile(cap);
			}

			// add basic data
			data.put("title", title);
			data.put("type", type);
			data.put("yaams", YrgssCore.TITLE);
			data.put("log", FileHelper.readFileToString(Log.file));

			ClientHttpRequest post = new ClientHttpRequest("http://www.yaams.de/file/sendFeedback.php");

			// add files
			post.setParameter("cap", cap);

			// Construct data
			// final StringBuffer send = new StringBuffer("1=1");
			for (final String key : data.keySet()) {
				post.setParameter(key, data.get(key));
			}

			// // data
			// final URL url = new
			// URL("http://www.yaams.de/launcher/feedback.php");
			// final URLConnection conn = url.openConnection();
			// conn.setDoOutput(true);
			// final OutputStreamWriter wr = new
			// OutputStreamWriter(conn.getOutputStream());
			// wr.write(send.toString());
			// wr.flush();
			//
			// Get the response
			final BufferedReader rd = new BufferedReader(new InputStreamReader(post.post()));
			String line;
			final StringBuffer erg = new StringBuffer("");
			while ((line = rd.readLine()) != null) {
				erg.append(line);
			}

			// clean up
			FileHelper.deleteFile(cap);

			// show it & close all
			YDialog.ok(I18N.t("Feedback"), erg.toString(), "monitor");
			// wr.close();
			rd.close();

		} catch (final Throwable t) {
			YEx.info("Can not send feedback to http://www.yaams.de/launcher/feedback.php", t);
		}
		y.close();
	}
}
