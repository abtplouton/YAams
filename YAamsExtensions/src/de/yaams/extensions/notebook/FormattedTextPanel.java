/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * - Neither the name of Oracle or the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior
 * written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package de.yaams.extensions.notebook;

/*
 * TextComponentDemo.java requires one additional file: DocumentSizeFilter.java
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.undo.UndoManager;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.YToolBar;
import de.yaams.maker.helper.gui.rightclick.YRightClickMenuText;

public class FormattedTextPanel extends JPanel {

	private static final long serialVersionUID = 1505315494248025588L;
	protected JTextPane textPane;
	protected AbstractDocument doc;
	private static final String newline = "\n";
	protected HashMap<Object, Action> actions;

	// undo helpers
	protected UndoAction undoAction;
	protected RedoAction redoAction;
	protected UndoManager undo = new UndoManager();

	public FormattedTextPanel(String text) {
		super(new BorderLayout());

		// Create the text pane and configure it.
		textPane = new JTextPane();
		textPane.setCaretPosition(0);
		textPane.setMargin(new Insets(5, 5, 5, 5));
		textPane.setText(text);
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setPreferredSize(new Dimension(200, 200));

		// Create the text area for the status log and configure it.
		doc = (AbstractDocument) textPane.getDocument();

		// Create the status area.
		JPanel statusPane = new JPanel(new GridLayout(1, 1));
		CaretListenerLabel caretListenerLabel = new CaretListenerLabel("Caret Status");
		statusPane.add(caretListenerLabel);

		// Add the components.
		add(scrollPane, BorderLayout.CENTER);
		add(statusPane, BorderLayout.PAGE_END);

		// Set up the menu bar.
		actions = createActionTable(textPane);
		YToolBar toolbar = new YToolBar();
		createEditMenu(toolbar);
		createStyleMenu(toolbar);
		add(toolbar, BorderLayout.NORTH);

		// Add some key bindings.
		addBindings();

		// Put the initial text into the text pane.
		// doc.insertString(offs, str, a)
		// initDocument();
		textPane.setCaretPosition(0);

		YRightClickMenuText.install(textPane);

		// Start watching for undoable edits and caret changes.
		doc.addUndoableEditListener(new MyUndoableEditListener());
		textPane.addCaretListener(caretListenerLabel);
	}

	// This listens for and reports caret movements.
	protected class CaretListenerLabel extends JLabel implements CaretListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2540042809932141177L;

		public CaretListenerLabel(String label) {
			super(label);
		}

		// Might not be invoked from the event dispatch thread.
		@Override
		public void caretUpdate(CaretEvent e) {
			displaySelectionInfo(e.getDot(), e.getMark());
		}

		// This method can be invoked from any thread. It
		// invokes the setText and modelToView methods, which
		// must run on the event dispatch thread. We use
		// invokeLater to schedule the code for execution
		// on the event dispatch thread.
		protected void displaySelectionInfo(final int dot, final int mark) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (dot == mark) { // no selection
						try {
							Rectangle caretCoords = textPane.modelToView(dot);
							// Convert it to view coordinates.
							setText("caret: text position: " + dot + ", view location = [" + caretCoords.x + ", " + caretCoords.y + "]"
									+ newline);
						} catch (BadLocationException ble) {
							setText("caret: text position: " + dot + newline);
						}
					} else if (dot < mark) {
						setText("selection from: " + dot + " to " + mark + newline);
					} else {
						setText("selection from: " + mark + " to " + dot + newline);
					}
				}
			});
		}
	}

	// This one listens for edits that can be undone.
	protected class MyUndoableEditListener implements UndoableEditListener {
		@Override
		public void undoableEditHappened(UndoableEditEvent e) {
			// Remember the edit and update the menus.
			undo.addEdit(e.getEdit());
			undoAction.updateUndoState();
			redoAction.updateRedoState();
		}
	}

	// Add a couple of emacs key bindings for navigation.
	protected void addBindings() {
		InputMap inputMap = textPane.getInputMap();

		// Ctrl-b to go backward one character
		KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK);
		inputMap.put(key, DefaultEditorKit.backwardAction);

		// Ctrl-f to go forward one character
		key = KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK);
		inputMap.put(key, DefaultEditorKit.forwardAction);

		// Ctrl-p to go up one line
		key = KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK);
		inputMap.put(key, DefaultEditorKit.upAction);

		// Ctrl-n to go down one line
		key = KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK);
		inputMap.put(key, DefaultEditorKit.downAction);
	}

	/**
	 * Create the edit menu.
	 * 
	 * @param toolbar
	 */
	protected void createEditMenu(YToolBar toolbar) {
		// Undo and redo are actions of our own creation.
		toolbar.addMiddle(YFactory.tb(new UndoAction(), I18N.t("Rückgängig"), "undo", 16));
		toolbar.addMiddle(YFactory.tb(new RedoAction(), I18N.t("Wiederholen"), "redo", 16));
		toolbar.addMiddleSeparator();

		// These actions come from the default editor kit.
		// Get the ones we want and stick them in the menu.
		toolbar.addMiddle(YFactory.tb(getActionByName(DefaultEditorKit.cutAction), I18N.t("Ausschneiden"), "cut", 16));
		toolbar.addMiddle(YFactory.tb(getActionByName(DefaultEditorKit.copyAction), I18N.t("Kopieren"), "copy", 16));
		toolbar.addMiddle(YFactory.tb(getActionByName(DefaultEditorKit.pasteAction), I18N.t("Einfügen"), "paste", 16));
		toolbar.addMiddleSeparator();

		toolbar.addMiddle(YFactory.tb(getActionByName(DefaultEditorKit.selectAllAction), I18N.t("Alles auswählen"), "selectAll", 16));
	}

	/**
	 * Create the style menu.
	 * 
	 * @param toolbar
	 */
	protected void createStyleMenu(YToolBar toolbar) {
		// add format
		toolbar.add(YFactory.tb(new StyledEditorKit.BoldAction(), I18N.t("Fett"), "bold", 16));
		toolbar.add(YFactory.tb(new StyledEditorKit.ItalicAction(), I18N.t("Kursiv"), "italic", 16));
		toolbar.add(YFactory.tb(new StyledEditorKit.UnderlineAction(), I18N.t("Unterstrichen"), "underline", 16));
		toolbar.addSeparator();

		// add size
		toolbar.add(new JButton(new StyledEditorKit.FontSizeAction("12", 12)));
		toolbar.add(new JButton(new StyledEditorKit.FontSizeAction("14", 14)));
		toolbar.add(new JButton(new StyledEditorKit.FontSizeAction("18", 18)));
		toolbar.addSeparator();

		// add font
		toolbar.add(new JButton(new StyledEditorKit.FontFamilyAction("Serif", "Serif")));
		toolbar.add(new JButton(new StyledEditorKit.FontFamilyAction("SansSerif", "SansSerif")));

		// add color
		toolbar.addRight(YFactory.tb(new StyledEditorKit.ForegroundAction("", Color.red), I18N.t("Rot"), "leaf-red", 16));
		toolbar.addRight(YFactory.tb(new StyledEditorKit.ForegroundAction("", Color.yellow), I18N.t("Gelb"), "leaf-yellow", 16));
		toolbar.addRight(YFactory.tb(new StyledEditorKit.ForegroundAction("", Color.green), I18N.t("Grün"), "leaf-green", 16));
		toolbar.addRight(YFactory.tb(new StyledEditorKit.ForegroundAction("", Color.blue), I18N.t("Blau"), "leaf-blue", 16));
		toolbar.addRight(YFactory.tb(new StyledEditorKit.ForegroundAction("", Color.black), I18N.t("Schwarz"), "leaf-black", 16));
	}

	protected SimpleAttributeSet[] initAttributes(int length) {
		// Hard-code some attributes.
		SimpleAttributeSet[] attrs = new SimpleAttributeSet[length];

		attrs[0] = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attrs[0], "SansSerif");
		StyleConstants.setFontSize(attrs[0], 16);

		attrs[1] = new SimpleAttributeSet(attrs[0]);
		StyleConstants.setBold(attrs[1], true);

		attrs[2] = new SimpleAttributeSet(attrs[0]);
		StyleConstants.setItalic(attrs[2], true);

		attrs[3] = new SimpleAttributeSet(attrs[0]);
		StyleConstants.setFontSize(attrs[3], 20);

		attrs[4] = new SimpleAttributeSet(attrs[0]);
		StyleConstants.setFontSize(attrs[4], 12);

		attrs[5] = new SimpleAttributeSet(attrs[0]);
		StyleConstants.setForeground(attrs[5], Color.red);

		return attrs;
	}

	// The following two methods allow us to find an
	// action provided by the editor kit by its name.
	private HashMap<Object, Action> createActionTable(JTextComponent textComponent) {
		HashMap<Object, Action> actions = new HashMap<Object, Action>();
		// Action[] actionsArray = textComponent.getActions();
		// for (int i = 0; i < actionsArray.length; i++) {
		// Action a = actionsArray[i];
		// // actions.put(a.getValue(Action.NAME), a);
		// }
		return actions;
	}

	private Action getActionByName(String name) {
		return actions.get(name);
	}

	class UndoAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 5804565705684201686L;

		public UndoAction() {
			super("Undo");
			setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				undo.undo();
			} catch (Throwable t) {
				YEx.info("Unable to undo: " + t, t);
			}
			updateUndoState();
			redoAction.updateRedoState();
		}

		protected void updateUndoState() {
			if (undo.canUndo()) {
				setEnabled(true);
				putValue(Action.NAME, undo.getUndoPresentationName());
			} else {
				setEnabled(false);
				putValue(Action.NAME, "Undo");
			}
		}
	}

	class RedoAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 6972977923163219821L;

		public RedoAction() {
			super("Redo");
			setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				undo.redo();
			} catch (Throwable t) {
				YEx.info("Unable to redo: " + t, t);
			}
			updateRedoState();
			undoAction.updateUndoState();
		}

		protected void updateRedoState() {
			if (undo.canRedo()) {
				setEnabled(true);
				putValue(Action.NAME, undo.getRedoPresentationName());
			} else {
				setEnabled(false);
				putValue(Action.NAME, "Redo");
			}
		}
	}

	/**
	 * @return the doc
	 */
	public AbstractDocument getDoc() {
		return doc;
	}

	/**
	 * @return the textPane
	 */
	public JTextPane getTextPane() {
		return textPane;
	}
}
