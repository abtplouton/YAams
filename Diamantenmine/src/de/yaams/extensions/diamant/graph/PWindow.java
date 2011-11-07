/**
 * 
 */
package de.yaams.extensions.diamant.graph;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.yaams.extensions.diamant.Project;
import de.yaams.extensions.diamant.graph.scene.BaseScene;
import de.yaams.extensions.diamant.helper.YEx;

/**
 * @author abby
 * 
 */
public class PWindow extends JFrame {

	private static final long serialVersionUID = -7365308811436418770L;
	protected BaseScene actScene;
	protected JPanel content;
	public static PWindow window;

	/**
	 * 
	 */
	public PWindow() {
		super(Project.project.getTitle());
		setLayout(new GridLayout(1, 1));
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		content = new JPanel(new GridLayout(1, 1));
		content.setFocusable(true);
		add(content);

		window = this;
	}

	/**
	 * @return the actScene
	 */
	public BaseScene getActScene() {
		return actScene;
	}

	/**
	 * @param actScene
	 *            the actScene to set
	 */
	public void setActScene(BaseScene actScene) {
		// remove old scene
		if (this.actScene != null) {
			this.actScene.close();
		}

		// add it
		content.removeAll();
		actScene.setWindow(this);
		actScene.init();
		content.add(actScene);

		// check it
		actScene.invalidate();
		actScene.revalidate();
		this.actScene = actScene;
	}

	/**
	 * Start it
	 */
	public void start() {
		pack();
		setLocationRelativeTo(null);
		setVisible(true);

		// add timer
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (true) {
						Thread.sleep(50);
						actScene.update();
						repaint();
					}
				} catch (InterruptedException e) {
					YEx.info("can not wait", e);
				}

			}
		}).start();
	}

	/**
	 * @return the content
	 */
	public JPanel getContent() {
		return content;
	}
}
