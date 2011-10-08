/**
 * 
 */
package de.yaams.maker.helper.gui.form;

import javax.swing.JButton;

import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.icons.IconCache;

/**
 * @author Nebli
 * 
 */
public class FormButton extends FormElement {
	
	protected JButton button;
	
	/**
	 * @param title
	 */
	public FormButton(final String title, final String icon, final AE e) {
		super("");
		
		button = YFactory.b(title, icon, e);
		element = button;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.form.FormElement#getContentAsString()
	 */
	@Override public String getContentAsString() {
		return button.getText();
	}
	
	public void set(String title, String icon) {
		button.setText(title);
		button.setIcon(IconCache.get(icon));
	}
}
