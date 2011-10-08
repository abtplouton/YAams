/*
 * Copyright (c) 2009-2010, EzWare All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution.Neither the name of the EzWare nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
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

package com.ezware.dialog.task;

import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.ezware.common.EmptyIcon;
import com.ezware.common.Strings;
import com.ezware.common.SwingBean;
import com.ezware.dialog.task.design.TaskDialogContent;

public class TaskDialog extends SwingBean {

	private static final String INSTANCE_PROPERTY = "task.dialog.instance";

	private static final String DEBUG_PROPERTY = "task.dialog.debug";

	static final String I18N_PREFIX = "@@";
	private static final String LOCALE_BUNDLE_NAME = "task-dialog";

	private static IContentDesign design = ContentDesignFactory.getDesignByOperatingSystem();

	static {
		getDesign().updateUIDefaults();
	}

	static final IContentDesign getDesign() {
		return design;
	}

	/**
	 * Makes resource bundle key based on the provided text
	 * 
	 * @param text
	 * @return
	 */
	public static final String makeKey(String text) {
		return I18N_PREFIX + text;
	}

	/**
	 * Returns task dialog instance associated with source component
	 * 
	 * @param source
	 * @return task dialog instance associated with source component or null if
	 *         none
	 */
	public final static TaskDialog getInstance(Component source) {

		if (source == null) {
			return null;
		}

		Window w = SwingUtilities.getWindowAncestor(source);
		if (w instanceof JDialog) {
			JComponent c = (JComponent) ((JDialog) w).getContentPane();
			return (TaskDialog) c.getClientProperty(INSTANCE_PROPERTY);
		}

		return null;

	}

	/**
	 * Sets debug mode for task dialog framework.
	 * 
	 * @param debug
	 */
	public final static void setDebugMode(boolean debug) {

		if (debug) {
			System.setProperty(DEBUG_PROPERTY, "true");
		} else {
			System.clearProperty(DEBUG_PROPERTY);
		}

	}

	/**
	 * True if debug mode is set. In debug mode component bounds and grid cells
	 * are outlined. This helps with debugging overall appearance of task dialog
	 * UI.
	 * 
	 * @return true if debug mode is set
	 */
	public final static boolean isDebugMode() {
		return System.getProperty(DEBUG_PROPERTY) != null;
	}

	/**
	 * Set of standard dialog icons. Look and Feel dependent
	 */
	public static enum StandardIcon implements Icon {

		INFO(IContentDesign.ICON_INFO), // "OptionPane.informationIcon"
		QUESTION(IContentDesign.ICON_QUESTION), // "OptionPane.questionIcon"
		WARNING(IContentDesign.ICON_WARNING), // "OptionPane.warningIcon"
		ERROR(IContentDesign.ICON_ERROR); // "OptionPane.errorIcon"

		private final String key;

		private StandardIcon(String key) {
			this.key = key;
		}

		@Override
		public int getIconHeight() {
			return getIcon().getIconHeight();
		}

		@Override
		public int getIconWidth() {
			return getIcon().getIconWidth();
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			getIcon().paintIcon(c, g, x, y);
		}

		/**
		 * Always returns an valid instance of icon.
		 * 
		 * @return
		 */
		private synchronized javax.swing.Icon getIcon() {
			Icon icon = UIManager.getIcon(key); // No caching to facilitate LAF
												// switching.
			return icon == null ? getEmptyIcon() : icon;
		}

		private Icon emptyIcon;

		private synchronized Icon getEmptyIcon() {
			if (emptyIcon == null) {
				emptyIcon = EmptyIcon.hidden();
			}
			return emptyIcon;
		}

	}

	private static final KeyStroke KS_ESCAPE = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
	private static final KeyStroke KS_ENTER = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
	private static final KeyStroke KS_F1 = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);

	/**
	 * Standard Command Tags, correspond directly to button tags in MigLayout
	 * 
	 */
	public static enum CommandTag {

		OK("ok", "ok", KS_ENTER, true, true), // OK button
		CANCEL("cancel", "cancel", KS_ESCAPE, true), // Cancel button
		HELP("help", "help", KS_F1, true),
		// Help button that is normally on the right
		HELP2("help2", "help", "Help", KS_F1, false),
		// Help button that on some platforms are placed on the left
		YES("yes", "yes", KS_ENTER, true, true), // Yes button
		NO("no", "no", KS_ESCAPE, true), // No button
		APPLY("apply", "ok"), // Apply button
		NEXT("next", "arrow", true, false), // Next or Forward Button
		BACK("back", "arrow-left"), // Previous or Back Button
		FINISH("finish", "ok", KS_ENTER, true, true), // Finish button
		LEFT("left", "arrow-left"),
		// Button that should normally always be placed on the far left
		RIGHT("right", "arrow");
		// Button that should normally always be placed on the far right

		private String tag;
		private final String defaultTitle;
		private boolean useValidationResult = false;
		private boolean closing;
		private KeyStroke defaultKeyStroke;
		private String icon;

		CommandTag(String tag, String icon, String defaultTitle, KeyStroke defaultKeyStroke, boolean useValidationResult, boolean closing) {
			this.tag = "tag " + tag;
			this.defaultTitle = makeKey(Strings.isEmpty(defaultTitle) ? Strings.capitalize(tag) : defaultTitle);
			this.useValidationResult = useValidationResult;
			this.closing = closing;
			this.defaultKeyStroke = defaultKeyStroke;
			this.icon = icon;
		}

		CommandTag(String tag, String icon, String defaultTitle, KeyStroke defaultKeyStroke, boolean closing) {
			this(tag, icon, defaultTitle, defaultKeyStroke, false, closing);
		}

		CommandTag(String tag, String icon, KeyStroke defaultKeyStroke, boolean closing) {
			this(tag, icon, null, defaultKeyStroke, false, closing);
		}

		CommandTag(String tag, String icon, boolean closing) {
			this(tag, icon, null, closing);
		}

		CommandTag(String tag, String icon) {
			this(tag, icon, false);
		}

		CommandTag(String tag, String icon, KeyStroke defaultKeyStroke, boolean useValidationResult, boolean closing) {
			this(tag, icon, null, defaultKeyStroke, useValidationResult, closing);
		}

		CommandTag(String tag, String icon, boolean useValidationResult, boolean closing) {
			this(tag, icon, null, useValidationResult, closing);
		}

		public String getDefaultTitle() {
			return defaultTitle;
		}

		public String getIcon() {
			return icon;
		}

		@Override
		public String toString() {
			return tag;
		}

		public boolean isEnabled(boolean validationResult) {
			return useValidationResult ? validationResult : true;
		}

		public boolean isClosing() {
			return closing;
		}

		public KeyStroke getDefaultKeyStroke() {
			return defaultKeyStroke;
		}

	}

	public interface ValidationListener {

		void validationFinished(boolean validationResult);
		// possibly need a collection of errors/warnings

	}

	/**
	 * Interface to define task dialog commands
	 * 
	 */
	public interface Command {

		public String getTitle();

		public String getIcon();

		public CommandTag getTag();

		public String getDescription();

		public boolean isClosing();

		public int getWaitInterval(); // in seconds

		public boolean isEnabled(boolean validationResult);

		public KeyStroke getKeyStroke();
	}

	/**
	 * Set of standard task dialog commands
	 */
	public static enum StandardCommand implements Command {

		OK(CommandTag.OK, "ok"), CANCEL(CommandTag.CANCEL, "cancel"), HELP(CommandTag.HELP, "help");

		private final CommandTag tag;
		private String icon;

		StandardCommand(CommandTag tag, String icon) {
			this.tag = tag;
			this.icon = icon;
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public String getIcon() {
			return icon;
		}

		@Override
		public CommandTag getTag() {
			return tag;
		}

		@Override
		public String getTitle() {
			return tag.getDefaultTitle();
		}

		@Override
		public boolean isClosing() {
			return tag.isClosing();
		}

		@Override
		public int getWaitInterval() {
			return 0;
		}

		@Override
		public boolean isEnabled(boolean validationResult) {
			return tag.isEnabled(validationResult);
		}

		@Override
		public KeyStroke getKeyStroke() {
			return tag.getDefaultKeyStroke();
		}

		/**
		 * Creates a copy of the command with specified title
		 * 
		 * @param title
		 *            new title. Used as key for i18n if starts with I18N_PREFIX
		 * @param waitInterval
		 *            in seconds
		 * @return copy of the command
		 */
		public Command derive(final String title, final String icon, final int waitInterval) {

			return new CustomCommand(StandardCommand.this) {

				@Override
				public String getTitle() {
					return title;
				}

				@Override
				public String getIcon() {
					return icon;
				}

				@Override
				public int getWaitInterval() {
					return waitInterval;
				}

			};

		}

		/**
		 * Creates a copy of the command with specified title
		 * 
		 * @param title
		 *            new title. Used as key for i18n if starts with
		 *            #I18N_PREFIX
		 * @return copy of the command
		 */
		public Command derive(final String title, final String icon) {

			return new CustomCommand(StandardCommand.this) {

				@Override
				public String getTitle() {
					return title;
				}

				@Override
				public String getIcon() {
					return icon;
				}

			};

		}

	}

	public static abstract class CustomCommand implements Command {

		private final StandardCommand command;

		public CustomCommand(StandardCommand command) {
			if (command == null) {
				throw new IllegalArgumentException("Command should not be null");
			}
			this.command = command;
		}

		@Override
		public String getDescription() {
			return command.getDescription();
		}

		@Override
		public CommandTag getTag() {
			return command.getTag();
		}

		@Override
		public String getTitle() {
			return command.getTitle();
		}

		@Override
		public boolean isClosing() {
			return command.isClosing();
		}

		@Override
		public int getWaitInterval() {
			return command.getWaitInterval();
		}

		@Override
		public boolean isEnabled(boolean validationResult) {
			return command.isEnabled(validationResult);
		}

		@Override
		public boolean equals(Object obj) {
			return command.equals(obj);
		}

		@Override
		public KeyStroke getKeyStroke() {
			return command.getKeyStroke();
		}

	}

	/**
	 * Task Dialog Details
	 * 
	 */
	public interface Details {

		/**
		 * Return text for collapsed label
		 * 
		 * @return
		 */
		String getCollapsedLabel();

		/**
		 * Sets text for collapsed label
		 * 
		 * @param label
		 */
		void setCollapsedLabel(String label);

		/**
		 * Returns text for expanded label
		 * 
		 * @return
		 */
		String getExpandedLabel();

		/**
		 * Sets text for expanded label
		 * 
		 * @param label
		 */
		void setExpandedLabel(String label);

		/**
		 * Checks if details are in expansion state
		 * 
		 * @return
		 */
		boolean isExpanded();

		/**
		 * Sets expansion state
		 * 
		 * @param expanded
		 */
		void setExpanded(boolean expanded);

		/**
		 * Sets the state always expanded and hide the expand checkbox.
		 * 
		 * @param always
		 */
		void setAlwaysExpanded(boolean always);

		/**
		 * Checks if the details are always expanded.
		 * 
		 * @return
		 */
		boolean isAlwaysExpanded();

		/**
		 * Returns component which becomes visible when details are expanded
		 * 
		 * @return
		 */
		JComponent getExpandableComponent();

		/**
		 * Sets component which becomes visible when details are expanded
		 * 
		 * @param cmpt
		 */
		void setExpandableComponent(JComponent cmpt);

	}

	/**
	 * Task Dialog Footer
	 * 
	 */
	public interface Footer {

		/**
		 * True if footer's check box is selected (checked)
		 * 
		 * @return
		 */
		boolean isCheckBoxSelected();

		/**
		 * Sets footer's check box selection status
		 * 
		 * @param selected
		 */
		void setCheckBoxSelected(boolean selected);

		/**
		 * Returns footer's check box text
		 * 
		 * @return
		 */
		String getCheckBoxText();

		/**
		 * Sets footer's check box text. Check box is only visible if it has a
		 * text
		 * 
		 * @param text
		 */
		void setCheckBoxText(String text);

		/**
		 * Returns footer's text icon
		 * 
		 * @return
		 */
		Icon getIcon();

		/**
		 * Sets footer's text icon. Icon is only visible if corresponding text
		 * is not empty
		 * 
		 * @param icon
		 */
		void setIcon(Icon icon);

		/**
		 * Returns footer's text
		 * 
		 * @return
		 */
		String getText();

		/**
		 * Sets footer's text. The text and corresponding icon are visible if
		 * text is not empty
		 * 
		 * @param text
		 */
		void setText(String text);

	}

	/*----------------------------------------------------------------------------------------------*/

	private Locale resourceBundleLocale = null; // has to be null
	private ResourceBundle resourceBundle = null;

	private Command result = StandardCommand.CANCEL;

	private final JDialog dlg;
	private final TaskDialogContent content;

	private Set<Command> commands = new LinkedHashSet<Command>(Arrays.asList(StandardCommand.OK));
	private final List<ValidationListener> validationListeners = new ArrayList<ValidationListener>();

	/**
	 * Creates a task dialog.<br/>
	 * Automatically pick up currently active window as a parent window
	 * 
	 * @param title
	 */
	public TaskDialog(final Window parent, final String title) {

		Window pWnd = parent;

		if (pWnd == null) {
			// find active visible top level window
			pWnd = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
			while (pWnd != null && !pWnd.isVisible() && pWnd.getParent() != null) {
				pWnd = (Window) pWnd.getParent();
			}
		}

		dlg = new JDialog(pWnd);

		dlg.setMinimumSize(new Dimension(300, 150)); // TODO: make constants -
														// should be based on
														// LAF
		setResizable(false);
		setModalityType(JDialog.DEFAULT_MODALITY_TYPE); // TODO explore
														// different modality
														// types

		dlg.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				result = StandardCommand.CANCEL;
			}
		});

		setTitle(title);
		content = design.buildContent();
		dlg.setContentPane(content);

		// store task dialog instance
		JComponent c = (JComponent) dlg.getContentPane();
		c.putClientProperty(INSTANCE_PROPERTY, this);

	}

	// /**
	// * This constructor us deprecated and may be removed in the next version.
	// * Use the constructor with parent and title parameters
	// * @param title
	// */
	// @Deprecated()
	// public TaskDialog( String title ) {
	// this( null, title );
	// }

	public ModalityType getModalityType() {
		return dlg.getModalityType();
	}

	public void setModalityType(ModalityType modalityType) {
		dlg.setModalityType(modalityType);
	}

	public boolean isResizable() {
		return dlg.isResizable();
	}

	public void setResizable(boolean resizable) {
		dlg.setResizable(resizable);
	}

	public final void addValidationListener(ValidationListener listener) {
		if (listener != null) {
			validationListeners.add(listener);
		}
	}

	public final void removeValidationListener(ValidationListener listener) {
		if (listener != null) {
			validationListeners.remove(listener);
		}
	}

	public final void fireValidationFinished(boolean validationResult) {

		// Iterate in reverse sequence so 'newer' listeners get message first
		ListIterator<ValidationListener> iter = validationListeners.listIterator();
		while (iter.hasPrevious()) {
			iter.previous().validationFinished(validationResult);
		}
	}

	/**
	 * Gets the Locale object that is associated with this window, if the locale
	 * has been set. If no locale has been set, then the default locale is
	 * returned.
	 * 
	 * @return the locale that is set for this dialog
	 */
	public Locale getLocale() {
		return dlg.getLocale();
	}

	/**
	 * Sets locale which will be used as dialog's locale
	 * 
	 * @param newLocale
	 *            null is allowed and will be interpreted as default locale
	 */
	public void setLocale(final Locale locale) {
		dlg.setLocale(locale);
	}

	private synchronized final ResourceBundle getLocaleBundle() {

		Locale currentLocale = getLocale();
		if (!currentLocale.equals(resourceBundleLocale)) {
			resourceBundleLocale = currentLocale;
			resourceBundle = ResourceBundle.getBundle(LOCALE_BUNDLE_NAME, resourceBundleLocale, getClass().getClassLoader());
		}
		return resourceBundle;

	}

	/**
	 * Returns a string localized using currently set locale
	 * 
	 * @param key
	 * @return
	 */
	public String getLocalizedString(String key) {
		try {
			return getLocaleBundle().getString(key);
		} catch (MissingResourceException ex) {
			return String.format("<%s>", key);
		}
	}

	/**
	 * Tries to use text as a key to get localized text. If not found the text
	 * itself is returned
	 * 
	 * @param text
	 * @return
	 */
	public String getString(String text) {
		return text.startsWith(I18N_PREFIX) ? getLocalizedString(text.substring(I18N_PREFIX.length())) : text;
	}

	/**
	 * Shows or hides this {@code Dialog} depending on the value of parameter
	 * 
	 * @param visible
	 *            if true dialog is shown
	 */
	public void setVisible(boolean visible) {

		if (visible) {
			// set commands first because they may depend on "visible" property
			// change
			content.setCommands(commands, getDesign().isCommandButtonSizeLocked());
		}

		if (!firePropertyChange("visible", isVisible(), visible)) {
			return;
		}

		if (visible) {
			dlg.pack();

			// location is set relative to currently active window
			// this way task dialog stays on the same monitor as it's owner
			dlg.setLocationRelativeTo(KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow());
			dlg.setVisible(true);
		} else {
			dlg.dispose(); // releases native resources
		}

	}

	/**
	 * Determines whether this component should be visible when its parent is
	 * visible.
	 * 
	 * @return true if visible
	 */
	public boolean isVisible() {
		return dlg.isVisible();
	}

	public Command getResult() {
		return result;
	}

	public void setResult(Command result) {
		this.result = result; // should always set
		firePropertyChange("result", null, result);
	}

	/**
	 * Shows the dialog
	 * 
	 * @return dialog result
	 */
	public Command show() {
		setVisible(true);
		return getResult();
	}

	@Override
	public String toString() {
		return getTitle();
	}

	/**
	 * Returns the title of the dialog
	 * 
	 * @return
	 */
	public String getTitle() {
		return dlg.getTitle();
	}

	/**
	 * Sets the title of the dialog
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		dlg.setTitle(getString(title));
	}

	/**
	 * Sets icon for the dialog
	 * 
	 * @param icon
	 */
	public void setIcon(Icon icon) {
		if (firePropertyChange("icon", getIcon(), icon)) {
			content.setMainIcon(icon);
		}
	}

	/**
	 * Returns dialog's icon
	 * 
	 * @return
	 */
	public Icon getIcon() {
		return content.getMainIcon();
	}

	/**
	 * Sets dialog's instruction
	 * 
	 * @param instruction
	 */
	public void setInstruction(String instruction) {
		if (firePropertyChange("instruction", getInstruction(), instruction)) {
			content.setInstruction(instruction);
		}
	}

	/**
	 * Returns dialog instruction
	 * 
	 * @return
	 */
	public String getInstruction() {
		return content.getInstruction();
	}

	/**
	 * Sets dialog text
	 * 
	 * @param text
	 */
	public void setText(String text) {
		if (firePropertyChange("text", getText(), text)) {
			content.setMainText(text);
		}
	}

	/**
	 * Returns Dialog text
	 * 
	 * @return
	 */
	public String getText() {
		return content.getMainText();
	}

	/**
	 * Sets Dialog component
	 * 
	 * @param c
	 */
	public void setFixedComponent(JComponent c) {
		content.setComponent(c);
	}

	/**
	 * Returns dialog's component
	 * 
	 * @return
	 */
	public JComponent getFixedComponent() {
		return content.getComponent();
	}

	public TaskDialog.Details getDetails() {
		return content;
	}

	public TaskDialog.Footer getFooter() {
		return content;
	}

	public void setCommands(Collection<TaskDialog.Command> commands) {
		this.commands = new LinkedHashSet<Command>(commands);
	}

	public void setCommands(TaskDialog.Command... commands) {
		setCommands(Arrays.asList(commands));
	}

	public Collection<TaskDialog.Command> getCommands() {
		return commands;
	}

	public boolean isCommandsVisible() {
		return content.isCommandsVisible();
	}

	public void setCommandsVisible(boolean visible) {
		content.setCommandsVisible(visible);
	}

}
