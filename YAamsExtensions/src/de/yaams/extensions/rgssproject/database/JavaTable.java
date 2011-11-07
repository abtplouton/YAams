/**
 * 
 */
package de.yaams.extensions.rgssproject.database;

import org.jruby.RubyArray;
import org.jruby.RubyObject;
import org.jruby.runtime.builtin.IRubyObject;

import de.yaams.extensions.jruby.RubyHelper;

/**
 * Wrapperclass for Ruby Table
 * 
 * @author abby
 * 
 */
public class JavaTable {

	protected RubyObject table;
	protected int x, y, z;

	/**
	 * 
	 */
	public JavaTable(IRubyObject table) {
		this.table = (RubyObject) table;
		x = RubyHelper.toInt(table, "@xsize");
		y = RubyHelper.toInt(table, "@ysize");
		z = RubyHelper.toInt(table, "@zsize");
	}

	/**
	 * Get Data
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public int get(int x, int y, int z) {
		RubyArray data = (RubyArray) table.getInstanceVariable("@data");
		return Integer.valueOf(data.get(x + y * this.y + z * this.z * this.y).toString());
	}

	/**
	 * Set Data
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public void set(int x, int y, int z, long data) {
		RubyArray holder = (RubyArray) table.getInstanceVariable("@data");
		RubyHelper.setNum(holder, x + y * this.y + z * this.z * this.y, Long.valueOf(data));
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(int x) {
		this.x = x;
		RubyHelper.setNum(table, "@xsize", x);
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(int y) {
		this.y = y;
		RubyHelper.setNum(table, "@ysize", y);
	}

	/**
	 * @return the z
	 */
	public int getZ() {
		return z;
	}

	/**
	 * @param z
	 *            the z to set
	 */
	public void setZ(int z) {
		this.z = z;
		RubyHelper.setNum(table, "@zsize", z);
	}

}
