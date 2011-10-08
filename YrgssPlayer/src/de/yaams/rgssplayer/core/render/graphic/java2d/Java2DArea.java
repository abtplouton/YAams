package de.yaams.rgssplayer.core.render.graphic.java2d;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import de.yaams.core.YrgssCore;
import de.yaams.rgssplayer.core.java.BaseGraphicElement;
import de.yaams.rgssplayer.core.java.Sprite;
import de.yaams.rgssplayer.core.java.YGraphics;
import de.yaams.rgssplayer.core.java.Yrgss;
import de.yaams.rgssplayer.core.render.audio.core.SoundRender;
import de.yaams.rgssplayer.core.render.graphic.core.Render;

public class Java2DArea extends JComponent {
	private static final long serialVersionUID = 184900923728055704L;
	protected BufferedImage transtionIn;
	protected BufferedImage transtionOut;
	protected int transtionTime;
	protected int transtionStep;

	public Java2DArea(int width, int height) {
		setPreferredSize(new Dimension(width, height));
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.black);
		g2.fillRect(0, 0, getWidth(), getHeight());

		if (Render.getWindow() == null || Render.getWindow().getSprites().size() == 0) {
			g2.setColor(Color.white);
			g2.drawString(YrgssCore.TITLE + " " + " loading with " + Render.er().getName() + " and " + SoundRender.er().getName(), 4, 14);

			g2.drawString(Yrgss.game.getName() + " from " + Yrgss.game.getPath(), 4, 34);
			return;
		}

		if (this.transtionIn != null) {
			g2.drawImage(this.transtionIn, 0, 0, null);

			if (this.transtionOut == null) {
				return;
			}

			Composite c = g2.getComposite();
			g2.setComposite(AlphaComposite.getInstance(3, this.transtionStep / this.transtionTime));

			g2.drawImage(this.transtionOut, 0, 0, null);

			this.transtionStep += 1;

			if (this.transtionStep == this.transtionTime) {
				this.transtionIn = null;
				this.transtionOut = null;
			}

			g2.setComposite(c);

			return;
		}

		drawSprites(g2);
	}

	public void drawSprites(Graphics g) {
		int i = 0;
		while (i < Render.getWindow().getSprites().size()) {
			BaseGraphicElement s = Render.getWindow().getSprites().get(i);

			((Java2DSprite) ((Sprite) s).getSprite()).renderSprite((Graphics2D) g);

			i++;
		}
	}

	public void transition(BufferedImage start, BufferedImage end, int time) {
		this.transtionIn = start;
		this.transtionOut = end;
		this.transtionTime = time;
		this.transtionStep = 0;

		if (time > 0) {
			YGraphics.wait(time);
		}
	}

	public BufferedImage getTranstionIn() {
		return this.transtionIn;
	}

	public BufferedImage getTranstionOut() {
		return this.transtionOut;
	}
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.render.graphic.java2d.Java2DArea JD-Core Version:
 * 0.6.0
 */