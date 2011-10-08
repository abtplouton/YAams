/**
 * 
 */
package de.yaams.extensions.rgssproject;

import java.awt.Component;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;

import org.ini4j.Ini;
import org.jruby.RubyObject;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.form.FormGraphEle;
import de.yaams.extensions.rgssproject.database.form.FormMusicEle;
import de.yaams.extensions.rgssproject.database.form.RubyForm;
import de.yaams.extensions.rgssproject.database.form.list.FormDBList;
import de.yaams.extensions.rgssproject.map.YMapView;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.extensions.IExtension;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.YDialog;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.gui.form.FormButton;
import de.yaams.maker.helper.gui.form.FormInfo;
import de.yaams.maker.helper.gui.form.FormTextField;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;
import de.yaams.maker.helper.gui.list.BasisListElement;
import de.yaams.maker.helper.gui.tabs.SplitActionListElement;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.project.tab.ProjectOptionsTab;

/**
 * @author abby
 * 
 */
public class GameOptions {

	/**
	 * Add Game options to the tab
	 */
	public static void addGameOptions() {
		// add options
		// add run settings
		ExtentionManagement.add(ProjectOptionsTab.EXADD, new IExtension() {

			@Override
			public void work(HashMap<String, Object> objects) {
				ArrayList<BasisListElement> eles = (ArrayList<BasisListElement>) objects.get("list");

				final Project project = (Project) objects.get("project");

				// right project?
				if (!RGSSProjectHelper.is(project, true, false)) {
					return;
				}

				// load system
				final RubyObject system = RGSS1Helper.get(project, Type.SYSTEM).get(0).getObject();
				final RubyObject words = (RubyObject) system.getInstanceVariable("@words");

				// add basics
				eles.add(new SplitActionListElement(I18N.t("Basics"), I18N.t("Verwaltet die Grundlagen des Spiels"), "game") {

					@Override
					protected Component getComponent(Project p) {
						final FormBuilder form = new FormBuilder("game.basic");

						form.getHeader("basic").setTitle(I18N.t("Grundlegenes")).setIcon("info").setColumn(4);

						// add basics
						final FormTextField ftf = new FormTextField(I18N.t("Name"), RGSSProjectHelper.getGameIni(project).get("Title"));
						form.addElement("basic.name", ftf);

						// build rtp
						// RTP.

						form.addElement("basic.button", new FormButton(I18N.t("In Game.ini speichern"), "disk", new AE() {

							@Override
							public void run() {
								FileReader f = null;
								try {
									// load it
									f = new FileReader(RGSSProjectHelper.getGameIniFile(project));

									// build game ini
									Ini ini = new Ini();

									ini.load();

									Ini.Section section = ini.get("Game");

									// add it
									section.add("Title", ftf.getContentAsString());
									section.add("RTP1", ftf.getContentAsString());
									section.add("RTP2", ftf.getContentAsString());
									section.add("RTP3", ftf.getContentAsString());

									// save
									ini.store(RGSSProjectHelper.getGameIniFile(project));

								} catch (Throwable t) {
									YEx.info("Can not save " + RGSSProjectHelper.getGameIniFile(project), t);
								} finally {
									if (f != null) {
										try {
											f.close();
										} catch (IOException e) {
											YEx.info("Can not close " + f, e);
										}
									}
								}

							}
						}).setSorting(2));

						// add party
						form.addHeader("party", new FormHeader(I18N.t("Party"), "hero"));
						form.addElement("party.hero", new FormDBList(project, Type.ACTOR, system.getInstanceVariable("@party_members"),
								I18N.t("Wähle die Helden für die Party aus")));
						// add map
						form.addElement(
								"party.pos",
								new FormButton(I18N.t(I18N.t("Start Position {0} - {1}/{2}", RubyHelper.toInt(system, "@start_map_id"),
										RubyHelper.toInt(system, "@start_x"), RubyHelper.toInt(system, "@start_y"))), "map", new AE() {

									@Override
									public void run() {
										// load
										YMapView map = new YMapView(RubyHelper.toInt(system, "@start_map_id"), project);
										map.installClickSupport();

										map.setSelectX(RubyHelper.toInt(system, "@start_x"));
										map.setSelectY(RubyHelper.toInt(system, "@start_y"));
										map.installMapChance();

										// show
										if (YDialog.show(I18N.t("Start Position"), "map", map, true)) {
											//
											RubyHelper.setNum(system, "@start_x", map.getSelectX());
											RubyHelper.setNum(system, "@start_y", map.getSelectY());
											RubyHelper.setNum(system, "@start_map_id", map.getMapID());
										}
										((JButton) form.getElement("party.pos").getElement()).setText(I18N.t(
												"Start Position {0} - {1}/{2}", RubyHelper.toInt(system, "@start_map_id"),
												RubyHelper.toInt(system, "@start_x"), RubyHelper.toInt(system, "@start_y")));

									}
								}));

						// build objects
						// ExtentionManagement.work("project.game_setting.basic",
						// JavaHelper.createHashStringObj("project",
						// project,"form",
						// form,"system", system));

						return form.getPanel(true);
					}
				});

				// vocab
				eles.add(new SplitActionListElement(I18N.t("Vocabulary"), I18N.t("Übersetzt die Hauptelemente des Spiels"), "vocab") {

					@Override
					protected Component getComponent(Project p) {
						FormBuilder form = new FormBuilder("game.voc");

						// add it
						form.addHeader("help", new FormHeader(I18N.t("Info"), "help"));
						form.addElement("help.more",
								new FormInfo("", I18N.t("You will find more Vocabulary directly in the script section")));

						form.addHeader("basic", new FormHeader(I18N.t("Basics"), "vocab"));
						form.addElement("basic.gold", RubyForm.getString(I18N.t("Gold"), "@gold", words));
						form.addElement("basic.hp", RubyForm.getString(I18N.t("HP"), "@hp", words));
						form.addElement("basic.sp", RubyForm.getString(I18N.t("SP"), "@sp", words));
						form.addElement("basic.str", RubyForm.getString(I18N.t("Strength"), "@str", words));
						form.addElement("basic.dex", RubyForm.getString(I18N.t("Dexterity"), "@dex", words));
						form.addElement("basic.agi", RubyForm.getString(I18N.t("Agility"), "@agi", words));
						form.addElement("basic.int", RubyForm.getString(I18N.t("Intelligence"), "@int", words));
						form.addElement("basic.att", RubyForm.getString(I18N.t("Attack Power"), "@atk", words));
						form.addElement("basic.phy", RubyForm.getString(I18N.t("Physical Defense"), "@pdef", words));
						form.addElement("basic.mag", RubyForm.getString(I18N.t("Magic Defense"), "@mdef", words));

						form.addHeader("fight", new FormHeader(I18N.t("Fight"), "weapon"));
						form.addElement("fight.weapon", RubyForm.getString(I18N.t("Weapon"), "@weapon", words));
						form.addElement("fight.armor", RubyForm.getString(I18N.t("Shield"), "@armor1", words));
						form.addElement("fight.helm", RubyForm.getString(I18N.t("Helmet"), "@armor2", words));
						form.addElement("fight.body", RubyForm.getString(I18N.t("Body armor"), "@armor3", words));
						form.addElement("fight.acc", RubyForm.getString(I18N.t("Accessory"), "@armor4", words));
						form.addElement("fight.att", RubyForm.getString(I18N.t("Attack"), "@attack", words));
						form.addElement("fight.skill", RubyForm.getString(I18N.t("Skill"), "@skill", words));
						form.addElement("fight.def", RubyForm.getString(I18N.t("Defense"), "@guard", words));
						form.addElement("fight.item", RubyForm.getString(I18N.t("Item"), "@item", words));
						form.addElement("fight.equ", RubyForm.getString(I18N.t("Equip"), "@equip", words));

						form.setColumn(6);
						// get it
						return form.getPanel(true);
					}
				});

				// graphics & audio
				eles.add(new SplitActionListElement(I18N.t("Backgrounds & Music"), I18N.t("Set Options for Title, Gameover, .."),
						"graphic_audio") {

					@Override
					protected Component getComponent(Project p) {
						FormBuilder form = new FormBuilder("game.graph");

						// add it
						form.addHeader("basic", new FormHeader(I18N.t("Basics"), "graphic"));
						form.addElement("basic.window", new FormGraphEle(I18N.t("Windowskin"), project, RTP.WINDOWSKIN, system,
								"@windowskin_name", null));

						// title screen
						form.addHeader("title", new FormHeader(I18N.t("Title"), "graphic"));
						form.addElement("title.graph", new FormGraphEle(I18N.t("Graphic"), project, RTP.TITLE, system, "@title_name", null));
						form.addElement("title.music",
								new FormMusicEle(I18N.t("Music"), project, RTP.BGM, system.getInstanceVariable("@title_bgm")));

						// game over screen
						form.addHeader("gameover", new FormHeader(I18N.t("Game Over"), "graphic"));
						form.addElement("gameover.graph", new FormGraphEle(I18N.t("Graphic"), project, RTP.GAMEOVER, system,
								"@gameover_name", null));
						form.addElement("gameover.music",
								new FormMusicEle(I18N.t("Music"), project, RTP.ME, system.getInstanceVariable("@gameover_me")));

						// battle screen
						form.addHeader("battle", new FormHeader(I18N.t("Battle"), "weapon"));
						form.addElement("battle.trans", new FormGraphEle(I18N.t("Transition"), project, RTP.TRANSITION, system,
								"@battle_transition", null));
						form.addElement("battle.mus",
								new FormMusicEle(I18N.t("Music"), project, RTP.BGM, system.getInstanceVariable("@battle_bgm")));
						form.addElement("battle.start",
								new FormMusicEle(I18N.t("Start"), project, RTP.SE, system.getInstanceVariable("@battle_start_se")));
						form.addElement("battle.end",
								new FormMusicEle(I18N.t("End"), project, RTP.ME, system.getInstanceVariable("@battle_end_me")));
						form.addElement("battle.es",
								new FormMusicEle(I18N.t("Escape"), project, RTP.SE, system.getInstanceVariable("@escape_se")));
						form.addElement("battle.adie",
								new FormMusicEle(I18N.t("Actor die"), project, RTP.SE, system.getInstanceVariable("@actor_collapse_se")));
						form.addElement("battle.edie",
								new FormMusicEle(I18N.t("Enemy die"), project, RTP.SE, system.getInstanceVariable("@enemy_collapse_se")));

						// another soundeffects
						form.addHeader("sound", new FormHeader(I18N.t("Sound effects"), "audio"));
						form.addElement("sound.dec",
								new FormMusicEle(I18N.t("Decision"), project, RTP.SE, system.getInstanceVariable("@decision_se")));
						form.addElement("sound.can",
								new FormMusicEle(I18N.t("Cancel"), project, RTP.SE, system.getInstanceVariable("@cancel_se")));
						form.addElement("sound.buzz",
								new FormMusicEle(I18N.t("Buzzer"), project, RTP.SE, system.getInstanceVariable("@buzzer_se")));
						form.addElement("sound.equip",
								new FormMusicEle(I18N.t("Equip"), project, RTP.SE, system.getInstanceVariable("@equip_se")));
						form.addElement("sound.shop",
								new FormMusicEle(I18N.t("Shop"), project, RTP.SE, system.getInstanceVariable("@shop_se")));
						form.addElement("sound.load",
								new FormMusicEle(I18N.t("Load"), project, RTP.SE, system.getInstanceVariable("@load_se")));
						form.addElement("sound.save",
								new FormMusicEle(I18N.t("Save"), project, RTP.SE, system.getInstanceVariable("@save_se")));

						form.setColumn(4);
						//
						// form.addElement(GTab.getFileSelector(I18N.t(""), "@",
						// system,
						// project, "", true));
						//
						// form.addElement(new FormMusicRessElement(I18N.t(""),
						// project,
						// (RubyObject) system
						// .getInstanceVariable("@"),
						// FormMusicRessElement.BGM));

						// get it
						return form.getPanel(true);
					}
				});
			}
		});
	}

}
