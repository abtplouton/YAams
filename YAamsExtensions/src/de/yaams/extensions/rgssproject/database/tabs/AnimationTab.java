/**
 * 
 */
package de.yaams.extensions.rgssproject.database.tabs;

import org.jruby.RubyObject;

import de.yaams.extensions.rgssproject.RGSSProjectHelper;
import de.yaams.extensions.rgssproject.RTP;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.form.FormGraphEle;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.extensions.rgssproject.database.form.list.FormAnimationTimingList;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;
import de.yaams.maker.programm.project.Project;

/**
 * @author abt
 * 
 */
public class AnimationTab extends GTab {
	private static final long serialVersionUID = -7224278425132595022L;

	/**
	 * Create a new ItemTab
	 * 
	 * @param project
	 */
	public AnimationTab(final Project project) {
		super(project);

		loadFile(Type.ANIMATION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.genericLoader.GTab#getInternContent(int)
	 */
	@Override
	public void buildForm(FormBuilder form, int id) {
		// prepare
		RubyObject act = elements.get(id).getObject();

		// build basics
		form.addHeader("basic", new FormHeader(I18N.t("Grundlegenes"), getIcon() + "_info").setColumn(4));
		form.addElement("basic.name", RubyForm.getString(I18N.t("Name"), "@name", act));
		form.addElement(
				"basic.position",
				RubyForm.getComboBoxNum("Position", new String[] { "0", "1", "2", "3" }, new String[] { "Top", "Middle", "Bottom",
						"Whole Screen" }, act, "@position"));

		form.addHeader("graphic", new FormHeader(I18N.t("Graphics"), "graphic"));
		form.addElement("graphic.graphic", new FormGraphEle(I18N.t("Graphic"), project, RTP.ANIMATION, act, "@animation_name",
				"@animation_hue"));

		// build panel
		form.addHeader("animation", new FormHeader(I18N.t("Animationsoptionen"), "animation").setSorting(1).setColumn(2));
		form.addElement("animation.timing", new FormAnimationTimingList(getProject(), act.getInstanceVariable("@timings")));

		// build panel
		form.addHeader("unsupported", new FormHeader(I18N.t("Nicht unterstützt"), "error").setColumn(4).setCollapsed(true));
		form.addElement("unsupported.frames", RubyForm.getError("Frames", "@frames", act));
		form.addElement("unsupported.max", RubyForm.getError("Frames Max", "@frame_max", act));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.genericLoader.GTab#getText(java.lang.Integer)
	 */
	@Override
	public Object getText(final Integer value) {
		return elements.get(value).getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.extensions.genericLoader.GTab#createObject()
	 */
	@Override
	public RubyObject createObject() {
		return (RubyObject) RGSSProjectHelper.getInterpreter(project).runScriptlet("return RPG::Animation.new");
	}

}
