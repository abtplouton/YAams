/**
 * 
 */
package de.yaams.maker.programm.tabs;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JComponent;

import org.apache.commons.lang.SystemUtils;

import de.yaams.maker.helper.FileHelper;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.JavaHelper;
import de.yaams.maker.helper.Log;
import de.yaams.maker.helper.SystemHelper;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.YSettingHelper;
import de.yaams.maker.helper.gui.bcb.BcbBuilder;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormInfo;
import de.yaams.maker.helper.gui.form.FormLink;
import de.yaams.maker.helper.gui.form.FormTextArea;
import de.yaams.maker.helper.gui.form.FormTextField;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.helper.gui.tabs.ProjectSplitTab;
import de.yaams.maker.helper.gui.tabs.SplitActionListElement;
import de.yaams.maker.programm.YAamsCore;
import de.yaams.maker.programm.environment.YLevel;
import de.yaams.maker.programm.plugins.PluginLoader;
import de.yaams.maker.programm.project.Project;

/**
 * @author Nebli
 * 
 */
public class OptionsTab extends ProjectSplitTab {
	/**
	 * 
	 */
	public static final String OPTIONS_INFO = "options.info";

	private static final long serialVersionUID = -8849887394292456819L;

	/**
	 * Contains list, with ArrayList<BasisListElement> . Add
	 * SplitActionListElement to create a new setting
	 */
	public static final String EXADD = "options.list", EXSYS = "form.options.system";

	/**
	 * Create a Optionstab
	 */
	public OptionsTab() {
		super(buildList(), null);

		list.buildToolbar(null, null);

		buildGui();
		addSaveButton();
	}

	/**
	 * Save chanced, overwrite it to implement it, and call addSaveTab() to add
	 * the support button
	 */
	@Override
	protected void saveIntern() {
		YAamsCore.save();
	}

	/**
	 * Helpermethod to build the list
	 * 
	 * @return
	 */
	private static ArrayList<BasisListElement> buildList() {
		final ArrayList<BasisListElement> ary = new ArrayList<BasisListElement>();

		// info
		ary.add(new SplitActionListElement(I18N.t("Infos"), null, "info") {

			@Override
			protected Component getComponent(final Project p) {

				return addInfo();
			}
		});

		ary.add(new SplitActionListElement(I18N.t("System"), null, "monitor") {

			@Override
			protected Component getComponent(final Project p) {
				return addSystem();
			}
		});

		ary.add(new SplitActionListElement(I18N.t("Feedback"), null, "mail_web") {

			@Override
			protected Component getComponent(final Project p) {
				return addFeedback();
			}
		});

		// inform
		ExtentionManagement.work(EXADD, JavaHelper.createHashStringObj("list", ary));

		return ary;
	}

	/**
	 * @return
	 */
	public static Component addInfo() {
		// build form
		final FormBuilder f = new FormBuilder(OPTIONS_INFO);
		f.addHeader("basic", new FormHeader(I18N.t("Basics"), "info").setColumn(4));

		f.addElement("basic.yaams", new FormInfo("", YAamsCore.TITLE));

		if (YLevel.IS_ADVANCED) {
			f.addElement("basic.path", new FormTextField(I18N.t("Optionen"), YAamsCore.programPath.getAbsolutePath()));
			f.addElement("basic.temp", new FormTextField(I18N.t("Temp"), YAamsCore.tmpFolder.getAbsolutePath()));
			f.addElement("basic.system", new FormInfo(I18N.t("System"), SystemUtils.OS_NAME + " " + SystemUtils.OS_VERSION + " "
					+ SystemUtils.OS_ARCH));
			f.addElement("basic.log", new FormInfo(I18N.t("Log level"), Log.ger.getLevel().toString()));
			f.addElement("basic.ylevel", new FormInfo(I18N.t("Program level"), YLevel.getName(YLevel.TYP)));
			f.addElement("basic.java", new FormInfo(I18N.t("Java"), SystemUtils.JAVA_RUNTIME_NAME + " " + SystemUtils.JAVA_VENDOR + " "
					+ SystemUtils.JAVA_VERSION));
			f.addElement(
					"basic.ram",
					new FormInfo(I18N.t("RAM"), FileHelper.humanReadableByteCount(Runtime.getRuntime().freeMemory(), false) + " / "
							+ FileHelper.humanReadableByteCount(Runtime.getRuntime().totalMemory(), false) + " ("
							+ FileHelper.humanReadableByteCount(Runtime.getRuntime().maxMemory(), false) + ")"));
		}

		// add plugins
		f.addHeader("plugins", new FormHeader(I18N.t("Plugins"), "plugin").setColumn(8));
		for (String key : PluginLoader.getPlugins().keySet()) {
			f.addElement("plugins." + key, new FormInfo("", PluginLoader.get(key).getTitle()));
		}
		if (PluginLoader.getPlugins().keySet().size() <= 1) {
			f.addElement("plugins.znone", new FormInfo("", I18N.t("Keine Plugins gefunden. Installiere zuerst welche")).setInfoTxt(I18N
					.t("Öffne zur Installation den Katalog vom Starttab und wähle die Plugins aus. Danach musst du YAams neustarten.")));
		}
		// add special thanks
		f.addHeader("thankuser", new FormHeader(I18N.t("Speziellen Dank an"), "user").setColumn(4));
		f.addElement("thankuser.studio", new FormInfo("rpg-studio.de", "fürs Hosting, den Server und vieles mehr"));
		f.addElement("thankuser.steve", new FormInfo("Steve", "fürs Entwickeln"));
		f.addElement("thankuser.wolfsmutter", new FormInfo("Wolfsmutter", "fürs Testen und vieles mehr :)"));
		f.addElement("thankuser.evrey", new FormInfo("Evrey", "fürs Testen und ganz viel anderes"));
		f.addElement("thankuser.you", new FormInfo("Du?", "Wenn du willst, entwickle YAams mit! Mitarbeiter immer gesucht!"));

		// add thanks
		f.addHeader("thx", new FormHeader(I18N.t("Vielen Dank an"), "java").setColumn(8));
		f.addElement("thx.apachecommon", new FormLink("Apache Commons", "http://commons.apache.org"));
		f.addElement("thx.jidesoft", new FormLink("Jidesoft", "http://www.jidesoft.com"));
		f.addElement("thx.swingx", new FormLink("SwingX", "http://www.swinglibs.com"));
		f.addElement("thx.flamingo", new FormLink("Flamingo", "https://flamingo.dev.java.net"));
		f.addElement("thx.log4j", new FormLink("Log4J", "http://logging.apache.org/log4j/"));
		f.addElement("thx.ini4j", new FormLink("Ini4J", "http://ini4j.sourceforge.net"));
		f.addElement("thx.taskdialog", new FormLink("TaskDialog", "http://code.google.com/p/oxbow/"));
		f.addElement("thx.miglayout", new FormLink("MigLayout", "http://www.miglayout.com"));
		f.addElement("thx.xstream", new FormLink("XStream", "http://xstream.codehaus.org"));
		f.addElement("thx.fugueicons", new FormLink("Fugue Icons", "http://p.yusukekamiyamane.com/"));
		f.addElement("thx.crystalproject", new FormLink("Crystal Project", "http://crystalxp.net"));
		f.addElement("thx.faenza", new FormLink("Faenza", "http://tiheum.deviantart.com/art/Faenza-Icons-173323228"));
		f.addElement("thx.install4j", new FormLink("install4j", "http://www.ej-technologies.com/products/install4j/overview.html"));

		// add open buttons
		if (YLevel.IS_ADVANCED) {
			f.addButton("opts", YFactory.b(I18N.t("Öffne Optionen Ordner"), "opts_folder", new AE() {

				@Override
				public void run() {
					SystemHelper.viewFile(YAamsCore.programPath);

				}
			}));
			f.addButton("tmp", YFactory.b(I18N.t("Öffne Temp Ordner"), "open", new AE() {

				@Override
				public void run() {
					SystemHelper.viewFile(YAamsCore.tmpFolder);

				}
			}));
			f.addButton("log", YFactory.b(I18N.t("Öffne Log Datei"), "disk", new AE() {

				@Override
				public void run() {
					SystemHelper.viewFile(Log.file);

				}
			}));
		}

		// add modi
		// f.addChangeListener(new FormElementChangeListener() {
		//
		// @Override
		// public void stateChanged(FormElement form) {
		// setModified(true);
		//
		// }
		// });

		return f.getPanel(true);
	}

	/**
	 * @return
	 */
	public static Component addSystem() {
		// build form
		final FormBuilder f = new FormBuilder("options.system");
		f.addHeader("basic", new FormHeader(I18N.t("Basics"), "opts"));

		f.addElement(
				"basic.level",
				YSettingHelper
						.combo(null,
								I18N.t("Programm Level"),
								"gui.level",
								String.valueOf(YLevel.TYP),
								new String[] { "0", "1", "2" },
								new String[] { YLevel.getName(YLevel.BEGINNER), YLevel.getName(YLevel.ADVANCED),
										YLevel.getName(YLevel.DEVELOPER) }).addChangeListener(new FormElementChangeListener() {

							@Override
							public void stateChanged(FormElement form) {
								YLevel.load(Integer.valueOf(form.getContentAsString()));

							}
						}));

		// add modi
		// f.addChangeListener(new FormElementChangeListener() {
		//
		// @Override
		// public void stateChanged(FormElement form) {
		// setModified(true);
		//
		// }
		// });

		return f.getPanel(true);
	}

	/**
	 * @return
	 */
	public static Component addFeedback() {

		final HashMap<String, String> data = new HashMap<String, String>();
		// build form
		final FormBuilder f = new FormBuilder("feedback");
		f.addHeader("basic", new FormHeader(I18N.t("Basics"), "info").setColumn(4));
		f.addElement("basic.atitle", createField(data, I18N.t("Titel"), "", "title"));
		f.addElement("basic.bname", createField(data, I18N.t("Name"), SystemUtils.USER_NAME, "user"));
		f.addElement(
				"basic.system",
				createField(data, I18N.t("System"), I18N.t("{0} ({1}) {2} - {3} ({4}) {5}", SystemUtils.OS_NAME, SystemUtils.OS_VERSION,
						SystemUtils.OS_ARCH, SystemUtils.JAVA_RUNTIME_NAME, SystemUtils.JAVA_VERSION, SystemUtils.JAVA_VM_INFO), "system"));
		f.addElement(
				"basic.contact",
				createField(data, I18N.t("Kontakt"), "", "contact").setInfoTxt(
						I18N.t("Wie ICQ/E-Mail, nur wichtig, wenn du eine Antwort wünschst.")));

		f.addHeader("mess", new FormHeader(I18N.t("Nachricht"), "mail"));
		f.addElement("mess.mess", new FormTextArea("", "") {

			@Override
			protected JComponent getInternElement() {
				return YFactory.createOverlayTextArea(area, I18N.t("Schreibe die Nachricht hier ein."));
			}
		});

		f.addButton("feedback", YFactory.b(I18N.t("Send Feedback"), "mail_web", new AE() {

			@Override
			public void run() {
				data.put("system", f.getElement("basic.system").getContentAsString());
				data.put("stack", "feedback " + f.getElement("basic.atitle").getContentAsString() + " "
						+ f.getElement("basic.bname").getContentAsString() + " " + f.getElement("basic.contact").getContentAsString());
				data.put("messages", f.getElement("mess.mess").getContentAsString());
				SystemHelper.sendData("feedback", "Feedback - " + f.getElement("basic.atitle").getContentAsString(), data);

			}
		}, 32));

		// add modi
		// f.addChangeListener(new FormElementChangeListener() {
		//
		// @Override
		// public void stateChanged(FormElement form) {
		// setModified(true);
		//
		// }
		// });

		return f.getPanel(true);
	}

	/**
	 * Helpermethod
	 * 
	 * @param title
	 * @param value
	 * @param id
	 * @return
	 */
	private static FormElement createField(final HashMap<String, String> data, String title, String value, final String id) {
		data.put(id, value);
		return new FormTextField(title, value).addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement e) {
				data.put(id, e.getContentAsString());

			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getBottom()
	 */
	@Override
	public JComponent getBottom() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getIcon()
	 */
	@Override
	public String getIcon() {
		return "opts";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getTitle()
	 */
	@Override
	public String getTitle() {
		return I18N.t("Options");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.tabs.YaTab#getBcb(de.yaams.maker.helper.gui.bcb
	 * .BcbBuilder)
	 */
	@Override
	protected void buildBcb(BcbBuilder bcb) {
		BasicTabEvent.buildBcb(bcb, getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.tabs.YaTab#getId()
	 */
	@Override
	public String getId() {
		return HomeTab.OPTIONS;
	}

	/**
	 * This method will called, if reopen the tab or open it, to parse the
	 * arguments
	 * 
	 * @param id
	 */
	@Override
	public void parseArguments(HashMap<String, String> arguments) {
		// select?
		if (arguments.containsKey("select")) {
			selectActionByIcon(arguments.get("select"));
		}

	}

}
