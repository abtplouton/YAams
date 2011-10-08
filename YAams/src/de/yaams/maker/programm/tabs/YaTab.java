/**
 * 
 */
package de.yaams.maker.programm.tabs;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jidesoft.document.DocumentComponent;
import com.jidesoft.document.DocumentComponentAdapter;
import com.jidesoft.document.DocumentComponentEvent;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.YActionDialog;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.YToolBar;
import de.yaams.maker.helper.gui.bcb.BcbBuilder;
import de.yaams.maker.helper.gui.bcb.YBCB;
import de.yaams.maker.helper.gui.icons.IconCache;

/**
 * @author abt
 * 
 */
public abstract class YaTab extends JPanel {
	private static final long serialVersionUID = 2986098553296662160L;

	// protected YHeader info;
	protected JComponent content, bottom;
	protected YBCB header;
	protected JComponent bar;
	protected DocumentComponent document;
	protected boolean modified;

	/**
	 * Create a new YAPanel
	 */
	public YaTab() {
		super(new BorderLayout());
	}

	protected void buildGui() {
		// info = new YHeader("");
		// setHeader();
		// create it
		content = getContent();
		bottom = getBottom();

		setHeader();

		// build layout

		add(BorderLayout.CENTER, content);
		if (bottom != null) {
			add(BorderLayout.SOUTH, bottom);
		}

		// show id
		// if (YLevel.IS_DEVELOPER)
		// info.getText().addMouseListener(new MouseAdapter() {
		//
		// @Override public void mouseClicked(final MouseEvent e) {
		// // show id
		// YDialog.ok(getTitle(), id, getIcon() + "_debug");
		//
		// }
		// });
	}

	/**
	 * Set the Header
	 */
	protected void setHeader() {
		// build toplayout
		header = new YBCB();

		// build bcb
		BcbBuilder bcb = new BcbBuilder();
		try {
			buildBcb(bcb);
		} catch (Throwable t) {
			YEx.info("Can not create Bread crump navigation", t);
		}
		header.build(bcb, this);

		add(BorderLayout.NORTH, header);
	}

	/**
	 * Build the navi
	 */
	protected abstract void buildBcb(BcbBuilder bcb);

	/**
	 * Set the Header
	 * 
	 * @param name
	 * @param ico
	 */
	// protected void setHeader(final String title, final String icon) {
	// info.setText(title, icon);
	// }

	/**
	 * Set the layout as box, so that you can use content as box
	 */
	// protected void setBox() {
	// add(BorderLayout.CENTER, YFactory.createVerticalBox(content));
	// }

	/**
	 * This method is called, when the frame will be closed
	 * 
	 * @return return true, if the tab can close or false, if it not possible
	 */
	public boolean closeTab() {
		if (modified) {
			// ask user
			YActionDialog yad = new YActionDialog("tabsave", I18N.t("Speichere Änderungen in {0}?", getTitle()));
			yad.addLink("disk_" + getIcon(), I18N.t("Speichern"), I18N.t("Änderungen speichern und dann den Tab schließen"));
			yad.addLink("trash_" + getIcon(), I18N.t("Nicht speichern"),
					I18N.t("Alle nicht gespeicherten Änderungen verwerfen und den Tab schließen."));
			yad.addLink("cancel", I18N.t("Abbrechen"), I18N.t("Zurück zum offen Tab"));

			int c = yad.show();

			// save?
			if (c == 0) {
				saveChanged();
			}

			// close tab, based on value
			return c == 0 || c == 1;
		}
		return true;
	}

	/**
	 * Add a savebutton to the tab
	 */
	protected void addSaveButton() {

		// add save button
		header.getRight().add(YFactory.tb(I18N.t("Save"), "disk", new AE() {

			@Override
			public void run() {
				saveChanged();
			}
		}));
	}

	/**
	 * Save Content
	 */
	public void saveChanged() {
		try {
			setModified(false);
			saveIntern();
		} catch (final Throwable t) {
			YEx.info("Can not save changed", t);
		}
	}

	/**
	 * Save chanced, overwrite it to implement it, and call addSaveTab() to add
	 * the support button
	 */
	protected void saveIntern() {}

	/**
	 * @return the bottom
	 */
	public JComponent getBottom() {
		return null;
	}

	/**
	 * Get the icon for this tab
	 * 
	 * @return
	 */
	public abstract String getIcon();

	/**
	 * Get the name for this tab
	 * 
	 * @return
	 */
	public abstract String getTitle();

	/**
	 * @return the info
	 */
	// public YHeader getHeader() {
	// return info;
	// }

	/**
	 * @return the content
	 */
	public abstract JComponent getContent();

	/**
	 * @return the document
	 */
	public DocumentComponent getDocument(DocumentComponent comp, String uid) {
		// init it
		if (document == null) {
			document = comp;

			// build
			document.setTitle(String.valueOf(getTitle()));
			document.setIcon(IconCache.get(String.valueOf(getIcon())));
			document.setComponent(this);

			// add closing method
			document.addDocumentComponentListener(new DocumentComponentAdapter() {

				@Override
				public void documentComponentClosing(final DocumentComponentEvent e) {
					document.setAllowClosing(closeTab());

				}
			});

			// add arguments
			parseArguments(TabEvent.getArguments(uid));
		}

		return document;
	}

	public static DocumentComponent getLoadingDocument(String uid) {
		// loading element
		JLabel j = new JLabel(I18N.t("Lädt Tab"), IconCache.get("wait", 32), JLabel.CENTER);
		j.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// build
		DocumentComponent document = new DocumentComponent(j, uid, "Lädt", IconCache.get("wait"));

		return document;
	}

	/**
	 * @return the modified
	 */
	public boolean isModified() {
		return modified;
	}

	/**
	 * @param modified
	 *            the modified to set
	 */
	public void setModified(final boolean modified) {
		this.modified = modified;
		document.setTitle((modified ? "* " : "") + getTitle());
	}

	/**
	 * @return the toolbar
	 */
	public YToolBar getToolbar() {
		return header.getRight();
	}

	/**
	 * @return the uid
	 */
	public abstract String getId();

	public static void addBcb(BcbBuilder bcb) {
		// bcb.addElement(title, icon, tabID)
	}

	/**
	 * This method will called, if reopen the tab or open it, to parse the
	 * arguments
	 * 
	 * @param id
	 */
	public void parseArguments(HashMap<String, String> arguments) {}
}
