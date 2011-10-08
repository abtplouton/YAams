/**
 * 
 */
package de.yaams.extensions.ress;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JPanel;

import org.jdesktop.swingx.JXImageView;

import de.yaams.maker.helper.gui.YDialog;
import de.yaams.maker.helper.gui.icons.IconCache;
import de.yaams.maker.programm.ress.FormRessElement;
import de.yaams.maker.programm.ress.RessRess;

/**
 * @author abby
 * 
 */
public class PreviewIcon extends JPanel {

	private static final long serialVersionUID = -4252472758671453388L;

	protected transient FormRessElement ress;
	protected JXImageView view;
	protected File file;

	/**
	 * @param layout
	 */
	public PreviewIcon(final FormRessElement ress) {
		super(new GridLayout(1, 1));

		this.ress = ress;
		view = new JXImageView();
		view.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				PreviewPanel preview = new PreviewPanel();
				preview.setFile(file);
				YDialog.show(PreviewIcon.this.ress.getContentAsString(), "ress", preview, false);

			}
		});

		setFile(ress.getSelected());
		add(view);
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(File file) {
		this.file = file;
		updatePreview();
	}

	/**
	 * Set update
	 */
	protected void updatePreview() {
		// has file?

		// is graphic?
		// look in the project
		if (file != null && RessRess.endWithExtention(file, new String[] { ".png", ".jpg", ".jpeg", ".jpe", ".gif" })) {
			BufferedImage bi = RessRess.getGraphic(ress.getProject(), ress.getFolder(), ress.getSelected().getName());
			view.setImage(bi);
			view.setScale(bi.getHeight() < 32 ? 1 : 32d / bi.getHeight());
			view.setPreferredSize(new Dimension((int) (bi.getWidth() * view.getScale() + 1), bi.getHeight() < 32 ? bi.getHeight() : 32));
			view.invalidate();
			view.revalidate();
			return;
		}

		// is audio?
		if (file != null && RessRess.endWithExtention(file, new String[] { ".mp3", ".ogg", ".wma", ".mid", ".midi" })) {
			view.setImage(IconCache.getImage("audio"));
			view.setScale(1);
			view.setPreferredSize(new Dimension(16, 16));
			return;
		}

		// other?
		view.setImage(IconCache.getImage("dummy"));
		view.setScale(1);
		view.setPreferredSize(new Dimension(16, 16));

	}

	/**
	 * Update it
	 */
	@Override
	public void updateUI() {
		super.updateUI();
		if (ress != null && file != ress.getSelected()) {
			setFile(ress.getSelected());
		}
	}
}
