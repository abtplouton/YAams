/**
 * /**
 * 
 */
/**
 * /**
 * 
 */
package de.yaams.extensions.rgssproject.database;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.jruby.RubyArray;
import org.jruby.RubyClass;
import org.jruby.RubyFixnum;
import org.jruby.RubyHash;
import org.jruby.RubyObject;
import org.jruby.RubyString;
import org.jruby.runtime.builtin.IRubyObject;

import de.yaams.extensions.rgssproject.RGSSProjectHelper;
import de.yaams.extensions.rgssproject.RTP;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.ress.RessRess;

/**
 * Load all RGSS Classes in the project
 * 
 * @author abt
 * 
 */
public class RGSS1Helper {

	private static HashMap<String, HashMap<String, Object>> icons = new HashMap<String, HashMap<String, Object>>();

	public enum Type {
		ACTOR, CLASS, ITEM, WEAPON, ARMOR, ANIMATION, MAP, COMMONEVENT, TILESET, SYSTEM, SKILL, ENEMY, STATUS, TROOP, VARIABLE, SWITCH, ELEMENT
	};

	/**
	 * Load Animations
	 * 
	 * @param project
	 * @return
	 */
	public static ArrayList<SystemGObject> get(Project project, Type type) {
		// check it
		Validate.notNull(project, "Project is null");
		Validate.notNull(type, "Type is null");

		// create id
		String id = getIcon(type);

		// contains in project?
		if (project.getCache().containsKey(id)) {
			return (ArrayList<SystemGObject>) project.getCache().get(id);
		}

		// special objects?
		if (type == Type.VARIABLE || type == Type.SWITCH || type == Type.ELEMENT) {
			// load system
			RubyObject system = get(project, Type.SYSTEM).get(0).getObject();

			// create list
			ArrayList<SystemGObject> list = new ArrayList<SystemGObject>();
			list.add(null);
			// load vars
			IRubyObject[] ary = ((RubyArray) system.getInstanceVariable(type == Type.VARIABLE ? "@variables"
					: type == Type.ELEMENT ? "@elements" : "@switches")).toJavaArray();

			boolean first = true;

			// add vars
			for (IRubyObject i : ary) {
				// skip first
				if (first) {
					first = false;
					continue;
				}

				// build Object
				RubyObject o = new RubyObject(RubyClass.newClass(system.getRuntime(), system.getMetaClass().getSuperClass()));
				o.setInstanceVariable("@id", RubyFixnum.newFixnum(system.getRuntime(), list.size()));
				o.setInstanceVariable("@name", i);
				list.add(new SystemGObject(o));
			}

			// save
			project.getCache().put(id, list);

			return list;
		} else if (type == Type.MAP) {
			final RubyHash r = (RubyHash) RGSS1Load.loadFile(project, new File(RGSS1Load.getDataFile(project), "MapInfos.rxdata"));
			Set<?> set = r.keySet();

			int max = 0;

			HashMap<Integer, RubyObject> o = new HashMap<Integer, RubyObject>();
			o.put(0, null);

			// build the list
			for (Object i : set) {
				int roid = Integer.valueOf(i.toString());
				o.put(roid, (RubyObject) RGSS1Load.loadFile(project, RGSS1Load.getMapFile(project, roid)));
				o.get(roid).setInstanceVariable("@id", RubyFixnum.newFixnum(o.get(roid).getRuntime(), roid));
				o.get(roid).setInstanceVariable("@name", ((RubyObject) r.get(roid)).getInstanceVariable("@name"));

				// search for the hightes id
				if (roid > max) {
					max = roid;
				}
			}

			// build the list
			ArrayList<SystemGObject> ro = new ArrayList<SystemGObject>();
			for (int i = 0; i <= max; i++) {
				// add it
				if (o.containsKey(i)) {
					ro.add(new SystemGObject(o.get(i)));
				} else {
					// build filler
					RubyObject no = (RubyObject) RGSSProjectHelper.getInterpreter(project).runScriptlet("return RPG::Map.new(20,15)");
					no.setInstanceVariable("@name", RubyString.newString(r.getRuntime(), "Fill " + i));
					no.setInstanceVariable("@id", RubyFixnum.newFixnum(o.get(i).getRuntime(), i));

					// add filler
					ro.add(new SystemGObject(no));
				}

			}

			// save
			project.getCache().put(id, ro);

			return ro;

		} else if (type == Type.SYSTEM) {
			// load
			RubyObject o = (RubyObject) RGSS1Load.loadFile(project, new File(RGSS1Load.getDataFile(project), "System.rxdata"));

			// put in array
			ArrayList<SystemGObject> objs = new ArrayList<SystemGObject>();
			objs.add(new SystemGObject(o));
			project.getCache().put(id, objs);

			// get
			return objs;

		} else {

			// open code
			final Object[] objects = ((RubyArray) RGSS1Load.loadFile(project, new File(RGSS1Load.getDataFile(project), getFileName(type)
					+ ".rxdata"))).toArray();

			// convert code
			ArrayList<SystemGObject> objs = new ArrayList<SystemGObject>();
			for (Object o : objects) {
				objs.add(new SystemGObject((RubyObject) o));
			}

			// load
			project.getCache().put(id, objs);

			// get
			return objs;
		}
	}

	/**
	 * Get Tab ID
	 */
	public static String getTabID(Type type) {
		return "rgssproject." + type.toString();
	}

	/**
	 * Get singular name
	 * 
	 * @param type
	 * @param id
	 * @return
	 */
	public static String getDesc(Project project, Type type, int id) {
		// check
		Validate.notNull(project, "Project is null");
		Validate.isTrue(id >= 0, "ID " + id + " is negative");

		// load basic
		SystemGObject obj = get(project, type).get(id);

		switch (type) {
		case ACTOR:
			int clas = Integer.valueOf(obj.getObject().getInstanceVariable("@class_id").toString());

			return RGSS1Helper.get(project, Type.CLASS).get(clas).getName();
		case TILESET:
			final String file = obj.getObject().getInstanceVariable("@tileset_name").toString();

			// has icon?
			if (file == null || file.length() == 0) {
				return "-";
			}
			final BufferedImage i = RessRess.getGraphic(project, RTP.TILESET, file);

			return I18N.t("{0}x{1}", i.getWidth() / 32, i.getHeight() / 32);
		case ENEMY:
			return I18N.t("HP:{0} SP:{0}", obj.getObject().getInstanceVariable("@maxhp").toString(),
					obj.getObject().getInstanceVariable("@maxsp").toString());
		default:
			return null;

		}
	}

	/**
	 * Get singular name
	 * 
	 * @param type
	 * @param id
	 * @return
	 */
	public static String getName(Type type) {
		switch (type) {
		case ACTOR:
			return "Actor";
		case CLASS:
			return "Class";
		case ITEM:
			return "Item";
		case WEAPON:
			return "Weapon";
		case VARIABLE:
			return "Variable";
		case SWITCH:
			return "Switch";
		case ARMOR:
			return "Armor";
		case ANIMATION:
			return "Animation";
		case TILESET:
			return "Tileset";
		case MAP:
			return "Map";
		case COMMONEVENT:
			return "CommonEvent";
		case TROOP:
			return "Troop";
		case SYSTEM:
			return "System";
		case SKILL:
			return "Skill";
		case ENEMY:
			return "Enemy";
		case STATUS:
			return "Status";
		case ELEMENT:
			return "Element";

		}

		// show info & return default
		YEx.info("Name for " + type + " is not support", new IllegalArgumentException("getName:" + type));
		return "?" + type.toString() + "?";
	}

	/**
	 * Get plural name
	 * 
	 * @param type
	 * @param id
	 * @return
	 */
	public static String getIcon(Type type) {
		switch (type) {
		case ACTOR:
			return "hero";
		case CLASS:
			return "class";
		case ITEM:
			return "item";
		case WEAPON:
			return "weapon";
		case ARMOR:
			return "armor";
		case ANIMATION:
			return "animation";
		case TILESET:
			return "tileset";
		case VARIABLE:
			return "variable";
		case SWITCH:
			return "switch";
		case MAP:
			return "map";
		case COMMONEVENT:
			return "event";
		case TROOP:
			return "troop";
		case SYSTEM:
			return "opts";
		case SKILL:
			return "skill";
		case ENEMY:
			return "enemy";
		case STATUS:
			return "status";
		case ELEMENT:
			return "element";

		}

		// show info & return default
		YEx.info("Icons for " + type + " is not support", new IllegalArgumentException("getIcon:" + type));
		return "dummy";
	}

	/**
	 * Get the spezific Icon
	 * 
	 * @param project
	 * @param type
	 * @param id
	 * @return
	 */
	public static Object getIcon(Project project, Type type, int id) {
		// right type?
		if (type == Type.COMMONEVENT || type == Type.CLASS || type == Type.ANIMATION || type == Type.TROOP || type == Type.VARIABLE
				|| type == Type.MAP || type == Type.SWITCH || type == Type.SYSTEM || type == Type.STATUS || type == Type.ELEMENT) {
			return getIcon(type);
		}

		// check
		Validate.notNull(project, "Project is null");
		Validate.isTrue(id >= 0, "ID " + id + " is negative");

		// exist?
		if (!icons.containsKey(type.toString())) {
			icons.put(type.toString(), new HashMap<String, Object>());
		}

		// get name
		String name = null;
		switch (type) {

		case ACTOR:
			name = "@character_name";
			break;
		case SKILL:
		case ITEM:
		case WEAPON:
		case ARMOR:
			name = "@icon_name";
			break;
		case TILESET:
			name = "@tileset_name";
			break;
		case ENEMY:
			name = "@battler_name";
			break;
		default:
			// show info & return default
			YEx.info("Ind. Icons for " + type + " is not support", new IllegalArgumentException("getIcon:" + type));
			return "dummy";
		}

		// load basic
		SystemGObject obj = get(project, type).get(id);
		HashMap<String, Object> cache = icons.get(type.toString());

		// load file
		String file = obj.getObject().getInstanceVariable(name).toString();

		// has icon?
		if (file == null || file.length() == 0) {
			return getIcon(type);
		}

		// load it?
		if (!cache.containsKey(file)) {
			switch (type) {
			case ACTOR:
				// load it
				BufferedImage i = RessRess.getGraphic(project, RTP.CHARACTER, file);
				BufferedImage b = i.getSubimage(0, 0, i.getWidth() / 4, i.getHeight() / 4);
				cache.put(file, b);
				break;

			case SKILL:
			case ITEM:
			case WEAPON:
			case ARMOR:
				// load it
				cache.put(file, RessRess.getGraphic(project, RTP.ICON, file));
				break;

			case TILESET:
				// load it
				i = RessRess.getGraphic(project, RTP.TILESET, file).getSubimage(0, 0, 96, 96);
				b = new BufferedImage(32, 32, BufferedImage.TYPE_4BYTE_ABGR);
				b.getGraphics().drawImage(i, 0, 0, 32, 32, null);
				cache.put(file, b);
				break;

			case ENEMY:
				// load it
				cache.put(file, RessRess.getGraphic(project, RTP.BATTLERS, file));
				break;

			default:
				// show info & return default
				YEx.info("Ind. Icons for " + type + " is not support", new IllegalArgumentException("getIcon:" + type));
				return "dummy";
			}
		}

		return cache.get(file);
	}

	/**
	 * Get plural name
	 * 
	 * @param type
	 * @param id
	 * @return
	 */
	public static String getNames(Type type) {
		switch (type) {
		case ACTOR:
			return "Actors";
		case CLASS:
			return "Classes";
		case ITEM:
			return "Items";
		case WEAPON:
			return "Weapons";
		case ARMOR:
			return "Armors";
		case ANIMATION:
			return "Animations";
		case TILESET:
			return "Tilesets";
		case SWITCH:
			return "Switchs";
		case VARIABLE:
			return "Variables";
		case MAP:
			return "Maps";
		case COMMONEVENT:
			return "CommonEvents";
		case TROOP:
			return "Troops";
		case SYSTEM:
			return "System";
		case SKILL:
			return "Skills";
		case ENEMY:
			return "Enemies";
		case STATUS:
			return "State";
		case ELEMENT:
			return "Attributes";

		}

		// show info & return default
		YEx.info("Names for " + type + " is not support", new IllegalArgumentException("getNames:" + type));
		return "?" + type.toString() + "s?";
	}

	/**
	 * Get Filename
	 * 
	 * @param type
	 * @param id
	 * @return
	 */
	public static String getFileName(Type type) {
		switch (type) {
		case ACTOR:
			return "Actors";
		case CLASS:
			return "Classes";
		case ITEM:
			return "Items";
		case WEAPON:
			return "Weapons";
		case ARMOR:
			return "Armors";
		case ANIMATION:
			return "Animations";
		case TILESET:
			return "Tilesets";
		case MAP:
			return "MapInfos";
		case COMMONEVENT:
			return "CommonEvents";
		case TROOP:
			return "Troops";
		case SYSTEM:
			return "System";
		case SKILL:
			return "Skills";
		case ENEMY:
			return "Enemies";
		case STATUS:
			return "States";

		}

		// show info & return default
		YEx.info("FileName for " + type + " is not support", new IllegalArgumentException("getFileName:" + type));
		return "?" + type.toString() + "?";
	}

	/**
	 * Save the loaded elementes
	 * 
	 * @param project
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static void save(Project project, Type type) {
		// create id
		String id = getFileName(type);

		// contains in project?
		if (!project.getCache().containsKey(id)) {
			// dont do something
			return;
		}

		// load it
		ArrayList<SystemGObject> aro = (ArrayList<SystemGObject>) project.getCache().get(id);

		// remove modizized
		for (SystemGObject sgo : aro) {
			sgo.setModified(false);
		}

		// special objects?
		if (type == Type.VARIABLE || type == Type.SWITCH || type == Type.ELEMENT) {
			save(project, Type.SYSTEM);
		} else if (type == Type.MAP) {
			// run over the array and save all maps
			for (int i = 1, l = aro.size(); i < l; i++) {
				RGSS1Load.saveMapData(project, aro.get(i).getObject());
			}

			// get MapInfo
			File file = new File(RGSS1Load.getDataFile(project), "MapInfos.rxdata");

			// get it
			RubyHash mapinfos = (RubyHash) RGSS1Load.loadFile(project, file);

			// run over all
			HashMap<Integer, Boolean> idCache = new HashMap<Integer, Boolean>();

			// update elements & add missings
			for (int i = 1, l = aro.size(); i < l; i++) {
				mapinfos.put(aro.get(i).getObject().getInstanceVariable("@id"), aro.get(i).getObject());
				idCache.put((Integer) aro.get(i).getObject().getInstanceVariable("@id").toJava(Integer.class), true);
			}

			// remove unused
			for (int i = 1, l = aro.size(); i < l; i++) {
				int nid = (Integer) aro.get(i).getObject().getInstanceVariable("@id").toJava(Integer.class);
				RubyFixnum rid = (RubyFixnum) aro.get(i).getObject().getInstanceVariable("@id");
				if (mapinfos.containsKey(rid) && !idCache.containsKey(nid)) {
					mapinfos.remove(mapinfos.get(rid));
				}
			}

			// save it
			RGSS1Load.saveFile(project, file, mapinfos);

		} else if (type == Type.SYSTEM) {

			// add vars
			addChild(project, aro, Type.VARIABLE, "@elements");

			// add switch
			addChild(project, aro, Type.SWITCH, "@switches");

			// add attributs
			addChild(project, aro, Type.ELEMENT, "@variables");

			// save it
			RGSS1Load.saveFile(project, new File(RGSS1Load.getDataFile(project), id + ".rxdata"), aro.get(0).getObject());

		} else {
			// convert array
			ArrayList<RubyObject> ra = new ArrayList<RubyObject>();

			// add dummy element
			ra.add(RubyFixnum.newFixnum(RGSSProjectHelper.getInterpreter(project).getRuntime(), 0));

			for (SystemGObject sgo : aro) {
				if (sgo != null && sgo.getObject() != null) {
					ra.add(sgo.getObject());
				}
			}

			// convert the array
			RubyArray r = RubyArray.newArray(RGSSProjectHelper.getInterpreter(project).getRuntime(),
					ra.toArray(new IRubyObject[aro.size()]));

			// save it
			RGSS1Load.saveFile(project, new File(RGSS1Load.getDataFile(project), id + ".rxdata"), r);
		}

	}

	/**
	 * @param project
	 * @param aro
	 */
	private static void addChild(Project project, ArrayList<SystemGObject> aro, Type type, String var) {
		ArrayList<SystemGObject> list;
		RubyArray r;
		// load it
		list = get(project, type);

		// build list
		r = RubyArray.newArray(list.get(1).getObject().getRuntime(), list.size());

		// add dummy element
		r.add(RubyFixnum.newFixnum(RGSSProjectHelper.getInterpreter(project).getRuntime(), 0));

		// add
		for (SystemGObject sgo : list) {
			if (sgo != null) {
				r.add(sgo.getObject().getInstanceVariable("@name"));
			}
		}

		// save switch
		aro.get(0).getObject().setInstanceVariable(var, r);
	}
}
