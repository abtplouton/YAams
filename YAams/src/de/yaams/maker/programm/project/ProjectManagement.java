/**
 * 
 */
package de.yaams.maker.programm.project;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import de.yaams.maker.helper.FileHelper;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.extensions.IExtension;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.programm.YAamsCore;
import de.yaams.maker.programm.project.empty.EmptyProjectType;
import de.yaams.maker.programm.project.tab.ProjectOptionManager;

/**
 * @author abt
 * 
 */
public class ProjectManagement {

	protected static ArrayList<BasisListElement> projects;
	protected static HashMap<String, ProjectType> types;
	protected static ProjectType empty;

	/**
	 * Create a new ProjectCache
	 */
	@SuppressWarnings("unchecked")
	public static void start() {
		projects = (ArrayList<BasisListElement>) FileHelper.loadXML(new File(YAamsCore.programPath, "projects.xml"));
		types = new HashMap<String, ProjectType>();
		empty = new EmptyProjectType();

		// add save
		ExtentionManagement.add(ExtentionManagement.SAVE, new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {
				save();

			}
		});

		// found nothing?
		if (projects == null) {
			projects = new ArrayList<BasisListElement>();
		} else {
			// for (BasisListElement p : projects) {
			// ((Project) p).load();
			// }
		}

		// add options
		ExtentionManagement.add(Project.EXLOAD, new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {
				Project p = (Project) objects.get("project");

				// exist options?
				if (p.getType() != ProjectManagement.getType("empty") && !p.getObjects().containsKey(ProjectOptionManager.ID)) {
					p.addObjManager(new ProjectOptionManager(), ProjectOptionManager.ID);

				}
			}
		});

		// crypt projects
		ExtentionManagement.add("close", new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {
				for (BasisListElement p : projects) {
					((Project) p).closeIt();
				}

			}
		});
	}

	/**
	 * Load a project
	 * 
	 * @param path
	 * @return
	 */
	public static Project getProject(String hash) {
		for (BasisListElement p : projects) {
			if (((Project) p).getHash().equals(hash)) {
				return (Project) p;
			}
		}
		throw new IllegalArgumentException("Project for hash " + hash + " doesn't exist");
	}

	/**
	 * Load a project
	 * 
	 * @param path
	 * @return
	 */
	public static ProjectType getType(String id) {
		// check it
		if (!types.containsKey(id)) {
			return empty;
		}

		return types.get(id);
	}

	/**
	 * Load a project
	 * 
	 * @param path
	 * @return
	 */
	public static void registerType(ProjectType p) {
		types.put(p.getType(), p);
	}

	/**
	 * Load a project
	 * 
	 * @param path
	 * @return
	 */
	public static void save() {
		FileHelper.saveXML(new File(YAamsCore.programPath, "projects.xml"), projects);
	}

	/**
	 * @return the projects
	 */
	public static ArrayList<BasisListElement> getProjects() {
		return projects;
	}
}
