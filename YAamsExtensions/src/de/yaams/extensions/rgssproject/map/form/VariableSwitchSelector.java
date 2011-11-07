/**
 * 
 */
package de.yaams.extensions.rgssproject.map.form;

import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyFixnum;
import org.jruby.RubyObject;
import org.jruby.RubyString;

import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.SystemGObject;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.YDialog;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.list.YArrayList;
import de.yaams.maker.programm.project.Project;

/**
 * @author abby
 * 
 */
public class VariableSwitchSelector extends YArrayList<SystemGObject> {

	private static final long serialVersionUID = 5294350359947944063L;

	protected Type type;
	protected Project project;

	/**
	 * Create a new FormDBList
	 * 
	 * @param project
	 * @param ary
	 */
	public VariableSwitchSelector(Project project, Type type) {
		super(RGSS1Helper.get(project, type));

		// set vars
		this.type = type;
		this.project = project;

		// set it
		add = true;
		delete = true;
		config = true;
		offSet = 1;
		showNumbers = true;

		// build it
		buildToolbar(RGSS1Helper.getName(type), RGSS1Helper.getIcon(type));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.gui.form.FormList#getIcon(java.lang.Object)
	 */
	@Override
	public Object getIcon(SystemGObject element) {
		return icon;
	}

	@Override
	public void add() {
		Ruby run = RGSS1Helper.get(project, type).get(1).getObject().getRuntime();

		// build Object
		RubyObject o = new RubyObject(RubyClass.newClass(run, RGSS1Helper.get(project, Type.SYSTEM).get(0).getObject().getMetaClass()
				.getSuperClass()));
		o.setInstanceVariable("@id", RubyFixnum.newFixnum(run, ary.size() + 1));
		o.setInstanceVariable("@name", RubyString.newEmptyString(run));

		SystemGObject g = new SystemGObject(o);

		// add & config
		add(g);
		config();

	}

	@Override
	public boolean isModified(SystemGObject o) {
		return false;
	}

	@Override
	protected void info() {

	}

	@Override
	public String getDesc(SystemGObject o) {
		return null;
	}

	@Override
	protected void configIntern() {
		// build form
		FormBuilder f = new FormBuilder("vss." + type);
		f.addElement("basic.name", RubyForm.getString(I18N.t("Name"), getSelectedObject().getObject().getInstanceVariable("@name")));

		YDialog.showForm(I18N.t("Konfigurie {0}", RGSS1Helper.getName(type)), icon, f);

	}

	/**
	 * Select the form and return the selected value, or -1 for cancel
	 * 
	 * @param project
	 * @param type
	 * @return
	 */
	public static int display(Project project, Type type) {
		VariableSwitchSelector v = new VariableSwitchSelector(project, type);

		// show form
		if (YDialog.show(I18N.t("Wähle die {0} aus", RGSS1Helper.getName(type)), RGSS1Helper.getIcon(type), v, true)) {
			return v.getList().getSelectedIndex();
		} else {
			return -1;
		}
	}

	@Override
	public Object getText(final SystemGObject value) {
		return value.getName();
	}
}
