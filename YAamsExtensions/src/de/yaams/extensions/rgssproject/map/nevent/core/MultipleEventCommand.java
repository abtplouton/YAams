/**
 * 
 */
package de.yaams.extensions.rgssproject.map.nevent.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.SystemUtils;
import org.jruby.RubyArray;
import org.jruby.RubyFixnum;
import org.jruby.RubyObject;
import org.jruby.RubyString;
import org.jruby.runtime.builtin.IRubyObject;

import de.yaams.extensions.rgssproject.RGSSProjectHelper;
import de.yaams.extensions.rgssproject.map.nevent.YEventCommandList;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormTextArea;

/**
 * @author abt
 * 
 */
public abstract class MultipleEventCommand extends EventCommand {

	protected int id2;

	/**
	 * Create a new MultipleEventCommand
	 * 
	 * @param id2
	 */
	public MultipleEventCommand(int id2) {
		super();
		this.id2 = id2;
	}

	/**
	 * Build from raw list the view list
	 * 
	 * @param list
	 * @param os
	 * @param firstObject
	 */
	@Override
	public void rawToView(ArrayList<EventCode> list, List<IRubyObject> os, RubyObject firstObject, YEventCommandList yecl) {
		super.rawToView(list, os, firstObject, yecl);

		// has 2. parameter?
		while (os.size() > 0 && Integer.valueOf(os.get(0).getInstanceVariables().getInstanceVariable("@code").toString()) == id2) {
			// add it
			list.get(list.size() - 1)
					.getParameters()
					.add(RubyString.newString(os.get(0).getRuntime(), (String) ((RubyArray) os.get(0).getInstanceVariables()
							.getInstanceVariable("@parameters")).get(0)));

			// remove it
			os.remove(0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.extensions.map.nevent.core.EventCommand#getTitle(de.yaams.extensions
	 * .map.nevent.core.EventCode)
	 */
	@Override
	protected String getInternTitle(EventCode e) {
		// build list
		if (e.getParameters().size() == 1) {
			return e.getParameters().get(0).toString();
		} else {
			StringBuilder s = new StringBuilder(e.getParameters().get(0).toString()
					+ (e.getParameters().size() > 1 ? " *" + e.getParameters().size() : ""));

			// for (IRubyObject i : e.getParameters()) {
			// s.append(i.toString());
			// s.append("<br>");
			// s.append(SystemUtils.LINE_SEPARATOR);
			// };
			return s.toString();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getStartParameter()
	 */
	@Override
	public String getStartParameter() {
		return "\"\"";
	}

	/**
	 * Build from view list the raw list
	 * 
	 * @param list
	 * @param rawList
	 * @param firstObject
	 */
	@Override
	public void viewToRaw(ArrayList<EventCode> list, List<Object> rawList, EventCode code) {

		// create for every parameter a new event raw code
		for (int i = 0, l = code.getParameters().size(); i < l; i++) {
			RubyObject obj;
			int nid = code.getId();
			int indent = code.getIndent();

			// first code?
			if (i == 0) {
				// get obj
				obj = code.getObject();
			} else {
				obj = (RubyObject) RGSSProjectHelper.getInterpreter(code.getYecl().getProject()).runScriptlet(
						"return RPG::EventCommand.new(" + id + ")");
				indent = code.getIndent();
				nid = id2;
			}

			// update indent
			obj.setInstanceVariable("@indent", new RubyFixnum(code.getObject().getRuntime(), indent));

			// update id
			obj.setInstanceVariable("@code", new RubyFixnum(code.getObject().getRuntime(), nid));

			// set parameter
			RubyArray r = (RubyArray) code.getObject().getInstanceVariable("@parameters");
			r.rb_clear();
			r.add(code.getParameters().get(i));
			rawList.add(obj);

		}
	}

	protected FormTextArea getArea(String name, final EventCode e) {
		// build text
		StringBuilder s = new StringBuilder("");

		for (IRubyObject i : e.getParameters()) {
			// add break
			if (s.length() > 0) {
				s.append(SystemUtils.LINE_SEPARATOR);
			}
			s.append(i.toString());
		}

		// get area
		return (FormTextArea) new FormTextArea(name, s.toString()).addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement form) {
				e.getParameters().clear();
				// build list
				String[] s = form.getContentAsString().split(SystemUtils.LINE_SEPARATOR);
				for (int i = 0, l = s.length; i < l; i++) {
					e.getParameters().add(RubyString.newString(e.getObject().getRuntime(), s[i]));
				}
			}
		});
	}
}
