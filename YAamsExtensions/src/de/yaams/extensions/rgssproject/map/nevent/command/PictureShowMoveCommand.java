/**
 * 
 */
package de.yaams.extensions.rgssproject.map.nevent.command;

import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.extensions.rgssproject.map.form.FormSwitchVarSelector;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCode;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCommand;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.FormComboBox;
import de.yaams.maker.helper.gui.form.FormHelper;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;

/**
 * @author abt
 * 
 */
public class PictureShowMoveCommand extends EventCommand {

	/**
	 * Create a new LabelCommand
	 */
	public PictureShowMoveCommand() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.extensions.map.nevent.core.EventCommand#getTitle(de.yaams.extensions
	 * .map.nevent.core.EventCode)
	 */
	@Override
	protected String getInternTitle(EventCode e) {
		String mess = e.getId() == 231 ? "Bewege Bild {0}" : "Zeige Bild {0}";
		return I18N.t(mess, e.getParameters().get(0).toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getIcon()
	 */
	@Override
	public String getIcon() {
		return "ress";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getStartParameter()
	 */
	@Override
	public String getStartParameter() {
		return "\"\",1,0,0,0,0,100,100";
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
		return getIcon();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getName()
	 */
	@Override
	public String getName() {
		return I18N.t("Bild anzeigen & bewegen");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.map.nevent.core.EventCommand#getGroup()
	 */
	@Override
	public String getGroup() {
		return I18N.t("Bild");
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
	public void buildPanel(FormBuilder f, final EventCode e) {
		f.getHeader("basic").setColumn(4);

		// add type
		f.addElement(
				"basic.typ",
				CommandFormHelper.buildCodeIdChancer(e, I18N.t("Aktion"), new String[] { "119", "118" },
						new String[] { "Anzeigen", "Bewegen" }).setSorting(-1));

		// add name
		f.addElement("basic.nr", RubyForm.getNumber("ID", e.getParameters(), 0).setMinMax(1, 50, 1));

		// add point
		f.addHeader("pos", new FormHeader(I18N.t("Position"), "parameter").setColumn(4));
		f.addElement(
				"pos.rel",
				RubyForm.getComboBoxNum("Ausrichtung", new String[] { "0", "1" }, new String[] { "Oben Linkes Pixel", "Mittige Pixel" },
						e.getParameters(), 2).setSorting(-1));

		FormComboBox c = RubyForm.getComboBoxNum("Koordinaten", new String[] { "0", "1" }, new String[] { "Fest anzeigen",
				"aus Variable laden" }, e.getParameters(), 3);

		// add coordinates
		f.addElement("pos.coor", c);
		f.addElement("pos.coorX",
				FormHelper.setEnabeldWhenRightElementSelect(c, RubyForm.getNumber("X", e.getParameters(), 4).setMinMax(0, 640, 1), "0"));
		f.addElement("pos.coorY",
				FormHelper.setEnabeldWhenRightElementSelect(c, RubyForm.getNumber("Y", e.getParameters(), 5).setMinMax(0, 480, 1), "0"));
		f.addElement(
				"pos.coorVX",
				FormHelper.setEnabeldWhenRightElementSelect(c,
						new FormSwitchVarSelector(e.getYecl().getProject(), "Var X", e.getParameters(), 4, Type.VARIABLE), "1"));
		f.addElement(
				"pos.coorVY",
				FormHelper.setEnabeldWhenRightElementSelect(c,
						new FormSwitchVarSelector(e.getYecl().getProject(), "Var Y", e.getParameters(), 5, Type.VARIABLE), "1"));

		c.informListeners();

		// add zoom
		f.addHeader("another", new FormHeader(I18N.t("Sonstiges"), "dummy").setSorting(2).setColumn(4).setCollapsed(true));
		f.addElement("another.zoomX", RubyForm.getNumber("Zoom X", e.getParameters(), 6));
		f.addElement("another.zoomY", RubyForm.getNumber("Zoom Y", e.getParameters(), 7));
		f.addElement("another.opa", RubyForm.getNumber("Opacity", e.getParameters(), 8).setMinMax(0, 255, 1));
		f.addElement("another.blend", RubyForm.getComboBoxNum("Blending", new String[] { "0", "1", "2" }, new String[] { "Normal",
				"Addjektive", "Subjektive" }, e.getParameters(), 9));
	}

}
