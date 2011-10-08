/**
 * 
 */
package de.yaams.core.helper.gui.form.validator;

import java.io.File;

import de.yaams.core.helper.FileHelper;
import de.yaams.core.helper.gui.YMessagesDialog;

/**
 * @author Praktikant
 * 
 */
public class ValidatorFile extends ValidatorBase {

	protected boolean dir, checkWrite;

	/**
	 * @param dir
	 * @param checkWrite
	 */
	public ValidatorFile(boolean dir, boolean checkWrite) {
		super();
		this.dir = dir;
		this.checkWrite = checkWrite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.helper.gui.form.validator.ValidatorBase#isValide(de.yaams
	 * .maker.helper.gui.YMessagesDialog)
	 */
	@Override
	public void isValide(YMessagesDialog y) {
		FileHelper.checkPath(new File(form.getContentAsString()), y, dir, checkWrite);

	}

}
