/**
 * 
 */
package de.yaams.maker.helper.wizard;

import javax.swing.JComponent;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.Setting;
import de.yaams.maker.helper.gui.YDialog;
import de.yaams.maker.helper.gui.form.FormCheckbox;
import de.yaams.maker.helper.gui.form.FormInfo;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.wizard.core.WizardBasePage;
import de.yaams.maker.programm.YAamsCore;
import de.yaams.maker.programm.plugins.core.PluginManager;

/**
 * @author Praktikant
 * 
 */
public class WizardPluginsPage extends WizardBasePage {

	protected FormBuilder form;

	/**
	 * 
	 */
	public WizardPluginsPage() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizard.core.WizardBasePage#getImgName()
	 */
	@Override
	protected String getImgName() {
		return "plugins.jpg";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizard.core.WizardBasePage#getIcon()
	 */
	@Override
	protected String getIcon() {
		return "plugin";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizard.core.WizardBasePage#getTitle()
	 */
	@Override
	public String getTitle() {
		return I18N.t("Bedienung");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizard.core.WizardBasePage#getForm()
	 */
	@Override
	public JComponent getForm() {

		form = new FormBuilder("wizard.plugins");
		form.addElement("basic.help",
				new FormInfo("", I18N.t("Damit YAams noch besser angepasst "
						+ "werden kann, muss jetzt eine Auswahl der Projekttypen getroffen werden, "
						+ "die man Nutzen möchte.<br>Support für:")).setSorting(-1));
		form.addElement("basic.xp", new FormCheckbox("RGSS Projekt", true));

		return form.getPanel(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wizard.core.WizardBasePage#getTitle()
	 */
	@Override
	public void finish() {
		if (Boolean.parseBoolean(form.getElement("basic.xp").getContentAsString())) {
			// install
			PluginManager.installFromOnline("rgssproject");

			// restart
			YDialog.ok(I18N.t("Damit die Änderungen übernommen werden können, muss {0} manuell neugestartet werden.", YAamsCore.TITLE), "",
					"plugin_restart");
		}
		Setting.set("wizard.plugin", true);
	}

}
