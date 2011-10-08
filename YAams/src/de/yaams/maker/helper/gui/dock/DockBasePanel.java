/**
 * 
 */
package de.yaams.maker.helper.gui.dock;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXTitledPanel;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.icons.IconCache;

/**
 * @author Praktikant
 * 
 */
public class DockBasePanel extends JXTitledPanel {

	private static final long serialVersionUID = 1L;
	protected JPanel content;
	protected JPanel rightButtons;
	protected DockHolder dock;

	/**
	 * 
	 */
	public DockBasePanel() {
		content = new JPanel(new GridLayout(1, 1));
		setBorder(BorderFactory.createEmptyBorder());
	}

	/**
	 * Build the gui
	 * 
	 * @param title
	 * @param icon
	 * @param config
	 */
	public void buildGui(String title, String icon, boolean config) {
		// set basic
		setTitle(title);
		setContentContainer(content);
		this.setLeftDecoration(new JLabel(IconCache.get(icon)));

		// add button
		if (config) {
			setRightDecoration(YFactory.tb(I18N.t("{0} anpassen.", title), "opts", new AE() {

				@Override
				public void run() {
					callConfigDialog();

				}
			}));
		}
	}

	/**
	 * If the user press the config icon, this method will call, overwrite it,
	 * to set the config dialoag
	 */
	protected void callConfigDialog() {
		throw new IllegalArgumentException("Config option is not avaible.");
	}

	/**
	 * @return the dock
	 */
	public DockHolder getDock() {
		return dock;
	}

	/**
	 * @param dock
	 *            the dock to set
	 */
	public void setDock(DockHolder dock) {
		this.dock = dock;
	}

}
