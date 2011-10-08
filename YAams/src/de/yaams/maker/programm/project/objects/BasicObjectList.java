/**
 * 
 */
package de.yaams.maker.programm.project.objects;

import de.yaams.maker.helper.gui.list.YBasisListElementArrayList;

/**
 * @author abby
 * 
 */
public class BasicObjectList extends YBasisListElementArrayList {

	private static final long serialVersionUID = -7778594417471309017L;

	protected transient BasicObjectManager bom;
	protected BasicObjectTab bot;

	/**
	 * @param ary
	 */
	public BasicObjectList(BasicObjectManager bom, BasicObjectTab bot) {
		super(bom.getObjects());

		this.bom = bom;
		this.bot = bot;

		add = true;
		delete = true;
		buildToolbar(bom.getTitle(), bom.getIcon());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.gui.list.YSimpleList#add()
	 */
	@Override
	protected void add() {
		add(bom.createNewObject());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.gui.list.YSimpleList#info()
	 */
	@Override
	protected void info() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.gui.list.YSimpleList#configIntern()
	 */
	@Override
	protected void configIntern() {
		bot.buildInternContent(getSelectedObject());
	}

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
