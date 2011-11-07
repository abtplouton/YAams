/**
 * 
 */
package de.yaams.extensions.diamant.graph.scene.menu;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.yaams.extensions.diamant.Project;
import de.yaams.extensions.diamant.graph.Plays;

/**
 * @author abby
 * 
 */
public class MenuMainScene extends MenuScene {

	private static final long serialVersionUID = -415429350897570948L;

	/**
	 * 
	 */
	public MenuMainScene() {
		super("Willkommen");

		Plays.playMusic("");

		addMenu(new AbstractAction("Tutorial") {

			private static final long serialVersionUID = 5652068212086090562L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				window.setActScene(new LevelChooserScene(Project.TUT));

			}
		});

		addMenu(new AbstractAction("Spiel") {
			private static final long serialVersionUID = -684758015991426321L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				window.setActScene(new LevelChooserScene(Project.GAME));

			}
		});

		addMenu(new AbstractAction("Beenden") {
			private static final long serialVersionUID = -6847580154991426321L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				window.setActScene(new EndScene());

			}
		});

		init();
	}
}
