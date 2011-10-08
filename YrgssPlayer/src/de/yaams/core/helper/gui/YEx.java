/**
 * 
 */
package de.yaams.core.helper.gui;

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

import de.yaams.core.YrgssCore;
import de.yaams.core.helper.I18N;
import de.yaams.core.helper.Log;
import de.yaams.core.helper.SystemHelper;
import de.yaams.core.helper.gui.form.FormTextArea;
import de.yaams.core.helper.gui.form.FormTextField;
import de.yaams.core.helper.gui.form.core.FormBuilder;
import de.yaams.core.helper.gui.icons.IconCache;

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
			TaskDialog task = new TaskDialog(null, "");

			// show it
			// build form
			FormBuilder f = new FormBuilder("errorform.info");
			f.getHeader("basic").setTitle(I18N.t("More Infos (optional)")).setIcon("mail");
			f.addElement("basic.contact", new FormTextField(I18N.t("eMail"), "").setInfoTxt(I18N.t("Add email, if you wish an answer.")));
			f.addElement("basic.details", new FormTextArea(I18N.t("Last Action"), "").setInfoTxt(I18N
					.t("Write your last steps, action, or comments about this error")));

			// add details
			// f.addHeader("error", new FormHeader(I18N.t("Details"),
			// icon).setCollapsed(true));
			// f.addElement("error.class", new FormTextField(I18N.t("Class"),
			// t.getClass().toString()));
			// f.addElement("error.mess", new FormTextField(I18N.t("Message"),
			// YEx.toString(title, t, false)));
			// f.addElement("error.stack", new FormTextArea(I18N.t("Stack"),
			// Strings.stackStraceAsString(t)));

			task.setInstruction(I18N.t("Send {0}report", typ));
			task.setText(mess + "<br>" + YEx.toString(title, t, false));
			task.setIcon(IconCache.get(icon, 64));
			task.setResizable(true);
			task.setFixedComponent(f.getPanel(true));
			task.setCommands(StandardCommand.OK.derive(I18N.t("Send {0}", typ)),
					StandardCommand.CANCEL.derive(I18N.t(exit ? "Close program" : "Ignore")));
			TaskDialog.Command erg = task.show();
			if (erg != null && erg.getTitle().equals(I18N.t("Send {0}", typ))) {
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

		mess(title, "Error", t, "error", I18N.t("{0} has a big problem and must be close.", YrgssCore.TITLE), true);

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
				I18N.t("<html>A module has caused an error, usually that's all fine, but sometimes you can lose data. <br> If you see the message for the first time, send the report so that the problem can be solved. Then you can be ignored."),
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
				I18N.t("<html>A module has caused an error, but everything is ok. If you see the message for the first time, please send the report,<br> so problem can be solved. Then it can be ignored."),
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
