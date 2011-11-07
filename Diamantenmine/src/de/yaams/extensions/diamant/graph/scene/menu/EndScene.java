/**
 * 
 */
package de.yaams.extensions.diamant.graph.scene.menu;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * @author abby
 * 
 */
public class EndScene extends MenuScene {

	private static final long serialVersionUID = -415429350897570948L;

	/**
	 * 
	 */
	public EndScene() {
		super("Willst du das Spiel wirklich beenden?");

		addMenu(new AbstractAction("Beenden") {

			private static final long serialVersionUID = 5652068212086090562L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);

			}
		});

		addMenu(new AbstractAction("Zurück") {
			private static final long serialVersionUID = -6847580154991426321L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				window.setActScene(new MenuMainScene());

			}
		});

		init();
	}
}
