/**
 * 
 */
package de.yaams.extensions.diamant.graph.scene.menu;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.yaams.extensions.diamant.Project;
import de.yaams.extensions.diamant.graph.scene.GameScene;

/**
 * @author abby
 * 
 */
public class GameMenuEndScene extends MenuScene {

	private static final long serialVersionUID = -415429350897570948L;

	/**
	 * 
	 */
	public GameMenuEndScene(String title, final GameScene scene) {
		super(title);

		addMenu(new AbstractAction("Beenden") {

			private static final long serialVersionUID = 5652068212086090562L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);

			}
		});

		addMenu(new AbstractAction("Neustarten") {
			private static final long serialVersionUID = -6847580154991426321L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Project.project.startLevel(scene.getLevel().getMaptyp(), scene.getLevel().getId());

			}
		});

		addMenu(new AbstractAction("Zur Auswahl") {
			private static final long serialVersionUID = -6847580154991426321L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				window.setActScene(new LevelChooserScene(scene.getLevel().getMaptyp()));

			}
		});

		init();
	}
}
