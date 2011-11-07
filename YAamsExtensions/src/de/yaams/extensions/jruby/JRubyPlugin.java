/**
 * 
 */
package de.yaams.extensions.jruby;

import java.util.HashMap;

import com.jidesoft.editor.CodeEditor;

import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.extensions.IExtension;
import de.yaams.maker.helper.gui.YMessagesDialog;
import de.yaams.maker.helper.gui.form.FormLink;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.icons.IconCache;
import de.yaams.maker.programm.plugins.BasePlugin;
import de.yaams.maker.programm.tabs.OptionsTab;

/**
 * @author Praktikant
 * 
 */
public class JRubyPlugin extends BasePlugin {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.plugins.core.BasePlugin#start()
	 */
	@Override
	public void start() {
		// add plugin
		IconCache.addPNG(JRubyPlugin.class, "ruby");

		// add credits
		ExtentionManagement.add(OptionsTab.OPTIONS_INFO, new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {
				FormBuilder f = (FormBuilder) objects.get("form");
				f.addElement("thx.jruby", new FormLink("JRuby", "http://www.jruby.org"));

			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.plugins.core.BasePlugin#useable(de.yaams.maker
	 * .helper.gui.YMessagesDialog)
	 */
	@Override
	public boolean useable(YMessagesDialog md) {
		return isVersionInstall(null, 0.0053, -1, md);
	}

	/**
	 * Create a Codeeditor
	 * 
	 * @param content
	 * @return
	 */
	public static CodeEditor getCodeEditor(String content) {
		CodeEditor code = new CodeEditor();
		code.setVirtualSpaceAllowed(false);
		code.setLineHighlight(true);
		code.setText(content);
		return code;
	}

}
