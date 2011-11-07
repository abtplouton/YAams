/**
 * 
 */
package de.yaams.extensions.diamant.helper;

import java.util.ArrayList;

import org.apache.log4j.Level;

import com.ezware.dialog.task.TaskDialogs;

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
	// public static Object fileNotFound(final File f, final String relFilename)
	// {
	// // ask user
	// YActionDialog yad = new YActionDialog("ressress",
	// I18N.t("Datei {0} nicht gefunden.", relFilename));
	// yad.addLink("disk_recall", I18N.t("Erneut prüfen"),
	// I18N.t("Wenn die Datei manuell wieder gestellt wurde, danach erneut prüfen"));
	// yad.addLink("search_folder", I18N.t("Alternative verwenden"),
	// I18N.t("Manuell alternative Datei auswählen"));
	// yad.addLink("cancel", I18N.t("Leere Datei verwenden (empfohlen)"), "");
	//
	// Log.ger.info("", new FileNotFoundException(relFilename));
	// int c = yad.show();
	//
	// // search again
	// if (c == 0) {
	// return -2;
	// } else if (c == 1) {
	// File[] files = EditorIntegration.openDialog(false, false);
	// if (files == null) {
	// return -1;
	// }
	// return files[0];
	// } else {
	// return -1;
	// }
	// }

	/**
	 * Show Info dialog
	 * 
	 * @param title
	 * @param mess
	 * @param icon
	 *            (optional)
	 */
	public static void ok(final String title, final String mess, final Object icon) {

		YMessagesDialog d = new YMessagesDialog(title, "ok" + title);
		d.add(mess, Level.INFO_INT);
		// d.setIcon(IconCache.getS(icon == null ? "yaams" : icon, 64));
		d.getYesnoText()[0] = I18N.t("Ok");
		d.getYesnoIcon()[0] = "ok";
		d.showOk();
	}

	/**
	 * Will delete it?
	 * 
	 * @param element
	 * @param icon
	 * @return true -> delete, false -> dont delete
	 */
	public static boolean delete(final String element, final String icon) {
		// build dialog
		YMessagesDialog md = new YMessagesDialog(I18N.t("<html><i>{0}</i> löschen?", element), "del." + icon);
		md.add(I18N.t("<html><i>{0}</i> wird dann wirklich gelöscht.", element), Level.INFO_INT);
		// md.setIcon(IconCache.get("trash" + (icon == null ? "" : "_" + icon),
		// 64));
		md.getYesnoText()[0] = I18N.t("Lösche {0}", element);
		md.getYesnoIcon()[0] = "trash_ok";
		md.getYesnoText()[1] = I18N.t("Abbrechen & Nicht Löschen");
		md.getYesnoIcon()[1] = "cancel";

		// show
		return md.showQuestion();
	}

	/**
	 * Show all messages who happend
	 * 
	 * @param errors
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
	// public static boolean show(final String name, final String icon, final
	// JComponent c, boolean addOKButton) {
	// // build panel
	// JPanel p = new JPanel(new BorderLayout());
	// p.add(new YHeader(name, icon), BorderLayout.NORTH);
	// p.add(c, BorderLayout.CENTER);
	//
	// return show("", p, addOKButton);
	// }

	/**
	 * Create from a dialog
	 * 
	 * @param name
	 * @param icon
	 * @param component
	 * @param addOkButton
	 * @return true, the user select ok, or false otherwise
	 */
	// public static boolean show(String name, final JComponent content, boolean
	// addOKButton) {
	// // build frame
	// final JDialog j = new JDialog(null, name,
	// Dialog.ModalityType.APPLICATION_MODAL);
	// j.setLayout(new BorderLayout());
	// j.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	// j.add(content, BorderLayout.CENTER);
	//
	// final StringBuilder pressOk = new StringBuilder("");
	//
	// // add button?
	// if (addOKButton) {
	// JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
	//
	// // add ok
	// p.add(YFactory.b(I18N.t("Ok"), "ok", new AE() {
	//
	// @Override
	// public void run() {
	// pressOk.append("1");
	// j.setVisible(false);
	// j.dispose();
	// }
	// }));
	//
	// // add cancel
	// p.add(YFactory.b(I18N.t("Abbrechen"), "cancel", new AE() {
	//
	// @Override
	// public void run() {
	// j.setVisible(false);
	// j.dispose();
	// }
	// }));
	// j.add(p, BorderLayout.SOUTH);
	// }
	//
	// // set it
	// try {
	// j.pack();
	// j.setLocationRelativeTo(null);
	// } catch (Throwable t) {
	// Log.ger.info("Can not pack Dialog", t);
	// }
	// j.setVisible(true);
	// return pressOk.length() == 1;
	// }

	/**
	 * Create from a formbuilder a dialog
	 * 
	 * @param f
	 * @return true, user press ok, false otherwise
	 */
	// public static boolean showForm(final String name, final String icon,
	// final FormBuilder f) {
	//
	// // add it
	// JComponent p = f.getPanel(true);
	//
	// while (true) {
	// // show it
	// if (!show(name, icon, p, true)) {
	// return false;
	// }
	//
	// // check form
	// if (f.isValidate()) {
	// return true;
	// }
	// }
	// }

	/**
	 * Helpermethod for single YMessagesDialog message
	 * 
	 * @param title
	 * @param id
	 * @param icon
	 * @param mess
	 * @param yes
	 * @param no
	 * @param yesIcon
	 * @param noIcon
	 * @return
	 */
	public static boolean askUser(String title, String id, String icon, String mess, String yes, String no, String yesIcon, String noIcon) {
		// create it
		YMessagesDialog y = new YMessagesDialog(title, id);
		y.add(mess, Level.INFO_INT);
		// y.setIcon(IconCache.get(icon, 64));
		y.getYesnoText()[0] = yes;
		y.getYesnoText()[1] = no;
		y.getYesnoIcon()[0] = yesIcon;
		y.getYesnoIcon()[1] = noIcon;
		return y.showQuestion();
	}
}
