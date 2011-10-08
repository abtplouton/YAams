package de.yaams.maker.helper.gui.rightclick;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.icons.IconCache;

// @author Santhosh Kumar T - santhosh@in.fiorano.com
class SelectAllAction extends AbstractAction {
	private static final long serialVersionUID = 5588128629620319865L;
	protected JTextComponent comp;
	
	public SelectAllAction(JTextComponent comp) {
		super(I18N.t("Select All"), IconCache.get("selectAll"));
		this.comp = comp;
	}
	
	public void actionPerformed(ActionEvent e) {
		comp.selectAll();
	}
	
	@Override public boolean isEnabled() {
		return comp.isEnabled() && comp.getText().length() > 0;
	}
}