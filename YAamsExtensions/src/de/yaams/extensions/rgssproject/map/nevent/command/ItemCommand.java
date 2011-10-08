/**
 * 
 */
package de.yaams.extensions.rgssproject.map.nevent.command;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.SystemGObject;
import de.yaams.extensions.rgssproject.database.form.FormDBComboBox;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCode;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCommand;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.FormComboBox;
import de.yaams.maker.helper.gui.form.FormHelper;
import de.yaams.maker.helper.gui.form.core.FormBuilder;

/**
 * @author abt
 * 
 */
public class ItemCommand extends EventCommand {

	protected final String[] types = new String[] { "Gold", "Item", "Weapon", "Armor" };
	protected final Type[] icons = new Type[] { Type.ITEM, Type.ITEM, Type.WEAPON, Type.ARMOR };

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getIcon()
	 */
	@Override
	public String getIcon() {
		return RGSS1Helper.getIcon(icons[2]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.extensions.map.nevent.core.EventCommand#getIcon(de.yaams.extensions
	 * .map.nevent.core.EventCode)
	 */
	@Override
	public Object getIcon(EventCode e) {
		if (e.getId() == 125) {
			return "money";
		} else {
			int iid = RubyHelper.toInt(e.getParameters().get(0));
			return RGSS1Helper.getIcon(e.getYecl().getProject(), icons[e.getId() - 125], iid);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getStartParameter()
	 */
	@Override
	public String getStartParameter() {
		return "1,0,0,0";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getName()
	 */
	@Override
	public String getName() {
		return I18N.t("Bestücke Gruppe");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getGroup()
	 */
	@Override
	public String getGroup() {
		return I18N.t("Party");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.extensions.map.nevent.core.EventCommand#buildPanel(de.yaams.
	 * core.helper.gui.form.core.FormBuilder,
	 * de.yaams.extensions.map.nevent.core.EventCode)
	 */
	@Override
	public void buildPanel(FormBuilder f, EventCode e) {
		// build panel

		FormComboBox c = CommandFormHelper.buildCodeIdChancer(e, I18N.t("Setze"), new String[] { "125", "126", "127", "128" }, types);

		// add operator
		CommandFormHelper.addOperatorSupport(f, e, e.getId() == 125 ? 0 : 1);

		f.addElement("basic.type", c);

		// add selector
		f.addElement("basic.typeitem", FormHelper.setEnabeldWhenRightElementSelect(c, new FormDBComboBox(RGSS1Helper.getName(Type.ITEM), e
				.getYecl().getProject(), Type.ITEM, e.getParameters(), 0, false), "126"));
		f.addElement("basic.typearmor", FormHelper.setEnabeldWhenRightElementSelect(c, new FormDBComboBox(RGSS1Helper.getName(Type.ARMOR),
				e.getYecl().getProject(), Type.ARMOR, e.getParameters(), 0, false), "128"));
		f.addElement("basic.typeweapon", FormHelper.setEnabeldWhenRightElementSelect(c, new FormDBComboBox(
				RGSS1Helper.getName(Type.WEAPON), e.getYecl().getProject(), Type.WEAPON, e.getParameters(), 0, false), "127"));

		c.informListeners();

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
		if (e.getId() == 125) {
			return I18N.t("{0} {1}", CommandFormHelper.getOperatorText(e, 0), types[e.getId() - 125]);
		} else {
			return I18N.t("{0} {1}", CommandFormHelper.getOperatorText(e, 1), getSelectedObject(e).getName());
		}

		// return I18N.t("{1}: {0}", CommandFormHelper.getOperatorText(e,
		// e.getId() == 125 ? 0 : 1), types[e.getId() - 125]);
	}

	/**
	 * Helpermethod to get the selected object
	 * 
	 * @param e
	 * @return
	 */
	protected SystemGObject getSelectedObject(EventCode e) {
		int id = RubyHelper.toInt(e.getParameters().get(0));

		switch (e.getId()) {
		case 126:
			return RGSS1Helper.get(e.getYecl().getProject(), Type.ITEM).get(id);
		case 127:
			return RGSS1Helper.get(e.getYecl().getProject(), Type.WEAPON).get(id);
		case 128:
			return RGSS1Helper.get(e.getYecl().getProject(), Type.ARMOR).get(id);
		default:
			throw new IllegalArgumentException("ID " + e.getId() + " isn't supported");
		}
	}
}
