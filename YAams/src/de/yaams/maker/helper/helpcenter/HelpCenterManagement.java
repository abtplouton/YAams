/**
 * 
 */
package de.yaams.maker.helper.helpcenter;

import java.net.URL;
import java.util.HashMap;

import de.yaams.maker.helper.I18N;

/**
 * @author abt
 * 
 */
public class HelpCenterManagement {
	
	protected static HashMap<String, HelpFile> helps = load();
	
	/**
	 * Create a new HelpCenterManagement
	 */
	public HelpCenterManagement() {
		// TODO Auto-generated constructor stub
	}
	
	private static HashMap<String, HelpFile> load() {
		HashMap<String, HelpFile> h = new HashMap<String, HelpFile>();
		h.put("error", new HelpFile("error", "", "error", HelpCenterManagement.class.getResource("Error.html")));
		
		return h;
	}
	
	public static void register(String id, String title, String icon, URL url) {
		helps.put(id, new HelpFile(id, title, icon, url));
	}
	
	public static HelpFile get(String id) {
		// has it?
		if (helps.containsKey(id)) {
			return helps.get(id);
		} else {
			return helps.get("error").setTitle(I18N.t("Help for {0} not found.", id));
		}
	}
	
}
