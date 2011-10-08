/**
 * 
 */
package de.yaams.extensions.rgssproject.database;

import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.maker.programm.project.objects.SingleObjectManager;

/**
 * @author abby
 * 
 */
public class DatabaseObjectManager extends SingleObjectManager {

	protected Type type;
	protected String group;

	/**
	 * @param title
	 * @param desc
	 * @param icon
	 */
	public DatabaseObjectManager(Type type, String group) {
		super(RGSS1Helper.getName(type), null, RGSS1Helper.getIcon(type));

		this.type = type;
		this.group = group;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectManager#getTabUId()
	 */
	@Override
	public String getTabUId() {
		return RGSS1Helper.getTabID(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.programm.project.objects.BasicObjectManager#getGroup()
	 */
	@Override
	public String getGroup() {
		return group;
	}

}
