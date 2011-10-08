/**
 * 
 */
package de.yaams.maker.helper.gui.dock;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.hyperlink.AbstractHyperlinkAction;

import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.icons.IconCache;

/**
 * @author Praktikant
 * 
 */
public class DockLinkPanel extends DockBasePanel {

	private static final long serialVersionUID = 1L;
	protected ArrayList<JComponent> links;

	/**
	 * 
	 */
	public DockLinkPanel() {
		links = new ArrayList<JComponent>();
	}

	/**
	 * Build the gui
	 * 
	 * @param title
	 * @param icon
	 * @param config
	 */
	@Override
	public void buildGui(String title, String icon, boolean config) {
		HashMap<String, Object> o = new HashMap<String, Object>();
		o.put("dock", this);
		o.put("links", links);

		// ask all
		ExtentionManagement.work("dock." + dock.getId() + ".links", o);

		// build content
		YFactory.createVerticalBox(content, true);

		// add links
		for (JComponent l : links) {
			content.add(l);
		}

		super.buildGui(title, icon, config);
	}

	/**
	 * Helpermethod to add a link
	 * 
	 * @param title
	 * @param icon
	 * @param ae
	 */
	public void addLink(String title, String icon, final AE ae) {

		JComponent link;

		// add as link or label?
		if (ae != null) {
			AbstractHyperlinkAction<Object> linkAction = new AbstractHyperlinkAction<Object>(title) {
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					ae.actionPerformed(e);
				}
			};
			// build it
			link = new JXHyperlink(linkAction);
			if (icon != null) {
				((JXHyperlink) link).setIcon(IconCache.get(icon));
			}
		} else {
			// build it
			link = new JLabel(title);
			if (icon != null) {
				((JLabel) link).setIcon(IconCache.get(icon));
			}
		}

		link.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		links.add(link);
	}
}
