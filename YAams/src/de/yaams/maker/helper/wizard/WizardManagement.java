/**
 * 
 */
package de.yaams.maker.helper.wizard;

import java.util.ArrayList;

import de.yaams.maker.helper.Log;
import de.yaams.maker.helper.SwingHelper;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.wizard.core.WizardBasePage;
import de.yaams.maker.helper.wizard.core.WizardFrame;

/**
 * @author Praktikant
 * 
 */
public class WizardManagement {

	protected final static ArrayList<WizardBasePage> pages = new ArrayList<WizardBasePage>();

	/**
	 * 
	 */
	public WizardManagement() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Add the pages
	 * 
	 * @param wbp
	 */
	public static void addPage(WizardBasePage wbp) {
		pages.add(wbp);
	}

	/**
	 * Add the pages
	 * 
	 * @param wbp
	 */
	public static void start(final AE ae) {
		// has pages?
		if (pages.size() > 0) {
			Log.ger.info("Start wizard");
			new SwingHelper(false) {

				@Override
				public void run() {
					new WizardFrame(pages, ae);
				}
			};

			return;
		}
		// no pages?
		// start ae
		ae.actionPerformed(null);
	}
}
