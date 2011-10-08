/**
 * 
 */
package de.yaams.extensions.rgssproject.map.event;

import org.jruby.RubyObject;

import de.yaams.extensions.rgssproject.database.SystemGObject;
import de.yaams.maker.helper.gui.list.YSimpleList;

/**
 * @author abt
 * 
 */
public class EventPageList extends YSimpleList<Integer> {

	private static final long serialVersionUID = -6652256367688017210L;
	protected EventTab tab;

	/**
	 * Create a new GList
	 * 
	 * @param ary
	 */
	public EventPageList(final EventTab tab) {
		super();
		this.tab = tab;

		// build list
		add = true;
		delete = true;
		buildToolbar("Seite", "eventpage");
		buildGui();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#add()
	 */
	@Override
	protected void add() {
		// ask the tab
		RubyObject o = tab.createObject();

		// add it
		tab.getElements().add(new SystemGObject(o));
		tab.getElements().get(tab.getElements().size() - 1).setModified(true);
		add(tab.getElements().size() - 1);
		tab.setModified(true);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.packandgo.helper.gui.list.YSimpleList#getIcon(java.lang.Object)
	 */
	@Override
	public Object getIcon(final Integer o) {
		return tab.getIcon(o);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.packandgo.helper.gui.list.YSimpleList#isModified(java.lang.Object
	 * )
	 */
	@Override
	public boolean isModified(final Integer o) {
		return tab.isModified(o);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.packandgo.helper.gui.list.YSimpleList#getText(java.lang.Object )
	 */
	@Override
	public Object getText(final Integer value) {
		return tab.getText(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#info()
	 */
	@Override
	protected void info() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.packandgo.helper.gui.list.YSimpleList#getDesc(java.lang.Object)
	 */
	@Override
	public String getDesc(final Integer o) {
		return tab.getDesc(o);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#configIntern()
	 */
	@Override
	protected void configIntern() {
		tab.buildInternContent(getSelectedObject());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#getObjectSize()
	 */
	@Override
	public int getObjectSize() {
		return tab.getElements().size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.packandgo.helper.gui.list.YSimpleList#getObject(java.lang.Object
	 * )
	 */
	@Override
	public Integer getObject(final int id) {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.packandgo.helper.gui.list.YSimpleList#delObject(java.lang.Object
	 * )
	 */
	@Override
	public void delObject(final int id) {
		tab.delObject(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.packandgo.helper.gui.list.YSimpleList#addObject(java.lang.Object
	 * )
	 */
	@Override
	public void addObject(Integer object, int id) {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#oneClick()
	 */
	@Override
	protected void selected() {
		config();
	}

}
