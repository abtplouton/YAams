/**
 * 
 */
package de.yaams.maker.programm.project;

import java.awt.Dialog.ModalityType;
import java.io.File;
import java.util.HashMap;

import org.jdesktop.swingx.JXLoginDialog;
import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.auth.LoginService;

import de.yaams.maker.helper.FileHelper;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.JavaHelper;
import de.yaams.maker.helper.Log;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.gui.YDialog;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.programm.YAamsCore;
import de.yaams.maker.programm.project.objects.BasicObjectManager;

/**
 * Represent a project
 * 
 * @author Nebli
 * 
 */
public class Project extends BasisListElement {

	/**
	 * Will be saved as data.xml in the project folder
	 */
	protected transient HashMap<String, String> data;

	/**
	 * Will not be saved
	 */
	protected transient HashMap<String, Object> cache;

	/**
	 * Objects & Manager, will be safes itself
	 */
	protected transient HashMap<String, BasicObjectManager> objects;

	protected transient boolean valid = true;
	protected String hash, type;

	/**
	 * Folder of the file
	 */
	protected File path;

	public static final String EXLOAD = "project.load", EXSAVE = "project.save";

	/**
	 * Create a new project
	 * 
	 * @param path
	 * @param title
	 * @param icon
	 * @param type
	 */
	protected Project(final File path, String title, String icon, String type) {
		super(title, "", icon);
		this.path = path;
		this.hash = Long.toString(path.hashCode());
		this.type = type;
		load();
		updateDesc();
	}

	/**
	 * Update the desc
	 */
	public void updateDesc() {
		setDesc(I18N.t("{0} ({1})", ProjectManagement.getType(type).getTitle(), path));
	}

	/**
	 * Load the main settings after create
	 */
	@SuppressWarnings("unchecked")
	public void load() {

		// check is load?
		if (cache != null) {
			return;
		}

		// check path
		valid = FileHelper.checkPath(I18N.t("Fehler beim Laden des Projekts"), path, true, true);

		// decrypt it
		if (!decrypt()) {
			YDialog.ok(I18N.t("Kann Projekt nicht entschlüsseln"), "", getIcon() + "_lock");
			return;
		}

		cache = new HashMap<String, Object>();
		objects = new HashMap<String, BasicObjectManager>();

		// valid?
		if (valid) {
			// load settings
			data = (HashMap<String, String>) FileHelper.loadXML(new File(path, "data.xml"));
			if (data == null) {
				data = new HashMap<String, String>();
			}
		} else {
			data = new HashMap<String, String>();
		}

		// inform ex
		ExtentionManagement.work(Project.EXLOAD, JavaHelper.createHashStringObj("project", this));
	}

	/**
	 * Checked, if a project has crypto
	 * 
	 * @return
	 */
	public boolean isLock() {
		return getCryptotyp() != null;
	}

	/**
	 * Checked, if a project has crypto and is open
	 * 
	 * @return
	 */
	public boolean isLockOpen() {
		return isLock() && cache != null;
	}

	/**
	 * Get the type of the crypto or null
	 * 
	 * @return
	 */
	protected String getCryptotyp() {
		if (cache == null) {
			// check files
			for (String name : new String[] { "Blowfish", "DES", "RC2", "RC4", "RC5" }) {
				if (new File(path, name + ".crypt").exists()) {
					return name;
				}
			}
			return null;
		} else {
			// check settings
			if (cache.containsKey("projecttype")) {
				return (String) cache.get("projecttype");
			} else {
				return null;
			}
		}
	}

	/**
	 * Decrypt the project
	 * 
	 * @param type
	 */
	protected boolean decrypt() {

		// decrypt it?
		if (!isLock()) {
			return true;
		}

		// ask for data
		JXLoginDialog login = new JXLoginDialog(new LoginService() {
			@Override
			public boolean authenticate(String name, char[] password, String server) {

				String type = getCryptotyp();

				cache.put("projectpw", name + " - " + String.valueOf(password));
				cache.put("projecttype", type);

				File zip = new File(path, type + ".zip"), crypt = new File(path, type + ".crypt");

				// decrypt it
				try {
					FileHelper.decryptFile(crypt, zip, (String) cache.get("projectpw"), type);

					// extract archive
					FileHelper.extractArchiveThrow(zip, path);

					// delete it
					FileHelper.deleteFile(crypt);
					FileHelper.deleteFile(zip);

				} catch (Throwable t) {
					Log.ger.info("Can not extract project", t);
					return false;
				}

				return true;
			}
		}, null, null);
		login.setModalityType(ModalityType.APPLICATION_MODAL);
		login.setVisible(true);

		// don't want it?
		if (JXLoginPane.Status.NOT_STARTED.equals(login.getStatus()) || JXLoginPane.Status.CANCELLED.equals(login.getStatus())) {
			return false;
		}

		return true;
	}

	/**
	 * Encrypt the project
	 * 
	 * @param type
	 */
	protected boolean encrypt() {

		// has data?
		if (!isLockOpen()) {
			return true;
		}

		String type = (String) cache.get("projecttype");
		File crypt = new File(path, type + ".crypt"), zipT = new File(YAamsCore.programPath, type + ".zip");

		// decrypt it
		try {

			// build archive
			FileHelper.packZip(path, zipT);

			// empty folder
			FileHelper.deleteTree(path);

			// create folder
			FileHelper.mkdirs(path);

			// crypt it
			FileHelper.encryptFile(zipT, crypt, (String) cache.get("projectpw"), type);

			// delete it
			FileHelper.deleteFile(zipT);

		} catch (Throwable t) {
			YEx.warn("Can not encrypt Project.", t);
			return decrypt();
		}

		return true;
	}

	/**
	 * @return the path
	 */
	public File getPath() {
		return path;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return title;
	}

	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * Save this project
	 */
	public void save() {

		modified = false;

		// valid?
		if (cache == null || !valid
				&& !YDialog.showErrors("Nothing was save", I18N.t("Due some errors while loading is {0} in safe mode", title))) {
			return;
		}

		// save setting
		FileHelper.saveXML(new File(path, "setting.xml"), data);

		// save another things
		ProjectManagement.getType(type).save(this);

		// build objects
		// inform
		ExtentionManagement.work(EXSAVE, JavaHelper.createHashStringObj("project", this));

		// save objects
		for (String key : getObjects().keySet()) {
			BasicObjectManager bom = getObjects().get(key);
			// add it
			bom.saveObjects();
		}

	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(File path) {
		this.path = path;
	}

	/**
	 * @return the objects
	 */
	public HashMap<String, Object> getCache() {
		// load project?
		if (cache == null) {
			load();
		}

		return cache;
	}

	/**
	 * @return the type
	 */
	public ProjectType getType() {
		return ProjectManagement.getType(type);
	}

	/**
	 * @return the type
	 */
	public String getTypeAsString() {
		return type;
	}

	/**
	 * @return the data
	 */
	public HashMap<String, String> getData() {

		// load project?
		if (data == null) {
			load();
		}

		return data;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	@Override
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return the objects
	 */
	public HashMap<String, BasicObjectManager> getObjects() {

		// load project?
		if (objects == null) {
			load();
		}

		return objects;
	}

	/**
	 * Add a Object Manager to this project
	 * 
	 * @param bom
	 * @param id
	 */
	public void addObjManager(BasicObjectManager bom, String id) {
		// add project
		bom.setProject(this);
		bom.loadObjects();

		// add to project
		objects.put(id, bom);
	}

	/**
	 * Project is close and deloading
	 */
	public void closeIt() {
		save();
		encrypt();

		// clear data
		cache = null;
		objects = null;
		data = null;
	}

}
