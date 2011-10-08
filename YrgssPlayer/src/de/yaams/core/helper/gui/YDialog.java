/**
 * 
 */
package de.yaams.core.helper.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.apache.log4j.Level;
import org.apache.log4j.Priority;

import com.ezware.dialog.task.TaskDialog;
import com.ezware.dialog.task.TaskDialogs;

import de.yaams.core.helper.AE;
import de.yaams.core.helper.I18N;
import de.yaams.core.helper.Log;
import de.yaams.core.helper.gui.form.core.FormBuilder;
import de.yaams.core.helper.gui.icons.IconCache;
import de.yaams.core.helper.integration.EditorIntegration;

/**
 * @author Nebli
 * 
 */
public class YDialog {

	/**
	 * Show a dialog to select a file
	 * 
	 * @param f
	 * @return -2 = search again, -1 = use empty file, or file, use this file
	 */
	public static Object fileNotFound(final File f, final String relFilename) {
		// ask user
		YActionDialog yad = new YActionDialog("ressress", I18N.t("Datei {0} nicht gefunden.", relFilename));
		yad.addLink("disk_recall", I18N.t("Erneut prüfen"), I18N.t("Wenn die Datei manuell wieder gestellt wurde, danach erneut prüfen"));
		yad.addLink("search_folder", I18N.t("Alternative verwenden"), I18N.t("Manuell alternative Datei auswählen"));
		yad.addLink("cancel", I18N.t("Leere Datei verwenden (empfohlen)"), "");

		Log.ger.info("", new FileNotFoundException(relFilename));
		int c = yad.show();

		// search again
		if (c == 0) {
			return -2;
		} else if (c == 1) {
			return EditorIntegration.openDialog(false, false)[0];
		} else {
			return -1;
		}
	}

	/**
	 * Show Info dialog
	 * 
	 * @param title
	 * @param mess
	 * @param icon
	 *            (optional)
	 */
	public static void ok(final String title, final String mess) {

		TaskDialogs.inform(null, title, mess);
	}

	/**
	 * Show Info dialog
	 * 
	 * @param title
	 * @param mess
	 * @param icon
	 *            (optional)
	 */
	public static void ok(final String title, final String mess, final Object icon) {

		final TaskDialog dlg = new TaskDialog(null, "");
		dlg.setInstruction(title);
		dlg.setIcon(IconCache.getS(icon == null ? "yrgss" : icon, 64));
		dlg.setText(mess);
		dlg.setVisible(true);
	}

	/**
	 * Will delete it?
	 * 
	 * @param element
	 * @param icon
	 */
	public static boolean delete(final String element, final String icon) {
		// build dialog
		YMessagesDialog md = new YMessagesDialog(I18N.t("<html><i>{0}</i> löschen?", element), "del." + icon);
		md.add(I18N.t("<html><i>{0}</i> wird dann wirklich gelöscht.", element), Priority.INFO_INT);
		md.setIcon(IconCache.get("trash" + (icon == null ? "" : "_" + icon), 64));
		md.getYesno()[0] = I18N.t("Lösche {0}", element);
		md.getYesno()[1] = I18N.t("Abbrechen & Nicht Löschen");

		// show
		return md.showQuestion();
	}

	/**
	 * Show all messages who happend
	 * 
	 * @param errors
	 * @return true -> yes, run it, false -> no, close it
	 */
	public static void okMessages(final String title, String icon, final ArrayList<String> errors) {
		// found errors?
		if (errors != null && errors.size() > 0) {
			// build string
			final StringBuilder s = new StringBuilder("<html>");
			if (errors.size() > 1) {
				s.append("<ul>");
				for (final String e : errors) {
					s.append("<li>");
					s.append(e);
					s.append("</li>");
				}
				s.append("</ul>");
			} else {
				s.append(errors.get(0));
				s.append("<br>");
			}
			s.append("</html>");

			// show message
			ok(title, s.toString(), icon);
		}
	}

	/**
	 * Show the errors, who found
	 * 
	 * @param errors
	 * @return true -> yes, run it, false -> no, close it
	 */
	public static boolean showErrors(final String title, final String error) {
		// found errors?
		if (error != null) {
			// build string
			final StringBuilder s = new StringBuilder("<html>");
			s.append(error);
			s.append("<br>");
			s.append(I18N.t("Errors were found when running. Still trying?"));
			s.append("</html>");

			// show message
			if (!TaskDialogs.warn(null, title, s.toString())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Create from a dialog
	 * 
	 * @param name
	 * @param icon
	 * @param component
	 * @param addOkButton
	 * @return true, the user select ok, or false otherwise
	 */
	public static boolean show(final String name, final String icon, final JComponent c, boolean addOKButton) {
		// build panel
		JPanel p = new JPanel(new BorderLayout());
		p.add(new YHeader(name, icon), BorderLayout.NORTH);
		p.add(c, BorderLayout.CENTER);

		return show("", p, addOKButton);
	}

	/**
	 * Create from a dialog
	 * 
	 * @param name
	 * @param icon
	 * @param component
	 * @param addOkButton
	 * @return true, the user select ok, or false otherwise
	 */
	public static boolean show(String name, final JComponent content, boolean addOKButton) {
		// build frame
		final JDialog j = new JDialog(null, name, Dialog.ModalityType.APPLICATION_MODAL);
		j.setLayout(new BorderLayout());
		j.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		j.add(content, BorderLayout.CENTER);

		final StringBuilder pressOk = new StringBuilder("");

		// add button?
		if (addOKButton) {
			JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

			// add ok
			p.add(YFactory.b(I18N.t("Ok"), "ok", new AE() {

				@Override
				public void run() {
					pressOk.append("1");
					j.setVisible(false);
					j.dispose();
				}
			}));

			// add cancel
			p.add(YFactory.b(I18N.t("Abbrechen"), "cancel", new AE() {

				@Override
				public void run() {
					j.setVisible(false);
					j.dispose();
				}
			}));
			j.add(p, BorderLayout.SOUTH);
		}

		j.pack();
		j.setLocationRelativeTo(null);
		j.setVisible(true);
		return pressOk.length() == 1;
	}

	/**
	 * Create from a formbuilder a dialog
	 * 
	 * @param f
	 * @return true, user press ok, false otherwise
	 */
	public static boolean showForm(final String name, final String icon, final FormBuilder f) {

		// add it
		JComponent p = f.getPanel(true);

		while (true) {
			// show it
			if (!show(name, icon, p, true)) {
				return false;
			}

			// check form
			if (f.isValidate()) {
				return true;
			}
		}
	}

	/**
	 * Helüermethod for single YMessagesDialog message
	 * 
	 * @param title
	 * @param id
	 * @param icon
	 * @param mess
	 * @param yes
	 * @param no
	 * @return
	 */
	public static boolean askUser(String title, String id, String icon, String mess, String yes, String no) {
		// create it
		YMessagesDialog y = new YMessagesDialog(title, id);
		y.add(mess, Priority.INFO_INT);
		y.setIcon(IconCache.get(icon, 32));
		y.getYesno()[0] = yes;
		y.getYesno()[1] = no;
		return y.showQuestion();
	}
}
