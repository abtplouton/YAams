/**
 * 
 */
package de.yaams.extensions.rgssproject.database.tabs;

import org.jruby.RubyObject;

import de.yaams.extensions.rgssproject.RGSSProjectHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.extensions.rgssproject.database.form.list.FormDBList;
import de.yaams.extensions.rgssproject.database.form.list.FormLearningList;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;
import de.yaams.maker.programm.project.Project;

/**
 * @author abt
 * 
 */
public class ClassTab extends GTab {
	private static final long serialVersionUID = -7224278425132595022L;

	/**
	 * Create a new ItemTab
	 * 
	 * @param project
	 */
	public ClassTab(final Project project) {
		super(project);

		loadFile(Type.CLASS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.genericLoader.GTab#getInternContent(int)
	 */
	@Override
	public void buildForm(FormBuilder form, int id) {
		// prepare
		RubyObject act = elements.get(id).getObject();

		// build basics
		form.addHeader("basic", new FormHeader(I18N.t("Grundlegenes"), getIcon() + "_info").setColumn(4));
		form.addElement("basic.name", RubyForm.getString(I18N.t("Name"), "@name", act));
		form.addElement("basic.position", RubyForm.getComboBoxNum("Position", new String[] { "0", "1", "2" }, new String[] { "Front",
				"Middle", "Rear" }, act, "@position"));

		// build panel
		form.addHeader("unsupported", new FormHeader(I18N.t("Nicht unterstützt"), "error").setColumn(4).setCollapsed(true));
		form.addElement("unsupported.state", RubyForm.getError("Status effectiveness", "@state_ranks", act));
		form.addElement("unsupported.element", RubyForm.getError("Elemental effectiveness", "@element_ranks", act));

		form.addHeader("supported", new FormHeader(I18N.t("Auswirkungen"), "class").setColumn(6));
		form.addElement(
				"supported.weapon",
				new FormDBList(project, Type.WEAPON, act.getInstanceVariable("@weapon_set"), I18N
						.t("Angabe der Waffen mit denen sich die Klasse ausrüsten kann.")));
		form.addElement(
				"supported.armor",
				new FormDBList(project, Type.ARMOR, act.getInstanceVariable("@armor_set"), I18N
						.t("Angabe der Rüstungen mit denen sich die Klasse ausrüsten kann.")));
		form.addElement("supported.learn", new FormLearningList(project, act.getInstanceVariable("@learnings")).setSorting(1));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.genericLoader.GTab#getIcon(java.lang.Integer)
	 */
	@Override
	public Object getIcon(final Integer o) {
		return getIcon();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.genericLoader.GTab#getText(java.lang.Integer)
	 */
	@Override
	public Object getText(final Integer value) {
		return elements.get(value).getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.genericLoader.GTab#createObject()
	 */
	@Override
	public RubyObject createObject() {
		return (RubyObject) RGSSProjectHelper.getInterpreter(project).runScriptlet("return RPG::Class.new");
	}

}
