/*
 * Mappy Plugin for Tiled, (c) 2004-2006
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * Adam Turk <aturk@biggeruniverse.com> Bjorn Lindeijer <bjorn@lindeijer.nl>
 */

package de.yaams.extensions.basemap.tiled.plugins.mappy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Vector;

import de.yaams.extensions.basemap.tiled.core.Map;
import de.yaams.extensions.basemap.tiled.core.MapLayer;
import de.yaams.extensions.basemap.tiled.core.ObjectGroup;
import de.yaams.extensions.basemap.tiled.core.Tile;
import de.yaams.extensions.basemap.tiled.core.TileLayer;
import de.yaams.extensions.basemap.tiled.core.TileSet;
import de.yaams.maker.helper.Log;

/**
 * @version $Id$
 */
public class MappyMapReader {// implements MapReader {
	private LinkedList<Chunk> chunks;
	private Vector<BlkStr> blocks;
	private static final int BLKSTR_WIDTH = 32;
	private int twidth, theight;

	public static class BlkStr {
		public BlkStr() {}

		public long bg, fg0, fg1, fg2;
		public long user1, user2; // user long data
		public int user3, user4; // user short data
		public int user5, user6, user7; // user byte data
		public int bits; // collision and trigger bits
	}

	/**
	 * Loads a map from a file.
	 * 
	 * @param filename
	 *            the filename of the map file
	 */
	public Map readMap(String filename) throws Exception {
		return readMap(new FileInputStream(filename));
	}

	public Map readMap(InputStream in) throws Exception {
		Map ret = null;
		chunks = new LinkedList<Chunk>();
		blocks = new Vector<BlkStr>();
		byte[] hdr = new byte[4];

		in.read(hdr);
		long size = Util.readLongReverse(in);
		in.read(hdr);

		try {
			Chunk chunk = new Chunk(in);
			while (chunk.isGood()) {
				chunks.add(chunk);
				chunk = new Chunk(in);
			}
		} catch (IOException ioe) {
		}

		// now build a Tiled map...
		Chunk c = findChunk("MPHD");
		if (c != null) {
			ret = readMPHDChunk(c.getInputStream());
		} else {
			throw new IOException("No MPHD chunk found!");
		}

		c = findChunk("BODY");
		if (c != null) {
			readBODYChunk(ret, c.getInputStream());
		} else {
			throw new IOException("No BODY chunk found!");
		}

		return ret;
	}

	/**
	 * Loads a tileset from a file.
	 * 
	 * @param filename
	 *            the filename of the tileset file
	 */
	public TileSet readTileset(String filename) throws Exception {
		Log.ger.error("Tilesets aren't supported!");
		return null;
	}

	public TileSet readTileset(InputStream in) {
		Log.ger.error("Tilesets aren't supported!");
		return null;
	}

	/**
	 * @see de.yaams.extensions.basemap.tiled.io.PluggableMapIO#getFilter()
	 */
	public String getFilter() throws Exception {
		return "*.fmp";
	}

	public String getPluginPackage() {
		return "Mappy input plugin";
	}

	public String getDescription() {
		return "+---------------------------------------------+\n" + "|      An experimental reader for Mappy       |\n"
				+ "|                 FMAP v0.36                  |\n" + "|            (c) Adam Turk 2004               |\n"
				+ "|          aturk@biggeruniverse.com           |\n" + "|                                             |\n"
				+ "| Currently unsupported:                      |\n" + "|  * Animated tiles                           |\n"
				+ "|  * The ATHR block                           |\n" + "|  * Collision bits on BLKSTR structures      |\n"
				+ "|  * bitdepths other than 16bit               |\n" + "|  * object layers                            |\n"
				+ "+---------------------------------------------+";
	}

	public String getName() {
		return "Mappy Reader";
	}

	public boolean accept(File pathname) {
		try {
			String path = pathname.getCanonicalPath().toLowerCase();
			if (path.endsWith(".fmp")) {
				return true;
			}
		} catch (IOException e) {
		}
		return false;
	}

	private Chunk findChunk(String header) {
		Iterator<Chunk> itr = chunks.iterator();

		while (itr.hasNext()) {
			Chunk c = itr.next();
			if (c.equals(header)) {
				return c;
			}
		}
		return null;
	}

	private Map readMPHDChunk(InputStream in) throws IOException {
		Map ret = null;
		TileSet set = new TileSet();
		int major, minor;
		major = in.read();
		minor = in.read();
		in.skip(2); // skip lsb and reserved bytes - always msb
		ret = new Map(Util.readShort(in), Util.readShort(in));
		Properties retProps = ret.getProperties();
		ret.setOrientation(Map.MDO_ORTHO); // be sure to set the orientation!
		retProps.setProperty("(s)fmap reader", "Don't modify properties marked (s) unless you really know what you're doing.");
		retProps.setProperty("version", "" + major + "." + minor);
		in.skip(4); // reserved
		twidth = Util.readShort(in);
		theight = Util.readShort(in);
		ret.setTileWidth(twidth);
		ret.setTileHeight(theight);
		set.setName("Static tiles");
		ret.addTileset(set);
		int depth = Util.readShort(in);
		if (depth < 16) {
			throw new IOException("Tile bitdepths less than 16 are not supported!");
		}
		retProps.setProperty("(s)depth", String.valueOf(depth));
		in.skip(2);
		int numBlocks = Util.readShort(in);
		int numBlocksGfx = Util.readShort(in);
		Chunk c = findChunk("BKDT");
		if (c == null) {
			throw new IOException("No BKDT block found!");
		}
		MapLayer ml = new TileLayer(ret, ret.getWidth(), ret.getHeight());
		ml.setName("bg");
		ret.addLayer(ml);
		for (int i = 1; i < 7; i++) {
			// TODO: I believe this should be ObjectGroup
			ml = new ObjectGroup(ret, 0, 0);
			ml.setName("ObjectLayer " + i);
			ret.addLayer(ml);
		}
		ml = new TileLayer(ret, ret.getWidth(), ret.getHeight());
		ml.setName("fg 1");
		ret.addLayer(ml);
		ml = new TileLayer(ret, ret.getWidth(), ret.getHeight());
		ml.setName("fg 2");
		ret.addLayer(ml);
		ml = new TileLayer(ret, ret.getWidth(), ret.getHeight());
		ml.setName("fg 3");
		ret.addLayer(ml);

		readBKDTChunk(ret, c.getInputStream(), numBlocks);

		c = findChunk("BGFX");
		if (c != null) {
			readBGFXChunk(ret, c.getInputStream(), numBlocksGfx);
		} else {
			throw new IOException("No BGFX chunk found!");
		}

		Log.ger.debug(ret.toString());
		return ret;
	}

	private void readATHRChunk(Map m, InputStream in) {

	}

	private void readBKDTChunk(Map m, InputStream in, int num) throws IOException {
		Log.ger.debug("Reading " + num + " blocks...");
		for (int i = 0; i < num; i++) {
			blocks.add(readBLKSTR(in));
		}
	}

	/**
	 * Read a BODY chunk from a Mappy map. BODY chunks contain data for the 4
	 * main layers of the map.
	 * 
	 * @param m
	 * @param in
	 * @throws IOException
	 */
	private void readBODYChunk(Map m, InputStream in) throws IOException {
		TileSet set = m.getTilesets().get(0);
		TileLayer bg = (TileLayer) m.getLayer(0), fg0 = (TileLayer) m.getLayer(7), fg1 = (TileLayer) m.getLayer(8), fg2 = (TileLayer) m
				.getLayer(9);

		for (int i = 0; i < m.getHeight(); i++) {
			for (int j = 0; j < m.getWidth(); j++) {
				int block = (Util.readShort(in) & 0x00FF) / BLKSTR_WIDTH;
				// System.out.print("" + block);
				BlkStr blk = blocks.get(block);
				bg.setTileAt(j, i, set.getTile((int) blk.bg));
				fg0.setTileAt(j, i, set.getTile((int) blk.fg0));
				fg1.setTileAt(j, i, set.getTile((int) blk.fg1));
				fg2.setTileAt(j, i, set.getTile((int) blk.fg2));
			}
		}
	}

	/**
	 * BGFX blocks are synonymous with {@link Tile}s
	 * 
	 * @param m
	 *            The Map to add Tiles to
	 * @param in
	 * @param num
	 *            Number of Tiles to read
	 * @throws IOException
	 */
	private void readBGFXChunk(Map m, InputStream in, int num) throws IOException {
		TileSet set = m.getTilesets().get(0);
		set.addTile(new Tile());
		Util.readRawImage(in, twidth, theight); // skip the null-tile
		for (int i = 1; i < num; i++) {
			Tile t = new Tile();
			// t.setAppearance(image_id, 0);
			set.addTile(t);
		}
	}

	private BlkStr readBLKSTR(InputStream in) throws IOException {
		BlkStr ret = new BlkStr();
		long widthMod = twidth * theight * 512;
		ret.bg = Util.readLongReverse(in) / widthMod;
		ret.fg0 = Util.readLongReverse(in) / widthMod;
		ret.fg1 = Util.readLongReverse(in) / widthMod;
		ret.fg2 = Util.readLongReverse(in) / widthMod;

		ret.user1 = Util.readLongReverse(in);
		ret.user2 = Util.readLongReverse(in);
		ret.user3 = Util.readShort(in);
		ret.user4 = Util.readShort(in);
		ret.user5 = in.read();
		ret.user6 = in.read();
		ret.user7 = in.read();

		ret.bits = in.read();

		return ret;
	}
}
