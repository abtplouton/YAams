package de.yaams.maker.helper.gui.rightclick;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.icons.IconCache;

// @author Santhosh Kumar T - santhosh@in.fiorano.com
class CopyAction extends AbstractAction {
	private static final long serialVersionUID = -453739934222615468L;
	protected JTextComponent comp;
	
	public CopyAction(JTextComponent comp) {
		super(I18N.t("Copy"), IconCache.get("copy"));
		this.comp = comp;
	}
	
	public void actionPerformed(ActionEvent e) {
		comp.copy();
	}
	
	@Override public boolean isEnabled() {
		return comp.isEnabled() && comp.getSelectedText() != null;
	}
}