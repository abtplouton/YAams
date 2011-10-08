package de.yaams.maker.helper.gui.rightclick;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.icons.IconCache;

// @author Santhosh Kumar T - santhosh@in.fiorano.com
public class CutAction extends AbstractAction {
	private static final long serialVersionUID = 1377503577744816671L;
	protected JTextComponent comp;
	
	public CutAction(JTextComponent comp) {
		super(I18N.t("Cut"), IconCache.get("cut"));
		this.comp = comp;
	}
	
	public void actionPerformed(ActionEvent e) {
		comp.cut();
	}
	
	@Override public boolean isEnabled() {
		return comp.isEditable() && comp.isEnabled() && comp.getSelectedText() != null;
	}
}