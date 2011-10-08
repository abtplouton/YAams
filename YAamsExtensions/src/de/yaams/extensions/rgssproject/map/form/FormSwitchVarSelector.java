/**
 * 
 */
package de.yaams.extensions.rgssproject.map.form;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormNumberSpinner;
import de.yaams.maker.programm.project.Project;

/**
 * @author abt
 * 
 */
public class FormSwitchVarSelector extends FormNumberSpinner {

	protected JLabel name;
	protected Type type;
	protected JComponent select;

	/**
	 * Create a new FormSwitchVarSelector
	 * 
	 * @param project
	 * @param title
	 * @param obj
	 * @param type
	 */
	public FormSwitchVarSelector(final Project project, String title, final Object holder, final Object value, Type type) {
		super(title, RubyHelper.toInt(holder, value));
		this.type = type;

		// set it
		setMinMax(1, RGSS1Helper.get(project, type).size(), 1);

		// fix it?
		int id = RubyHelper.toInt(holder, value);
		name = new JLabel(id >= 1 && id < RGSS1Helper.get(project, type).size() ? RGSS1Helper.get(project, type).get(id).getName() : "??");

		// add chance
		addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				// state change?
				RubyHelper.setNum(holder, value, form.getContentAsString());
				name.setText(RGSS1Helper.get(project, FormSwitchVarSelector.this.type).get(Integer.valueOf(form.getContentAsString()))
						.getName());
				name.invalidate();
				name.revalidate();

			}
		});

		// add select button
		select = YFactory.tb(I18N.t("Wähle es aus"), RGSS1Helper.getIcon(type), new AE() {

			@Override
			public void run() {
				int erg = VariableSwitchSelector.display(project, FormSwitchVarSelector.this.type);
				if (erg != -1) {
					field.setValue(erg);
				}

			}
		});
	}

	/**
	 * @return the element
	 */
	@Override
	public JComponent getElement(boolean withSaveFunction) {
		JPanel p = new JPanel(new BorderLayout());
		p.add(super.getElement(withSaveFunction), BorderLayout.CENTER);

		p.add(select, BorderLayout.EAST);
		p.add(name, BorderLayout.WEST);

		return p;
	}

	/**
	 * En/Disable
	 * 
	 * @param value
	 * @return
	 */
	@Override
	public FormElement setEnabled(boolean value) {
		select.setEnabled(value);
		return super.setEnabled(value);
	}

}
