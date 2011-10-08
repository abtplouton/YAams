/**
 * 
 */
package de.yaams.extensions.rgssproject.database.tabs;

import org.jruby.RubyObject;

import de.yaams.extensions.rgssproject.RGSSProjectHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.extensions.rgssproject.database.form.TroopEventPanel;
import de.yaams.extensions.rgssproject.database.form.list.FormMemberList;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;
import de.yaams.maker.programm.project.Project;

/**
 * @author abt
 * 
 */
public class TroopTab extends GTab {
	private static final long serialVersionUID = -7224278425132595022L;

	/**
	 * Create a new ItemTab
	 * 
	 * @param project
	 */
	public TroopTab(final Project project) {
		super(project);

		loadFile(Type.TROOP);
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
		form.addHeader("basic", new FormHeader(I18N.t("Grundlegenes"), getIcon() + "_info"));
		form.addElement("basic.name", RubyForm.getString(I18N.t("Name"), "@name", act));

		form.addHeader("enemy", new FormHeader(RGSS1Helper.getName(Type.ENEMY), RGSS1Helper.getIcon(Type.ENEMY)));
		form.addElement("enemy.enemy", new FormMemberList(project, act.getInstanceVariable("@members")));

		// build panel
		form.setCenter(new TroopEventPanel(getProject(), act.getInstanceVariable("@pages")));

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
		return (RubyObject) RGSSProjectHelper.getInterpreter(project).runScriptlet("return RPG::Troop.new");
	}

}
