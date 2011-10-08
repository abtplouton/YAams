/**
 * 
 */
package de.yaams.core.helper.gui.form.validator;

import org.apache.log4j.Level;
import org.apache.log4j.Priority;

import de.yaams.core.helper.I18N;
import de.yaams.core.helper.gui.YMessagesDialog;

/**
 * @author Praktikant
 * 
 */
public class ValidatorNotEmpty extends ValidatorBase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.helper.gui.form.validator.ValidatorBase#isValide(de.yaams
	 * .maker.helper.gui.YMessagesDialog)
	 */
	@Override
	public void isValide(YMessagesDialog y) {
		// check it
		if (form.getContentAsString() == null || form.getContentAsString().length() == 0) {
			y.add(I18N.t("Feld {0} darf nicht leer sein.", form.getHeader().getText()), Priority.WARN_INT);
		}

	}

}
