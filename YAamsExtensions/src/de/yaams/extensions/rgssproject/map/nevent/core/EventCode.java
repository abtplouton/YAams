/**
 * 
 */
package de.yaams.extensions.rgssproject.map.nevent.core;

import java.util.ArrayList;
import java.util.Collections;

import org.jruby.RubyArray;
import org.jruby.RubyObject;
import org.jruby.runtime.builtin.IRubyObject;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.map.nevent.YEventCommandList;
import de.yaams.maker.helper.gui.form.core.FormBuilder;

/**
 * @author abt
 * 
 */
public class EventCode {

	protected EventCommand command;
	protected boolean modified;
	protected RubyObject object;
	protected ArrayList<IRubyObject> parameters;
	protected int id, indent;
	protected YEventCommandList yecl;
	protected String titleCache;

	/**
	 * Create a new EventCode
	 * 
	 * @param command
	 * @param object
	 */
	public EventCode(EventCommand command, RubyObject object, YEventCommandList yecl) {
		super();
		modified = false;
		this.command = command;
		this.object = object;
		this.yecl = yecl;
		id = Integer.valueOf(object.getInstanceVariable("@code").toString());
		indent = Integer.valueOf(object.getInstanceVariable("@indent").toString());

		// add parameter
		parameters = new ArrayList<IRubyObject>();
		Collections.addAll(parameters, ((RubyArray) object.getInstanceVariable("@parameters")).toJavaArray());

	}

	/**
	 * @return the command
	 */
	public EventCommand getCommand() {
		return command;
	}

	/**
	 * @param command
	 *            the command to set
	 */
	public void setCommand(EventCommand command) {
		this.command = command;
	}

	/**
	 * Build the panel for this element
	 * 
	 * @return
	 */
	public void buildPanel(FormBuilder f) {
		command.buildPanel(f, this);
	}

	/**
	 * @return the modified
	 */
	public boolean isModified() {
		return modified;
	}

	/**
	 * @return the object
	 */
	public RubyObject getObject() {
		return object;
	}

	/**
	 * @param modified
	 *            the modified to set
	 */
	public void setModified(boolean modified) {
		this.modified = modified;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return command.getTitle(this);
	}

	/**
	 * @return the parameters
	 */
	public ArrayList<IRubyObject> getParameters() {
		return parameters;
	}

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
	 * @return the indent
	 */
	public int getIndent() {
		return indent;
	}

	/**
	 * @param indent
	 *            the indent to set
	 */
	public void setIndent(int indent) {
		this.indent = indent;
	}

	/**
	 * @return the yecl
	 */
	public YEventCommandList getYecl() {
		return yecl;
	}

	/**
	 * @return the titleCache
	 */
	public String getTitleCache() {
		return titleCache;
	}

	/**
	 * @param titleCache
	 *            the titleCache to set
	 */
	public void setTitleCache(String titleCache) {
		this.titleCache = titleCache;
	}

	/**
	 * Save the settings back to ruby
	 */
	public void saveBack() {
		modified = false;

		// set basics
		RubyHelper.setNum(object, "@code", id);
		RubyHelper.setNum(object, "@indent", indent);

		// set parameters
		RubyArray para = (RubyArray) object.getInstanceVariable("@parameters");
		para.clear();

		for (IRubyObject r : parameters) {
			para.add(r);
		}
	}

}
