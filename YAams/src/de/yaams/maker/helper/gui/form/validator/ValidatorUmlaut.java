/**
 * 
 */
package de.yaams.maker.helper.gui.form.validator;

import java.io.File;

import org.apache.log4j.Level;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.YMessagesDialog;

/**
 * @author abby
 * 
 */
public class ValidatorUmlaut extends ValidatorBase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.helper.gui.form.validator.ValidatorBase#isValide(de.yaams
	 * .maker.helper.gui.YMessagesDialog)
	 */
	@Override
	public void isValide(YMessagesDialog y) {
		// check for special elements
		StringBuffer erg = new StringBuffer("");
		for (Character ch : form.getContentAsString().toCharArray()) {
			if (!Character.isLetterOrDigit(ch) && !ch.equals(File.separatorChar) && !ch.equals(':')) {
				erg.append(ch);
			}
		}

		// found something
		if (erg.length() >= 1) {
			y.add(I18N.t(
					"Die Angaben in {0} enthalten unzulässige Zeichen: {1}. Möglicherweise kann es dadurch Ausführungsprobleme geben.",
					form.getHeader().getText(), erg.toString()), Level.INFO_INT);
		}

	}
}
