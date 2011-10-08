/*    */package de.yaams.rgssplayer.core.render.graphic.java2d.tone;

/*    */
/*    */import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;

/*    */
/*    */public class AddComposite
/*    */implements Composite
/*    */{
	/*    */@Override
	public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints arg2)
	/*    */{
		/* 19 */return new AddCompositeContext(srcColorModel, dstColorModel);
		/*    */}
	/*    */}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.render.graphic.java2d.tone.AddComposite JD-Core
 * Version: 0.6.0
 */