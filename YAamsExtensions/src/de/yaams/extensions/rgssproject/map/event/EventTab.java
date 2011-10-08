/**
 * 
 */
package de.yaams.extensions.rgssproject.map.event;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jruby.RubyArray;
import org.jruby.RubyObject;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.RGSSProjectHelper;
import de.yaams.extensions.rgssproject.RTP;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.SystemGObject;
import de.yaams.extensions.rgssproject.database.form.FormGraphEle;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.extensions.rgssproject.map.form.FormSwitchVarSelector;
import de.yaams.extensions.rgssproject.map.nevent.YEventCommandList;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.bcb.BcbBuilder;
import de.yaams.maker.helper.gui.form.FormCheckbox;
import de.yaams.maker.helper.gui.form.FormComboBox;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormEmpty;
import de.yaams.maker.helper.gui.form.FormHelper;
import de.yaams.maker.helper.gui.form.FormInfo;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;
import de.yaams.maker.helper.helpcenter.HelpViewer;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.tab.ProjectTab;
import de.yaams.maker.programm.tabs.TabEvent;

/**
 * @author abt
 * 
 */
public class EventTab extends ProjectTab {

	private static final long serialVersionUID = -7038427336325635360L;

	public static final String ID = "project.event.page";

	protected ArrayList<SystemGObject> elements;
	protected EventPageList list;
	protected JPanel gContent;
	protected RubyObject event, map;

	/**
	 * Create a new GTab
	 * 
	 * @param project
	 */
	public EventTab(Project project, RubyObject map, RubyObject event) {
		super(project);

		this.event = event;
		this.map = map;

		RubyArray ra = (RubyArray) event.getInstanceVariable("@pages");

		// convert code
		elements = new ArrayList<SystemGObject>();
		for (Object o : ra) {
			elements.add(new SystemGObject((RubyObject) o));
		}

		// build gui
		buildGui();

		// save it
		addSaveButton();
	}

	/**
	 * Save chanced, overwrite it to implement it
	 */
	@Override
	protected void saveIntern() {
		RGSS1Helper.save(project, Type.MAP);
	}

	/**
	 * Build the gui
	 */
	@Override
	protected void buildGui() {
		list = new EventPageList(this);
		gContent = new JPanel(new GridLayout(1, 1));
		super.buildGui();

		buildEmptyView();
	}

	/**
	 * Remove all and show the help
	 */
	protected void buildEmptyView() {

		// build panel
		gContent.removeAll();

		// add helpfile
		gContent.add(new HelpViewer("event.pages"));

		// build it
		gContent.invalidate();
		gContent.revalidate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getContent()
	 */
	@Override
	public JComponent getContent() {
		return YFactory.createHorizontPanel(list, gContent, "db." + getIcon());
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
		FormBuilder form = new FormBuilder("event.page." + getIcon());
		buildForm(form, id);

		// add modi
		form.addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				// set modifizied
				elements.get(id).setModified(true);
				setModified(true);

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

		// build
		form.addHeader("basic", new FormHeader(I18N.t("Grundlegenes"), "event"));
		form.addElement(
				"basic.trigger",
				RubyForm.getComboBoxNum(I18N.t("Trigger"), new String[] { "0", "1", "2", "3", "4" }, new String[] { "action button",
						"contact with player", "contact with event", "autorun", "parallel processing" }, page, "@trigger"));

		// animation
		form.addHeader("animation", new FormHeader(I18N.t("Animation"), "animation").setColumn(10).setCollapsed(true));
		form.addElement("animation.ani", RubyForm.getBoolean("Moving Animation", page, "@walk_anime"));
		form.addElement("animation.stop", RubyForm.getBoolean("Stopped Animation", page, "@step_anime"));
		form.addElement("animation.fix", RubyForm.getBoolean("Fixed Direction", page, "@direction_fix"));
		form.addElement("animation.move", RubyForm.getBoolean("Move Through", page, "@through"));
		form.addElement("animation.top", RubyForm.getBoolean("Always On Top", page, "@always_on_top"));

		// animation
		form.addHeader("move", new FormHeader(I18N.t("Bewegung"), "animation").setColumn(6));
		form.addElement(
				"move.typ",
				RubyForm.getComboBoxNum(I18N.t("Type"), new String[] { "0", "1", "2", "3" }, new String[] { "Fixed", "Random", "Approach",
						"Custom" }, page, "@move_type"));
		form.addElement(
				"move.speed",
				RubyForm.getComboBoxNum(I18N.t("Speed"), new String[] { "1", "2", "3", "4", "5", "6" }, new String[] { "slowest", "slower",
						"slow", "fast", "faster", "fastest" }, page, "@move_speed"));
		form.addElement(
				"move.freq",
				RubyForm.getComboBoxNum(I18N.t("Frequency"), new String[] { "1", "2", "3", "4", "5", "6" }, new String[] { "lowest",
						"lower", "low", "high", "higher", "highest" }, page, "@move_frequency"));

		// add contition
		RubyObject cond = (RubyObject) page.getInstanceVariable("@condition");
		form.addHeader("cond", new FormHeader(I18N.t("Bedingung"), "if").setColumn(4).setCollapsed(true));

		// add local switch
		FormCheckbox v = RubyForm.getBoolean("", cond, "@self_switch_valid");
		form.addElement("cond.ls", v);
		form.addElement(
				"cond.ls2",
				FormHelper.setEnabeldWhenRightElementSelect(
						v,
						RubyForm.getComboBox("Local Switch", new String[] { "A", "B", "C", "D" }, new String[] { "A", "B", "C", "D" },
								cond.getInstanceVariable("@self_switch_ch")), "true"));
		v.informListeners();

		// add switch 1
		v = RubyForm.getBoolean("", cond, "@switch1_valid");
		form.addElement("cond.s1", v);
		form.addElement("cond.s12", FormHelper.setEnabeldWhenRightElementSelect(v, new FormSwitchVarSelector(project, "Switch 1", cond,
				"@switch1_id", Type.SWITCH), "true"));
		v.informListeners();

		// add switch 2
		v = RubyForm.getBoolean("", cond, "@switch2_valid");
		form.addElement("cond.s2", v);
		form.addElement("cond.s22", FormHelper.setEnabeldWhenRightElementSelect(v, new FormSwitchVarSelector(project, "Switch 2", cond,
				"@switch2_id", Type.SWITCH), "true"));
		v.informListeners();

		// add variable
		v = RubyForm.getBoolean("", cond, "@variable_valid");
		form.addElement("cond.v", v);
		form.addElement("cond.v2", FormHelper.setEnabeldWhenRightElementSelect(v, new FormSwitchVarSelector(project, "Variable", cond,
				"@variable_id", Type.VARIABLE), "true"));
		form.addElement("cond.v3", new FormEmpty());
		form.addElement("cond.v4",
				FormHelper.setEnabeldWhenRightElementSelect(v, RubyForm.getNumber(">=", cond, "@variable_value"), "true"));
		v.informListeners();

		// graphic
		RubyObject graph = (RubyObject) page.getInstanceVariable("@graphic");

		form.addHeader("graphic", new FormHeader(I18N.t("Grafik"), "graphic").setColumn(4).setCollapsed(true));

		// add select
		FormComboBox gS = new FormComboBox(I18N.t("Typ"), new String[] { "1", "0" }, new String[] { "Tile", "Character" });
		gS.selectField(RubyHelper.toInt(graph.getInstanceVariable("@tile_id")) == 0 ? "1" : "0");
		form.addElement("graphic.typ", gS.setSorting(-2));

		// add tile
		form.addElement("graphic.tile",
				FormHelper.setEnabeldWhenRightElementSelect(gS, RubyForm.getNumber(I18N.t("Tile"), graph, "@tile_id").setSorting(-1), "1"));

		// add char
		form.addElement("graphic.char", FormHelper.setEnabeldWhenRightElementSelect(gS, new FormGraphEle(I18N.t("Char"), project,
				RTP.CHARACTER, graph, "@character_name", "@character_hue"), "0"));
		form.addElement("graphic.char2", new FormEmpty());
		form.addElement("graphic.charpatt", FormHelper.setEnabeldWhenRightElementSelect(gS,
				RubyForm.getNumber(I18N.t("Pattern"), graph, "@pattern").setMinMax(0, 3, 1), "0"));
		form.addElement(
				"graphic.chardir",
				FormHelper.setEnabeldWhenRightElementSelect(
						gS,
						RubyForm.getComboBoxNum(I18N.t("Direction"), new String[] { "2", "4", "6", "8" }, new String[] { "Down", "Left",
								"Right", "Up" }, graph, "@direction"), "0"));
		form.addElement(
				"graphic.charopa",
				FormHelper.setEnabeldWhenRightElementSelect(gS,
						RubyForm.getNumber(I18N.t("Opacity"), graph, "@opacity").setMinMax(0, 255, 1), "0"));
		form.addElement(
				"graphic.charblend",
				FormHelper.setEnabeldWhenRightElementSelect(gS,
						RubyForm.getNumber(I18N.t("Blendtype"), graph, "@blend_type").setMinMax(0, 2, 1), "0"));

		gS.informListeners();

		// unsupported
		form.addHeader("unsupported", new FormHeader(I18N.t("Nicht unterstützt"), "error").setCollapsed(true));
		form.addElement("unsupported.ele", new FormInfo("", I18N.t("Movement route")));
		// @movement_route

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
		return getIcon();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getIcon()
	 */
	@Override
	public String getIcon() {
		return "event_edit";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getTitle()
	 */
	@Override
	public String getTitle() {
		return event.getInstanceVariable("@name").toString();
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
		return (RubyObject) RGSSProjectHelper.getInterpreter(project).runScriptlet("return RPG::Event::Page.new");
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
		setModified(true);
		buildEmptyView();
	}

	@Override
	protected void buildBcb(BcbBuilder bcb) {
		int act = RubyHelper.toInt(map.getInstanceVariable("@id"));

		EventsEditorTab.buildMapBcB(bcb, project, act);

		bcb.addSeperator();

		// add all events
		RubyArray ra = (RubyArray) map.getInstanceVariable("@events");

		// convert code
		elements = new ArrayList<SystemGObject>();
		for (Object o : ra) {
			elements.add(new SystemGObject((RubyObject) o));
		}

		int act2 = RubyHelper.toInt(event.getInstanceVariable("@id"));

		for (int i = 1, l = elements.size(); i < l; i++) {
			bcb.addElement(elements.get(i).getName(), "event", getId(project, act, i), i == act2);
		}
	}

	@Override
	public String getId() {
		return getId(project, RubyHelper.toInt(map.getInstanceVariable("@id")), RubyHelper.toInt(event.getInstanceVariable("@id")));
	}

	/**
	 * Get it
	 * 
	 * @param p
	 * @param map
	 * @param event
	 * @return
	 */
	public static String getId(Project p, int map, int event) {
		return TabEvent.buildParameter(ID, p, null, "map", Integer.toString(map), "event", Integer.toString(event));
	}
}
