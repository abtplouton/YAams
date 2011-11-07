/**
 * 
 */
package de.yaams.extensions.diamant.graph.scene.menu;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import de.yaams.extensions.diamant.graph.img.LoadFiles;
import de.yaams.extensions.diamant.graph.scene.BaseScene;
import de.yaams.extensions.diamant.helper.YEx;

/**
 * @author abby
 * 
 */
public abstract class MenuScene extends BaseScene {

	private static final long serialVersionUID = 7128135777446474612L;
	protected ArrayList<Action> menu;
	protected String title;

	/**
	 * 
	 */
	public MenuScene(String title) {
		menu = new ArrayList<Action>();
		this.title = title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pirat.graph.scene.BaseScene#init()
	 */
	@Override
	public void init() {
		setLayout(new BorderLayout());

		// build layout
		JPanel main = new JPanel(new GridLayout(menu.size(), 1));
		main.setFocusable(true);
		main.setBorder(new EmptyBorder(20, 20, 20, 20));

		// add button
		for (Action e : menu) {
			JButton j = new JButton(e);
			j.setBorder(new EmptyBorder(20, 20, 20, 20));
			main.add(j);
		}

		// add title
		JLabel j = new JLabel(title) {

			private static final long serialVersionUID = 3547594210364335856L;

			@Override
			public void paint(Graphics g) {
				g.drawImage(LoadFiles.getInternImage("MainMenuBack.jpg"), 0, 0, getWidth(), getHeight(), null);
				super.paint(g);
			}
		};
		j.setBorder(new EmptyBorder(20, 20, 0, 20));
		InputStream fin = LoadFiles.class.getResourceAsStream("FaithCollapsing.ttf");
		try {
			j.setFont(Font.createFont( // Load font from InputStream fin
					Font.PLAIN, fin).deriveFont(30f));
		} catch (Throwable t) {
			YEx.info("Can not create font", t);
		}

		add(j, BorderLayout.NORTH);
		add(main, BorderLayout.CENTER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pirat.graph.scene.BaseScene#update()
	 */
	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	/**
	 * @param menu
	 *            the menu to set
	 */
	public void addMenu(Action menu) {
		this.menu.add(menu);
	}

	@Override
	public void close() {

	}

}
