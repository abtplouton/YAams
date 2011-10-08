/**
 * yaams - de.yaams.core.utils FileHelper.java
 */
package de.yaams.maker.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.channels.ByteChannel;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Level;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;

import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.gui.YMessagesDialog;
import de.yaams.maker.helper.gui.YProgressWindowRepeat;

/**
 * @author Administrator
 */
public class FileHelper {
	/**
	 * Delete all files in the path, if not exist, do nothing
	 * 
	 * @author 
	 *         http://fbim.fh-regensburg.de/~saj39122/pgj/javainsel/javainsel_13_001
	 *         .htm
	 * @param path
	 */
	public static void deleteTree(final File path) {

		// can delete?
		if (path == null || !path.exists()) {
			return;
		}

		// delete
		for (final File file : path.listFiles()) {
			if (file.isDirectory()) {
				deleteTree(file);
			} else {
				deleteFile(file);
			}
		}
		deleteFile(path);
	}

	/**
	 * Copy all files in the path to another path
	 * 
	 * @param infopath
	 */
	public static void copyTree(final File source, final File dest) {
		YProgressWindowRepeat y = new YProgressWindowRepeat(I18N.t("Kopiere {0} nach {1}", source, dest), "folder_copy");
		for (final File file : source.listFiles()) {
			final File d = new File(dest + File.separator + file.getName());
			y.setNote(d.getName());
			if (file.isDirectory()) {
				mkdirs(d);
				copyTree(file, d);
			} else {
				copy(file, d);
			}
		}
		y.close();
	}

	/**
	 * Copy files
	 * 
	 * @author http://www.tutorials.de/forum/java/328830-schnell-grosse-dateien-
	 *         kopieren-mit-java-nio.html
	 * @param source
	 * @param destination
	 * @return true, all fine, false otherwise
	 */
	public static boolean copy(final File source, final File destination) {
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		try {
			fileInputStream = new FileInputStream(source);
			fileOutputStream = new FileOutputStream(destination);

			final FileChannel inputChannel = fileInputStream.getChannel();
			final FileChannel outputChannel = fileOutputStream.getChannel();

			transfer(inputChannel, outputChannel, source.length(), 1024 * 1024 * 32, true);

			fileInputStream.close();
			fileOutputStream.close();

			return destination.exists();
		} catch (final Throwable t) {
			YEx.warn("Error while copy " + source + " to " + destination, t);
			deleteFile(destination);
			return false;
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
				}
			}
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * Copy helper
	 * 
	 * @param fileChannel
	 * @param byteChannel
	 * @param lengthInBytes
	 * @param chunckSizeInBytes
	 * @param verbose
	 * @param fromFile
	 * @throws IOException
	 */
	private static void transfer(final FileChannel fileChannel, final ByteChannel byteChannel, final long lengthInBytes,
			final long chunckSizeInBytes, final boolean fromFile) throws IOException {

		long overallBytesTransfered = 0L;
		while (overallBytesTransfered < lengthInBytes) {

			long bytesTransfered = 0L;

			if (fromFile) {
				bytesTransfered = fileChannel.transferTo(0, Math.min(chunckSizeInBytes, lengthInBytes - overallBytesTransfered),
						byteChannel);
			} else {
				bytesTransfered = fileChannel.transferFrom(byteChannel, overallBytesTransfered,
						Math.min(chunckSizeInBytes, lengthInBytes - overallBytesTransfered));
			}

			overallBytesTransfered += bytesTransfered;

		}

	}

	/**
	 * Extract File from the jar
	 * 
	 * @author 
	 *         http://forum.chip.de/java-delphi-pascal/datei-jar-festplatte-kopieren
	 *         -774406.html
	 * @param InputStream
	 *            in
	 * @param File
	 *            destDir
	 * @return true, yes false otherwise
	 */
	public static boolean extractFromJar(InputStream in, final File destDir) {
		BufferedInputStream bufIn = new BufferedInputStream(in);
		BufferedOutputStream bufOut = null;

		try {
			bufOut = new BufferedOutputStream(new FileOutputStream(destDir));

			byte[] inByte = new byte[4096];
			int count = -1;
			while ((count = bufIn.read(inByte)) != -1) {
				bufOut.write(inByte, 0, count);
			}

			bufOut.close();
			bufIn.close();
			return true;
		} catch (Throwable t) {
			YEx.warn("Can not extract " + in + " to " + destDir, t);
			return false;
		}

	}

	/**
	 * Extract Archive
	 * 
	 * @author http://www.tutorials.de/forum/java/215919-zip-entpacken.html
	 * @param archive
	 * @param destDir
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static boolean extractArchive(final File archive, final File destDir) {
		boolean erg = true;
		try {
			return extractArchiveThrow(archive, destDir);
		} catch (final Throwable t) {
			YEx.warn("Error while extract archive from " + archive + " to " + destDir, t);
			erg = false;
		}

		return erg;
	}

	/**
	 * Extract Archive
	 * 
	 * @author http://www.tutorials.de/forum/java/215919-zip-entpacken.html
	 * @param archive
	 * @param destDir
	 * @throws IOException
	 * @throws ZipException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static boolean extractArchiveThrow(final File archive, final File destDir) throws ZipException, IOException {
		boolean erg = true;
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		// check paramter
		Validate.notNull(archive);
		Validate.notNull(destDir);

		// check paramter
		Validate.isTrue(archive.canRead(), "Archive " + archive + " not found.");
		Validate.isTrue(destDir.canWrite(), "Folder " + destDir + " isn't writeable.");

		// do it
		final ZipFile zipFile = new ZipFile(archive);
		final Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zipFile.entries();

		// progress
		YProgressWindowRepeat y = new YProgressWindowRepeat(I18N.t("Entpacke {0} nach", archive.getName()), "archive_folder");

		final byte[] buffer = new byte[16384];
		int len;
		while (entries.hasMoreElements()) {
			// update progress

			final ZipEntry entry = entries.nextElement();

			final String entryFileName = entry.getName();

			final File dir = buildDirectoryHierarchyFor(entryFileName, destDir);
			FileHelper.mkdirs(dir);

			y.setNote(dir.getAbsolutePath());

			if (!entry.isDirectory()) {
				final BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(destDir, entryFileName)));

				final BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));

				while ((len = bis.read(buffer)) > 0) {
					bos.write(buffer, 0, len);
				}

				bos.flush();
				bos.close();
				bis.close();
			}
		}

		y.close();

		return erg;
	}

	/**
	 * Helpermethod
	 * 
	 * @param entryName
	 * @param destDir
	 * @return
	 */
	private static File buildDirectoryHierarchyFor(final String entryName, final File destDir) {
		final int lastIndex = entryName.lastIndexOf('/');
		final String internalPathToEntry = entryName.substring(0, lastIndex + 1);
		return new File(destDir, internalPathToEntry);
	}

	/**
	 * Read a textfile and return the content without linebreak
	 */
	public static String readFile(final File file) {
		StringBuilder sb = new StringBuilder();

		// exist file?
		if (!checkPath("Try to read it anywhere?", file, false, false)) {
			YEx.warn("IO", new FileNotFoundException("Can not interpret " + file + ", because the file don't exist."));
			return "";
		}

		// read the file
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file.getAbsolutePath()));

			String zeile = "";
			// read the lines and interpret it
			while ((zeile = br.readLine()) != null) {
				sb.append(zeile);
			}
		} catch (final Throwable t) {
			YEx.warn("Can not interpret file " + file, t);
			return sb.toString();
		} finally {
			try {
				br.close();
			} catch (final Throwable t) {
			}
		}

		return sb.toString();
	}

	/**
	 * Try to delete file
	 * 
	 * @param file
	 */
	public static void deleteFile(File file) {
		// check access
		if (checkPath(I18N.t("Can not delete {0}", file), file, false, true)) {
			// delete it
			if (!file.delete()) {
				file.deleteOnExit();
			}
		}
	}

	/**
	 * Read a textfile to a string with line break
	 * 
	 * @param infopath
	 * @return the hashmap
	 */
	public static String readFileToString(final File file) {
		StringBuilder erg = new StringBuilder();

		// exist file?
		if (!checkPath("Try to read it anywhere?", file, false, false)) {
			YEx.info("IO", new FileNotFoundException("Can not interpret " + file + ", because the file don't exist."));
			return erg.toString();
		}

		// read the file
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file.getAbsolutePath()));

			String zeile = "";
			// read the lines and interpret it
			while ((zeile = br.readLine()) != null) {
				erg.append(zeile);
				erg.append(SystemUtils.LINE_SEPARATOR);
			}
		} catch (final Throwable t) {
			YEx.warn("Can not read file " + file, t);
			return erg.toString();
		} finally {
			try {
				br.close();
			} catch (final Throwable t) {
			}
		}

		return erg.toString();
	}

	/**
	 * Read a textfile and all lines with = will be splitted and put in the
	 * hashmap
	 * 
	 * @param infopath
	 * @return the hashmap
	 */
	public static HashMap<String, String> interpretFile(final File file, final boolean warning) {
		final HashMap<String, String> h = new HashMap<String, String>();

		// exist file?
		if (warning && !checkPath("Try to interpret it anywhere?", file, false, false)) {
			YEx.info("IO", new FileNotFoundException("Can not interpret " + file + ", because the file don't exist."));
			return h;
		}

		// read the file
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file.getAbsolutePath()));

			String zeile = "";
			// read the lines and interpret it
			while ((zeile = br.readLine()) != null) {
				final String s[] = zeile.split("=", 2);
				// add the string?
				if (s.length == 2) {
					h.put(s[0], s[1]);
				}
			}
		} catch (final Throwable t) {
			if (warning) {
				YEx.warn("Can not interpret file " + file, t);
			}
			return h;
		} finally {
			try {
				br.close();
			} catch (final Throwable t) {
			}
		}

		return h;
	}

	/**
	 * Return the md5 checksum for a file
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String checkSum(final String path) throws Exception {
		final MessageDigest digest = MessageDigest.getInstance("MD5");
		final File f = new File(path);
		final InputStream is = new FileInputStream(f);
		final byte[] buffer = new byte[8192];
		byte[] md5sum = null;
		int read = 0;
		try {
			while ((read = is.read(buffer)) > 0) {
				digest.update(buffer, 0, read);
			}
			md5sum = digest.digest();
		} catch (final IOException e) {
			throw new RuntimeException("Unable to process file for MD5", e);
		} finally {
			try {
				is.close();
			} catch (final IOException e) {
			}
		}
		// add fix?
		String erg = new BigInteger(1, md5sum).toString(16);
		if (erg.length() == 31) {
			erg = "0" + erg;
		}
		return erg;
	}

	/**
	 * load a class in from his own file, helpermethod
	 */
	public static Object load(final String name) {
		Object o = null;
		// load options
		if (new File(name).exists()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(name);
				final ObjectInputStream ois = new ObjectInputStream(fis);
				o = ois.readObject();
			} catch (final Throwable t) {
				YEx.warn("Can not load " + name, t);
			} finally {
				try {
					fis.close();
				} catch (final Exception e) {
				}
			}
		}
		return o;
	}

	/**
	 * save the class in his own file, helpermethod
	 * 
	 * @param name
	 *            , filepath
	 * @param o
	 *            , to save object
	 */
	public static void save(final String name, final Object o) {
		// save options
		OutputStream fos = null;
		try {
			fos = new FileOutputStream(name);
			final ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(o);
		} catch (final Throwable t) {
			YEx.info("Can not save " + name, t);
		} finally {
			try {
				fos.close();
			} catch (final Exception e) {
			}
		}
	}

	/**
	 * Create the folder, if possible
	 * 
	 * @param path
	 */
	public static void mkdirs(final File f) {
		try {
			// exist?
			if (!f.isDirectory() || !f.canRead()) {
				// can create folder?
				if (!f.mkdirs()) {
					// not possible
					throw new IllegalArgumentException("File.mkdirs");
				}
			}
		} catch (final Throwable t) {
			YEx.warn("Can not create folders " + f, t);
		}
	}

	/**
	 * Pack a dictornay in a zip archiv, recursive
	 * 
	 * @author http://www.wsoftware.de/practices/file-io.html
	 * @param zip
	 * @param goal
	 */
	public static void packZip(final File dir, final File goal) {
		YProgressWindowRepeat y = new YProgressWindowRepeat(I18N.t("Packe {0} nach {1}", dir, goal.getName()), "archive_setup");
		try {
			int prefixLength;
			ZipOutputStream zipOut;
			prefixLength = dir.getAbsolutePath().length() + 1;
			zipOut = new ZipOutputStream(new FileOutputStream(goal.getAbsolutePath()));
			try {
				createZipFrom(y, zipOut, prefixLength, dir);
			} finally {
				zipOut.close();
			}
		} catch (final Throwable t) {
			YEx.warn("Can not pack zip from " + dir + " to " + goal, t);
		}
		y.close();
	}

	/**
	 * Helpermethod to pack archiv
	 * 
	 * @param zipOut
	 * @param prefixLength
	 * @param dir
	 * @throws Throwable
	 */
	private static void createZipFrom(YProgressWindowRepeat y, final ZipOutputStream zipOut, final int prefixLength, final File dir)
			throws Throwable {
		if (dir.exists() && dir.canRead() && dir.isDirectory()) {
			final byte[] ioBuffer = new byte[4096];
			final File[] files = dir.listFiles();
			if (files != null) {
				for (final File file : files) {
					y.setNote(file.getName());
					if (file.isDirectory()) {
						createZipFrom(y, zipOut, prefixLength, file);
					} else {
						final String filePath = file.getPath().replace('\\', '/');
						final FileInputStream in = new FileInputStream(filePath);
						try {
							zipOut.putNextEntry(new ZipEntry(filePath.substring(prefixLength)));
							int bytesRead;
							while ((bytesRead = in.read(ioBuffer)) > 0) {
								zipOut.write(ioBuffer, 0, bytesRead);
							}
						} finally {
							zipOut.closeEntry();
							in.close();
						}
					}
				}
			}
		}
	}

	/**
	 * Exist the special file? Relative to the main jar
	 * 
	 * @param name
	 * @return the file or null
	 */
	public static File fileRelativeExist(final String name) {
		File f = new File(name);

		// is in the same path the game?
		if (f.exists()) {
			return f;
		}

		// is relative to the jar?
		f = new File(new File(FileHelper.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParent() + File.separator
				+ name);
		if (f.exists()) {
			return f;
		}

		return null;
	}

	/**
	 * load a class in from his own file from a humanfriendly format,
	 * helpermethod
	 */
	public static Object loadXML(File name) {
		Object o = null;
		BufferedReader br = null;
		final StringBuffer erg = new StringBuffer("");
		String puffer;

		// load options
		if (name.exists()) {
			try {
				// load text
				br = new BufferedReader(new FileReader(name));
				puffer = br.readLine();
				while (puffer != null) {
					erg.append(puffer);
					puffer = br.readLine();

				}
				// load xml
				o = new XStream().fromXML(erg.toString());
			} catch (ConversionException c) {
				// wrong format
				Log.ger.info("Can not load xml " + name);
				Log.ger.info("Wrong format", c);
			} catch (final Throwable t) {
				YEx.warn("Can not load xml file from " + name, t);

			} finally {
				try {
					br.close();
				} catch (final Exception e) {
				}
			}
		}
		return o;
	}

	/**
	 * save the class in his own file a humanfriendly yaml format, helpermethod
	 * 
	 * @param name
	 *            , filepath
	 * @param o
	 *            , to save object
	 */
	public static void saveXML(File name, final Object o) {
		// save options
		FileWriter w = null;
		try {
			w = new FileWriter(name);
			w.write(new XStream().toXML(o));
		} catch (final Throwable t) {
			YEx.info("Can not save xml format from " + o.getClass() + " to " + name, t);
		} finally {
			try {
				w.close();// wichtig, sonst wird nichts geschrieben!
			} catch (final Exception e) {
			}
		}
	}

	/**
	 * Helpermethod to check if the dir/file, or the parent dir exist and read &
	 * writeable
	 * 
	 * @param errors
	 * @param dir
	 * @return true, try it/all ok, false otherwise
	 */
	public static boolean checkPath(String title, File path, boolean dir, boolean checkWrite) {
		// collect it
		YMessagesDialog errors = new YMessagesDialog(title, "checkpath.direct");
		checkPath(path, errors, dir, checkWrite);

		// show it
		return errors.setFooter(I18N.t("Zugriffsfehler beim Dateizugriff, trotzdem probieren?")).showQuestion();
	}

	/**
	 * Helpermethod to check if the dir, or the parent dir exist and read &
	 * writeable
	 * 
	 * @param errors
	 * @param dir
	 * 
	 * @return true = all fine, false otherwise
	 */
	public static boolean checkPath(File path, YMessagesDialog errors, boolean dir, boolean checkWrite) {
		boolean allOK = true;

		// create folder
		if (dir && !path.canRead() && !path.mkdirs()) {
			errors.add(I18N.t("Kann Ordner {0} nicht erstellen.", path), Level.ERROR_INT);
			allOK = false;
		}

		// create parent
		if (!dir && !path.getParentFile().canRead() && !path.getParentFile().mkdirs()) {
			errors.add(I18N.t("Kann Ordner {0} für Datei {1} nicht erstellen.", path.getParentFile(), path.getName()), Level.WARN_INT);
			allOK = false;
		}

		// readable?
		if (dir && !path.isDirectory()) {
			errors.add(I18N.t("Pfad {0} ist kein Ordner.", path), Level.INFO_INT);
			allOK = false;
		}

		// readable?
		if (dir && !path.canRead()) {
			errors.add(I18N.t("Kann nicht vom Ordner {0} lesen.", path), Level.WARN_INT);
			allOK = false;
		}

		// readable?
		if (!dir && !path.getParentFile().canRead()) {
			errors.add(I18N.t("Kann nicht Datei {1} aus {0} lesen.", path.getName(), path.getParentFile()), Level.WARN_INT);
			allOK = false;
		}

		// writeable?
		if (checkWrite && dir && !path.canWrite()) {
			errors.add(I18N.t("Kann nicht in Ordner {0} schreiben.", path), Level.WARN_INT);
			allOK = false;
		}

		// exist?
		if (!dir && !checkWrite && (!path.exists() || !path.canRead())) {
			errors.add(I18N.t("Datei {0} existiert nicht.", path), Level.WARN_INT);
			allOK = false;
		}

		// writeable?
		// if (checkWrite && !dir && !path.getParentFile().canWrite()) {
		// errors.add(I18N.t("In {0} kann nicht geschrieben werden.",
		// path.getParentFile()));
		// }

		// exist?
		// if (!dir && path.exists() && checkWrite) {
		// errors.add(I18N.t("File {0} is exist before.", path));
		// allOK = false;
		// }

		// check space
		if (dir && path.getFreeSpace() <= 1024 * 1024 * 50) {
			errors.add(
					I18N.t("Auf {0} sind nur noch {1} Speicherplatz verfügbar.", path, humanReadableByteCount(path.getFreeSpace(), false)),
					Level.INFO_INT);
			allOK = false;
		}

		// check space
		if (!dir && path.getParentFile().getFreeSpace() <= 1024 * 1024 * 50) {
			errors.add(
					I18N.t("Auf {0} sind nur noch {1} Speicherplatz für {2} verfügbar.", path.getParentFile(),
							humanReadableByteCount(path.getParentFile().getFreeSpace(), false), path.getName()), Level.INFO_INT);
			allOK = false;
		}

		return allOK;
	}

	/**
	 * Helpermethod to display filesize
	 * 
	 * @author 
	 *         http://stackoverflow.com/questions/3758606/how-to-convert-byte-size
	 *         -into-human-readable-format-in-java
	 * @param bytes
	 * @param si
	 * @return
	 */
	public static String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit) {
			return bytes + " B";
		}
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
		return new DecimalFormat("#,##0.#").format(bytes / Math.pow(unit, exp)) + " " + pre + "B";
	}

	/**
	 * Helpermetho to get the contet of the stream
	 * 
	 * @param inputStream
	 * @throws IOException
	 */
	public static String inputStreamToString(InputStream inputStream) throws IOException {
		StringBuilder out = new StringBuilder();
		byte[] b = new byte[4096];
		for (int n; (n = inputStream.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}

		return out.toString();

	}

	/**
	 * Encrypt a File
	 * 
	 * See: http://www.java-forum.org/java-basics-anfaenger-themen/6941-datei-
	 * verschluesseln.html#post36422
	 * 
	 * @param originalFile
	 * @param encryptedFile
	 * @param password
	 * @param type
	 *            , DES, Blowfish, RC2, RC4, RC5
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 */
	public static void encryptFile(File originalFile, File encryptedFile, String password, String type) throws NoSuchAlgorithmException,
			NoSuchPaddingException {

		YProgressWindowRepeat y = new YProgressWindowRepeat(I18N.t("Verschlüssle " + originalFile.getName()), "lock");

		FileInputStream in = null;
		CipherOutputStream out = null;
		try {
			// set basics
			Cipher cipher = Cipher.getInstance(type);
			SecretKey key = new SecretKeySpec(password.getBytes(), type);
			cipher.init(Cipher.ENCRYPT_MODE, key);

			// do it
			in = new FileInputStream(originalFile);
			out = new CipherOutputStream(new FileOutputStream(encryptedFile), cipher);
			byte[] byteBuffer = new byte[1024];
			for (int n; (n = in.read(byteBuffer)) != -1; out.write(byteBuffer, 0, n)) {
				;
			}
			in.close();
			out.close();
			// new File(originalFile).delete();
		} catch (Throwable t) {
			YEx.warn("Can not encrypt File " + originalFile + " to " + encryptedFile, t);
		} finally {
			// close it
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
			// close it
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
			}
		}

		y.close();
	}

	/**
	 * Decryp a file
	 * 
	 * See: http://www.java-forum.org/java-basics-anfaenger-themen/6941-datei-
	 * verschluesseln.html#post36422
	 * 
	 * @param encryptedFile
	 * @param decryptedFile
	 * @param password
	 * @param type
	 *            , DES, Blowfish, RC2, RC4, RC5
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 */
	public static void decryptFile(File encryptedFile, File decryptedFile, String password, String type) throws NoSuchAlgorithmException,
			NoSuchPaddingException {

		YProgressWindowRepeat y = new YProgressWindowRepeat(I18N.t("Entschlüssle " + encryptedFile.getName()), "unlock");

		CipherInputStream in = null;
		OutputStream out = null;
		try {
			// set basics
			Cipher cipher = Cipher.getInstance(type);
			SecretKey key = new SecretKeySpec(password.getBytes(), type);
			cipher.init(Cipher.DECRYPT_MODE, key);
			// do it
			in = new CipherInputStream(new FileInputStream(encryptedFile), cipher);
			out = new FileOutputStream(decryptedFile);
			byte[] byteBuffer = new byte[1024];
			for (int n; (n = in.read(byteBuffer)) != -1; out.write(byteBuffer, 0, n)) {
				;
			}
			in.close();
			out.close();
			// new File(encryptedFile).delete();
		} catch (Throwable t) {
			YEx.warn("Can not decrypt File " + encryptedFile + " to " + decryptedFile, t);
		} finally {
			// close it
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
			// close it
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
			}

		}

		y.close();
	}
}
