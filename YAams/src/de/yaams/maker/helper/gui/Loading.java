/**
 * 
 */
package de.yaams.maker.helper.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.SwingHelper;
import de.yaams.maker.helper.gui.icons.IconCache;

/**
 * Show a scree for loading
 * 
 * @author abt
 * 
 */
public class Loading {

	protected JWindow loading;
	protected JProgressBar bar;
	protected JLabel icon, label;
	protected int act, max;

	/**
	 * show the loading bar
	 * 
	 * @param max
	 *            , max steps, if 0 indimet
	 * @param icon
	 *            , main icon, text will set be show()
	 * @param thread
	 *            , if given, add a stop button to break the thread
	 */
	public Loading(int max, String icon, final Thread thread) {
		this.max = max;

		// frame
		loading = new JWindow();

		// contain icon & all
		final JPanel info = new JPanel(new BorderLayout());
		// contain bar & label
		final JPanel content = new JPanel(new BorderLayout());

		// icon
		this.icon = new JLabel(IconCache.get(icon, 32));
		this.icon.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		// label
		label = new JLabel("YAams loading");
		label.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		// build
		info.add(this.icon, BorderLayout.WEST);
		content.add(label, BorderLayout.NORTH);

		bar = new JProgressBar(0, max);
		if (max == 0)
			bar.setIndeterminate(true);
		bar.setBorder(BorderFactory.createEmptyBorder(0, 2, 2, 2));
		bar.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		content.add(bar, BorderLayout.SOUTH);

		info.add(content, BorderLayout.CENTER);
		info.setBorder(BorderFactory.createEtchedBorder());
		// Integration.styleLauncher(info);

		// add stopping?
		if (thread != null) {
			info.add(YFactory.tb(I18N.t("Abbrechen"), "close", new AE() {

				@Override
				public void run() {
					thread.interrupt();

				}
			}), BorderLayout.EAST);
		}

		loading.setLayout(new GridLayout(1, 1));
		loading.add(info);
		// loading.pack();
		loading.setSize(300, 40);
		loading.setLocationRelativeTo(null);
		loading.setVisible(true);

	}

	/**
	 * Set text
	 * 
	 * @param message
	 */
	public void show(final String message) {
		new SwingHelper() {

			@Override
			public void run() {
				bar.setValue(act);
				act++;
				label.setText(message);
			}
		};
	}

	/**
	 * Set text
	 * 
	 * @param message
	 */
	public void show(final String message, final String icon) {
		new SwingHelper() {

			@Override
			public void run() {
				show(message);
				Loading.this.icon.setIcon(IconCache.get(icon, 32));

			}
		};
	}

	/**
	 * Close it
	 */
	public void exit() {
		new SwingHelper() {

			@Override
			public void run() {
				loading.setVisible(false);
				loading.dispose();
				loading = null;

			}
		};
	}

	/**
	 * @param max
	 *            the max to set
	 */
	public void setMax(int max) {
		this.max = max;
		bar.setIndeterminate((max == 0));
		bar.setMaximum(max);
	}
}
