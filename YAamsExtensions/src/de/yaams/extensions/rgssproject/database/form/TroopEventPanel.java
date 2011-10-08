/**
 * 
 */
package de.yaams.extensions.rgssproject.database.form;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.jruby.RubyArray;
import org.jruby.RubyObject;
import org.jruby.runtime.builtin.IRubyObject;

import de.yaams.extensions.rgssproject.RGSSProjectHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.SystemGObject;
import de.yaams.extensions.rgssproject.map.form.FormSwitchVarSelector;
import de.yaams.extensions.rgssproject.map.nevent.YEventCommandList;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.form.FormCheckbox;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormEmpty;
import de.yaams.maker.helper.gui.form.FormHelper;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;
import de.yaams.maker.helper.helpcenter.HelpViewer;
import de.yaams.maker.programm.project.Project;

/**
 * @author abt
 * 
 */
public class TroopEventPanel extends JPanel {

	private static final long serialVersionUID = -7038427336325635360L;

	protected ArrayList<SystemGObject> elements;
	protected TroopEventPageList list;
	protected JPanel gContent;
	protected RubyObject pages;
	protected Project project;

	/**
	 * Create a new GTab
	 * 
	 * @param project
	 */
	public TroopEventPanel(Project project, IRubyObject pages) {
		super(new GridLayout(1, 1));
		this.project = project;

		RubyArray ra = (RubyArray) pages;

		// convert code
		elements = new ArrayList<SystemGObject>();
		for (Object o : ra) {
			elements.add(new SystemGObject((RubyObject) o));
		}

		// build gui
		buildGui();
	}

	/**
	 * Build the gui
	 */
	protected void buildGui() {
		list = new TroopEventPageList(this);
		gContent = new JPanel(new GridLayout(1, 1));

		add(YFactory.createHorizontPanel(list, gContent, "troop.event.panel"));

		buildEmptyView();
	}

	/**
	 * Remove all and show the help
	 */
	protected void buildEmptyView() {

		// build panel
		gContent.removeAll();

		// add helpfile
		gContent.add(new HelpViewer("troop.pages"));

		// build it
		gContent.invalidate();
		gContent.revalidate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getContent()
	 */
	public void buildInternContent(final int id) {

		// if (id > 0) {
		// build panel
		gContent.removeAll();

		// build it
		FormBuilder form = new FormBuilder("troop.event");
		buildForm(form, id);

		// add modi
		form.addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				// set modifizied
				elements.get(id).setModified(true);

			}
		});

		// build it
		gContent.add(form.getPanel(true));
		gContent.invalidate();
		gContent.revalidate();
		// } else {
		// buildEmptyView();
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getContent()
	 */
	public void buildForm(FormBuilder form, int id) {
		// get page
		RubyObject page = elements.get(id).getObject();
		RubyObject condition = (RubyObject) page.getInstanceVariable("@condition");

		// build
		form.addElement("basic.span", RubyForm.getComboBoxNum(I18N.t("Span"), new String[] { "0", "1", "2" }, new String[] { "Battle",
				"Turn", "Movement" }, page, "@span"));

		form.addHeader("cond", new FormHeader(I18N.t("Bedingung"), "if").setCollapsed(true).setColumn(6));

		// add turn
		FormCheckbox c = RubyForm.getBoolean(I18N.t("Turn"), condition, "@turn_valid");
		form.addElement("cond.turn_a", c);
		form.addElement("cond.turn_turn_a",
				FormHelper.setEnabeldWhenRightElementSelect(c, RubyForm.getNumber("", condition, "@turn_a"), "true"));
		form.addElement("cond.turn_turn_b",
				FormHelper.setEnabeldWhenRightElementSelect(c, RubyForm.getNumber("+ X*", condition, "@turn_b"), "true"));
		c.informListeners();

		// add enemy
		c = RubyForm.getBoolean(I18N.t(RGSS1Helper.getName(Type.ENEMY)), condition, "@enemy_valid");
		form.addElement("cond.enemy_a", c);
		form.addElement("cond.enemy_def", FormHelper.setEnabeldWhenRightElementSelect(c, RubyForm.getNumber("", condition, "@enemy_index")
				.setMinMax(1, 8, 1), "true"));
		form.addElement(
				"cond.enemy_hp",
				FormHelper.setEnabeldWhenRightElementSelect(c,
						RubyForm.getNumber("Hp is or below (%):", condition, "@enemy_hp").setMinMax(0, 100, 1), "true"));
		c.informListeners();

		// add actor
		c = RubyForm.getBoolean(RGSS1Helper.getName(Type.ACTOR), condition, "@actor_valid");
		form.addElement("cond.actor_a", c);
		form.addElement("cond.actor_def", FormHelper.setEnabeldWhenRightElementSelect(c, new FormDBComboBox("", project, Type.ACTOR,
				condition, "@actor_id", true), "true"));
		form.addElement(
				"cond.actor_hp",
				FormHelper.setEnabeldWhenRightElementSelect(c,
						RubyForm.getNumber("Hp is or below (%):", condition, "@actor_hp").setMinMax(0, 100, 1), "true"));
		c.informListeners();

		// add actor
		c = RubyForm.getBoolean(RGSS1Helper.getName(Type.SWITCH), condition, "@switch_valid");
		form.addElement("cond.switch_a", c);
		form.addElement("cond.switch_id", FormHelper.setEnabeldWhenRightElementSelect(c, new FormSwitchVarSelector(project,
				I18N.t("Is on"), condition, "@switch_id", Type.SWITCH), "true"));
		form.addElement("cond.switch_id2", new FormEmpty());
		c.informListeners();

		// add code
		form.setCenter(new YEventCommandList(page.getInstanceVariable("@list"), project));
	}

	/**
	 * @return the elements
	 */
	public ArrayList<SystemGObject> getElements() {
		return elements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.packandgo.helper.gui.list.YSimpleList#getIcon(java.lang.Object)
	 */
	public Object getIcon(final Integer o) {
		return "event_edit";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.packandgo.helper.gui.list.YSimpleList#isModified(java.lang.Object
	 * )
	 */
	public boolean isModified(final Integer o) {
		return elements.get(o).isModified();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.packandgo.helper.gui.list.YSimpleList#getText(java.lang.Object )
	 */
	public Object getText(final Integer value) {
		return I18N.t("{0}.Seite", value + 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.packandgo.helper.gui.list.YSimpleList#getDesc(java.lang.Object)
	 */
	public String getDesc(final Integer o) {
		return null;
	}

	/**
	 * Create a new object, for this type
	 * 
	 * @return
	 */
	public RubyObject createObject() {
		return (RubyObject) RGSSProjectHelper.getInterpreter(project).runScriptlet("return RPG::Troop::Page.new");
	}

	/**
	 * Delete the selected element
	 * 
	 * @param id
	 */
	public void delObject(int id) {

		// last element?
		if (elements.size() - 1 == id) {
			// remove it
			elements.remove(id);
		} else {
			// reset only element
			elements.get(id).setObject(createObject());
			elements.get(id).setModified(true);
		}

		// inform tab
		buildEmptyView();
	}
}
