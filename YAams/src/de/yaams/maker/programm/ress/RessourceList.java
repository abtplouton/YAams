/**
 * 
 */
package de.yaams.maker.programm.ress;

import java.io.File;

import javax.swing.JList;

import de.yaams.maker.helper.FileHelper;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.YDialog;
import de.yaams.maker.helper.gui.list.YArrayList;
import de.yaams.maker.helper.integration.EditorIntegration;
import de.yaams.maker.programm.project.Project;

/**
 * @author abt
 * 
 */
public class RessourceList extends YArrayList<RessInfoFile> {
	private static final long serialVersionUID = 6626562093124509075L;

	protected String folder;
	protected Project project;

	/**
	 * Create a new RessiList
	 * 
	 * @param ary
	 */
	public RessourceList(Project project, String folder) {
		super(project.getType().getFilesInFolder(project, folder));
		this.project = project;
		this.folder = folder;
		icon = "ress";
		open = true;
		delete = true;
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		buildToolbar(I18N.t("Ressource"), icon);

	}

	/**
	 * Can delete the element? return true for yes or false for no. With
	 * notification message.
	 * 
	 * @return
	 */
	@Override
	public boolean canDelete() {
		if (!canEdit()) {
			return false;
		}

		// allowed?
		if (!getSelectedObject().isCanDelete()) {
			YDialog.ok(I18N.t("Kann {0} nicht löschen", getSelectedObject().getAbsolutePath().getName()),
					I18N.t("Die Ressource ist eine Systemressource und gegen Löschen gesperrt."), "error_ress");
			return false;
		}

		return super.canDelete();
	}

	/**
	 * Helpermetho to import the files in the folder
	 */
	@Override
	public void open() {

		// Create a file chooser
		final File[] fl = EditorIntegration.openDialog(true, false);

		// user select ok?
		if (fl == null) {
			return;
		}

		// copy all files
		for (final File f : fl) {
			File n = new File(new File(project.getPath(), folder), f.getName());
			FileHelper.copy(f, n);
			add(new RessInfoFile(project, folder + File.separator + f.getName()));
		}

	}

	/**
	 * Delete the files
	 */
	public void delFile() {

		// check
		if (!canDelete()) {
			return;
		}

		// delete selected
		for (final Object o : list.getSelectedValues()) {
			FileHelper.deleteFile(((RessInfoFile) o).getAbsolutePath());
		}

		delete();

	}

	/**
	 * If the user select one element
	 */
	@Override
	protected void selected() {
		config();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#add()
	 */
	@Override
	protected void add() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#info()
	 */
	@Override
	protected void info() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.packandgo.helper.gui.list.YSimpleList#getModified(java.lang.
	 * Object)
	 */
	@Override
	public String getIcon(final RessInfoFile o) {
		// check ending

		return RessRess.endWithExtention(o.getAbsolutePath(), new String[] { "jpg", "jpeg", "png", "bmp", "tiff" }) ? "graphic" : RessRess
				.endWithExtention(o.getAbsolutePath(), new String[] { "mp3", "ogg", "mid", "midi", "wma" }) ? "audio" : "setup";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.packandgo.helper.gui.list.YSimpleList#getModified(java.lang.
	 * Object)
	 */
	@Override
	public String getDesc(final RessInfoFile o) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.packandgo.helper.gui.list.YSimpleList#getModified(java.lang.
	 * Object)
	 */
	@Override
	public boolean isModified(final RessInfoFile o) {
		return false;
	}

	/**
	 * Helpermethod to get the text for the list element/title, normally
	 * toString.
	 * 
	 * @param value
	 * @return
	 */
	@Override
	public Object getText(final RessInfoFile value) {
		return value.getAbsolutePath().getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#configIntern()
	 */
	@Override
	protected void configIntern() {

	}

	/**
	 * Get it
	 * 
	 * @return
	 */
	public File getSelectedFile() {
		return getSelectedObject().getAbsolutePath();
	}

	/**
	 * Set it
	 * 
	 * @return
	 */
	public void setSelectedFile(String name) {
		// find right element
		for (RessInfoFile r : ary) {
			if (r.getAbsolutePath().getName().contains(name)) {
				list.setSelectedValue(r, true);
				break;
			}
		}
	}

	/**
	 * @return the folder
	 */
	public String getFolder() {
		return folder;
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

}
