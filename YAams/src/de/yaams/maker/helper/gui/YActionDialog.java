/**
 * 
 */
package de.yaams.maker.helper.gui;

import java.util.ArrayList;
import java.util.Collection;

import com.ezware.dialog.task.CommandLink;
import com.ezware.dialog.task.TaskDialogs;

import de.yaams.maker.helper.Setting;
import de.yaams.maker.helper.gui.icons.IconCache;

/**
 * @author Praktikant
 * 
 */
public class YActionDialog {

	protected String id, title, message;
	protected ArrayList<CommandLink> links;
	protected ArrayList<String> icons;
	protected int defaultAnswer;

	/**
	 * 
	 * @param id
	 * @param title
	 */
	public YActionDialog(String id, String title) {
		links = new ArrayList<CommandLink>();
		icons = new ArrayList<String>();
		this.id = id;
		this.title = title;
	}

	/**
	 * Add a link
	 * 
	 * @param icon
	 * @param title
	 * @param text
	 */
	public void addLink(String icon, String title, String text) {
		links.add(new CommandLink(IconCache.get(icon, 32), title, text));
		icons.add(icon);
	}

	/**
	 * Add a link
	 * 
	 * @param icon
	 * @param title
	 * @param text
	 */
	public String getIcon(int id) {
		return icons.get(id);
	}

	/**
	 * Show it and return the selected id or -1 for cancel/abort
	 * 
	 * @return
	 */
	public int show() {
		String key = "show.actiondialog." + id + links.size();

		// exist?
		if (Setting.get(key, -1) != -1) {
			return Setting.get(key, -1);
		}

		// ask user
		int erg = TaskDialogs.choice(null, title, message, defaultAnswer, (Collection<CommandLink>) links, key);

		return erg;
	}

	/**
	 * @return the links
	 */
	public ArrayList<CommandLink> getLinks() {
		return links;
	}

}
