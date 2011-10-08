/**
 * 
 */
package de.yaams.extensions.ress;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXImageView;

import de.yaams.extensions.ress.audioplayer.Player;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.NetHelper;
import de.yaams.maker.helper.SystemHelper;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.IZoom;
import de.yaams.maker.helper.gui.YDialog;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.YProgressWindowRepeat;
import de.yaams.maker.helper.gui.YToolBar;
import de.yaams.maker.helper.gui.form.FormTextField;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;
import de.yaams.maker.programm.YAamsCore;
import de.yaams.maker.programm.ress.RessRess;

/**
 * @author abt
 * 
 */
public class PreviewPanel extends JPanel implements IZoom {
	private static final long serialVersionUID = -7065667324497904070L;

	private File file;
	private final JXImageView image;
	private final JPanel imagepanel;
	private final YToolBar bar;
	private transient Player player;

	/**
	 * Create a new PreviewPanel
	 */
	public PreviewPanel() {
		setLayout(new GridLayout(1, 1));
		imagepanel = new JPanel(new BorderLayout());
		setPreferredSize(new Dimension(320, 160));
		image = new JXImageView();
		// set content
		imagepanel.setBorder(BorderFactory.createEmptyBorder());
		imagepanel.add(new JScrollPane(image), BorderLayout.CENTER);

		// set toolbar
		bar = YFactory.installZoomlevel(new YToolBar(), this);
		bar.addSeparator();

		bar.add(YFactory.tb(I18N.t("Info about the file"), "info", new AE() {

			@Override
			public void run() {
				info();

			}
		}));

		bar.addSeparator();
		// add special toolbar buttons
		bar.add(YFactory.tb(I18N.t("View the file, with the default system viewer"), "view", new AE() {

			@Override
			public void run() {
				if (file != null) {
					SystemHelper.viewFile(file);
				}

			}
		}));

		// add toolbar buttons
		bar.add(YFactory.tb(I18N.t("Datei mit dem Systemeditor editieren"), "edit", new AE() {

			@Override
			public void run() {
				if (file != null) {
					SystemHelper.editFile(file);
				}

			}
		}));

		// add toolbar buttons
		bar.add(YFactory.tb(I18N.t("Edit the Graphic with the Image Editor of JH Labs"), "imageeditor", new AE() {

			@Override
			public void run() {
				if (file == null) {
					return;
				}

				// imageeditor exist?
				final File f = new File(YAamsCore.programPath, "imageeditor.jar");

				YProgressWindowRepeat y = new YProgressWindowRepeat("Starting " + f.getAbsolutePath(), "imageeditor");
				if (!f.exists()) {
					// dl it
					NetHelper.downloadFile(f, "http://www.yaams.de/file/plugins/ImageEditor.jar");
				}

				// check file
				if (RessRess.endWithExtention(file, new String[] { "ie", "jpg", "jpeg", "jpe", "gif", "png", "psd", "bmp", "pict", "tga",
						"ras", "pcx" })
						|| !RessRess.endWithExtention(file, new String[] { "ie", "jpg", "jpeg", "jpe", "gif", "png", "psd", "bmp", "pict",
								"tga", "ras", "pcx" })
						&& YDialog.askUser(I18N.t("Bild {0} wird nicht unterstützt.", file.getName()), "ress.imageeditor",
								"imageeditor_warn",
								I18N.t("Wahrscheinlich kann ImageEditor die Datei nicht öffnen. Soll Sie dennoch geöffnet werden?"),
								I18N.t("Trotzdem öffnen"), I18N.CANCEL, "imageeditor_ok", "cancel")) {
					// run it
					SystemHelper.runExternal(
							new String[] { "java", "-jar", "-Xms128m", "-Xmx512M", f.getAbsolutePath(), file.getAbsolutePath() }, true);
				}

				y.close();
			}
		}));

		imagepanel.add(bar, BorderLayout.NORTH);
	}

	/**
	 * Show the tech. informations
	 */
	protected void info() {
		// can show?
		if (file == null || image == null) {
			return;
		}

		// build form
		final FormBuilder f = new FormBuilder("preview.graphic");
		f.addHeader("basic", new FormHeader("", null));
		f.addElement("basic.path", new FormTextField(I18N.t("Pfad"), file.getAbsolutePath()));
		f.addElement(
				"basic.size",
				new FormTextField(I18N.t("Größe"), I18N.t("{0}x{1}x{2}", image.getWidth(), image.getHeight(), image.getImage()
						.getAccelerationPriority())));

		YDialog.showForm(file.getName(), "graphic_info", f);
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(final File file) {
		this.file = file;
		removeAll();

		// no file?
		if (file == null) {
			return;
		}
		// witch file?
		final String f = file.getAbsolutePath();
		if (f.endsWith(".wav") || f.endsWith(".midi") || f.endsWith(".mid") || f.endsWith(".mp3") || f.endsWith(".ogg")) {
			// build panel
			JPanel p = new JPanel(new BorderLayout());
			final JLabel e = new JLabel();
			final JButton b = YFactory.b(I18N.t("Play {0}", file.getName()), "audio", null);
			b.setEnabled(true);
			b.addActionListener(new AE() {

				@Override
				public void run() {
					// play?
					if (player == null) {
						e.setText("");
						// play
						player = new Player(file);
						player.setErrorCode(new AE() {

							@Override
							public void run() {
								// set exception
								if (player != null && player.getEx() != null) {
									e.setText(player.getEx().toString());
								}

							}
						});
						player.setEndCode(new AE() {

							@Override
							public void run() {
								// stop
								player.stop();
								player = null;

								// set button
								b.setText(I18N.t("Play {0}", file.getName()));

							}
						});
						player.start();

						// set button
						b.setText(I18N.t("Stop {0}", file.getName()));

					} else {

						// stop
						player.stop();
						player = null;

						// set button
						b.setText(I18N.t("Play {0}", file.getName()));

					}

				}
			});

			p.add(b, BorderLayout.NORTH);
			p.add(e, BorderLayout.CENTER);

			add(p);
			// add(SoundPlayer.getSoundPlayer(file));
			return;
		}

		// try to load
		try {
			// no file?
			// if (file == null) {
			// image.setImage(new BufferedImage(1, 1,
			// BufferedImage.TYPE_4BYTE_ABGR));
			// return;
			// }
			double zoom = image.getScale();

			// load image
			image.setImage(file);
			image.setScale(zoom);

			add(imagepanel);
			image.invalidate();
			image.revalidate();
			image.repaint();
			invalidate();
			revalidate();
			repaint();

		} catch (final Throwable t) {
			add(new JLabel(file.getName() + " is not supported: " + t));
			// YException.warn("Can not load the image " + file, t);

		}

	}

	@Override
	public double getZoomLevel() {
		return image.getScale();
	}

	@Override
	public void setZoomLevel(double scale) {
		image.setScale(scale);
	}

	@Override
	public Dimension getViewDimension() {
		return new Dimension(image.getImage().getWidth(null), image.getImage().getHeight(null));
	}

	@Override
	public Dimension getObjectDimension() {
		return imagepanel.getSize();
	}
}
