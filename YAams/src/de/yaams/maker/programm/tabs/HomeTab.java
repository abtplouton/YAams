/**
 * 
 */
package de.yaams.maker.programm.tabs;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.bcb.BcbBuilder;
import de.yaams.maker.helper.gui.dock.DockHolder;
import de.yaams.maker.helper.gui.dock.DockInfoPanel;
import de.yaams.maker.helper.gui.dock.DockLinkPanel;
import de.yaams.maker.programm.YaFrame;
import de.yaams.maker.programm.project.ProjectManagement;
import de.yaams.maker.programm.project.ProjectPanel;

/**
 * @author Nebli
 * 
 */
public class HomeTab extends YaTab {
	private static final long serialVersionUID = -4268585238319796616L;
	public static final String OPTIONS = "opts";

	/**
	 * @param project
	 */
	public HomeTab() {
		buildGui();
		addSaveButton();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getIcon()
	 */
	@Override
	public String getIcon() {
		return "home";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getTitle()
	 */
	@Override
	public String getTitle() {
		return I18N.t("Willkommen");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getContent()
	 */
	@Override
	public JComponent getContent() {

		JPanel cont = new JPanel(new BorderLayout());
		cont.add(new ProjectPanel(), BorderLayout.CENTER);

		// build links
		DockHolder d = new DockHolder("home", 3, 1);
		DockLinkPanel dlp = new DockLinkPanel();
		dlp.addLink(I18N.t("Options"), "opts", new AE() {

			@Override
			public void run() {
				YaFrame.open(HomeTab.OPTIONS);

			}
		});
		dlp.addLink(I18N.t("Plugins"), "plugin", new AE() {

			@Override
			public void run() {
				BasicTabEvent.openPlugin();

			}
		});
		dlp.addLink(I18N.t("Feedback"), "mail_web", new AE() {

			@Override
			public void run() {
				BasicTabEvent.openFeedback();

			}
		});
		d.add(dlp);
		dlp.buildGui(I18N.t("Basis Optionen"), "yaams", false);

		d.add(new DockInfoPanel(I18N.t("Erste Schritte"), I18N.t("Erstelle/Importiere ein Projekt und öffne es dann.")));

		// add messages
		DockLinkPanel p = new DockLinkPanel();
		p.addLink(I18N.t("Alle Plugins sind uptodate."), "ok", null);
		d.add(p);
		p.buildGui(I18N.t("Messages"), "info", false);

		cont.add(d, BorderLayout.SOUTH);

		// JPanel p = new JPanel(new GridLayout(1, 1));
		// p.add();
		return cont;
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
		return BasicTabEvent.HOME;
	}

	/**
	 * Save chanced, overwrite it to implement it, and call addSaveTab() to add
	 * the support button
	 */
	@Override
	protected void saveIntern() {
		ProjectManagement.save();
	}

}
