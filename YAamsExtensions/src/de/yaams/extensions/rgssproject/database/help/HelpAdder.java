/**
 * 
 */
package de.yaams.extensions.rgssproject.database.help;

import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.helpcenter.HelpCenterManagement;

/**
 * @author abt
 * 
 */
public class HelpAdder {

	/**
	 * Create a new HelpAdder
	 */
	public HelpAdder() {
		HelpCenterManagement.register(RGSS1Helper.getFileName(Type.ACTOR), I18N.t("Actors"), "hero",
				HelpAdder.class.getResource("Actors.html"));

		HelpCenterManagement.register(RGSS1Helper.getFileName(Type.ITEM), I18N.t("Items"), "item",
				HelpAdder.class.getResource("Items.html"));
	}

}
