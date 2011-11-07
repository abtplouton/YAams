/**
 * 
 */
package de.yaams.extensions.diamant.graph.scene.menu;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.yaams.extensions.diamant.Project;
import de.yaams.extensions.diamant.graph.PWindow;

/**
 * @author abby
 * 
 */
public class LevelChooserScene extends MenuScene {

	private static final long serialVersionUID = -415429350897570948L;

	protected String maptyp;

	/**
	 * 
	 */
	public LevelChooserScene(final String maptyp) {
		super("Wähle das Level aus");

		this.maptyp = maptyp;

		// show all
		for (int i = 0, l = Project.project.getMapLevel(maptyp); i <= l; i++) {
			final int j = i;
			addMenu(new AbstractAction(Project.project.getMaps().get(maptyp).get(i).getTitle()) {

				private static final long serialVersionUID = 5652068212086090562L;

				@Override
				public void actionPerformed(ActionEvent arg0) {
					Project.project.startLevel(maptyp, j);

				}
			});
		}

		addMenu(new AbstractAction("Zurück") {

			private static final long serialVersionUID = 5652068212086090562L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				PWindow.window.setActScene(new MenuMainScene());

			}
		});

		init();
	}
}
