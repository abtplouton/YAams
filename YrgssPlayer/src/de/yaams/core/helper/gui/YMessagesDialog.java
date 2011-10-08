/**
 * 
 */
package de.yaams.core.helper.gui;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Icon;

import org.apache.log4j.Level;
import org.apache.log4j.Priority;

import com.ezware.dialog.task.TaskDialog;
import com.ezware.dialog.task.TaskDialog.Command;
import com.ezware.dialog.task.TaskDialog.StandardCommand;

import de.yaams.core.helper.I18N;
import de.yaams.core.helper.gui.icons.IconCache;

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
	protected boolean defaultSelect;

	/**
	 * Create a new YErrorDialog Create a new YErrorDialog
	 * 
	 * @param title
	 * @param id
	 */
	public YMessagesDialog(String title, String id) {
		messages = new ArrayList<String>();
		level = Priority.INFO_INT;
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
			if (messages.size() > 1) {
				s.append("<ul>");
				for (final String e : messages) {
					s.append("<li>");
					s.append(e);
					s.append("</li>");
				}
				s.append("</ul>");
			} else {
				s.append(messages.get(0));
				s.append("<br>");
			}

			// build string
			s.append("<br>");
			s.append(footer);
			s.append("</html>");

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
	protected Icon getIcon() {
		if (icon != null) {
			return icon;
		} else if (level == Priority.INFO_INT) {
			return IconCache.get("yaams", 64);
		} else if (level == Priority.WARN_INT) {
			return IconCache.get("warn", 64);
		} else if (level == Priority.ERROR_INT) {
			return IconCache.get("error", 64);
		} else {
			return IconCache.get("dummy", 64);
		}
	}

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
			dlg.setIcon(getIcon());

			// check boxes
			// dlg.getFooter().setCheckBoxText(I18N.t("Diesen Dialog nicht mehr anzeigen."));
			dlg.show();
			// Setting.set(mid, !dlg.getFooter().isCheckBoxSelected());
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
			dlg.setIcon(getIcon());
			dlg.setCommands(StandardCommand.OK.derive(yesno[0]), StandardCommand.CANCEL.derive(yesno[1]));

			// check boxes
			// dlg.getFooter().setCheckBoxText(I18N.t("Diesen Dialog nicht mehr anzeigen."));
			Command erg = dlg.show();
			// Setting.set(mid, !dlg.getFooter().isCheckBoxSelected());

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
	public String[] getYesno() {
		return yesno;
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
