/**
 * 
 */
package de.yaams.extensions.rgssproject.database.tabs;

import org.jruby.RubyObject;
import org.jruby.RubyString;

import de.yaams.extensions.rgssproject.RGSSProjectHelper;
import de.yaams.extensions.rgssproject.RTP;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.form.FormDBComboBox;
import de.yaams.extensions.rgssproject.database.form.FormMusicEle;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.extensions.rgssproject.database.form.list.FormDBList;
import de.yaams.extensions.rgssproject.map.MapEditorTab;
import de.yaams.extensions.rgssproject.map.YMapView;
import de.yaams.extensions.rgssproject.map.event.EventsEditorTab;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormInfo;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;
import de.yaams.maker.programm.YaFrame;
import de.yaams.maker.programm.project.Project;

/**
 * @author abt
 * 
 */
public class MTab extends GTab {
	private static final long serialVersionUID = -3578896152081951738L;

	// public static int id;

	/**
	 * Create a new MTab
	 * 
	 * @param project
	 */
	public MTab(final Project project) {
		super(project);
		loadFile(Type.MAP);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.genericLoader.GTab#getInternContent(int)
	 */
	@Override
	public void buildForm(FormBuilder form, final int id) {
		// Load Map
		final RubyObject map = RGSS1Helper.get(project, Type.MAP).get(id).getObject();

		// add map view
		final YMapView view = new YMapView(map, project);
		view.recreateEventImage();

		// build basics
		form.addHeader("basic", new FormHeader(I18N.t("Grundlegenes"), getIcon() + "_info").setColumn(6));
		form.addElement("basic.name", RubyForm.getString("Name", "@name", map));
		form.addElement("basic.tile", new FormDBComboBox(I18N.t("Tileset"), project, Type.TILESET, map, "@tileset_id", false)
				.addChangeListener(new FormElementChangeListener() {

					@Override
					public void stateChanged(FormElement form) {
						view.recreateImage();

					}
				}));
		form.addElement("basic.size",
				new FormInfo(I18N.t("Size"), I18N.t("{0}x{1}", map.getInstanceVariable("@width"), map.getInstanceVariable("@height")))
						.setInfoTxt(I18N.t("You can chance the map size in the mapeditor.")));

		// sounds
		form.addHeader("sound", new FormHeader(I18N.t("Sounds"), "audio").setColumn(8));
		form.addElement("sound.bgm", new FormMusicEle("BGM", project, RTP.BGM, map.getInstanceVariable("@bgm")));
		form.addElement("sound.bgmauto", RubyForm.getBoolean("Autoplay", map, "@autoplay_bgm"));
		form.addElement("sound.bgs", new FormMusicEle("BGS", project, RTP.BGS, map.getInstanceVariable("@bgs")));
		form.addElement("sound.bgsauto", RubyForm.getBoolean("Autoplay", map, "@autoplay_bgs"));

		// build panel
		form.addHeader("troop",
				new FormHeader(RGSS1Helper.getName(Type.TROOP), RGSS1Helper.getIcon(Type.TROOP)).setColumn(4).setCollapsed(true));
		form.addElement("troop.step", RubyForm.getNumber("Encounter Step", map, "@encounter_step").setMinMax(0, 500, 1));
		form.addElement(
				"troop.troop",
				new FormDBList(project, Type.TROOP, map.getInstanceVariable("@encounter_list"), I18N.t("{0} auf der Karte",
						RGSS1Helper.getName(Type.TROOP))));

		form.addButton("map", YFactory.b("Edit Map", "map_edit", new AE() {

			@Override
			public void run() {

				YaFrame.open(MapEditorTab.getId(project, id));
			}
		}, 32));

		// can edit events?
		form.addButton("event", YFactory.b("Edit Events", "events_edit", new AE() {

			@Override
			public void run() {
				YaFrame.open(EventsEditorTab.getId(project, id));

			}
		}, 32));

		form.setCenter(view);
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
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getIcon()
	 */
	@Override
	public String getIcon() {
		return "map";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getTitle()
	 */
	@Override
	public String getTitle() {
		return I18N.t("Map");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.genericLoader.GTab#createObject()
	 */
	@Override
	public RubyObject createObject() {
		// create map
		RubyObject r = (RubyObject) RGSSProjectHelper.getInterpreter(project).runScriptlet("return RPG::Map.new(20,15)");
		r.setInstanceVariable("@name", RubyString.newEmptyString(r.getRuntime()));
		return r;

		// build it
		// return (RubyObject)
		// RBRunTime.interpreter.runScriptlet("return RPG::MapInfo()");

		// return (RubyObject)
		// RBRunTime.interpreter.runScriptlet("return RPG::Actor.new");
	}
}
