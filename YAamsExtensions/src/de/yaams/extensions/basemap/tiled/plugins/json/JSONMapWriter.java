/*
 * Tiled Map Editor, (c) 2004-2006
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * JSON map writer contributed by Nader Akhres <nader.akhres@laposte.net>
 */

package de.yaams.extensions.basemap.tiled.plugins.json;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import de.yaams.extensions.basemap.org.json.XML;
import de.yaams.extensions.basemap.tiled.core.Map;
import de.yaams.extensions.basemap.tiled.core.TileSet;
import de.yaams.extensions.basemap.tiled.io.MapReader;
import de.yaams.extensions.basemap.tiled.io.MapWriter;
import de.yaams.extensions.basemap.tiled.io.xml.XMLMapWriter;

/**
 * @version $Id$
 */
public class JSONMapWriter extends XMLMapWriter implements MapWriter {
	public JSONMapWriter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * this method write a map OR a tileset in json format
	 * 
	 * @param map
	 * @param set
	 * @param filename
	 * @throws Exception
	 */
	private void writeMapOrTileset(Map map, TileSet set, File filename) throws Exception {

		// Create a temporary file
		File tempFile = File.createTempFile("tiled_json_", ".tmx");

		// Write in this temp file an xml content (tmx format)
		if (map != null) {
			super.writeMap(map, tempFile);
		} else {
			if (set != null) {
				super.writeTileset(set, tempFile); // write in
													// this
													// temp
													// file
													// an
													// xml
													// content
													// (tmx
													// format)
			} else {
				return;
			}
		}

		tempFile = new File(tempFile.getAbsolutePath()); // TODO useful?

		// Now read this temp file and get the tmx content
		int fileSize = 100000; // TODO Replace here by true file size //(int)
								// tempFile.length(); //100000;
		char[] TMXContent = new char[fileSize];

		FileReader fileR = new FileReader(tempFile.getAbsolutePath());
		fileR.read(TMXContent);
		fileR.close();

		// Avoid retrieving xml header like <?xml version=\"1.0\"?>, JSON parser
		// doesn't like it!
		String TMXContentString = new String(TMXContent).trim().replaceFirst("\\<\\?.*\\?\\>", "");

		System.out.println("temp file path=" + tempFile.getAbsolutePath());
		System.out.println("filesize=" + fileSize);
		System.out.println("content=" + TMXContentString);

		// Delete useless temp file
		tempFile.delete();

		// Turn it into JSON format string
		String JSONContent = XML.toJSONObject(TMXContentString).toString(2);

		System.err.println("json content=" + JSONContent);

		// Write in destination file
		FileWriter fileW = new FileWriter(filename);
		fileW.write(JSONContent);
		fileW.flush();
		fileW.close();
	}

	@Override
	public void writeMap(Map map, File filename) throws Exception {
		writeMapOrTileset(map, null, filename);
	}

	@Override
	public void writeTileset(TileSet set, File filename) throws Exception {
		writeMapOrTileset(null, set, filename);
	}

	@Override
	public void writeMap(Map map, OutputStream out) throws Exception {
		super.writeMap(map, out); // not implemented because can't turn into
									// JSON an OutputStream
	}

	@Override
	public void writeTileset(TileSet set, OutputStream out) throws Exception {
		super.writeTileset(set, out); // not implemented because can't turn into
										// JSON an OutputStream
	}

	/**
	 * @see MapReader#getFilter()
	 */
	@Override
	public String getFilter() throws Exception {
		return "*.js,*.js.gz";
	}

	@Override
	public String getPluginPackage() {
		return "Tiled JSON writer";
	}

	@Override
	public String getDescription() {
		return "The Tiled JSON format (TMX converted) writer\n" + "\n" + "Nader AKHRES" + "\n for \n" + "Tiled Map Editor, (c) 2005\n"
				+ "Adam Turk\n" + "Bjorn Lindeijer";
	}

	@Override
	public String getName() {
		return "Tiled JSON (TMX converted) map writer";
	}

	@Override
	public boolean accept(File pathname) {
		try {
			String path = pathname.getCanonicalPath();
			if (path.endsWith(".js") || path.endsWith(".js.gz")) {
				return true;
			}
		} catch (IOException e) {
		}
		return false;
	}
}
