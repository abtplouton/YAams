package de.yaams.core.helper.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.yaams.core.helper.gui.icons.IconCache;

public class YHeader extends JPanel {
	private static final long serialVersionUID = -5538804683438095238L;
	private static transient BufferedImage i;
	private final JLabel text;

	public YHeader(String title) {
		this(title, "yrgss");
	}

	public YHeader(String title, String icon) {
		super(new BorderLayout());
		this.text = new JLabel(title);
		this.text.setIcon(IconCache.get(icon, 32));
		this.text.setForeground(Color.white);
		this.text.setFont(this.text.getFont().deriveFont(22.0F));
		this.text.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		add(this.text);

		if (i == null) {
			try {
				i = ImageIO.read(YHeader.class.getResourceAsStream("top_bg.gif"));
			} catch (Throwable t) {
				i = null;
				YEx.info("Can not load background image top_bg.gif (" + getClass().getResourceAsStream("top_bg.gif") + ")", t);
			}

		}

		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					YHeader.this.setVisible(false);
				}
			}
		});
	}

	public void setText(String title, String icon) {
		if (title != null) {
			this.text.setText(title);
		}

		if (icon != null) {
			this.text.setIcon(IconCache.get(icon, 32));
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (i != null) {
			g.drawImage(i, 0, 0, getWidth(), getHeight(), null);
		}
	}

	public JLabel getText() {
		return this.text;
	}
}