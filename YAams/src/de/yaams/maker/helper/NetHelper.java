/**
 * 
 */
package de.yaams.maker.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JFileChooser;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.Validate;

import de.yaams.maker.helper.gui.YActionDialog;
import de.yaams.maker.helper.gui.YDialog;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.gui.form.FormFileSelectField;
import de.yaams.maker.helper.gui.form.FormInfo;
import de.yaams.maker.helper.gui.form.FormTextArea;
import de.yaams.maker.helper.gui.form.FormTextField;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.validator.ValidatorNotEmpty;

/**
 * @author abt
 * 
 */
public class NetHelper {

	/**
	 * Helpermethod to check the settings
	 * 
	 * @param link
	 * @param text
	 * @return
	 */
	protected static Object checkRights(String link, boolean text) {
		if (Setting.get("net.access", true)) {
			return 1;
		}

		return showMessage(I18N.t("Der Zugriff aufs Internet ist deaktiviert. <br>" + "{1}: {0}", link,
				text ? "Kann die Seite nicht anzeigen" : "Kann die Datei nicht runterladen"), link, true, true, text);
	}

	/**
	 * Check, if can contact yaams.de
	 * 
	 * @param message
	 *            , show message?
	 * @return
	 */
	public static boolean hasInternet(boolean message) {
		while (true) {
			try {
				URL url = new URL("http://www.yaams.de/");
				url.openStream();
				return true;
			} catch (Throwable t) {
				// ask user?
				if (!message) {
					return false;
				}
				return YDialog.askUser(I18N.t("Internetzugriff nicht möglich"), "inet.error", "error_web", I18N.t(
						"Der Zugriff auf yaams.de ist nicht möglich. Wahrscheinlich besteht keine Verbindung ins Internet. <br>"
								+ "Stelle zuerst eine Verbindung ins Internet her oder breche den Vorgang ab, <br>"
								+ "dann ist aber möglicherweise der Funktionsumfang eingeschränkt.({1}: {0})", t.getLocalizedMessage(), t
								.getClass().getSimpleName()), I18N.t("Internetzugriff steht"), I18N.CANCEL, "web_ok", "cancel");
			}
		}
	}

	/**
	 * Read a content of a website
	 * 
	 * @param link
	 * @return
	 */
	public static String getContentAsString(String link) {
		StringBuffer content = new StringBuffer();
		try {
			// check parameter
			Validate.notEmpty(link);

			// has inet?
			if (!hasInternet(true)) {
				return null;
			}

			// check it
			Object o = checkRights(link, true);
			if (o instanceof String) {
				return (String) o;
			}
			if (o == null) {
				return null;
			}

			URL url = new URL(link);

			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

			try {
				String line;
				while ((line = reader.readLine()) != null) {
					// add line?
					if (content.length() > 0) {
						content.append(SystemUtils.LINE_SEPARATOR);
					}
					content.append(line);
				}
			} finally {
				reader.close();
			}

		} catch (Throwable t) {
			YEx.info("Can not read " + link, t);
		} finally {
		}

		return content.toString();
	}

	/**
	 * Download a file
	 * 
	 * @param dest
	 * @param source
	 * @return true=file loaded, false otherwise
	 */
	public static boolean downloadFile(File dest, String source) {
		boolean loaded = true;

		try {
			// check parameter
			FileHelper.checkPath(I18N.t("Kann in {0} nicht schreiben.", dest), dest, false, true);
			Validate.notEmpty(source, "Source is null");

			// check it
			Object o = checkRights(source, false);
			if (o instanceof File) {
				FileHelper.copy((File) o, dest);
				return true;
			}
			if (o == null) {
				return false;
			}

			new Download(new URL(source), dest, true);

		} catch (final Throwable t) {
			YEx.info("Can not download " + source, t);
			loaded = false;
		}

		return loaded;
	}

	/**
	 * Show Message
	 * 
	 * @param title
	 * @param dlMan
	 */
	protected static Object showMessage(String title, String link, boolean ignore, boolean dlMan, boolean isText) {
		YActionDialog y = new YActionDialog("net.download", title);

		// add ignore?
		if (ignore) {
			y.addLink("web_ok", I18N.t("Ignorieren und Trotzdem runterladen"), I18N.t("Ignoriert diesmal die Option"));
		}

		// download manually?
		if (dlMan) {
			y.addLink("open_web", I18N.t("Manuell runterladen"), I18N.t("Es öffnet sich ein Fenster mit den Link und der Anleitung"));
		}

		y.addLink("cancel", I18N.t("Abbrechen"), "");

		int id = y.show();

		// cancel?
		if (id == -1 || y.getIcon(id).equals("cancel")) {
			return null;
		}

		// ignore?
		if (y.getIcon(id).equals("web_ok")) {
			return 1;
		}

		// download manually?
		if (y.getIcon(id).equals("open_web")) {
			if (isText) {
				// build form
				FormBuilder f = new FormBuilder("net.string");
				f.addElement("basic.help", new FormInfo("", I18N.t("Im 1. Feld steht der Link zur Seite. Den Link kopieren und mit einem "
						+ "geeigneten Programm/Pc öffnen, danach im 2. Feld den Inhalt der  " + "Seite einfügen und Ok drücken"))
						.setSorting(-1));
				f.addElement("basic.source", new FormTextField("", link));
				f.addElement("basic.dest", new FormTextArea("", "").addValidator(new ValidatorNotEmpty()).setSorting(1));

				if (YDialog.showForm(I18N.t("Seite manuell anzeigen"), "open_web", f)) {
					return f.getElement("basic.dest").getContentAsString();
				}
				return null;
			} else {
				// build form
				FormBuilder f = new FormBuilder("net.download");
				f.addElement("basic.help",
						new FormInfo("", I18N.t("Im 1. Feld steht der Link zur Datei. Den Link kopieren und mit einem "
								+ "geeigneten Programm/Pc runterladen, danach im 2. Feld die runtergeladene "
								+ "Datei auswählen und Ok drücken")).setSorting(-1));
				f.addElement("basic.source", new FormTextField("", link));
				f.addElement("basic.dest", new FormFileSelectField("", null, JFileChooser.OPEN_DIALOG, false, false).setSorting(1));

				if (YDialog.showForm(I18N.t("Datei manuell runterladen"), "open_web", f)) {
					return new File(f.getElement("basic.dest").getContentAsString());
				}
				return null;
			}
		}

		// never reach code
		return null;
	}
}