/**
 * 
 */
package de.yaams.extensions.rgssproject.type;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Priority;
import org.ini4j.Wini;

import de.yaams.extensions.rgssproject.RTP;
import de.yaams.maker.helper.FileHelper;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.NetHelper;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.gui.YMessagesDialog;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.programm.YAamsCore;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.ProjectType;
import de.yaams.maker.programm.project.tab.ProjectHomeTab;
import de.yaams.maker.programm.ress.RessInfoFile;
import de.yaams.maker.programm.ress.RessRess;
import de.yaams.maker.programm.tabs.YaTab;

/**
 * @author Praktikant
 * 
 */
public abstract class RGSSProjectType extends ProjectType {

	/**
	 * @param type
	 */
	public RGSSProjectType(String type) {
		super(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.ProjectType#leftForm(de.yaams.maker.helper
	 * .gui.form.core.FormBuilder, de.yaams.maker.programm.project.Project)
	 */
	@Override
	public void leftForm(FormBuilder form, Project project) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.ProjectType#openHomeTab(de.yaams.maker
	 * .programm.project.Project)
	 */
	@Override
	public YaTab getHomeTab(Project project) {

		return new ProjectHomeTab(project);

	}

	/**
	 * Helpermethod to read the data
	 * 
	 * @param p
	 */
	protected void readGameIniToData(Project p) {
		// read Game.ini
		try {
			Wini ini = new Wini(new File(p.getPath(), "Game.ini"));
			p.getData().put("library", ini.get("Game", "Library"));
			p.getData().put("scripts", ini.get("Game", "Scripts"));
		} catch (Throwable t) {
			YEx.info("Can not edit " + new File(p.getPath(), "Game.ini"), t);
		}

		YMessagesDialog mess = new YMessagesDialog(I18N.t("Probleme beim Öffnen des Projects {0}", p.getTitle()), "rgss.project.open");

		// crypted?
		if (new File(p.getPath(), "Game.rgssad").exists() || new File(p.getPath(), "Game.rgssa2").exists()) {
			mess.add(I18N.t("Encrypted Games not supported."), Priority.INFO_INT);
		}

		mess.showOk();
	}

	/**
	 * Helpermethod to save game settings
	 * 
	 * @param p
	 */
	protected void saveDataToGameIni(Project p) {
		// save title
		try {
			Wini ini = new Wini(new File(p.getPath(), "Game.ini"));
			ini.put("Game", "Title", p.getTitle());
			ini.put("Game", "Library", p.getData().get("library"));
			ini.store();
		} catch (Throwable t) {
			YEx.info("Can not edit " + new File(p.getPath(), "Game.ini"), t);
		}
	}

	/**
	 * Helpermethod to download and extract a template
	 * 
	 * @param p
	 * @param type
	 */
	protected boolean extractTemplate(Project p, String type) {
		// has template?
		File zip = new File(YAamsCore.programPath, "templates" + File.separator + type + ".zip");

		// create folder
		FileHelper.mkdirs(zip.getParentFile());
		if (!zip.exists()) {
			// dl
			if (!NetHelper.downloadFile(zip, NetHelper.getContentAsString("http://www.yaams.de/file/?typ=template&name=" + type))) {
				return false;
			}
		}

		// can? extract
		if (!FileHelper.extractArchive(zip, p.getPath())) {
			return false;
		}

		// edit Game.ini
		try {
			Wini ini = new Wini(new File(p.getPath(), "Game.ini"));
			ini.put("Game", "Title", p.getTitle());
			ini.store();
		} catch (Throwable t) {
			YEx.info("Can not edit " + new File(p.getPath(), "Game.ini"), t);
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.ProjectType#getFilesInFolder(de.yaams
	 * .maker.programm .project.Project, java.io.File)
	 */
	@Override
	public ArrayList<RessInfoFile> getFilesInFolder(Project project, String folder) {
		ArrayList<RessInfoFile> res = new ArrayList<RessInfoFile>();
		boolean graphic = folder.contains("Graphic");
		String[] end = graphic ? new String[] { ".png", ".jpg", ".jpeg", ".jpe", ".gif", ".bmp" } : new String[] { ".mp3", ".ogg", ".wma",
				".mid", ".midi" };
		File fold = new File(project.getPath(), folder);

		// list folder
		if (fold.canRead() && fold.isDirectory()) {
			for (File f : fold.listFiles()) {
				// right file?
				if (!f.canRead() || !f.isFile() || f.isHidden()) {
					continue;
				}
				// look in the project
				if (RessRess.endWithExtention(f, end)) {
					res.add(new RessInfoFile(project, folder + File.separator + f.getName()));
				}
			}
		}

		HashMap<String, File> rtp = RTP.getRTPs(project);

		// add rtp
		for (String name : rtp.keySet()) {
			// build path
			fold = new File(rtp.get(name), folder);

			// contain files?
			if (fold.listFiles() == null || fold.listFiles().length == 0) {
				continue;
			}

			// list folder
			for (File f : fold.listFiles()) {
				// right file?
				if (!f.canRead() || !f.isFile()) {
					continue;
				}

				// look in the project
				if (RessRess.endWithExtention(f, end)) {
					RessInfoFile r = new RessInfoFile(project, folder + File.separator + f.getName());
					r.setAbsolutePath(f);
					r.setCanDelete(false);
					res.add(r);
				}
			}
		}

		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.ProjectType#getFilesInFolder(de.yaams
	 * .maker.programm .project.Project, java.io.File)
	 */
	@Override
	public File getFile(Project project, String file) {
		boolean graphic = file.contains("Graphic");
		List<String> end = graphic ? Arrays.asList("", ".png", ".jpg", ".jpeg", ".jpe", ".gif", ".bmp") : Arrays.asList("", ".mp3", ".ogg",
				".wma", ".mid", ".midi");
		File path = new File(project.getPath(), file);

		// right file?
		if (path.canRead() && path.isFile()) {
			return path;
		}

		// look in the project
		for (final String e : end) {
			path = new File(project.getPath(), file + e);
			if (path.canRead() && path.isFile()) {
				return path;
			}
		}

		HashMap<String, File> rtp = RTP.getRTPs(project);

		// add rtp
		for (String name : rtp.keySet()) {
			// build path
			path = new File(rtp.get(name), file).getParentFile();

			// contain files?
			if (path.listFiles() == null || path.listFiles().length == 0) {
				continue;
			}

			// look in the project
			for (final String e : end) {
				path = new File(rtp.get(name), file + e);
				if (path.canRead() && path.isFile()) {
					return path;
				}
			}
		}

		return null;
	}
}
