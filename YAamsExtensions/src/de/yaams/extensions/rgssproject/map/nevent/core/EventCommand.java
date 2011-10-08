/**
 * 
 */
package de.yaams.extensions.rgssproject.map.nevent.core;

import java.util.ArrayList;
import java.util.List;

import org.jruby.RubyBoolean;
import org.jruby.RubyFixnum;
import org.jruby.RubyObject;
import org.jruby.RubyString;
import org.jruby.runtime.builtin.IRubyObject;

import de.yaams.extensions.rgssproject.RGSSProjectHelper;
import de.yaams.extensions.rgssproject.map.nevent.YEventCommandList;
import de.yaams.maker.helper.gui.form.core.FormBuilder;

/**
 * @author abt
 * 
 */
public abstract class EventCommand {

	protected int id;

	/**
	 * Create a new YBaseEventCodeManager
	 */
	public EventCommand() {}

	/**
	 * Get the title for the event command
	 * 
	 * @param e
	 * @return
	 */
	public String getTitle(EventCode e) {
		// has no cache?
		if (e.getTitleCache() == null) {
			e.setTitleCache(getInternTitle(e));
		}
		return e.getTitleCache();
	}

	/**
	 * Get the title for the event command
	 * 
	 * @param e
	 * @return
	 */
	protected abstract String getInternTitle(EventCode e);

	/**
	 * Build from raw list the view list
	 * 
	 * @param list
	 * @param os
	 * @param firstObject
	 */
	public void rawToView(ArrayList<EventCode> list, List<IRubyObject> os, RubyObject firstObject, YEventCommandList yecl) {
		list.add(new EventCode(this, firstObject, yecl));
		os.remove(0);
	}

	/**
	 * Build from view list the raw list
	 * 
	 * @param list
	 * @param rawList
	 * @param firstObject
	 */
	public void viewToRaw(ArrayList<EventCode> list, List<Object> rawList, EventCode code) {
		code.saveBack();

		// remove the first object
		rawList.add(code.getObject());
		list.remove(0);
	}

	/**
	 * Create a new EventCode
	 */
	public void createNew(YEventCommandList yecl, int indent) {
		// build it
		EventCode e = new EventCode(this, (RubyObject) RGSSProjectHelper.getInterpreter(yecl.getProject()).runScriptlet(
				"return RPG::EventCommand.new(" + id + "," + indent + ",[" + getStartParameter() + "])"), yecl);

		// add it
		yecl.getList().add(yecl.getList().getList().getSelectedIndex(), e);
		yecl.saveBack();
	}

	/**
	 * Helpermethod to get the content of the start parameter array.
	 * 
	 * @return
	 */
	protected abstract String getStartParameter();

	/**
	 * Get the icon
	 * 
	 * @return
	 */
	public abstract String getIcon();

	/**
	 * Get the icon
	 * 
	 * @return
	 */
	public abstract Object getIcon(EventCode e);

	/**
	 * Get the translated name
	 * 
	 * @return
	 */
	public abstract String getName();

	/**
	 * Get the translated group
	 * 
	 * @return
	 */
	public abstract String getGroup();

	/**
	 * Build the panel for this element
	 * 
	 * @return
	 */
	public abstract void buildPanel(FormBuilder f, EventCode e);

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Helpermethod to add a missing boolean in the parameters
	 * 
	 * @param EventCode
	 * @param index
	 * @param standard
	 *            Value
	 */
	protected void addBoolean(final EventCode e, int index, boolean std) {
		if (e.getParameters().size() <= index) {
			e.getParameters().add(new RubyBoolean(e.getObject().getRuntime(), std));
		}
	}

	/**
	 * Helpermethod to add a missing string in the parameters
	 * 
	 * @param EventCode
	 * @param index
	 * @param standard
	 *            Value
	 */
	protected void addString(final EventCode e, int index, String std) {
		if (e.getParameters().size() <= index) {
			e.getParameters().add(RubyString.newString(e.getObject().getRuntime(), std));
		}
	}

	/**
	 * Helpermethod to add a missing integer in the parameters
	 * 
	 * @param EventCode
	 * @param index
	 * @param standard
	 *            Value
	 */
	protected void addInt(final EventCode e, int index, long std) {
		if (e.getParameters().size() <= index) {
			e.getParameters().add(RubyFixnum.int2fix(e.getObject().getRuntime(), std));
		}
	}

}
