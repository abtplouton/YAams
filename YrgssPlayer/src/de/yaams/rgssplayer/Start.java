package de.yaams.rgssplayer;

import java.awt.FileDialog;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.UIManager;

import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Priority;

import com.ezware.dialog.task.TaskDialog;
import com.ezware.dialog.task.TaskDialogs;

import de.yaams.core.RGSSGame;
import de.yaams.core.YrgssCore;
import de.yaams.core.helper.FileHelper;
import de.yaams.core.helper.I18N;
import de.yaams.core.helper.gui.YEx;
import de.yaams.core.helper.gui.YMessagesDialog;
import de.yaams.core.helper.gui.icons.IconCache;
import de.yaams.rgssplayer.core.java.Yrgss;
import de.yaams.rgssplayer.core.rb.RBLocater;
import de.yaams.rgssplayer.core.render.audio.core.SoundRender;
import de.yaams.rgssplayer.core.render.audio.spi.SpiRenderer;
import de.yaams.rgssplayer.core.render.graphic.core.Render;
import de.yaams.rgssplayer.core.render.graphic.java2d.Java2D;

public class Start {
	public static void main(String[] args) {

		// init system
		YMessagesDialog errors = new YMessagesDialog(I18N.t("{0} can't start.", YrgssCore.NAME), "start");

		initSystem(errors);
		Yrgss.init(errors);
		IconCache.init(errors);

		if (!errors.showQuestion()) {
			System.exit(0);
		}

		YEx.info("1", new Throwable("2"));

		File path = null;
		try {
			if (args != null && args.length >= 1) {
				path = new File(args[0]);
			} else {
				File f = FileHelper.fileRelativeExist("Game.ini");

				if (f != null) {
					path = f.getAbsoluteFile().getParentFile();
				}

			}

			if (path == null) {
				TaskDialog dlg = new TaskDialog(null, "");
				dlg.setInstruction(I18N.t("Found no game. Select manually?"));
				dlg.setText(I18N.t("{1} found in {0} no game.<br>Please select in the next dialog the game.ini of the game.",
						new File("").getAbsolutePath(), YrgssCore.TITLE));
				dlg.setIcon(IconCache.get("yrgss", 32));
				dlg.setCommands(new TaskDialog.Command[] { TaskDialog.StandardCommand.OK.derive(I18N.t("Select manually")),
						TaskDialog.StandardCommand.CANCEL.derive(I18N.t("Close it")) });

				if (dlg.show().equals(TaskDialog.StandardCommand.CANCEL)) {
					System.exit(0);
				}

				FileDialog f = new FileDialog(new JDialog());
				f.setVisible(true);
				path = new File(f.getDirectory());

				// check path
				if (path == null || !path.canRead()) {
					TaskDialogs.error(null, I18N.t("Can not read from {0}", path),
							I18N.t("{0} can't read the folder {1} and will exit.", YrgssCore.TITLE, path));
				}
			}

			Yrgss.game = RGSSGame.loadProject(path);

			if (Yrgss.game == null) {
				main(null);
				System.exit(0);
			}

			Render.set(new Java2D());
			SoundRender.set(new SpiRenderer());

			RBLocater.loadRGSSData();

		} catch (Throwable t) {
			YEx.error("Can not start game", t);
		}

	}

	public static void initSystem(YMessagesDialog errors) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable t) {
			errors.add(I18N.t("Can not set look and feel"), Priority.INFO_INT);
		}

		if (!SystemUtils.IS_JAVA_1_6) {
			errors.add(I18N.t("To run correctly, please install at least Java 6 (1.6). You have only {0}", SystemUtils.JAVA_VERSION),
					Priority.ERROR_INT);
		}
	}
}