/**
 * 
 */
package de.yaams.extensions.rgssproject.database.form;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JPanel;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.SystemGObject;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.JavaHelper;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.form.FormComboBox;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.programm.YaFrame;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.tabs.TabEvent;

/**
 * @author abt
 * 
 */
public class FormDBComboBox extends FormComboBox {

	protected Object holder, value;
	protected int startFix;
	protected Type type;
	protected Project project;

	/**
	 * Create a new FormDBComboBox
	 * 
	 * @param title
	 * @param project
	 * @param type
	 * @param holder
	 * @param value
	 * @param AllowNothing
	 */
	public FormDBComboBox(String title, Project project, Type type, final Object holder, final Object value, boolean AllowNothing) {
		super(title);

		this.holder = holder;
		this.value = value;
		this.type = type;
		this.project = project;

		startFix = AllowNothing ? 0 : 1;

		// set it
		values = buildList();
		create(new DefaultComboBoxModel(values));
		int id = RubyHelper.toInt(holder, value) - startFix;
		if (id < values.length) {
			box.setSelectedIndex(id);
		}

		// save it
		addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				RubyHelper.setNum(holder, value, getBox().getSelectedIndex() + startFix);

			}
		});
	}

	/**
	 * Helpermethod to create the right list
	 * 
	 * @param type
	 * @return
	 */
	private String[] buildList() {

		// build List
		ArrayList<SystemGObject> objs = RGSS1Helper.get(project, type);

		String[] eles = new String[objs.size() - startFix];
		for (int i = 0, l = eles.length; i < l; i++) {
			if (i + startFix == 0) {
				eles[i] = I18N.t("(Nichts)");
			} else {
				eles[i] = objs.get(i + startFix).getName();
			}
		}

		// get it
		return eles;
	}

	/**
	 * Get intern element, only for rendering
	 * 
	 * @return
	 */
	@Override
	protected JComponent getInternElement() {
		final JPanel p = new JPanel(new BorderLayout());
		p.add(super.getInternElement(), BorderLayout.CENTER);
		// add config button
		p.add(YFactory.tb(I18N.t("Öffne das ausgewählte Element im neuen Tab zum Konfigurieren."), "opts", new AE() {

			@Override
			public void run() {
				// open tab
				if (box.getSelectedIndex() == 0 && startFix == 0) {
					YaFrame.open(TabEvent.buildParameter(RGSS1Helper.getTabID(type), project, null));
				} else {
					YaFrame.open(TabEvent.buildParameter(RGSS1Helper.getTabID(type), project,
							JavaHelper.createHashString("select", Integer.toString(box.getSelectedIndex() - startFix + 1))));
				}
			}
		}), BorderLayout.EAST);

		return p;
	}

}
