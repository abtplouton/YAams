package de.yaams.extensions.jruby;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang.Validate;
import org.jruby.Ruby;
import org.jruby.embed.ScriptingContainer;
import org.jruby.util.KCode;

import de.yaams.maker.helper.Log;
import de.yaams.maker.helper.gui.YEx;

/**
 * 
 * @author Nebli
 * 
 */
public class RBRunTime {

	public ScriptingContainer interpreter;
	public final static RBRunTime get = new RBRunTime();

	/**
	 * Get the runtime
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Ruby getRuntime() {
		return interpreter.getRuntime();
	}

	/**
	 * Get it
	 * 
	 * @return
	 */
	protected RBRunTime() {
		interpreter = new ScriptingContainer();
		// interpreter.setArgv(new String[] { "-K" });
		interpreter.setKCode(KCode.UTF8);
	}

	/**
	 * @param args
	 */
	public Object interpretFile(final File file) {
		try {
			// file exist?
			if (file == null || !file.exists()) {
				throw new FileNotFoundException("File " + file + " not found.");
			}

			Log.ger.debug("Read & Interpret " + file.getName());

			return interpreter.runScriptlet(new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(file)),
					"UTF-8")), file.getName());
		} catch (final Throwable t) {
			YEx.warn("Can not interpret scriptfile " + file, t);

			/*
			 * JDialog.exception(new Throwable(
			 * "Can not interpret rubyscriptfile " +
			 * RBLocater.class.getResource(file) + " (" + file + ")", t));
			 */
		}
		return null;
	}

	/**
	 * @param args
	 */
	public Object interpretInternFile(final Class<?> c, final String file) {
		InputStream stream = null;
		try {
			// validate
			Validate.notNull(c);
			Validate.notEmpty(file);

			stream = c.getResourceAsStream(file);

			// file exist?
			if (stream == null) {
				throw new FileNotFoundException("Filestream " + file + " in Class " + c + " not found.");
			}
			return interpreter.runScriptlet(stream, file);
		} catch (final Throwable t) {
			YEx.info("Can not interpret scriptstreamfile " + file, t);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
				}
				/*
				 * JDialog.exception(new Throwable(
				 * "Can not interpret rubyscriptfile " +
				 * RBLocater.class.getResource(file) + " (" + file + ")", t));
				 */
			}
		}
		return null;
	}

	/**
	 * Run Scriptlet
	 * 
	 * @param script
	 * @return
	 */
	public Object runScriptlet(String script) {
		return interpreter.runScriptlet(script);
	}
}
