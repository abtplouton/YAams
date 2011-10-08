/**
 * 
 */
package de.yaams.maker.helper.gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import com.ezware.common.Strings;
import com.ezware.dialog.task.TaskDialog;
import com.ezware.dialog.task.TaskDialog.StandardCommand;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.Log;
import de.yaams.maker.helper.SystemHelper;
import de.yaams.maker.helper.gui.form.FormTextArea;
import de.yaams.maker.helper.gui.form.FormTextField;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;
import de.yaams.maker.helper.gui.icons.IconCache;
import de.yaams.maker.programm.YAamsCore;
import de.yaams.maker.programm.YaFrame;
import de.yaams.maker.programm.environment.YLevel;

/**
 * @author abt
 * 
 */
public class YEx {

	/**
	 * Show the last exit message, awt will use for rendering
	 * 
	 * User see the message/some stack and can close it
	 * 
	 * @param title
	 * @param t
	 */
	public static void lastExit(final String title, final Throwable t) {
		t.printStackTrace();
		Frame f = new Frame("Fatal error");
		f.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				SystemHelper.exit(2);
			}
		});

		// build gui
		Panel p = new Panel(new BorderLayout());
		p.add(new Label(title), BorderLayout.NORTH);
		p.add(new Label(Strings.stackStraceAsString(t)), BorderLayout.CENTER);

		Button b = new Button("Send Data");
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendError(t, "fatal error: " + title, "", "");
				SystemHelper.exit(2);

			}
		});
		p.add(b, BorderLayout.SOUTH);

		f.add(p);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	/**
	 * Show a fatal message, the programm can not be used and will close
	 * 
	 * User can send it (or try),close program and view it
	 * 
	 * @param title
	 * @param t
	 */
	protected static void mess(String title, String typ, Throwable t, String icon, String mess, boolean exit) {

		try {
			TaskDialog task = new TaskDialog(YaFrame.get(), "");

			// show it
			// build form
			FormBuilder f = new FormBuilder("errorform.info");
			f.getHeader("basic").setTitle(I18N.t("Weitere Infos (freiwillig)")).setIcon("edit");
			f.addElement(
					"basic.contact",
					YSettingHelper.text(null, I18N.t("eMail"), "reporting.contact", "").setInfoTxt(
							I18N.t("Füge hier deine eMailadresse ein, wenn du eine Antwort wünscht oder Nachfragen erlaubst.")));
			f.addElement("basic.details", new FormTextArea(I18N.t("Ablauf"), "").setInfoTxt(I18N
					.t("Schreibe hier rein, was passiert ist, was du getan hast, wie es zu dem Fehler gekommen ist.")));

			// add details
			f.addHeader("error", new FormHeader(I18N.t("Details"), icon).setCollapsed(true));
			f.addElement("error.class", new FormTextField(I18N.t("Class"), t.getClass().toString()));
			f.addElement("error.mess", new FormTextField(I18N.t("Message"), YEx.toString(title, t, false)));
			f.addElement("error.stack", new FormTextArea(I18N.t("Stack"), Strings.stackStraceAsString(t)));

			task.setInstruction(I18N.t("{0}bericht einsenden", typ));
			task.setText(mess + (YLevel.IS_ADVANCED ? "<br>" + YEx.toString(title, t, false) : ""));
			task.setIcon(IconCache.get(icon, 64));
			task.setResizable(true);
			task.setFixedComponent(f.getPanel(true));
			task.setCommands(StandardCommand.OK.derive(I18N.t("{0} einsenden", typ), "mail"),
					StandardCommand.CANCEL.derive(I18N.t(exit ? "Programm beenden" : "Ignorieren"), "cancel"));
			TaskDialog.Command erg = task.show();
			if (erg != null && erg.getTitle().equals(I18N.t("{0} einsenden", typ))) {
				// send feedback
				sendError(t, title, f.getElement("basic.contact").getContentAsString(), f.getElement("basic.details").getContentAsString());
			} else {
				// close?
				if (exit) {
					SystemHelper.exit(0);
				}
			}

		} catch (Throwable t2) {
			lastExit("Errorreportingerror: " + YEx.toString(mess, t2, false), t2);
		}

		// YDialog.showForm(, "web_edit", f);
		// showMessage(title, I18N.t("{0} has a big problem and must be close.",
		// YAamsCore.NAME), t,
		// UIManager.getIcon("OptionPane.errorIcon"), true, true, false, true);
	}

	/**
	 * Show a fatal message, the programm can not be used and will close
	 * 
	 * User can send it (or try),close program and view it
	 * 
	 * @param title
	 * @param t
	 */
	public static void fatal(String title, Throwable t) {
		// log error
		Log.ger.fatal(toString(title, t, false), t);

		// show it
		mess(title, "Schaden", t, "error", I18N.t("{0} hat ein großes Problem und muss beendet werden.", YAamsCore.TITLE), true);
	}

	/**
	 * Show a error message, this program module can not be used and will close,
	 * but the program can used
	 * 
	 * User can send it or ignore it (advanced user also can view the message)
	 * 
	 * @param title
	 * @param t
	 */
	public static void error(String title, Throwable t) {
		// log error
		Log.ger.error(toString(title, t, false), t);

		mess(title,
				"Fehler",
				t,
				"error",
				I18N.t("<html>Ein Modul hat ein sehr großes Problem und muss beendet werden. Die Stabilität von {0} kann nicht mehr gewährleistet werde.<br> Es kann weiter laufen muss es aber nicht. Der Fehler sollte auf jeden Fall eingeschickt werden.",
						YAamsCore.TITLE), true);

	}

	/**
	 * Show a warning message, this program module has a problem, but will
	 * handle it
	 * 
	 * User can send it or ignore it (advanced user also can view the message)
	 * 
	 * @param title
	 * @param t
	 */
	public static void warn(String title, Throwable t) {
		// log error
		Log.ger.warn(toString(title, t, false), t);

		// show it
		mess(title,
				"Problem",
				t,
				"warn",
				I18N.t("<html>Ein Modul hat ein Fehler verursacht, normallerweise ist alles in Ordnung, aber manchmal können Daten verloren gehen.<br> Wenn du die Nachricht zum ersten Mal siehst, sende Sie ein, damit das Problem behoben werden kann. Danach kann Sie ignoriert werden."),
				false);

	}

	/**
	 * Show a info message, this program module has a simple problem, but can
	 * easy handle it
	 * 
	 * User can send it or ignore it (advanced user also can view the message)
	 * 
	 * @param title
	 * @param t
	 */
	public static void info(String title, Throwable t) {
		// log error
		Log.ger.info(toString(title, t, false), t);

		// show it
		mess(title,
				"Problem",
				t,
				"info",
				I18N.t("<html>Ein Modul hat ein Fehler verursacht, aber alles ist in Ordnung. Wenn du die Nachricht zum Ersten Mal siehst, sende Sie ein, <br>damit das Problem behoben werden kann. Danach kann es ignoriert werden."),
				false);

	}

	/**
	 * @param t
	 */
	protected static void sendError(Throwable t, String title, String author, String comment) {
		// collect data
		HashMap<String, String> data = new HashMap<String, String>();

		data.put("messages", t.getMessage());
		data.put("class", t.getClass().toString());
		data.put("stack", Strings.stackStraceAsString(t));
		data.put("type", "error");
		data.put("author", author);
		data.put("comment", comment);
		data.put("title", title);

		// send it
		SystemHelper.sendData("error", "Error - " + t.getMessage(), data);
	}

	/**
	 * Add Cause
	 * 
	 * @param t
	 * @return
	 */
	public static String toString(String mess, Throwable t, boolean print) {
		// print?
		if (print) {
			Log.ger.info(mess, t);
		}

		StringBuffer erg = new StringBuffer(mess);
		erg.append(" (");
		erg.append(t.getClass().getSimpleName());
		erg.append(": ");
		erg.append(t.getLocalizedMessage());

		// add cause?
		if (t.getCause() != null) {
			erg.append(", Caused by ");
			erg.append(t.getCause().getClass().getSimpleName());
			erg.append(": ");
			erg.append(t.getCause().getLocalizedMessage());
		}

		erg.append(") ");
		return erg.toString();
	}
}
