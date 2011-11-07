/**
 * 
 */
package de.yaams.maker.helper.gui.rightclick;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import de.yaams.maker.helper.FileHelper;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.gui.icons.IconCache;
import de.yaams.maker.helper.integration.EditorIntegration;
import de.yaams.maker.programm.environment.YLevel;

/**
 * @author abt
 * 
 */
public class YRightClickMenuText {

	/**
	 * Create a new RightClickMenu
	 */
	public static void install(final JTextComponent tc) {

		tc.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent me) {
				mousePressed(me);
			}

			@Override
			public void mousePressed(MouseEvent me) {
				// right click?
				if (me.isPopupTrigger()) {
					// create popup menu and show
					JPopupMenu menu = new JPopupMenu();
					menu.add(new CutAction(tc));
					menu.add(new CopyAction(tc));
					menu.add(new PasteAction(tc));
					menu.add(new DeleteAction(tc));
					menu.addSeparator();
					menu.add(new SelectAllAction(tc));

					if (YLevel.IS_ADVANCED) {
						menu.addSeparator();
						menu.add(new ImportAction(tc));
						menu.add(new ExportAction(tc));
					}

					// show it
					Point pt = SwingUtilities.convertPoint(me.getComponent(), me.getPoint(), tc);
					menu.show(tc, pt.x, pt.y);

				}

			}
		});

	}

	// @author Santhosh Kumar T - santhosh@in.fiorano.com
	static class SelectAllAction extends AbstractAction {
		private static final long serialVersionUID = 5588128629620319865L;
		protected JTextComponent comp;

		public SelectAllAction(JTextComponent comp) {
			super(I18N.t("Select All"), IconCache.get("selectAll"));
			this.comp = comp;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			comp.selectAll();
		}

		@Override
		public boolean isEnabled() {
			return comp.isEnabled() && comp.getText().length() > 0;
		}
	}

	// @author Santhosh Kumar T - santhosh@in.fiorano.com
	static class PasteAction extends AbstractAction {
		private static final long serialVersionUID = -4162711823686660551L;
		protected JTextComponent comp;

		public PasteAction(JTextComponent comp) {
			super(I18N.t("Paste"), IconCache.get("paste"));
			this.comp = comp;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			comp.paste();
		}

		@Override
		public boolean isEnabled() {
			if (comp.isEditable() && comp.isEnabled()) {
				Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
				return contents.isDataFlavorSupported(DataFlavor.stringFlavor);
			} else {
				return false;
			}
		}
	}

	// @author Santhosh Kumar T - santhosh@in.fiorano.com
	static class CopyAction extends AbstractAction {
		private static final long serialVersionUID = -453739934222615468L;
		protected JTextComponent comp;

		public CopyAction(JTextComponent comp) {
			super(I18N.t("Copy"), IconCache.get("copy"));
			this.comp = comp;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			comp.copy();
		}

		@Override
		public boolean isEnabled() {
			return comp.isEnabled() && comp.getSelectedText() != null;
		}
	}

	// @author Santhosh Kumar T - santhosh@in.fiorano.com
	static public class CutAction extends AbstractAction {
		private static final long serialVersionUID = 1377503577744816671L;
		protected JTextComponent comp;

		public CutAction(JTextComponent comp) {
			super(I18N.t("Cut"), IconCache.get("cut"));
			this.comp = comp;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			comp.cut();
		}

		@Override
		public boolean isEnabled() {
			return comp.isEditable() && comp.isEnabled() && comp.getSelectedText() != null;
		}
	}

	// @author Santhosh Kumar T - santhosh@in.fiorano.com
	static class DeleteAction extends AbstractAction {
		private static final long serialVersionUID = -2425514124546515657L;
		protected JTextComponent comp;

		public DeleteAction(JTextComponent comp) {
			super(I18N.t("Delete"), IconCache.get("del"));
			this.comp = comp;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			comp.replaceSelection(null);
		}

		@Override
		public boolean isEnabled() {
			return comp.isEditable() && comp.isEnabled() && comp.getSelectedText() != null;
		}
	}

	static class ImportAction extends AbstractAction {
		private static final long serialVersionUID = -2425514124546515657L;
		protected JTextComponent comp;

		public ImportAction(JTextComponent comp) {
			super(I18N.t("Import"), IconCache.get("folder"));
			this.comp = comp;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			File[] fs = EditorIntegration.openDialog(true, false);

			// include all
			for (File f : fs) {
				try {
					comp.getDocument().insertString(comp.getSelectionEnd(), FileHelper.readFile(f), null);
				} catch (Throwable t) {
					YEx.info("Can not import " + f, t);
				}
			}
		}

		@Override
		public boolean isEnabled() {
			return comp.isEditable() && comp.isEnabled();
		}
	}

	static class ExportAction extends AbstractAction {
		private static final long serialVersionUID = -2425514124546515657L;
		protected JTextComponent comp;

		public ExportAction(JTextComponent comp) {
			super(I18N.t("Export"), IconCache.get("arrow"));
			this.comp = comp;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			File f = EditorIntegration.saveDialog();
			if (f != null) {
				FileHelper.saveXML(f, comp.getSelectedText());
			}
		}

		@Override
		public boolean isEnabled() {
			return comp.isEditable() && comp.isEnabled() && comp.getSelectedText() != null;
		}
	}
}
