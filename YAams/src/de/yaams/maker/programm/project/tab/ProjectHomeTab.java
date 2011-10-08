/**
 * 
 */
package de.yaams.maker.programm.project.tab;

import javax.swing.JComponent;
import javax.swing.JLabel;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.JavaHelper;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.bcb.BcbBuilder;
import de.yaams.maker.helper.gui.dock.DockHolder;
import de.yaams.maker.helper.gui.dock.DockLinkPanel;
import de.yaams.maker.helper.gui.icons.IconCache;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.programm.YaFrame;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.ProjectTabEvent;
import de.yaams.maker.programm.project.objects.BasicObject;
import de.yaams.maker.programm.project.objects.BasicObjectManager;
import de.yaams.maker.programm.tabs.BasicTabEvent;
import de.yaams.maker.programm.tabs.TabEvent;

/**
 * @author abby
 * 
 */
public class ProjectHomeTab extends ProjectTab {

	private static final long serialVersionUID = -7817071123542539005L;

	/**
	 * @param project
	 */
	public ProjectHomeTab(Project project) {
		super(project);

		buildGui();
		addSaveButton();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.tabs.YaTab#buildBcb(de.yaams.maker.helper.gui
	 * .bcb.BcbBuilder)
	 */
	@Override
	protected void buildBcb(BcbBuilder bcb) {
		BasicTabEvent.buildBcb(bcb, project.getHash());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.tabs.YaTab#getIcon()
	 */
	@Override
	public String getIcon() {
		return project.getIcon();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.tabs.YaTab#getContent()
	 */
	@Override
	public JComponent getContent() {
		// load objects
		ExtentionManagement.work(Project.EXLOAD, JavaHelper.createHashStringObj("project", project));

		int w;
		int h;

		int c = project.getObjects().keySet().size();

		// has nothing?
		if (c == 0) {
			return new JLabel(
					I18N.t("Projekt {0} vom Projekttyp {1} enthält keine Objekte.", project.getTitle(), project.getTypeAsString()),
					IconCache.get("error", 32), JLabel.CENTER);
		}
		// set width&height
		switch (c) {
		case 1:
		case 2:
		case 3:
			w = c;
			h = 1;
			break;
		case 4:
			w = 2;
			h = 2;
			break;
		case 5:
		case 6:
			w = 3;
			h = 2;
			break;
		case 7:
		case 8:
			w = 4;
			h = 2;
			break;
		case 9:
			w = 3;
			h = 3;
			break;
		case 10:
		case 11:
		case 12:
			w = 4;
			h = 3;
			break;
		case 13:
		case 14:
		case 15:
			w = 5;
			h = 3;
			break;
		case 16:
			w = 4;
			h = 4;
			break;
		case 17:
		case 18:
		case 19:
		case 20:
			w = 5;
			h = 4;
			break;
		default:
			w = 6;
			h = c / 6 + 1;
			break;
		}

		// build content
		DockHolder d = new DockHolder("project.home", w, h);

		// add content
		for (String key : project.getObjects().keySet()) {
			final BasicObjectManager bom = project.getObjects().get(key);

			DockLinkPanel dlp = new DockLinkPanel();
			dlp.setDock(d);

			// add basic
			dlp.addLink(bom.getTitle(), bom.getIcon(), new AE() {

				@Override
				public void run() {
					YaFrame.open(bom.getTabId());

				}
			});

			// add desc
			if (bom.getDesc() != null) {
				dlp.addLink("<html>" + bom.getDesc(), null, null);
			}

			// add basic options
			if (bom.getObjects() != null && bom.getObjects().size() > 0) {
				int count = 0;

				// run over all
				for (final BasisListElement b : bom.getObjects()) {
					// add basic
					dlp.addLink(b.getTitle(), b.getIcon(), new AE() {

						@Override
						public void run() {
							YaFrame.open(bom.getTabId((BasicObject) b));

						}
					});

					// but stop at five
					count++;
					if (count >= 5) {
						dlp.addLink(I18N.t("Und {0} mehr", bom.getObjects().size() - count), null, null);
						break;
					}
				}
			}

			dlp.buildGui(bom.getGroup(), "yaams", false);
			d.add(dlp);
		}

		return d;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.tabs.YaTab#getTitle()
	 */
	@Override
	public String getTitle() {
		return project.getTitle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.tabs.YaTab#getId()
	 */
	@Override
	public String getId() {
		return TabEvent.buildParameter(ProjectTabEvent.HOME, project, null);
	}

	/**
	 * Save chanced, overwrite it to implement it, and call addSaveTab() to add
	 * the support button
	 */
	@Override
	protected void saveIntern() {
		project.save();
	}

}
