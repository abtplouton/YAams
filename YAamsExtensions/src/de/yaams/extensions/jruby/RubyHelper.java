/**
 * 
 */
package de.yaams.extensions.jruby;

import java.util.ArrayList;

import org.jruby.Ruby;
import org.jruby.RubyArray;
import org.jruby.RubyFixnum;
import org.jruby.RubyObject;
import org.jruby.RubyString;
import org.jruby.runtime.builtin.IRubyObject;

import de.yaams.maker.helper.gui.YEx;

/**
 * @author abt
 * 
 */
public class RubyHelper {

	/**
	 * Helpermethod to set a irb to the longvalue RubyObject & String OR
	 * ArrayList<RubyObject> & index
	 * 
	 * 
	 * @param holder
	 * @param index
	 * @param long, long, int or string
	 */
	public static Ruby getRunTime(Object holder, Object index) {

		// arraylisr and index id?
		if (holder instanceof ArrayList && index instanceof Integer) {
			// get vars
			ArrayList<IRubyObject> ary = (ArrayList<IRubyObject>) holder;
			int id = (Integer) index;

			// remove
			return ary.get(id).getRuntime();
		}

		// RubyArray and index id?
		if (holder instanceof IRubyObject) {
			// remove
			return ((IRubyObject) holder).getRuntime();
		}

		return null;
	}

	/**
	 * Helpermethod to set a irb to the longvalue RubyObject & String OR
	 * ArrayList<RubyObject> & index
	 * 
	 * 
	 * @param holder
	 * @param index
	 * @param value
	 *            = long, int or string
	 */
	public static void setNum(Object holder, Object index, Object value) {

		setObj(holder, index, new RubyFixnum(getRunTime(holder, index), Long.valueOf(value.toString())));
	}

	/**
	 * Helpermethod to set a irb to the longvalue RubyObject & String OR
	 * ArrayList<RubyObject> & index
	 * 
	 * 
	 * @param holder
	 * @param index
	 * @param long, long, int or string
	 */
	public static void setObj(Object holder, Object index, IRubyObject value) {

		// rubyobject and string name?
		if (holder instanceof RubyObject && index instanceof String) {
			// get vars
			RubyObject rb = (RubyObject) holder;
			String var = (String) index;

			// set it
			rb.setInstanceVariable(var, value);

			return;
		}

		// arraylisr and index id?
		if (holder instanceof ArrayList && index instanceof Integer) {
			// get vars
			ArrayList<IRubyObject> ary = (ArrayList<IRubyObject>) holder;
			int id = (Integer) index;

			// remove
			ary.remove(id);

			// set
			ary.add(id, value);

			return;
		}

		// RubyArray and index id?
		if (holder instanceof RubyArray && index instanceof Integer) {
			// get vars
			RubyArray ary = (RubyArray) holder;
			int id = (Integer) index;

			// remove
			ary.remove(id);

			// set
			ary.add(id, value);

			return;
		}

		// nothing?
		YEx.info("Can not set Ruby obj for holder " + holder.getClass() + " and index " + index.getClass(), new IllegalArgumentException(
				"holder " + holder.getClass() + " and index " + index.getClass()));
	}

	/**
	 * Helpermethod to return tne int value of a ruby
	 * 
	 * @param lon
	 * @param irb
	 */
	public static int toInt(IRubyObject irb) {
		return Integer.valueOf(irb.toString());
	}

	/**
	 * Helpermethod to return tne int value of a ruby
	 * 
	 * @param lon
	 * @param irb
	 */
	public static int toInt(Object holder, Object index) {

		// RubyArray and index id?
		if (holder instanceof RubyArray && index instanceof Integer) {
			// get vars
			RubyArray ary = (RubyArray) holder;
			int id = (Integer) index;

			return Integer.valueOf(((Integer) ary.get(id)).toString());
		}

		return Integer.valueOf(getObj(holder, index).toString());
	}

	/**
	 * Helpermethod to return the string value of a ruby
	 * 
	 * @param lon
	 * @param irb
	 */
	public static String toString(Object holder, Object index) {

		return getObj(holder, index).toString();
	}

	/**
	 * Helpermethod to return the string value of a ruby
	 * 
	 * @param lon
	 * @param irb
	 */
	public static IRubyObject getObj(Object holder, Object index) {

		// rubyobject and string name?
		if (holder instanceof RubyObject && index instanceof String) {
			// get vars
			RubyObject rb = (RubyObject) holder;
			String var = (String) index;

			return (IRubyObject) rb.getInternalVariable(var);

		}

		// arraylisr and index id?
		if (holder instanceof ArrayList && index instanceof Integer) {
			// get vars
			ArrayList<IRubyObject> ary = (ArrayList<IRubyObject>) holder;
			int id = (Integer) index;

			return ary.get(id);
		}

		// nothing?
		YEx.info("Can not get obj for Ruby for holder " + holder.getClass() + " and index " + index.getClass(),
				new IllegalArgumentException("holder " + holder.getClass() + " and index " + index.getClass()));

		return null;
	}

	/**
	 * Helpermethod to return the int value of a ruby
	 * 
	 * @param lon
	 * @param irb
	 */
	public static int toInt(RubyObject irb, String var) {
		return toInt(irb.getInstanceVariable(var));
	}

	/**
	 * Helpermethod to return tne int value of a ruby
	 * 
	 * @param lon
	 * @param irb
	 */
	public static long toLong(IRubyObject irb) {
		return Long.valueOf(irb.toString());
	}

	/**
	 * Set content for ruby string from java
	 * 
	 * @param irb
	 * @param text
	 */
	public static void setString(IRubyObject irb, String text) {
		// is string?
		if (irb instanceof RubyString) {
			RubyString rs = (RubyString) irb;
			rs.clear();
			rs.append(RubyString.newString(irb.getRuntime(), text));
		} else {
			throw new IllegalArgumentException(irb + " is not a String");
		}

	}
}
