/**
 * 
 */
package de.yaams.extensions.diamant.helper;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Icon;

import org.apache.log4j.Level;

import com.ezware.dialog.task.TaskDialog;
import com.ezware.dialog.task.TaskDialog.Command;
import com.ezware.dialog.task.TaskDialog.StandardCommand;

/**
 * @author abt
 * 
 */
public class YMessagesDialog {

	protected ArrayList<String> messages;
	protected int level;
	protected String title, header, footer, id, mid;
	protected Icon icon;
	protected String[] yesno = { I18N.t("Ja"), I18N.t("Nein") };
	protected String[] yesnoIcon = { "yes", "no" };
	protected boolean defaultSelect;

	/**
	 * Create a new YErrorDialog Create a new YErrorDialog
	 * 
	 * @param title
	 * @param id
	 */
	public YMessagesDialog(String title, String id) {
		messages = new ArrayList<String>();
		level = Level.INFO_INT;
		this.title = title;
		this.id = id;
		header = "";
		footer = "";
		mid = "show.errordialog." + id;

	}

	/**
	 * Add message
	 * 
	 * @param message
	 * @param level
	 */
	public YMessagesDialog add(String message, int level) {
		messages.add(message);

		// reset level?
		if (level != 0 && level > this.level) {
			this.level = level;
		}
		mid = "show.errordialog." + id + "." + level + "." + messages.size();

		return this;
	}

	/**
	 * Show messages as quest
	 * 
	 * @return
	 */
	public String formatDialog() {
		// build objects
		HashMap<String, Object> objects = new HashMap<String, Object>();
		objects.put("message", this);
		objects.put("id", id);
		// inform
		// ExtentionManagement.work("errordialog", objects);

		// found errors?
		if (messages != null && messages.size() > 0) {// && Setting.get(mid,
														// true)) {
			// build string
			final StringBuilder s = new StringBuilder("<html>");
			s.append(header);
			Log.ger.info("New Dialog: " + title);
			if (messages.size() > 1) {
				s.append("<ul>");
				for (final String e : messages) {
					s.append("<li>");
					s.append(e);
					s.append("</li>");
					Log.ger.info("* " + e);
				}
				s.append("</ul>");
			} else {
				s.append(messages.get(0));
				s.append("<br>");
				Log.ger.info("* " + messages.get(0));
			}

			// build string
			s.append("<br>");
			s.append(footer);
			s.append("</html>");
			Log.ger.info(footer);

			return s.toString();
		} else {
			return null;
		}

	}

	/**
	 * Helpermethod to get the right icon
	 * 
	 * @return
	 */
	// protected Icon getIcon() {
	// if (icon != null) {
	// return icon;
	// } else if (level == Level.INFO_INT) {
	// return IconCache.get("yaams", 64);
	// } else if (level == Level.WARN_INT) {
	// return IconCache.get("warn", 64);
	// } else if (level == Level.ERROR_INT) {
	// return IconCache.get("error", 64);
	// } else {
	// return IconCache.get("dummy", 64);
	// }
	// }

	/**
	 * Only inform the user
	 */
	public boolean showOk() {
		String cont = formatDialog();

		// found errors?
		if (cont != null) {
			TaskDialog dlg = new TaskDialog(null, "");
			dlg.setInstruction(title);
			dlg.setText(cont);
			// dlg.setIcon(getIcon());

			// if (YLevel.IS_DEVELOPER) {
			// dlg.setCommands(StandardCommand.OK.derive(yesno[0],
			// yesnoIcon[0]),
			// StandardCommand.HELP.derive(I18N.t("Zeige Stack"), "opts"));
			// }

			// check boxes
			// dlg.getFooter().setCheckBoxText(I18N.t("Diesen Dialog nicht mehr anzeigen."));
			Command erg = dlg.show();
			// Setting.set(mid, !dlg.getFooter().isCheckBoxSelected());

			// if (erg.equals(StandardCommand.HELP)) {
			// Log.ger.info("", new Throwable("Ok stack"));
			// TaskDialogs.showException(new Throwable("Ok stack"));
			// showOk();
			// }
		}
		return true;

	}

	/**
	 * 
	 * @return true, has errors, false otherwise
	 */
	public boolean hasErrors() {
		return messages != null && messages.size() > 0;
	}

	/**
	 * Show the errors, who found
	 * 
	 * @param errors
	 * @return true -> yes, run it, false -> no, close it
	 */
	public boolean showQuestion() {
		String cont = formatDialog();

		// found errors?
		if (cont != null) {
			TaskDialog dlg = new TaskDialog(null, "");
			dlg.setInstruction(title);
			dlg.setText(cont);
			// dlg.setIcon(getIcon());
			// if (YLevel.IS_DEVELOPER) {
			// dlg.setCommands(StandardCommand.OK.derive(yesno[0],
			// yesnoIcon[0]), StandardCommand.CANCEL.derive(yesno[1],
			// yesnoIcon[1]),
			// StandardCommand.HELP.derive(I18N.t("Zeige Stack"), "opts"));
			// } else {
			// dlg.setCommands(StandardCommand.OK.derive(yesno[0],
			// yesnoIcon[0]), StandardCommand.CANCEL.derive(yesno[1],
			// yesnoIcon[1]));
			// }

			// check boxes
			// dlg.getFooter().setCheckBoxText(I18N.t("Diesen Dialog nicht mehr anzeigen."));
			Command erg = dlg.show();
			// Setting.set(mid, !dlg.getFooter().isCheckBoxSelected());
			//
			// if (erg.equals(StandardCommand.HELP)) {
			// Log.ger.info("", new Throwable("Question stack"));
			// TaskDialogs.showException(new Throwable("Question stack"));
			// showQuestion();
			// }

			// get it
			return erg.equals(StandardCommand.OK);

		}
		return true;
	}

	/**
	 * Get infobox
	 * 
	 * @return
	 */
	// protected JCheckBox getBox() {
	// final JCheckBox c = new
	// JCheckBox(I18N.t("Diesen Dialog nicht mehr anzeigen."));
	//
	// // c.setSelected(defaultSelect);
	// c.addChangeListener(new ChangeListener() {
	//
	// @Override
	// public void stateChanged(ChangeEvent arg0) {
	// Setting.set(mid, !c.isSelected());
	//
	// }
	// });
	// return c;
	// }

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public YMessagesDialog setTitle(String title) {
		this.title = title;
		return this;
	}

	/**
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @param header
	 *            the header to set
	 */
	public YMessagesDialog setHeader(String header) {
		this.header = header;
		return this;
	}

	/**
	 * @return the footer
	 */
	public String getFooter() {
		return footer;
	}

	/**
	 * @param footer
	 *            the footer to set
	 */
	public YMessagesDialog setFooter(String footer) {
		this.footer = footer;
		return this;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public YMessagesDialog setIcon(Icon icon) {
		this.icon = icon;
		return this;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * 0=Yes, 1=No
	 * 
	 * @return the yesno
	 */
	public String[] getYesnoText() {
		return yesno;
	}

	/**
	 * 0=Yes, 1=No
	 * 
	 * @return the yesno
	 */
	public String[] getYesnoIcon() {
		return yesnoIcon;
	}

	/**
	 * @return the defaultSelect
	 */
	public boolean isDefaultSelect() {
		return defaultSelect;
	}

	/**
	 * @param defaultSelect
	 *            the defaultSelect to set
	 */
	public void setDefaultSelect(boolean defaultSelect) {
		this.defaultSelect = defaultSelect;
	}
}
