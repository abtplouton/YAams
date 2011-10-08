/**
 * 
 */
package de.yaams.maker.programm.ress;

import java.util.ArrayList;
import java.util.HashMap;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.helper.gui.tabs.ProjectSplitTab;
import de.yaams.maker.programm.project.Project;

/**
 * @author abt
 * 
 */
public abstract class RessourceTab extends ProjectSplitTab {
	private static final long serialVersionUID = -5286907430088147614L;

	/**
	 * Create a new RessourcePanel
	 */
	public RessourceTab(final Project p, ArrayList<RessAction> folders) {
		super(buildList(p, folders), p);

		buildGui();
	}

	/**
	 * Helpermethod to build the list
	 * 
	 * @return
	 */
	private static ArrayList<BasisListElement> buildList(final Project p, ArrayList<RessAction> folder) {
		final ArrayList<BasisListElement> ary = new ArrayList<BasisListElement>();

		// Add all Files
		for (final RessAction r : folder) {
			ary.add(r);
		}

		// build objects
		HashMap<String, Object> objects = new HashMap<String, Object>();
		objects.put("project", p);
		objects.put("list", ary);
		// inform
		ExtentionManagement.work("project.ress.list", objects);

		return ary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see editor.gui.tabs.YaTab#getIcon()
	 */
	@Override
	public String getIcon() {
		return "graphic";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see editor.gui.tabs.YaTab#getTitle()
	 */
	@Override
	public String getTitle() {
		return I18N.t("Ressourcen");
	}
}
