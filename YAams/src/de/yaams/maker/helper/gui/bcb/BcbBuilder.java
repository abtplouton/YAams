/**
 * 
 */
package de.yaams.maker.helper.gui.bcb;

import java.util.ArrayList;

import org.pushingpixels.flamingo.api.bcb.BreadcrumbItem;

import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.icons.IconCache;
import de.yaams.maker.programm.YaFrame;

/**
 * @author abby
 * 
 */
public class BcbBuilder {

	protected BcbElement root, act, nextAct;
	protected ArrayList<BreadcrumbItem<Object>> list;

	/**
	 * 
	 */
	public BcbBuilder() {
		// build root
		root = new BcbElement(null, null, null);
		act = root;
		list = new ArrayList<BreadcrumbItem<Object>>();
	}

	/**
	 * finish the last selection and create a new
	 */
	public void addSeperator() {
		// reset
		act = nextAct;
	}

	/**
	 * Add a element to select to the act level
	 * 
	 * @param title
	 * @param icon
	 * @param tabID
	 */
	public void addElement(String title, String icon, final String tabID, boolean isSelected) {
		// build it
		BcbElement bcb = new BcbElement(title, icon, new AE() {

			@Override
			public void run() {
				// close tab

				// open new tab
				if (tabID != null) {
					YaFrame.open(tabID);
				}

			}
		});
		act.add(bcb);

		// is selected
		if (isSelected) {
			nextAct = bcb;

			// add path
			BreadcrumbItem<Object> b = new BreadcrumbItem<Object>(title, nextAct);
			b.setIcon(IconCache.get(icon));

			list.add(b);
		}

	}

	/**
	 * @return the root
	 */
	public BcbElement getRoot() {
		return root;
	}

	/**
	 * @return the list
	 */
	public ArrayList<BreadcrumbItem<Object>> getList() {
		return list;
	}
}
