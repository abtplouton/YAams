/**
 * 
 */
package de.yaams.extensions.rgssproject.database.tabs;

import org.jruby.RubyObject;
import org.jruby.runtime.builtin.IRubyObject;

import de.yaams.extensions.rgssproject.RGSSProjectHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.form.FormTable;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.extensions.rgssproject.database.form.list.FormDBList;
import de.yaams.extensions.rgssproject.database.form.list.FormLearningList;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.FormEmpty;
import de.yaams.maker.helper.gui.form.FormInfo;
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

		// elements
		form.addHeader("ele", new FormHeader(RGSS1Helper.getName(Type.ELEMENT), RGSS1Helper.getIcon(Type.ELEMENT)).setCollapsed(true)
				.setColumn(4).setSorting(4));

		form.addElement("ele.0ele", new FormInfo("", I18N.t("Elemental effectiveness")));
		form.addElement("ele.0stat", new FormInfo("", I18N.t("Status effectiveness")));

		int e = RGSS1Helper.get(getProject(), Type.ELEMENT).size();
		IRubyObject eTable = act.getInstanceVariable("@element_ranks");
		int s = RGSS1Helper.get(getProject(), Type.STATUS).size();
		IRubyObject sTable = act.getInstanceVariable("@state_ranks");
		int l = e > s ? e : s;

		// run over all
		for (int i = 1; i < l; i++) {
			// add elements
			if (e > i) {
				form.addElement("ele." + i + "ele", new FormTable(RGSS1Helper.get(getProject(), Type.ELEMENT).get(i).getName(), eTable, i,
						0));
			} else {
				form.addElement("ele." + i + "ele", new FormEmpty());
			}

			// add status
			if (s > i) {
				form.addElement("ele." + i + "stat", new FormTable(RGSS1Helper.get(getProject(), Type.STATUS).get(i).getName(), sTable, i,
						0));
			} else {
				form.addElement("ele." + i + "stat", new FormEmpty());
			}
		}

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
