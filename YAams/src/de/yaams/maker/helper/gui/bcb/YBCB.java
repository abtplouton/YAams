/**
 * 
 */
package de.yaams.maker.helper.gui.bcb;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTree;

import org.pushingpixels.flamingo.api.bcb.BreadcrumbBarModel;
import org.pushingpixels.flamingo.api.bcb.BreadcrumbItem;
import org.pushingpixels.flamingo.api.bcb.BreadcrumbPathEvent;
import org.pushingpixels.flamingo.api.bcb.BreadcrumbPathListener;

import de.yaams.maker.helper.gui.YToolBar;
import de.yaams.maker.helper.gui.icons.IconCache;
import de.yaams.maker.programm.YaFrame;
import de.yaams.maker.programm.favorit.FavoritManagement;
import de.yaams.maker.programm.tabs.YaTab;

/**
 * @author abby
 * 
 */
public class YBCB extends JPanel {

	private static final long serialVersionUID = 3445143340398196389L;

	protected YToolBar right;
	protected YBreadcrumbTreeAdapterSelector bar;

	/**
	 * @param arg0
	 */
	public YBCB() {
		super(new BorderLayout());
		right = new YToolBar();
	}

	/**
	 * Build the navi
	 * 
	 * @param bcb
	 */
	public void build(BcbBuilder bcb, final YaTab tab) {
		final JTree tree = new JTree(bcb.getRoot());
		bar = new YBreadcrumbTreeAdapterSelector(tree.getModel(), new YBreadcrumbTreeAdapterSelector.TreeAdapter() {
			@Override
			public String toString(Object node) {

				BcbElement b = (BcbElement) node;
				return b.getTitle();
			}

			@Override
			public Icon getIcon(Object node) {
				BcbElement b = (BcbElement) node;

				if (b.getIcon() != null) {
					return IconCache.get(b.getIcon());
				} else {
					return null;
				}
			}

		}, false);

		bar.setPath(bcb.getList());

		// add listener
		bar.getModel().addPathListener(new BreadcrumbPathListener() {

			@Override
			public void breadcrumbPathEvent(BreadcrumbPathEvent b) {
				BreadcrumbBarModel<BcbElement> bbm = (BreadcrumbBarModel<BcbElement>) b.getSource();
				BreadcrumbItem<BcbElement> bci = bbm.getItem(bbm.getItemCount() - 1);
				BcbElement bcb = bci.getData();

				// close tab
				YaFrame.close(tab.getId());

				// run it
				bcb.getAction().actionPerformed(null);

			}
		});

		add(bar, BorderLayout.CENTER);

		// add fav
		add(FavoritManagement.getToolbar(tab), BorderLayout.WEST);

		// add button
		add(right, BorderLayout.EAST);
	}

	/**
	 * @return the right
	 */
	public YToolBar getRight() {
		return right;
	}

}
