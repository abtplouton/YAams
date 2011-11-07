/**
 * 
 */
package de.yaams.extensions.diamant.graph.scene;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;

import de.yaams.extensions.diamant.graph.img.LoadFiles;

/**
 * @author abby
 * 
 */
public class MessageScene extends BaseScene {
	private static final long serialVersionUID = 4300923477656866580L;

	protected KeyListener key;

	/**
	 * Create a new scene
	 * 
	 * @param level
	 */
	public MessageScene(String title, String mess, final BaseScene next) {
		setLayout(new GridLayout(1, 1));

		// add message
		JLabel j = new JLabel("<html><h1>" + title + "</h1><h2>" + (mess == null ? "" : mess)) {
			private static final long serialVersionUID = -6547885632427351283L;

			@Override
			public void paint(Graphics g) {
				g.drawImage(LoadFiles.getInternImage("capter.jpg"), 0, 0, MessageScene.this.getWidth(), MessageScene.this.getHeight(), null);
				super.paint(g);
			}
		};
		// InputStream fin = LoadFiles.class.getResourceAsStream("DeiGrat.ttf");
		// try {
		// j.setFont(Font.createFont( // Load font from InputStream fin
		// Font.PLAIN, fin).deriveFont(24f));
		// } catch (Throwable t) {
		// YEx.warn("Can not create font", t);
		// }
		j.setForeground(Color.black);
		j.setHorizontalAlignment(JLabel.CENTER);
		j.setVerticalAlignment(JLabel.CENTER);

		add(j);

		key = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyPressed(KeyEvent k) {
				window.getContent().removeKeyListener(key);
				window.setActScene(next);
			}
		};
	}

	@Override
	public void init() {
		window.getContent().addKeyListener(key);
	}

	@Override
	public void update() {
	}

	@Override
	public void close() {
	}

}
