package de.yaams.maker.helper.gui.rightclick;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.icons.IconCache;

// @author Santhosh Kumar T - santhosh@in.fiorano.com
class PasteAction extends AbstractAction {
	private static final long serialVersionUID = -4162711823686660551L;
	protected JTextComponent comp;
	
	public PasteAction(JTextComponent comp) {
		super(I18N.t("Paste"), IconCache.get("paste"));
		this.comp = comp;
	}
	
	public void actionPerformed(ActionEvent e) {
		comp.paste();
	}
	
	@Override public boolean isEnabled() {
		if (comp.isEditable() && comp.isEnabled()) {
			Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
			return contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		} else {
			return false;
		}
	}
}