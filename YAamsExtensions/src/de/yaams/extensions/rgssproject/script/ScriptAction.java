/**
 * 
 */
package de.yaams.extensions.rgssproject.script;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import com.jidesoft.editor.CodeEditor;

import de.yaams.extensions.jruby.JRubyPlugin;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormTextField;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.tabs.SplitActionListElement;
import de.yaams.maker.programm.project.Project;

/**
 * @author abt
 * 
 */
public class ScriptAction extends SplitActionListElement {

	protected String content;
	protected int number;

	/**
	 * Create a new ScriptAction
	 * 
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public ScriptAction(final Object title, final Object content, final Object number) {
		super(title.toString(), null, "script");
		this.content = content.toString();
		// this.number = Integer.valueOf(number);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.packandgo.helper.gui.tabs.SplitActionListElement#getComponent
	 * (de.yaams.packandgo.programm.project.Project)
	 */
	@Override
	protected Component getComponent(final Project p) {
		// add name
		final FormBuilder f = new FormBuilder("db.script");
		f.addElement("basic.name", new FormTextField(I18N.t("Name"), title).addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				title = form.getContentAsString();

				if (!modified) {
					modified = true;
				}

			}
		}));

		final JPanel j = new JPanel(new BorderLayout());
		j.add(f.getPanel(true), BorderLayout.NORTH);

		// add code
		final CodeEditor code = JRubyPlugin.getCodeEditor(content);
		code.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(final KeyEvent arg0) {}

			@Override
			public void keyReleased(final KeyEvent e) {
				// wrong keys?
				if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_DOWN
						|| e.getKeyCode() == KeyEvent.VK_RIGHT) {
					return;
				}

				if (!modified) {
					modified = true;
				}

				content = code.getText();
			}

			@Override
			public void keyPressed(final KeyEvent arg0) {}
		});

		j.add(code, BorderLayout.CENTER);
		return j;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

}
