/**
 * 
 */
package de.yaams.maker.helper.wizard;

import javax.swing.JComponent;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.Setting;
import de.yaams.maker.helper.gui.form.FormCheckbox;
import de.yaams.maker.helper.gui.form.FormInfo;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.wizard.core.WizardBasePage;
import de.yaams.maker.programm.YAamsCore;

/**
 * @author Praktikant
 * 
 */
public class WizardOnlinePage extends WizardBasePage {

	protected FormBuilder form;

	/**
	 * 
	 */
	public WizardOnlinePage() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizard.core.WizardBasePage#getImgName()
	 */
	@Override
	protected String getImgName() {
		return "online.jpg";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizard.core.WizardBasePage#getIcon()
	 */
	@Override
	protected String getIcon() {
		return "web";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizard.core.WizardBasePage#getTitle()
	 */
	@Override
	public String getTitle() {
		return I18N.t("Online");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizard.core.WizardBasePage#getForm()
	 */
	@Override
	public JComponent getForm() {

		form = new FormBuilder("wizard.online");
		form.addElement("basic.help", new FormInfo("", I18N.t("Nun muss gewählt werden, ob die automatische "
				+ "Suche nach Pluginupdates eingeschaltet werden soll. Bei der Prüfung wird nur die YAams-Version "
				+ "(Akt:{0}) und welche Version von welchem Plugin installiert ist, übertragen. Es werden keine "
				+ "weiteren oder personenbezogenen Informationen übertragen. Sollte keine dauerhafte "
				+ "Internetverbindung bestehen, sollten Updates deaktiviert werden.", YAamsCore.VERSION)).setSorting(-1));
		form.addElement("basic.update", new FormCheckbox("Updates aktivieren", true));

		return form.getPanel(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizard.core.WizardBasePage#getTitle()
	 */
	@Override
	public void finish() {
		// save it
		Setting.set("net.access", form.getElement("basic.update").getContentAsString());
	}

}
