/*
 * Tiled Map Editor, (c) 2004-2006
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * Adam Turk <aturk@biggeruniverse.com> Bjorn Lindeijer <bjorn@lindeijer.nl>
 */

package de.yaams.extensions.basemap.tiled.mapeditor.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.MouseInputAdapter;

import de.yaams.extensions.basemap.tiled.mapeditor.MapEditor;
import de.yaams.extensions.basemap.tiled.mapeditor.Resources;

/**
 * The about dialog.
 * 
 * @version $Id$
 */
public class AboutDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4387369920448662615L;
	private final JFrame parent;
	private JProgressBar memoryBar;

	public AboutDialog(JFrame parent) {
		super(parent, Resources.getString("dialog.main.title") + " v" + MapEditor.version);

		this.parent = parent;

		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(new UpdateTimerTask(), 0, 1000);

		setContentPane(createMainPanel());
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		pack();
	}

	private JPanel createMainPanel() {
		JLabel logo = new JLabel(Resources.getIcon("logo.png"));
		logo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		logo.setAlignmentX(Component.LEFT_ALIGNMENT);
		logo.addMouseListener(new MouseInputAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				setVisible(false);
			}
		});

		JSeparator separator = new JSeparator();
		separator.setAlignmentX(Component.LEFT_ALIGNMENT);

		memoryBar = new JProgressBar();
		memoryBar.setStringPainted(true);

		JButton gcButton = new JButton(new GarbageCollectAction());
		gcButton.setBorderPainted(false);
		gcButton.setBorder(null);
		gcButton.setOpaque(false);
		gcButton.setBackground(new Color(0, 0, 0, 0));

		JPanel barPanel = new JPanel();
		barPanel.setLayout(new BoxLayout(barPanel, BoxLayout.X_AXIS));
		barPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		barPanel.setMaximumSize(new Dimension(logo.getPreferredSize().width, Short.MAX_VALUE));
		barPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		barPanel.add(memoryBar);
		barPanel.add(Box.createRigidArea(new Dimension(5, 5)));
		barPanel.add(gcButton);

		JPanel mainPanel = new JPanel();
		mainPanel.setOpaque(false);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(logo);
		mainPanel.add(separator);
		mainPanel.add(barPanel);
		return mainPanel;
	}

	private void updateMemoryBar() {
		int total = (int) Runtime.getRuntime().totalMemory();
		int used = (int) (total - Runtime.getRuntime().freeMemory());
		memoryBar.setMaximum(total);
		memoryBar.setValue(used);
		memoryBar.setString(used / 1024 + " KB / " + total / 1024 + " KB");
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			updateMemoryBar();
			setLocationRelativeTo(parent);
		}
		super.setVisible(visible);
	}

	/**
	 * Used for updating the memory bar in intervals.
	 */
	private class UpdateTimerTask extends TimerTask {
		@Override
		public void run() {
			if (isVisible()) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						updateMemoryBar();
					}
				});
			}
		}
	}

	/**
	 * Collects garbage.
	 */
	private class GarbageCollectAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8384121843696667853L;

		public GarbageCollectAction() {
			putValue(Action.SMALL_ICON, Resources.getIcon("gnome-delete.png"));
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			System.gc();
			updateMemoryBar();
		}
	}
}
