/**
 * 
 */
package de.yaams.extensions.rgssproject.wizard;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.Setting;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.gui.form.FormCheckbox;
import de.yaams.maker.helper.gui.form.FormInfo;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.wizard.core.WizardBasePage;

/**
 * @author Praktikant
 * 
 */
public class WizardRpgXpVxLicenePage extends WizardBasePage {

	protected FormBuilder form;

	/**
	 * 
	 */
	public WizardRpgXpVxLicenePage() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.wizard.core.WizardBasePage#getImgName()
	 */
	@Override
	protected String getImgName() {
		return null;
	}

	/**
	 * Get the img for the left view
	 * 
	 * @return
	 */
	@Override
	public BufferedImage getImage() {
		if (cimg == null)
			try {
				cimg = ImageIO.read(WizardRpgXpVxLicenePage.class.getResource("licence.jpg"));
			} catch (Throwable t) {
				YEx.info("Can not load Image " + getImgName() + " for wizard", t);
			}
		return cimg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.wizard.core.WizardBasePage#getIcon()
	 */
	@Override
	protected String getIcon() {
		return "rpgxp_rpgvx";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.wizard.core.WizardBasePage#getTitle()
	 */
	@Override
	public String getTitle() {
		return I18N.t("Lizenzen für RPG XP/VX");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.wizard.core.WizardBasePage#getForm()
	 */
	@Override
	public JComponent getForm() {
		form = new FormBuilder("xp.licene.wizard");
		form.addElement(
				"basic.help",
				new FormInfo(
						"",
						I18N.t("Um alle RPG Maker XP/VX - Funktionen in YAams nutzen zu können, muss das Produkt aus lizenzrechtlichen Gründen von Enterbrain gekauft worden sein. Dazu muss hier angegeben werden, welches Produkt gekauft wurde."))
						.setSorting(-1));
		form.addElement(
				"basic.help2",
				new FormInfo(
						"",
						I18N.t("Sollte unklar sein, was gekauft wurde, dann im Zweifelsfall nichts auswählen. Dem Benutzer ist klar, dass bei Falschaussage, etwailige Ansprüche an ihm zu entrichten sind."))
						.setSorting(1));

		// add boxes
		form.addElement("basic.rpgxp", new FormCheckbox(I18N.t("RPG Maker XP"), false));
		form.addElement("basic.rpgvx", new FormCheckbox(I18N.t("RPG Maker VX"), false));

		return form.getPanel(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.wizard.core.WizardBasePage#finish()
	 */
	@Override
	public void finish() {
		// save it
		Setting.set("licence.xp", form.getElement("basic.rpgxp").getContentAsString());
		Setting.set("licence.vx", form.getElement("basic.rpgvx").getContentAsString());

	}

}
