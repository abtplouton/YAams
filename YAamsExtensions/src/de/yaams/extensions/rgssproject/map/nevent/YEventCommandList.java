/**
 * 
 */
package de.yaams.extensions.rgssproject.map.nevent;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.commons.lang.Validate;
import org.jruby.RubyArray;
import org.jruby.RubyObject;
import org.jruby.runtime.builtin.IRubyObject;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCode;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCommandManagement;
import de.yaams.extensions.rgssproject.map.nevent.core.YEventCodeList;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.programm.project.Project;

/**
 * @author abt
 * 
 */
public class YEventCommandList extends JPanel {

	private static final long serialVersionUID = 3413454715680093666L;

	protected ArrayList<EventCode> codes;
	protected YEventCodeList list;
	protected RubyArray rubylist;
	protected JPanel right;
	protected transient Project project;

	/**
	 * Create a new YEventCommandList
	 */
	public YEventCommandList(IRubyObject list, Project project) {
		super(new GridLayout(1, 1));

		// check it
		Validate.isTrue(list instanceof RubyArray, "List is not RubyArray");

		// set it
		this.project = project;
		this.rubylist = (RubyArray) list;

		// build basis list
		ArrayList<IRubyObject> os = new ArrayList<IRubyObject>();

		Collections.addAll(os, rubylist.toJavaArray());

		// create it
		codes = new ArrayList<EventCode>();

		// convert
		while (os.size() != 0) {
			int size = os.size();

			// get code
			RubyObject r = (RubyObject) os.get(0);

			int id = RubyHelper.toInt(r.getInstanceVariable("@code"));

			EventCommandManagement.get(id).rawToView(codes, os, r, this);

			// nothig happend?
			if (size == os.size()) {
				// add error
				EventCommandManagement.get(-1).rawToView(codes, os, r, this);
			}
		}

		// build gui
		right = new JPanel(new GridLayout(1, 1));
		this.list = new YEventCodeList(codes, this);

		add(YFactory.createHorizontPanel(this.list, right, "event.command"));
	}

	/**
	 * @return the right
	 */
	public JPanel getRight() {
		return right;
	}

	/**
	 * @param right
	 *            the right to set
	 */
	public void setRight(JComponent right) {
		this.right.removeAll();
		this.right.add(right);
		this.right.invalidate();
		this.right.revalidate();
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @return the list
	 */
	public YEventCodeList getList() {
		return list;
	}

	/**
	 * Save the javalist back to the rubylist
	 */
	public void saveBack() {
		rubylist.clear();
		// add all
		for (EventCode e : codes) {
			e.saveBack();
			rubylist.add(e.getObject());
		}
	}

}
