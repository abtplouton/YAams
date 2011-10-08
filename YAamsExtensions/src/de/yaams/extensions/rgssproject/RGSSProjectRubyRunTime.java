/**
 * 
 */
package de.yaams.extensions.rgssproject;

import de.yaams.extensions.jruby.RBRunTime;
import de.yaams.maker.helper.Log;
import de.yaams.maker.programm.project.Project;

/**
 * @author Praktikant
 * 
 */
public class RGSSProjectRubyRunTime extends RBRunTime {

	protected Project project;

	/**
	 * 
	 */
	public RGSSProjectRubyRunTime(Project project) {
		this.project = project;

		// load basics
		interpretInternFile(RGSSProjectRubyRunTime.class, "ruby/java.rb");
		interpretInternFile(RGSSProjectRubyRunTime.class, "ruby/table.rb");

		Log.ger.info("Load for Project " + project.getTitle() + " (" + project.getPath().getAbsolutePath() + ") RGSS "
				+ RGSSProjectHelper.getRGSSVersion(project));

		// load rgss version
		if (RGSSProjectHelper.getRGSSVersion(project) == 1) {
			loadRGSS1();
		} else if (RGSSProjectHelper.getRGSSVersion(project) == 2) {
			loadRGSS2();
		}
	}

	/**
	 * Load the rgss 1 data
	 */
	public void loadRGSS1() {

		final String[] rpg = { "actor.rb", "animation.rb", "animationFrame.rb", "animationTiming.rb", "armor.rb", "audioFile.rb",
				"class.rb", "classLearning.rb", "commonEvent.rb", "enemy.rb", "enemyAction.rb", "event.rb", "eventCommand.rb",
				"eventPage.rb", "eventPageCondition.rb", "eventPageGraphic.rb", "item.rb", "map.rb", "mapInfo.rb", "moveCommand.rb",
				"moveRoute.rb", "skill.rb", "state.rb", "system.rb", "systemTestBattle.rb", "systemWords.rb", "tileset.rb", "troop.rb",
				"troopMember.rb", "troopPage.rb", "troopPageCondition.rb", "weapon.rb" };

		for (final String s : rpg) {
			interpretInternFile(RGSSProjectRubyRunTime.class, "ruby/rgss1/" + s);
		}
	}

	/**
	 * Load the rgss 2 data
	 */
	public void loadRGSS2() {

		final String[] rpg = { "actor.rb", "animation.rb", "animationFrame.rb", "animationTiming.rb", "armor.rb", "audioFile.rb",
				"cache.rb", "class.rb", "classLearning.rb", "commonEvent.rb", "enemy.rb", "enemyAction.rb", "event.rb", "eventCommand.rb",
				"eventPage.rb", "eventPageCondition.rb", "eventPageGraphic.rb", "item.rb", "map.rb", "mapInfo.rb", "moveCommand.rb",
				"moveRoute.rb", "skill.rb", "sprite.rb", "state.rb", "system.rb", "systemTestBattle.rb", "systemWords.rb", "tileset.rb",
				"troop.rb", "troopMember.rb", "troopPage.rb", "troopPageCondition.rb", "weapon.rb", "weather.rb" };

		for (final String s : rpg) {
			interpretInternFile(RGSSProjectRubyRunTime.class, "ruby/rgss2/" + s);
		}
	}
}
