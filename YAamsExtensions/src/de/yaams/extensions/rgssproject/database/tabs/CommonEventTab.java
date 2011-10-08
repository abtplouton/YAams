/**
 * 
 */
package de.yaams.extensions.rgssproject.database.tabs;

import org.jruby.RubyObject;

import de.yaams.extensions.rgssproject.RGSSProjectHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.extensions.rgssproject.map.form.FormSwitchVarSelector;
import de.yaams.extensions.rgssproject.map.nevent.YEventCommandList;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.FormComboBox;
import de.yaams.maker.helper.gui.form.FormHelper;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.programm.project.Project;

/**
 * @author abt
 * 
 */
public class CommonEventTab extends GTab {
	private static final long serialVersionUID = -7224278425132595022L;

	/**
	 * Create a new ItemTab
	 * 
	 * @param project
	 */
	public CommonEventTab(final Project project) {
		super(project);

		loadFile(Type.COMMONEVENT);
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
		form.getHeader("basic").setColumn(6);
		form.addElement("basic.name", RubyForm.getString("Name", "@name", act));

		FormComboBox tg = RubyForm.getComboBoxNum("Trigger", new String[] { "0", "1", "2" },
				new String[] { "None", "Autorun", "Parallel" }, act, "@trigger");

		form.addElement("basic.strigger", tg);
		form.addElement("basic.switch", FormHelper.setEnabeldWhenNotRightElementSelect(tg,
				new FormSwitchVarSelector(project, I18N.t("Switch"), act, "@switch_id", Type.SWITCH), "0"));

		// add commands
		form.setCenter(new YEventCommandList(act.getInstanceVariable("@list"), getProject()));

		// set en/disabeld
		tg.informListeners();
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
	 * @see
	 * de.yaams.packandgo.helper.gui.list.YSimpleList#getDesc(java.lang.Object)
	 */
	@Override
	public String getDesc(final Integer o) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.genericLoader.GTab#createObject()
	 */
	@Override
	public RubyObject createObject() {
		return (RubyObject) RGSSProjectHelper.getInterpreter(project).runScriptlet("return RPG::CommonEvent.new");
	}

}
