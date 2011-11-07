/**
 * 
 */
package de.yaams.extensions.rgssproject.script;

import java.util.ArrayList;

import javax.swing.JComponent;

import org.jruby.RubyArray;

import de.yaams.extensions.rgssproject.RGSSProjectHelper;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.bcb.BcbBuilder;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.helper.gui.tabs.ProjectSplitTab;
import de.yaams.maker.helper.helpcenter.HelpViewer;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.ProjectTabEvent;

/**
 * @author Nebli
 * 
 */
public class ScriptTab extends ProjectSplitTab {
	private static final long serialVersionUID = 8730657283128195550L;

	public static final String ID = "rgssproject.script";

	/**
	 * Helperparameter for rb script load/save
	 */
	public static String scriptFile;
	/**
	 * Helperparameter for rb script save
	 */
	public static Object[] scripts;

	/**
	 * @param project
	 */
	public ScriptTab(final Project project) {
		super(buildList(project), project);

		list.setAdd(true);
		list.setDelete(true);
		list.buildToolbar(I18N.t("Script"), "script");

		buildGui();

		addSaveButton();
		psContent.add(new HelpViewer("scripting"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#add()
	 */
	@Override
	protected void add() {
		list.addExtern(new ScriptAction("Script", "", "0"));
	}

	/**
	 * Helpermethod to build the list
	 * 
	 * @return
	 */
	private static ArrayList<BasisListElement> buildList(final Project project) {
		// open code
		scriptFile = RGSSProjectHelper.getScriptPath(project).getAbsolutePath();

		final Object[] script = ((RubyArray) RGSSProjectHelper.getInterpreter(project)
				.interpretInternFile(ScriptTab.class, "scriptLoad.rb")).toArray();

		// convert code
		final ArrayList<BasisListElement> ary = new ArrayList<BasisListElement>();
		for (Object element : script) {
			ary.add((BasisListElement) element);
		}

		return ary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getBottom()
	 */
	@Override
	public JComponent getBottom() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getIcon()
	 */
	@Override
	public String getIcon() {
		return "script";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.programm.tabs.YaTab#getTitle()
	 */
	@Override
	public String getTitle() {
		return I18N.t("Scripts");
	}

	/**
	 * Save chanced, overwrite it to implement it
	 */
	@Override
	protected void saveIntern() {
		scripts = list.getAry().toArray();
		scriptFile = RGSSProjectHelper.getScriptPath(project).getAbsolutePath();

		// scripts
		RGSSProjectHelper.getInterpreter(project).interpretInternFile(ScriptTab.class, "scriptSave.rb");
	}

	@Override
	protected void buildBcb(BcbBuilder bcb) {
		ProjectTabEvent.buildBcb(bcb, project, ID);

	}

	@Override
	public String getId() {
		return ID;
	}

}
