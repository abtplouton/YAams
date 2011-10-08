/**
 * 
 */
package de.yaams.extensions.rgssproject.tab;

import java.io.File;
import java.util.ArrayList;

import de.yaams.maker.helper.gui.bcb.BcbBuilder;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.ProjectTabEvent;
import de.yaams.maker.programm.ress.RessAction;
import de.yaams.maker.programm.ress.RessourceTab;
import de.yaams.maker.programm.tabs.TabEvent;

/**
 * @author abby
 * 
 */
public class RGSSRessourceTab extends RessourceTab {

	private static final long serialVersionUID = -4567359711382005846L;

	public static final String ID = "rgss.ress";

	/**
	 * @param p
	 * @param folders
	 */
	public RGSSRessourceTab(Project p) {
		super(p, buildList(p));
	}

	/**
	 * Helpermethod to build the list
	 * 
	 * @return
	 */
	private static ArrayList<RessAction> buildList(final Project p) {
		final ArrayList<RessAction> ary = new ArrayList<RessAction>();

		// Add all Files
		addFolder(ary, "graphic", new File(p.getPath(), "Graphics"), "Graphics");
		addFolder(ary, "audio", new File(p.getPath(), "Audio"), "Audio");

		// get it
		return ary;
	}

	/**
	 * Recursive Helpermethod
	 * 
	 * @param ary
	 * @param icon
	 * @param path
	 * @param title
	 */
	protected static void addFolder(ArrayList<RessAction> ary, String icon, File path, String title) {
		// add self
		ary.add(new RessAction(path.getName(), title, icon));

		// Add all Files
		for (final File f : path.listFiles()) {
			if (!f.isDirectory()) {
				continue;
			}

			// add it
			addFolder(ary, icon, f, title + File.separator + f.getName());
		}
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
		ProjectTabEvent.buildBcb(bcb, project, ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.tabs.YaTab#getId()
	 */
	@Override
	public String getId() {
		return TabEvent.buildParameter(ID, project, null);
	}

}
