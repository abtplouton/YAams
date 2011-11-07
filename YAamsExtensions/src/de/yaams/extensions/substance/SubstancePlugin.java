/**
 * 
 */
package de.yaams.extensions.substance;

import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.UIManager;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.AutumnSkin;
import org.pushingpixels.substance.api.skin.BusinessBlackSteelSkin;
import org.pushingpixels.substance.api.skin.BusinessBlueSteelSkin;
import org.pushingpixels.substance.api.skin.BusinessSkin;
import org.pushingpixels.substance.api.skin.ChallengerDeepSkin;
import org.pushingpixels.substance.api.skin.CremeCoffeeSkin;
import org.pushingpixels.substance.api.skin.CremeSkin;
import org.pushingpixels.substance.api.skin.DustCoffeeSkin;
import org.pushingpixels.substance.api.skin.DustSkin;
import org.pushingpixels.substance.api.skin.EmeraldDuskSkin;
import org.pushingpixels.substance.api.skin.GeminiSkin;
import org.pushingpixels.substance.api.skin.GraphiteAquaSkin;
import org.pushingpixels.substance.api.skin.GraphiteGlassSkin;
import org.pushingpixels.substance.api.skin.GraphiteSkin;
import org.pushingpixels.substance.api.skin.MagellanSkin;
import org.pushingpixels.substance.api.skin.MistAquaSkin;
import org.pushingpixels.substance.api.skin.MistSilverSkin;
import org.pushingpixels.substance.api.skin.ModerateSkin;
import org.pushingpixels.substance.api.skin.NebulaBrickWallSkin;
import org.pushingpixels.substance.api.skin.NebulaSkin;
import org.pushingpixels.substance.api.skin.OfficeBlue2007Skin;
import org.pushingpixels.substance.api.skin.OfficeSilver2007Skin;
import org.pushingpixels.substance.api.skin.RavenSkin;
import org.pushingpixels.substance.api.skin.SaharaSkin;
import org.pushingpixels.substance.api.skin.TwilightSkin;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.Setting;
import de.yaams.maker.helper.SystemHelper;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.extensions.IExtension;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.gui.YMessagesDialog;
import de.yaams.maker.helper.gui.YSettingHelper;
import de.yaams.maker.helper.gui.form.FormComboBox;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormLink;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;
import de.yaams.maker.helper.gui.icons.IconCache;
import de.yaams.maker.programm.YAamsCore;
import de.yaams.maker.programm.YaFrame;
import de.yaams.maker.programm.plugins.BasePlugin;

/**
 * @author Nebli
 * 
 */
public class SubstancePlugin extends BasePlugin {

	final static String[] className = { "0", BusinessSkin.class.getName(), BusinessBlueSteelSkin.class.getName(),
			BusinessBlackSteelSkin.class.getName(), CremeSkin.class.getName(), CremeCoffeeSkin.class.getName(), SaharaSkin.class.getName(),
			ModerateSkin.class.getName(), OfficeSilver2007Skin.class.getName(), OfficeBlue2007Skin.class.getName(),
			NebulaSkin.class.getName(), NebulaBrickWallSkin.class.getName(), AutumnSkin.class.getName(), MistSilverSkin.class.getName(),
			MistAquaSkin.class.getName(), DustSkin.class.getName(), DustCoffeeSkin.class.getName(), GeminiSkin.class.getName(),
			TwilightSkin.class.getName(), MagellanSkin.class.getName(), GraphiteSkin.class.getName(), GraphiteGlassSkin.class.getName(),
			GraphiteAquaSkin.class.getName(), RavenSkin.class.getName(), ChallengerDeepSkin.class.getName(),
			EmeraldDuskSkin.class.getName() };

	final static String[] classTitle = { "System", BusinessSkin.NAME, BusinessBlueSteelSkin.NAME, BusinessBlackSteelSkin.NAME,
			CremeSkin.NAME, CremeCoffeeSkin.NAME, SaharaSkin.NAME, ModerateSkin.NAME, OfficeSilver2007Skin.NAME, OfficeBlue2007Skin.NAME,
			NebulaSkin.NAME, NebulaBrickWallSkin.NAME, AutumnSkin.NAME, MistSilverSkin.NAME, MistAquaSkin.NAME, DustSkin.NAME,
			DustCoffeeSkin.NAME, GeminiSkin.NAME, TwilightSkin.NAME, MagellanSkin.NAME, GraphiteSkin.NAME, GraphiteGlassSkin.NAME,
			GraphiteAquaSkin.NAME, RavenSkin.NAME, ChallengerDeepSkin.NAME, EmeraldDuskSkin.NAME };

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.core.helper.plugins.BasePlugin#useable(de.yaams.core.helper.
	 * gui.YMessagesDialog, int)
	 */
	@Override
	public boolean useable(YMessagesDialog md) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.core.helper.plugins.BasePlugin#start()
	 */
	@Override
	public void start() {

		// add plugin
		IconCache.addPNG(SubstancePlugin.class, "substance");

		// set look and feel
		final String skin = Setting.get("substance", "0");

		if (skin != null && !skin.equals("0")) {
			ExtentionManagement.add("yaframe.start.finish", new IExtension() {

				@Override
				public void work(HashMap<String, Object> objects) {
					setWindowLook(Setting.get("substance.decoration", true));
					setSkin(skin);

				}
			});

		}

		// add setting
		ExtentionManagement.add("form.options.system", new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {
				FormBuilder f = (FormBuilder) objects.get("form");

				// add it
				f.addHeader("substance", new FormHeader(I18N.t("Look and Feel"), "substance").setColumn(4));

				FormComboBox c = YSettingHelper.combo(null, I18N.t("Look"), "substance", "0", className, classTitle);

				c.selectField(Setting.get("substance", "0"));
				c.addChangeListener(new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						String skin = form.getContentAsString();
						YAamsCore.save();

						setSkin(skin);
					}
				});
				c.getBox().setRenderer(new SubstanceComboBoxRenderer());
				f.addElement("substance.look", c);

				f.addElement(
						"substance.window",
						YSettingHelper.bool(null, I18N.t("Use System Window Decoration"), "substance.decoration", true).addChangeListener(
								new FormElementChangeListener() {

									@Override
									public void stateChanged(FormElement form) {
										// set?
										setWindowLook(Boolean.getBoolean(form.getContentAsString()));
										YAamsCore.save();
									}
								}));

				String[] eles = new String[] { "default", "CamelThrownTrees", "CreteSenesi", "Gargoyle" };

				f.addElement("substance.startlogo",
						YSettingHelper.combo(null, I18N.t("Startlogo"), "substance.startlogo", eles[0], eles, eles));
			}
		});

		// add thanks
		ExtentionManagement.add("form.options.info", new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {
				((FormBuilder) objects.get("form")).addElement("thx.substance",
						new FormLink("Substance", "https://substance.dev.java.net/"));

			}
		});
	}

	/**
	 * @param skin
	 */
	protected void setSkin(String skin) {
		try {
			// set look and feel
			if (skin != null && !skin.equals("0")) {
				SubstanceLookAndFeel.setSkin(skin);
				// load it
				YMessagesDialog ymd = new YMessagesDialog(I18N.t("Kann Systemicons nicht laden."), "substance.systemicons");
				IconCache.loadSystmIcons(ymd);
				ymd.showOk();
			} else {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				// SwingUtilities.updateComponentTreeUI(YaFrame.get());
				SystemHelper.restart();
			}
		} catch (final Throwable t) {
			YEx.error("Can not set skin " + skin, t);
		}
	}

	/**
	 * @param form
	 */
	protected void setWindowLook(boolean set) {
		try {
			YaFrame.get().setVisible(false);
			// set it
			JFrame.setDefaultLookAndFeelDecorated(!set);
			// YaFrame.get().setUndecorated(!set);
			if (!set) {
				YaFrame.get().getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
			}
			YaFrame.get().setVisible(true);
		} catch (final Throwable t) {
			YEx.info("Can not set window look and feel " + set, t);
		}
	}

}
