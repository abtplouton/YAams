/**
 * 
 */
package de.yaams.maker.helper.gui.icons;

import org.apache.log4j.Level;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.YMessagesDialog;
import de.yaams.maker.helper.gui.form.validator.ValidatorBase;

/**
 * @author Praktikant
 * 
 */
public class ValidatorIcon extends ValidatorBase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.helper.gui.form.validator.ValidatorBase#isValide(de.yaams
	 * .maker.helper.gui.YMessagesDialog)
	 */
	@Override
	public void isValide(YMessagesDialog y) {
		if (!IconCache.exist(form.getContentAsString())) {
			y.add(I18N.t("Das gewählte Icon {0} existiert nicht für das Feld {1}", form.getContentAsString(), form.getHeader().getText()),
					Level.INFO_INT);
		}

	}

}
