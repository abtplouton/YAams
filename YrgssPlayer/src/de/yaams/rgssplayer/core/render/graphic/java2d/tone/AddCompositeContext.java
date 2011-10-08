/*    */package de.yaams.rgssplayer.core.render.graphic.java2d.tone;

/*    */
/*    */import java.awt.CompositeContext;
import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/*    */
/*    */class AddCompositeContext
/*    */implements CompositeContext
/*    */{
	/*    */ColorModel srcColorModel;
	/*    */ColorModel dstColorModel;
	/* 21 */int ALPHA = -16777216;
	/* 22 */int MASK7Bit = 16711423;

	/*    */
	/*    */public AddCompositeContext(ColorModel srcColorModel, ColorModel dstColorModel)
	/*    */{
		/* 26 */this.srcColorModel = srcColorModel;
		/* 27 */this.dstColorModel = dstColorModel;
		/*    */}

	/*    */
	/*    */int add(int color1, int color2)
	/*    */{
		/* 32 */int pixel = (color1 & this.MASK7Bit) + (color2 & this.MASK7Bit);
		/* 33 */int overflow = pixel & 0x1010100;
		/* 34 */overflow -= (overflow >> 8);
		/* 35 */return this.ALPHA | overflow | pixel;
		/*    */}

	/*    */
	/*    */@Override
	public void compose(Raster src, Raster dstIn, WritableRaster dstOut)
	/*    */{
		/* 40 */Rectangle srcRect = src.getBounds();
		/* 41 */Rectangle dstInRect = dstIn.getBounds();
		/* 42 */Rectangle dstOutRect = dstOut.getBounds();
		/* 43 */int x = 0;
		int y = 0;
		/* 44 */int w = Math.min(Math.min(srcRect.width, dstOutRect.width),
		/* 45 */dstInRect.width);
		/* 46 */int h = Math.min(Math.min(srcRect.height, dstOutRect.height),
		/* 47 */dstInRect.height);
		/* 48 */Object srcPix = null;
		Object dstPix = null;
		/* 49 */for (y = 0; y < h; y++)
			/* 50 */for (x = 0; x < w; x++) {
				/* 51 */srcPix = src.getDataElements(x + srcRect.x, y + srcRect.y,
				/* 52 */srcPix);
				/* 53 */dstPix = dstIn.getDataElements(x + dstInRect.x,
				/* 54 */y + dstInRect.y, dstPix);
				/* 55 */int sp = this.srcColorModel.getRGB(srcPix);
				/* 56 */int dp = this.dstColorModel.getRGB(dstPix);
				/* 57 */int rp = add(sp, dp);
				/* 58 */dstOut.setDataElements(x + dstOutRect.x, y + dstOutRect.y,
				/* 59 */this.dstColorModel.getDataElements(rp, null));
				/*    */}
		/*    */}

	/*    */
	/*    */@Override
	public void dispose()
	/*    */{
		/*    */}
	/*    */}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.core.render.graphic.java2d.tone.AddCompositeContext
 * JD-Core Version: 0.6.0
 */