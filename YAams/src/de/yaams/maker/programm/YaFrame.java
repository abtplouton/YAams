/**
 * 
 */
package de.yaams.maker.programm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

import com.jidesoft.document.DocumentComponent;
import com.jidesoft.document.DocumentPane;
import com.jidesoft.status.ButtonStatusBarItem;
import com.jidesoft.status.MemoryStatusBarItem;
import com.jidesoft.status.ProgressStatusBarItem;
import com.jidesoft.status.ResizeStatusBarItem;
import com.jidesoft.status.StatusBar;
import com.jidesoft.swing.JideTabbedPane;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.JavaHelper;
import de.yaams.maker.helper.Log;
import de.yaams.maker.helper.Setting;
import de.yaams.maker.helper.SwingHelper;
import de.yaams.maker.helper.SystemHelper;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.Run;
import de.yaams.maker.helper.gui.YDialog;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.gui.YFrame;
import de.yaams.maker.helper.gui.YToolBar;
import de.yaams.maker.helper.gui.icons.IconCache;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.programm.favorit.FavoritManagement;
import de.yaams.maker.programm.tabs.BasicTabEvent;
import de.yaams.maker.programm.tabs.TabEvent;
import de.yaams.maker.programm.tabs.YaTab;

/**
 * @author abt
 * 
 */
public class YaFrame extends YFrame {
	private static final long serialVersionUID = 9091390431677573128L;

	private final YToolBar toolbarLeft, toolbarRight; // top bar
	// private final HashMap<String, Integer> tabUnique; // save the translation
	private final DocumentPane pane;
	private final ArrayList<TabEvent> tabs; // save the tab events
	private final StatusBar statusbar;
	private final ProgressStatusBarItem tooltip;

	// for the
	// tabs, to now, with is open

	private static YaFrame frame;

	/**
	 * Create a new YAFrame
	 * 
	 * @throws HeadlessException
	 */
	public YaFrame() {
		super(YAamsCore.NAME);

		// init components
		toolbarLeft = new YToolBar();
		toolbarRight = new YToolBar();

		tabs = new ArrayList<TabEvent>();

		pane = new DocumentPane();
		pane.setRearrangeAllowed(true);
		pane.setGroupsAllowed(true);
		pane.setTabbedPaneCustomizer(new DocumentPane.TabbedPaneCustomizer() {
			@Override
			public void customize(final JideTabbedPane tabbedPane) {
				tabbedPane.setScrollSelectedTabOnWheel(true);
				tabbedPane.setShowCloseButton(true);
				tabbedPane.setUseDefaultShowCloseButtonOnTab(false);
				tabbedPane.setShowCloseButtonOnTab(true);
				tabbedPane.setBoldActiveTab(true);
				tabbedPane.setHideOneTab(true);
				// tabbedPane.setTabLeadingComponent(getToolbarLeft());
				// tabbedPane.setTabTrailingComponent(getToolbarRight());
				// tabbedPane.addChangeListener(new ChangeListener() {
				//
				// @Override
				// public void stateChanged(ChangeEvent e) {
				// tabbedPane.setShowCloseButtonOnTab((tabbedPane
				// .getTabCount() > 1));
				// tabbedPane.setShowCloseButton((tabbedPane.getTabCount() >
				// 1));
				//
				// // close last tab?
				// if (tabbedPane.getTabCount() == 0) {
				// // open home
				// YaFrame.open(new HomeTab());
				// }
				//
				// }
				// });
			}
		});

		// pane.addContainerListener(new ContainerListener() {
		//
		// @Override public void componentRemoved(final ContainerEvent e) {
		// final JButton j = YFactory.b(I18N.t("Zeige Startseite"), "home", new
		// AE() {
		//
		// @Override public void run() {
		// YaFrame.open("home", null);
		// add(pane, BorderLayout.CENTER);
		// ((JButton) ae.getSource()).setVisible(false);
		// }
		// }, 32);
		// add(j, BorderLayout.NORTH);
		// frame.setExtendedState(ICONIFIED);
		//
		// }
		//
		// @Override public void componentAdded(final ContainerEvent e) {}
		// });

		// build layout
		setLayout(new BorderLayout());
		// setPreferredSize(new Dimension(400, 300));
		pack();
		// setDefaultCloseOperation(EXIT_ON_CLOSE);
		loadPos();

		add(pane, BorderLayout.CENTER);

		// build mailbutton
		ButtonStatusBarItem bsbi = new ButtonStatusBarItem();
		bsbi.setToolTipText(I18N.t("Send feedback"));
		bsbi.setIcon(IconCache.get("mail_web"));
		bsbi.addActionListener(new AE() {

			@Override
			public void run() {
				BasicTabEvent.openFeedback();

			}
		});

		// build statusbar
		statusbar = new StatusBar();
		tooltip = new ProgressStatusBarItem();
		statusbar.add(bsbi);
		statusbar.add(tooltip);
		statusbar.add(new MemoryStatusBarItem());
		statusbar.add(new ResizeStatusBarItem());

		add(statusbar, BorderLayout.SOUTH);

		// set close event
		// Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		//
		// @Override public void run() {
		// Setting.save(false);
		//
		// }
		// }));

		// set close event
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				boolean mod = false;
				// open tab?
				for (String id : frame.pane.getDocumentNames()) {
					YaTab tab = getOpenTab(id);
					if (tab.isModified()) {
						mod = true;
					}
				}

				// is mod?
				if (mod
						&& !YDialog.askUser(I18N.t("Änderungen speichern?"), "yaams.exit.save", "disk", "", I18N.t("Änderungen speichern"),
								I18N.CANCEL, "disk_ok", "cancel")) {
					return;
				}

				// save tabs
				for (String id : frame.pane.getDocumentNames()) {
					getOpenTab(id).saveChanged();
				}

				ExtentionManagement.work("close", new HashMap<String, Object>());

				savePos();
				YAamsCore.save();
				setVisible(false);
				dispose();
				System.exit(0);
			}
		});

	}

	/**
	 * Set for the statusbar the information
	 * 
	 * @param text
	 * @param icon
	 */
	public static void setTooltip(String text, Object icon) {
		get().getTooltip().setStatus(text);
		get().getTooltip().setStatusIcon(icon == null ? null : IconCache.getS(icon, IconCache.SIZE));
	}

	/**
	 * Save the position of the frame
	 */
	protected void savePos() {
		// save window position

		Setting.set("yaframe.x", getX());
		Setting.set("yaframe.y", getY());
		Setting.set("yaframe.width", getWidth());
		Setting.set("yaframe.height", getHeight());
		Setting.set("yaframe.state", getExtendedState());

	}

	/**
	 * Save the position of the frame
	 */
	protected void loadPos() {
		// load position
		if (Setting.exist("yaframe.x")) {
			setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else {
			// check validate monitor values
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

			int x = Setting.get("yaframe.x", 0);
			int y = Setting.get("yaframe.y", 0);
			int width = Setting.get("yaframe.width", 400);
			int height = Setting.get("yaframe.height", 400);

			// check it
			x = (int) (x > d.getWidth() - 10 ? d.getWidth() - 100 : x);
			y = (int) (y > d.getHeight() - 10 ? d.getHeight() - 100 : y);

			setLocation(x, y);
			setPreferredSize(new Dimension(width, height));
			setExtendedState(Setting.get("yaframe.state", JFrame.MAXIMIZED_BOTH));
		}
	}

	/**
	 * Init the frame
	 */
	public static void init() {
		frame = new YaFrame();
	}

	/**
	 * Add a new tab
	 * 
	 * @param id
	 * @param e
	 */
	public static void registerTab(final TabEvent e) {
		frame.tabs.add(e);
	}

	/**
	 * Open new tab
	 * 
	 * @param id
	 */
	public static void open(final String id) {
		open(id, null);
	}

	/**
	 * Open new tab
	 * 
	 * @param id
	 * @param favorit
	 */
	public static void open(final String id, final BasisListElement fav) {
		final String uid = getTabID(id);

		// is open?
		if (isOpen(uid)) {
			frame.pane.setActiveDocument(uid);
			((YaTab) frame.pane.getDocument(uid).getComponent()).parseArguments(TabEvent.getArguments(id));
			return;
		}

		SystemHelper.runThread(new Run() {

			@Override
			public void go() {

				// open it
				try {
					final DocumentComponent doccom = YaTab.getLoadingDocument(uid);

					// build dummy tab
					get().getPane().openDocument(doccom);
					get().getPane().setActiveDocument(uid);

					try {
						// new SwingHelper(true) {
						//
						// @Override
						// public void run() {
						// search for the tab
						YaTab tab = TabEvent.getTab(id);

						// found something?
						if (tab == null) {
							YEx.info("Can not find tab for " + id, new IllegalArgumentException("Tab " + id + " is missing"));
							close(id);
							return;
						}
						Log.ger.info("Open Tab " + tab.getTitle() + " (" + id + ")");

						// inform
						ExtentionManagement.work("yaframe.openTab", JavaHelper.createHashStringObj("tab", tab));
						// frame.tabs.put(tab.getID(), tab);
						tab.getDocument(doccom, id);

						// overwrite with fav?
						if (fav != null) {
							doccom.setTitle(String.valueOf(fav.getTitle()));
							doccom.setIcon(IconCache.get(String.valueOf(fav.getIcon())));
							setTooltip(fav.getTitle(), fav.getIcon());
						} else {
							setTooltip(tab.getTitle(), tab.getIcon());
						}

						//
						// }
						// };
					} catch (Throwable t) {
						// close only while problem
						get().getPane().closeDocument(uid);
						throw t;
					}

					// ExtentionManagement.work("yaframe.opentab", id);
				} catch (final Throwable t) {
					YEx.info("Can not open tab " + id, t);

				}
				// y.close();

			}
		}, false);

	}

	/**
	 * Close it
	 * 
	 * @param id
	 */
	public static void close(String id) {
		String uid = getTabID(id);

		// is open, select it
		if (frame.pane.isDocumentOpened(uid)) {
			frame.pane.closeDocument(uid);
			// frame.tabs.remove(tab.getID());
		}
	}

	/**
	 * Exist this tab and is open
	 * 
	 * @param id
	 */
	public static boolean isOpen(String id) {
		return frame.pane.isDocumentOpened(getTabID(id));
	}

	/**
	 * Exist this tab?
	 * 
	 * @param id
	 */
	public static boolean existTab(String id) {
		return TabEvent.getTab(id) != null;
	}

	/**
	 * Get tab ID
	 * 
	 * @param id
	 */
	public static String getTabID(String id) {
		// split it
		if (id.contains("?")) {
			return id.substring(0, id.indexOf("?"));
		} else {
			return id;
		}
	}

	/**
	 * Open new tab
	 * 
	 * @param id
	 */
	// public static void reload(final String id) {
	// close(id);
	// open(id);
	// }

	/**
	 * Show the frame
	 */
	public void start() {

		new SwingHelper() {

			@Override
			public void run() {

				FavoritManagement.openLastTabs();

				getPane().invalidate();
				getPane().revalidate();

				// show Homepage

				// add yaams block
				// addNavi("yaams", "yaams", new JScrollPane(yaams.getTree()));
				//
				// // switch yaams to the start
				// final String[] o = oleft.getTabOrder();
				// final String s = o[0];
				// o[0] = o[o.length - 1];
				// o[o.length - 1] = s;
				// oleft.setTabOrder(o);
				//
				// loadPos();

				// load settings
				// Setting.editor.addSave(this);

				// build objects
				HashMap<String, Object> objects = new HashMap<String, Object>();
				// inform
				ExtentionManagement.work("yaframe.start.finish", objects);

				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					Log.ger.warn("Can not wait for finish loading", e);
				}

				setVisible(true);
			}
		};
	}

	/**
	 * Get the toolbar ro add/modi buttons
	 * 
	 * @return
	 */
	public YToolBar getToolbarLeft() {
		return toolbarLeft;
	}

	/**
	 * Get the toolbar ro add/modi buttons
	 * 
	 * @return
	 */
	public YToolBar getToolbarRight() {
		return toolbarRight;
	}

	/**
	 * Get the frame
	 * 
	 * @return
	 */
	public static YaFrame get() {
		return frame;
	}

	/**
	 * Is usable?
	 * 
	 * @return
	 */
	// public static boolean is() {
	// return !frame.isVisible();
	// }

	/**
	 * Close YAam
	 */
	public void closeYaams() {
		setVisible(false);
		dispose();
		frame = null;
	}

	/**
	 * @return the statusbar
	 */
	public StatusBar getStatusbar() {
		return statusbar;
	}

	/**
	 * @return the tooltip
	 */
	public ProgressStatusBarItem getTooltip() {
		return tooltip;
	}

	/**
	 * @return the pane
	 */
	public DocumentPane getPane() {
		return pane;
	}

	/**
	 * @return the tabs
	 */
	public ArrayList<TabEvent> getTabs() {
		return tabs;
	}

	/**
	 * Get the spec. open tab
	 * 
	 * @return the tabs
	 */
	public YaTab getOpenTab(String id) {
		DocumentComponent dc = frame.pane.getDocument(getTabID(id));

		// exist?
		if (dc == null) {
			return null;
		}

		return (YaTab) dc.getComponent();
	}

	/**
	 * Get all spec. open tab
	 * 
	 * @return the tabs
	 */
	public ArrayList<YaTab> getAllOpenTabs() {
		ArrayList<YaTab> tabs = new ArrayList<YaTab>();

		// run over all
		for (int i = 0, l = frame.pane.getDocumentCount(); i < l; i++) {
			tabs.add((YaTab) frame.pane.getDocumentAt(i).getComponent());
		}

		return tabs;
	}

	/**
	 * @return the tabs
	 */
	// public HashMap<String, YaTab> getTabs() {
	// return tabs;
	// }
}
