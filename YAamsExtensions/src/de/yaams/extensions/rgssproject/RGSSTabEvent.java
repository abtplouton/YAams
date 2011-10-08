/**
 * 
 */
package de.yaams.extensions.rgssproject;

import java.util.HashMap;

import org.jruby.RubyHash;
import org.jruby.RubyObject;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.tabs.ActorTab;
import de.yaams.extensions.rgssproject.database.tabs.AnimationTab;
import de.yaams.extensions.rgssproject.database.tabs.ArmorTab;
import de.yaams.extensions.rgssproject.database.tabs.ClassTab;
import de.yaams.extensions.rgssproject.database.tabs.CommonEventTab;
import de.yaams.extensions.rgssproject.database.tabs.EnemyTab;
import de.yaams.extensions.rgssproject.database.tabs.ItemTab;
import de.yaams.extensions.rgssproject.database.tabs.MTab;
import de.yaams.extensions.rgssproject.database.tabs.SkillTab;
import de.yaams.extensions.rgssproject.database.tabs.StatusTab;
import de.yaams.extensions.rgssproject.database.tabs.TilesetTab;
import de.yaams.extensions.rgssproject.database.tabs.TroopTab;
import de.yaams.extensions.rgssproject.database.tabs.WeaponTab;
import de.yaams.extensions.rgssproject.map.MapEditorTab;
import de.yaams.extensions.rgssproject.map.event.EventTab;
import de.yaams.extensions.rgssproject.map.event.EventsEditorTab;
import de.yaams.extensions.rgssproject.script.ScriptTab;
import de.yaams.extensions.rgssproject.tab.RGSSRessourceTab;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.tabs.TabEvent;
import de.yaams.maker.programm.tabs.YaTab;

/**
 * @author abby
 * 
 */
public class RGSSTabEvent extends TabEvent {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.programm.tabs.TabEvent#getTab(java.lang.String,
	 * de.yaams.maker.programm.project.Project, java.util.HashMap,
	 * java.util.HashMap)
	 */
	@Override
	public YaTab getTab(String id, Project p, HashMap<String, String> parameters, HashMap<String, String> arguments) {

		// check tabs
		if (ScriptTab.ID.equals(id)) {
			return new ScriptTab(p);
		}

		// register tabs
		if (RGSS1Helper.getTabID(Type.ACTOR).equals(id)) {
			return new ActorTab(p);
		}
		if (RGSS1Helper.getTabID(Type.ANIMATION).equals(id)) {
			return new AnimationTab(p);
		}
		if (RGSS1Helper.getTabID(Type.ARMOR).equals(id)) {
			return new ArmorTab(p);
		}
		if (RGSS1Helper.getTabID(Type.CLASS).equals(id)) {
			return new ClassTab(p);
		}
		if (RGSS1Helper.getTabID(Type.ENEMY).equals(id)) {
			return new EnemyTab(p);
		}
		if (RGSS1Helper.getTabID(Type.ITEM).equals(id)) {
			return new ItemTab(p);
		}
		if (RGSS1Helper.getTabID(Type.SKILL).equals(id)) {
			return new SkillTab(p);
		}
		if (RGSS1Helper.getTabID(Type.STATUS).equals(id)) {
			return new StatusTab(p);
		}
		if (RGSS1Helper.getTabID(Type.TROOP).equals(id)) {
			return new TroopTab(p);
		}
		if (RGSS1Helper.getTabID(Type.WEAPON).equals(id)) {
			return new WeaponTab(p);
		}
		if (RGSS1Helper.getTabID(Type.MAP).equals(id)) {
			return new MTab(p);
		}
		if (RGSS1Helper.getTabID(Type.COMMONEVENT).equals(id)) {
			return new CommonEventTab(p);
		}
		if (RGSS1Helper.getTabID(Type.TILESET).equals(id)) {
			return new TilesetTab(p);
		}

		// ress
		if (RGSSRessourceTab.ID.equals(id)) {
			return new RGSSRessourceTab(p);
		}

		// check for map
		if ((MapEditorTab.ID.equals(id) || EventsEditorTab.ID.equals(id) || EventTab.ID.equals(id)) && parameters.containsKey("map")) {
			RubyObject map = RGSS1Helper.get(p, Type.MAP).get(Integer.valueOf(parameters.get("map"))).getObject();

			// add tab
			if (EventsEditorTab.ID.equals(id)) {
				return new EventsEditorTab(p, map);
			}
			if (EventTab.ID.equals(id)) {
				// search for event
				RubyHash events = (RubyHash) map.getInstanceVariable("@events");
				int eid = Integer.valueOf(parameters.get("event"));

				for (Object key : events.keySet()) {
					// load event
					RubyObject event = (RubyObject) events.get(key);
					// right id?
					if (RubyHelper.toInt(event.getInstanceVariable("@id")) == eid) {
						return new EventTab(p, map, event);
					}
				}

			}

			if (MapEditorTab.ID.equals(id)) {
				return new MapEditorTab(p, map);
			}
		}

		return null;
	}
}
