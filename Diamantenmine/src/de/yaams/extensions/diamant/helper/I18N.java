/**
 * 
 */
package de.yaams.extensions.diamant.helper;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Nebli
 * 
 */
public class I18N {

	public static final String[] lang2 = new String[] { "Deutsch (Integriert)" };

	/**
	 * Default Lang Strings
	 */
	public static final String CANCEL = t("Abbrechen");

	/**
	 * LangFolder
	 */
	// public static final File FOLDER = new File(YAamsCore.programPath,
	// "lang");

	public static String SLANG;

	/**
	 * Contains the language cache for this language
	 */
	protected static HashMap<String, String> cache;

	/**
	 * Contains the language, which act lording
	 */
	protected static ArrayList<HashMap<String, String>> lang;

	public static void init() {
		cache = new HashMap<String, String>();
		lang = new ArrayList<HashMap<String, String>>();

		// SLANG = Setting.get("lang", "de");
		//
		// // create folder
		// FileHelper.mkdirs(FOLDER);
		//
		// // add setting
		// ExtentionManagement.add("form.options.info", new IExtension() {
		//
		// @Override
		// public void work(HashMap<String, Object> objects) {
		// FormBuilder form = (FormBuilder) objects.get("form");
		// form.addElement("basic.lang", new FormInfo(I18N.t("Sprache"),
		// getLangs().get(SLANG)));
		//
		// }
		// });
		//
		// // add setting
		// ExtentionManagement.add("form.options.system", new IExtension() {
		//
		// @Override
		// public void work(HashMap<String, Object> objects) {
		// FormBuilder form = (FormBuilder) objects.get("form");
		// form.addElement(
		// "basic.lang",
		// YSettingHelper
		// .combo(null, I18N.t("Sprache"), "lang", "de", I18N.getLangIDs(),
		// I18N.getLangNames())
		// .setInfoTxt(
		// I18N.t("Übersetzer gesucht! Wenn du YAams in deine Sprache übersetzen willst, "
		// +
		// "dann kontaktiere uns bitte mit dem Feedback Formular! Danke schön :)"))
		// .addChangeListener(new FormElementChangeListener() {
		//
		// @Override
		// public void stateChanged(FormElement form) {
		// I18N.loadLang(form.getContentAsString());
		//
		// }
		// }));
		//
		// // add management?
		// if (YLevel.IS_ADVANCED) {
		// form.addHeader("lang", new FormHeader(I18N.t("Sprachenverwaltung"),
		// "lang").setColumn(4));
		// form.addElement("lang.import", new
		// FormButton(I18N.t("Sprachdatei importieren"), "lang_add", new AE() {
		//
		// @Override
		// public void run() {
		// // build form
		// FormBuilder form = new FormBuilder("lang.import");
		// form.addElement(
		// "basic.desc",
		// new FormInfo(
		// "",
		// I18N.t("<html>Vorgehensweise:<ol><li>Export der Basissprache. Im Zweifelsfall Deutsch.</li><li>Die xml-Datei bearbeiten und alle Sprachpaare übersetzen, die man möchte. Den Rest am besten löschen.</li><li>Die xml hier importieren</li><li>Wenn die Übersetzung gut ist, dann per Feedbackformular einsenden. Danke</li></ol>"))
		// .setSorting(-1));
		// form.addElement("basic.id", new FormTextField(I18N.t("Sprachcode"),
		// "").setInfoTxt(I18N.t("zB de, en"))
		// .addValidator(new ValidatorNotEmpty()).addValidator(new
		// ValidatorBase() {
		//
		// @Override
		// public void isValide(YMessagesDialog y) {
		// if (new File(FOLDER, form.getContentAsString()).exists()) {
		// y.add(I18N.t("Sprachcode {0} existiert schon. Wirklich überschreiben?"),
		// Level.INFO_INT);
		// }
		//
		// }
		// }));
		// form.addElement("basic.name",
		// new FormTextField(I18N.t("Sprachname"),
		// "").setInfoTxt(I18N.t("zB Deutsch, Englisch")));
		// form.addElement("basic.author", new FormTextField(I18N.t("Autor"),
		// SystemUtils.USER_NAME));
		// form.addElement("basic.file", new
		// FormFileSelectField(I18N.t("Datei"), null, JFileChooser.OPEN_DIALOG,
		// false,
		// false));
		//
		// // show it
		// if (!YDialog.showForm(I18N.t("Sprachdatei importieren"), "lang_add",
		// form)) {
		// return;
		// }
		//
		// // create folder
		// File folder = new File(FOLDER,
		// form.getElement("basic.id").getContentAsString());
		// FileHelper.mkdirs(folder);
		//
		// // create info.xml
		// HashMap<String, String> info = new HashMap<String, String>();
		// info.put("title",
		// form.getElement("basic.name").getContentAsString());
		// info.put("author",
		// form.getElement("basic.author").getContentAsString());
		// FileHelper.saveXML(new File(folder, "info.xml"), info);
		//
		// // copy xml
		// File source = new
		// File(form.getElement("basic.file").getContentAsString());
		// FileHelper.copy(source, new File(folder, source.getName()));
		//
		// }
		// }));
		//
		// form.addElement("lang.export", new
		// FormButton(I18N.t("Sprachdatei exportieren"), "lang_arrow", new AE()
		// {
		//
		// @Override
		// public void run() {
		// // build form
		// FormBuilder form = new FormBuilder("lang.export");
		// form.addElement(
		// "basic.desc",
		// new FormInfo(
		// "",
		// I18N.t("Es wird die aktuell gewählte Sprache exportiert. Es werden nur Sprachpaare exportiert, die bisher benutzt wurden."))
		// .setSorting(-1));
		// form.addElement("basic.file", new
		// FormFileSelectField(I18N.t("Datei"), null, JFileChooser.SAVE_DIALOG,
		// false,
		// true));
		//
		// // show it
		// if (!YDialog.showForm(I18N.t("Sprachdatei exportieren"),
		// "lang_arrow", form)) {
		// return;
		// }
		//
		// // save xml
		// FileHelper.saveXML(new
		// File(form.getElement("basic.file").getContentAsString()), cache);
		//
		// }
		// }));
		// }
		// }
		// });

	}

	/**
	 * Translate t
	 * 
	 * @param t
	 * @return
	 */
	public static String t(String t) {
		// early loading?
		// if (cache == null) {
		return t;
		// }
		//
		// // in cache?
		// if (!cache.containsKey(t)) {
		// // search in the files
		// for (HashMap<String, String> map : lang) {
		// if (map.containsKey(t)) {
		// cache.put(t, map.get(t));
		// break;
		// }
		// }
		//
		// // add default
		// if (!cache.containsKey(t)) {
		// cache.put(t, t);
		// }
		// }
		//
		// return cache.get(t);
	}

	/**
	 * Translate t
	 * 
	 * @param t
	 * @return
	 */
	public static String t(String t, Object... data) {
		return MessageFormat.format(t(t), data);
	}

	// /**
	// * Load the language
	// *
	// * @param id
	// */
	// public static void loadLang(String id) {
	// cache.clear();
	// lang.clear();
	// Setting.set("lang", id);
	//
	// // is de?
	// if ("de".equals(id)) {
	// return;
	// }
	//
	// // load all files from this dir
	// for (File f : new File(FOLDER, id).listFiles()) {
	// if (f.isFile() && f.canRead() && f.getName().endsWith(".xml") &&
	// !f.getName().equals("info.xml")) {
	// lang.add((HashMap<String, String>) FileHelper.loadXML(f));
	// }
	// }
	// }
	//
	// /**
	// * Get the hashmap for lanuages & ids
	// *
	// * @return
	// */
	// public static HashMap<String, String> getLangs() {
	// // search for it
	// HashMap<String, String> names = new HashMap<String, String>();
	// names.put("de", "Deutsch (integriert) - German (included)");
	//
	// // load all files from this dir
	// for (File f : new File(YAamsCore.programPath, "lang").listFiles()) {
	// if (f.isDirectory() && new File(f, "info.xml").canRead()) {
	// HashMap<String, String> data = (HashMap<String, String>)
	// FileHelper.loadXML(new File(f, "info.xml"));
	// names.put(f.getName(), data.get("title"));
	// }
	// }
	//
	// return names;
	// }
	//
	// /**
	// * Helpermethod to build an array over language
	// *
	// * @return
	// */
	// public static String[] getLangNames() {
	// HashMap<String, String> ges = getLangs();
	//
	// String[] names = new String[ges.size()];
	// int i = 0;
	// // add all
	// for (String key : ges.keySet()) {
	// names[i] = ges.get(key);
	// i++;
	// }
	//
	// return names;
	// }
	//
	// /**
	// * Helpermethod to build an array over language ids
	// *
	// * @return
	// */
	// public static String[] getLangIDs() {
	// HashMap<String, String> ges = getLangs();
	//
	// String[] names = new String[ges.size()];
	// int i = 0;
	// // add all
	// for (String key : ges.keySet()) {
	// names[i] = key;
	// i++;
	// }
	//
	// return names;
	// }
}
