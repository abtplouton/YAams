/**
 * 
 */
package de.yaams.extensions.rgssproject.map.event;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jruby.RubyFixnum;
import org.jruby.RubyHash;
import org.jruby.RubyObject;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.RGSSProjectHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.extensions.rgssproject.map.YMapView;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.YDialog;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.bcb.BcbBuilder;
import de.yaams.maker.helper.gui.form.FormButton;
import de.yaams.maker.helper.gui.form.FormInfo;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;
import de.yaams.maker.programm.YaFrame;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.ProjectTabEvent;
import de.yaams.maker.programm.project.tab.ProjectTab;
import de.yaams.maker.programm.tabs.TabEvent;

/**
 * @author abt
 * 
 */
public class EventsEditorTab extends ProjectTab {

	private static final long serialVersionUID = -1238539580148933455L;
	protected RubyObject map;

	protected JPanel left, main;
	protected YMapView view;
	protected int x, y, mapID;
	protected String key;

	protected HashMap<String, RubyObject> events;

	public static final String ID = "project.event";

	/**
	 * Create a new EventTab
	 * 
	 * @param project
	 */
	public EventsEditorTab(Project project, RubyObject map) {
		super(project);
		this.map = map;
		mapID = RubyHelper.toInt(map.getInstanceVariable("@id"));

		try {

			// load events
			events = new HashMap<String, RubyObject>();
			RubyHash events = (RubyHash) map.getInstanceVariable("@events");

			for (Object id : events.keySet()) {
				// load event
				RubyObject event = (RubyObject) events.get(id);
				this.events
						.put(event.getInstanceVariable("@x").toJava(Integer.class) + "x"
								+ event.getInstanceVariable("@y").toJava(Integer.class), event);
			}

			// build main gui
			left = new JPanel(new GridLayout(1, 1));
			main = new JPanel(new BorderLayout());

			view = new YMapView(map, project);
			view.installClickSupport();
			view.getView().recreateEventImage();
			view.getView().addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					setSelect(e.getX() / 32, e.getY() / 32);
				}
			});

			main.add(view, BorderLayout.CENTER);

			buildGui();
		} catch (Throwable t) {
			YEx.warn("Can not create Event Editor", t);
		}
		setSelect(0, 0);
	}

	/**
	 * Get the postion of the mouse click and update the left panel. show the
	 * event or nothing
	 * 
	 * @param x
	 * @param y
	 */
	public void updateLeftPanel() {
		left.removeAll();
		FormBuilder form = new FormBuilder("event.main");

		// show general infos
		form.getHeader("basic").setTitle(I18N.t("Generell")).setIcon("event");
		form.addElement("basic.aapos", new FormInfo("Position", I18N.t("{0} x {1}", x, y)));
		form.addHeader("caction", new FormHeader(I18N.t("Aktion"), "map"));

		// add overview
		if (events.size() > 0) {
			form.addHeader("overview", new FormHeader(I18N.t("Alle Events"), "events"));
			for (final String key : events.keySet()) {
				// add button
				form.addElement("overview." + key, new FormButton(I18N.t("{0} ({1}x{2})", gEn(key), gEx(key), gEy(key)), "event", new AE() {

					@Override
					public void run() {
						setSelect(gEx(key), gEy(key));
					}
				}));
			}
		}

		// has event?
		if (events.containsKey(key)) {
			final RubyObject event = events.get(key);

			// add elements
			form.addElement("basic.name", RubyForm.getString(I18N.t("Name"), "@name", event));
			form.addElement("basic.id", new FormInfo("ID", event.getInstanceVariable("@id")));
			form.addElement("caction.edit", new FormButton(I18N.t("Editieren"), "edit", new AE() {

				@Override
				public void run() {
					YaFrame.open(EventTab.getId(project, mapID, RubyHelper.toInt(event.getInstanceVariable("@id"))));

				}
			}));
			form.addElement("caction.zdel", new FormButton(I18N.t("Löschen"), "trash", new AE() {

				@Override
				public void run() {
					deleteSelectedEvent();

				}
			}));

		} else {
			form.addElement("caction.add", new FormButton(I18N.t("Neues Event"), "event_add", new AE() {

				@Override
				public void run() {
					createNewEvent();

				}
			}));

		}

		left.add(form.getPanel(true));
		invalidate();
		revalidate();
	}

	/**
	 * Delete Event
	 */
	protected void deleteSelectedEvent() {

		// found something?
		if (!EventsEditorTab.this.events.containsKey(key)) {
			YDialog.ok(I18N.t("Kein Event gefunden."), I18N.t("Es wurde an der Position {0} kein Event zum Löschen gefunden.", key),
					"error_event");

			return;
		}

		// ask
		if (YDialog.delete(gEn(key), "event_del")) {
			// delete
			((RubyHash) EventsEditorTab.this.map.getInstanceVariable("@events")).remove(gEid(key));

			// delete
			EventsEditorTab.this.events.remove(key);

			// redraw
			view.recreateEventImage();

			// update left
			updateLeftPanel();

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getIcon()
	 */
	@Override
	public String getIcon() {
		return "events_map";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getTitle()
	 */
	@Override
	public String getTitle() {
		return (String) map.getInstanceVariable("@name").toJava(String.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getContent()
	 */
	@Override
	public JComponent getContent() {
		return YFactory.createHorizontPanel(left, main, "event.main");
	}

	/**
	 * Create a new event
	 */
	protected void createNewEvent() {

		// position is free?
		if (events.containsKey(key)) {
			// TODO buzzer
			return;
		}

		// find next free id
		int id = 1;
		boolean found;
		for (int z = 0; z < events.size(); z++) {
			found = false;
			// found a event with id 1?
			for (String k : events.keySet()) {
				// found?
				if (gEid(k) == id) {
					id++;
					found = true;
					break;
				}
			}

			// don't found?
			if (!found) {
				break;
			}
		}

		RubyObject r = (RubyObject) RGSSProjectHelper.getInterpreter(project).runScriptlet(
				"r = RPG::Event.new(" + x + "," + y + "); r.id=" + id + ";" + "return r;");

		// create new class
		events.put(key, r);

		// add it in the map
		RubyHash event = (RubyHash) map.getInstanceVariable("@events");
		event.put(new RubyFixnum(map.getRuntime(), id), events.get(key));

		// redraw
		view.recreateEventImage();
		updateLeftPanel();
	}

	/**
	 * Helpermethod get event x
	 * 
	 * @param key
	 * @return
	 */
	private int gEx(String key) {
		return RubyHelper.toInt(events.get(key).getInstanceVariable("@x"));
	}

	/**
	 * Helpermethod get event y
	 * 
	 * @param key
	 * @return
	 */
	private int gEy(String key) {
		return RubyHelper.toInt(events.get(key).getInstanceVariable("@y"));
	}

	/**
	 * Helpermethod get event id
	 * 
	 * @param key
	 * @return
	 */
	private int gEid(String key) {
		return RubyHelper.toInt(events.get(key).getInstanceVariable("@id"));
	}

	/**
	 * Helpermethod get event name
	 * 
	 * @param key
	 * @return
	 */
	private String gEn(String key) {
		return events.get(key).getInstanceVariable("@name").toString();
	}

	/**
	 * Select the field
	 * 
	 * @param x
	 * @param y
	 */
	protected void setSelect(int x, int y) {
		this.x = x;
		this.y = y;
		key = x + "x" + y;
		updateLeftPanel();
		view.setSelectX(x);
		view.setSelectY(y);
	}

	@Override
	protected void buildBcb(BcbBuilder bcb) {
		buildMapBcB(bcb, project, mapID);
	}

	@Override
	public String getId() {
		return getId(project, RubyHelper.toInt(map.getInstanceVariable("@id")));
	}

	/**
	 * @param bcb
	 * @return
	 */
	public static void buildMapBcB(BcbBuilder bcb, Project project, int id) {
		// add map
		ProjectTabEvent.buildBcb(bcb, project, RGSS1Helper.getTabID(Type.MAP));

		bcb.addSeperator();

		// add all maps
		for (int i = 1, l = RGSS1Helper.get(project, Type.MAP).size(); i < l; i++) {
			bcb.addElement(RGSS1Helper.get(project, Type.MAP).get(i).getName(), RGSS1Helper.getIcon(Type.MAP), getId(project, i), i == id);
		}
	}

	/**
	 * Get it
	 * 
	 * @param p
	 * @param map
	 * @param event
	 * @return
	 */
	public static String getId(Project p, int map) {
		return TabEvent.buildParameter(ID, p, null, "map", Integer.toString(map));
	}

}
