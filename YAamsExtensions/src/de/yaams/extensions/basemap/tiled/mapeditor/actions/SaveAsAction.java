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

package de.yaams.extensions.basemap.tiled.mapeditor.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import de.yaams.extensions.basemap.tiled.io.MapHelper;
import de.yaams.extensions.basemap.tiled.mapeditor.MapEditor;
import de.yaams.extensions.basemap.tiled.mapeditor.Resources;
import de.yaams.extensions.basemap.tiled.mapeditor.util.ConfirmingFileChooser;
import de.yaams.extensions.basemap.tiled.mapeditor.util.TiledFileFilter;
import de.yaams.extensions.basemap.tiled.util.TiledConfiguration;
import de.yaams.maker.helper.gui.YEx;

/**
 * A save action that always shows a file chooser.
 * 
 * @version $Id$
 */
public class SaveAsAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3760628207510569687L;
	protected MapEditor editor;
	private boolean savingCancelled;

	private static final String ACTION_NAME = Resources.getString("action.map.saveas.name");
	private static final String ACTION_TOOLTIP = Resources.getString("action.map.saveas.tooltip");
	private static final String SAVEAS_ERROR_MESSAGE = Resources.getString("dialog.saveas.error.message");
	private static final String SAVEAS_ERROR_TITLE = Resources.getString("dialog.saveas.error.title");

	public SaveAsAction(MapEditor editor) {
		super(ACTION_NAME);
		putValue(SHORT_DESCRIPTION, ACTION_TOOLTIP);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift S"));
		this.editor = editor;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		showFileChooser();
	}

	/**
	 * Shows the confirming file chooser and proceeds with saving the map when a
	 * filename was approved.
	 */
	protected void showFileChooser() {
		// Start at the location of the most recently loaded map file
		String startLocation = TiledConfiguration.node("recent").get("file0", null);

		TiledFileFilter byExtensionFilter = new TiledFileFilter(TiledFileFilter.FILTER_EXT);

		JFileChooser chooser = new ConfirmingFileChooser(startLocation);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.addChoosableFileFilter(byExtensionFilter);

		MapHelper.addExtension(chooser);

		chooser.setFileFilter(byExtensionFilter);

		int result = chooser.showSaveDialog(editor.getAppFrame());
		if (result == JFileChooser.APPROVE_OPTION) {
			savingCancelled = false;
			TiledFileFilter saver = (TiledFileFilter) chooser.getFileFilter();
			saveFile(saver, chooser.getSelectedFile());
		} else {
			savingCancelled = true;
		}
	}

	/**
	 * Actually saves the map.
	 * 
	 * @param saver
	 *            the file filter selected when the filename was chosen
	 * @param filename
	 *            the filename to save the map to
	 */
	protected void saveFile(TiledFileFilter saver, File filename) {
		try {
			// Either select the format by extension or use a specific format
			// when selected.
			MapHelper.saveMap(editor.getCurrentMap(), filename);

			// The file was saved successfully, update some things.
			// todo: this could probably be done a bit neater
			editor.getCurrentMap().setFilename(filename);
			editor.updateRecent(filename.getAbsolutePath());
			editor.getUndoHandler().commitSave();
		} catch (Throwable t) {
			YEx.info(SAVEAS_ERROR_MESSAGE + " " + filename + ": " + t.getLocalizedMessage(), t);
		}
	}

	public boolean isSavingCancelled() {
		return savingCancelled;
	}
}
