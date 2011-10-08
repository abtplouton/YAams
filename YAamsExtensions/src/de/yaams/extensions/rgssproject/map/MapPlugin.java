/**
 * 
 */
package de.yaams.extensions.rgssproject.map;

import java.util.HashMap;

import de.yaams.extensions.basemap.BaseMapPlugin;
import de.yaams.extensions.rgssproject.database.DatabasePlugin;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.map.nevent.command.AudioPlayCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.AudioSetFighCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.AudioStopCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.CallCommonEventCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.ChangeHeroCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.ChangeHeroNameGraphicCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.CommentCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.EraseEventCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.ErrorCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.InputCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.ItemCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.LabelCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.LocalSwitchCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.LoopBreakCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.MessageCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.MessageDisplayCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.NewCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.PartyCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.PartyValueCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.PermissionCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.PictureDelCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.PictureRotateCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.PictureShowMoveCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.SceneCallCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.ScriptCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.SetWindowSkinCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.SwitchCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.TransitionCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.VariableCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.WaitCommand;
import de.yaams.extensions.rgssproject.map.nevent.command.WeatherCommand;
import de.yaams.extensions.rgssproject.map.nevent.core.EventCommandManagement;
import de.yaams.extensions.rgssproject.map.rxdata.RXDataWriter;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.extensions.IExtension;
import de.yaams.maker.programm.project.Project;

/**
 * @author Nebli
 * 
 */
public class MapPlugin {

	/**
	 * 
	 */
	public MapPlugin() {

		registerEventCommands();

		// add db
		ExtentionManagement.add(Project.EXLOAD, new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {
				final Project p = (Project) objects.get("project");

				// add
				DatabasePlugin.add(Type.MAP, p, "Welt", true, false);
				DatabasePlugin.add(Type.TILESET, p, "Welt", true, false);
				DatabasePlugin.add(Type.COMMONEVENT, p, "Welt", true, false);

				// add
				// ary.add(new ProjectAction(I18N.t("Maps"),
				// I18N.t("Erstellt und ändert die Karten/die Spielwelt"),
				// "map", p) {
				//
				// @Override public void perform() {
				// YaFrame.open(new MTab(project));
				// }
				// });

			}
		});

		// add mapsupport
		BaseMapPlugin.getPlugs().add(new RXDataWriter());
	}

	/**
	 * 
	 */
	protected void registerEventCommands() {
		// register evemt commands
		EventCommandManagement.register(-1, new ErrorCommand(), false);
		EventCommandManagement.register(0, new NewCommand(), false);
		EventCommandManagement.register(101, new MessageCommand(), true);
		EventCommandManagement.register(104, new MessageDisplayCommand(), true);
		EventCommandManagement.registerM(new InputCommand(), 103, 105);
		EventCommandManagement.register(106, new WaitCommand(), true);
		EventCommandManagement.register(108, new CommentCommand(), true);
		EventCommandManagement.register(113, new LoopBreakCommand(), true);
		EventCommandManagement.register(115, new EraseEventCommand(), true);
		EventCommandManagement.register(116, EventCommandManagement.get(115), false);
		EventCommandManagement.register(117, new CallCommonEventCommand(), true);
		EventCommandManagement.register(118, new LabelCommand(), true);
		EventCommandManagement.register(119, EventCommandManagement.get(118), false);

		EventCommandManagement.register(121, new SwitchCommand(), true);
		EventCommandManagement.register(122, new VariableCommand(), true);
		EventCommandManagement.register(123, new LocalSwitchCommand(), true);

		EventCommandManagement.registerM(new ItemCommand(), 125, 126, 127, 128);
		EventCommandManagement.register(129, new PartyCommand(), true);

		EventCommandManagement.registerM(new SetWindowSkinCommand(), 131);
		EventCommandManagement.registerM(new AudioSetFighCommand(), 132, 133);

		EventCommandManagement.registerM(new PermissionCommand(), 134, 135, 136);

		EventCommandManagement.registerM(new TransitionCommand(), 221, 222);

		EventCommandManagement.registerM(new PictureShowMoveCommand(), 231, 232);
		EventCommandManagement.registerM(new PictureRotateCommand(), 233);
		EventCommandManagement.registerM(new PictureDelCommand(), 235);

		EventCommandManagement.registerM(new WeatherCommand(), 236);

		EventCommandManagement.registerM(new AudioPlayCommand(), 249, 250, 245, 241);
		EventCommandManagement.registerM(new AudioStopCommand(), 242, 246, 247, 248, 251);

		EventCommandManagement.registerM(new PartyValueCommand(), 311, 312, 315, 316);
		EventCommandManagement.registerM(new ChangeHeroCommand(), 317, 318, 319, 321);
		EventCommandManagement.registerM(new ChangeHeroNameGraphicCommand(), 320, 322);

		EventCommandManagement.registerM(new SceneCallCommand(), 351, 352, 353, 354);
		EventCommandManagement.register(355, new ScriptCommand(), true);
	}

}
