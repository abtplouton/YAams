/**
 * 
 */
package de.yaams.maker.helper.wizard;

import javax.swing.JComponent;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.Setting;
import de.yaams.maker.helper.gui.YSettingHelper;
import de.yaams.maker.helper.gui.form.FormComboBox;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormInfo;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;
import de.yaams.maker.helper.wizard.core.WizardBasePage;
import de.yaams.maker.programm.YAamsCore;
import de.yaams.maker.programm.environment.YLevel;

/**
 * @author Praktikant
 * 
 */
public class WizardWelcomePage extends WizardBasePage {

	protected FormBuilder form;

	/**
	 * 
	 */
	public WizardWelcomePage() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizard.core.WizardBasePage#getImgName()
	 */
	@Override
	protected String getImgName() {
		return "welcome.jpg";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizard.core.WizardBasePage#getIcon()
	 */
	@Override
	protected String getIcon() {
		return "yaams";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizard.core.WizardBasePage#getTitle()
	 */
	@Override
	public String getTitle() {
		return I18N.t("Willkommen");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizard.core.WizardBasePage#getForm()
	 */
	@Override
	public JComponent getForm() {

		form = new FormBuilder("wizard.start");
		form.getHeader("basic").setTitle(I18N.t("Willkommen zu {0}!", YAamsCore.TITLE));
		form.addElement("basic.zhelp", new FormInfo("", I18N.t("Dieser kurze Assistent führt "
				+ "durch die Basis-Einstellungen. Erweiterte Optionen kann man in den Program-Optionen festlegen.<br>"
				+ "Zunächst wird die Sprache benötigt:")).setSorting(-1));
		form.addElement("basic.lang", YSettingHelper.combo(null, "", "lang", "de", I18N.getLangIDs(), I18N.getLangNames())
				.addChangeListener(new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						I18N.loadLang(form.getContentAsString());

					}
				}));

		form.addHeader("view", new FormHeader(I18N.t("Ansicht"), null));
		form.addElement("view.help", new FormInfo("", I18N
				.t("Damit YAams besser an die Bedürfnisse angepasst werden kann, muss die Grundansicht "
						+ "ausgewählt werden. Es kann später jederzeit in den Optionen gewechselt werden.")).setSorting(-1));
		form.addElement("view.view", new FormComboBox(I18N.t(""), new String[] { "0", "1" }, new String[] { I18N.t("Einfache Ansicht"),
				I18N.t("Erweiterte Ansicht") }));

		return form.getPanel(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizard.core.WizardBasePage#getTitle()
	 */
	@Override
	public void finish() {
		// set view
		YLevel.load(Integer.valueOf(form.getElement("view.view").getContentAsString()));
		// save it
		Setting.set("gui.level", form.getElement("view.view").getContentAsString());
	}

}
