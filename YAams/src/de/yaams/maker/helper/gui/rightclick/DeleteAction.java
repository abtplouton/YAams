package de.yaams.maker.helper.gui.rightclick;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.icons.IconCache;

// @author Santhosh Kumar T - santhosh@in.fiorano.com
class DeleteAction extends AbstractAction {
	private static final long serialVersionUID = -2425514124546515657L;
	protected JTextComponent comp;
	
	public DeleteAction(JTextComponent comp) {
		super(I18N.t("Delete"), IconCache.get("del"));
		this.comp = comp;
	}
	
	public void actionPerformed(ActionEvent e) {
		comp.replaceSelection(null);
	}
	
	@Override public boolean isEnabled() {
		return comp.isEditable() && comp.isEnabled() && comp.getSelectedText() != null;
	}
}