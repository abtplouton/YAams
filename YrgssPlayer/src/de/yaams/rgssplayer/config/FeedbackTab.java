package de.yaams.rgssplayer.config;

import java.awt.Component;
import java.util.HashMap;

import org.apache.commons.lang.SystemUtils;

import de.yaams.core.helper.AE;
import de.yaams.core.helper.I18N;
import de.yaams.core.helper.SystemHelper;
import de.yaams.core.helper.gui.YFactory;
import de.yaams.core.helper.gui.form.FormElement;
import de.yaams.core.helper.gui.form.FormElementChangeListener;
import de.yaams.core.helper.gui.form.FormTextArea;
import de.yaams.core.helper.gui.form.FormTextField;
import de.yaams.core.helper.gui.form.core.FormBuilder;
import de.yaams.core.helper.gui.form.core.FormHeader;

public class FeedbackTab extends BaseTab {
	private static final long serialVersionUID = -1511668960746278912L;

	public FeedbackTab() {

		add(addFeedback());
	}

	/**
	 * @return
	 */
	public static Component addFeedback() {

		final HashMap<String, String> data = new HashMap<String, String>();
		// build form
		final FormBuilder f = new FormBuilder("feedback");
		f.addHeader("basic", new FormHeader(I18N.t("Basics"), "info").setColumn(4));
		f.addElement("basic.atitle", createField(data, I18N.t("Title"), "", "title"));
		f.addElement("basic.bname", createField(data, I18N.t("Name"), SystemUtils.USER_NAME, "user"));
		f.addElement(
				"basic.system",
				createField(data, I18N.t("System"), I18N.t("{0} ({1}) {2} - {3} ({4}) {5}", SystemUtils.OS_NAME, SystemUtils.OS_VERSION,
						SystemUtils.OS_ARCH, SystemUtils.JAVA_RUNTIME_NAME, SystemUtils.JAVA_VERSION, SystemUtils.JAVA_VM_INFO), "system"));
		f.addElement("basic.contact",
				createField(data, I18N.t("Kontakt"), "", "contact").setInfoTxt(I18N.t("email, if you wish an answer.")));

		f.addHeader("mess", new FormHeader(I18N.t("Message"), "mail"));
		f.addElement("mess.mess", new FormTextArea("", ""));

		f.addButton("feedback", YFactory.b(I18N.t("Send Feedback"), "mail_web", new AE() {

			@Override
			public void run() {
				data.put("system", f.getElement("basic.system").getContentAsString());
				data.put("stack", "feedback " + f.getElement("basic.atitle").getContentAsString() + " "
						+ f.getElement("basic.bname").getContentAsString() + " " + f.getElement("basic.contact").getContentAsString());
				data.put("messages", f.getElement("mess.mess").getContentAsString());
				SystemHelper.sendData("feedback", "Feedback - " + f.getElement("basic.atitle").getContentAsString(), data);

			}
		}, 32));

		// add modi
		// f.addChangeListener(new FormElementChangeListener() {
		//
		// @Override
		// public void stateChanged(FormElement form) {
		// setModified(true);
		//
		// }
		// });

		return f.getPanel(true);
	}

	/**
	 * Helpermethod
	 * 
	 * @param title
	 * @param value
	 * @param id
	 * @return
	 */
	private static FormElement createField(final HashMap<String, String> data, String title, String value, final String id) {
		data.put(id, value);
		return new FormTextField(title, value).addChangeListener(new FormElementChangeListener() {

			@Override
			public void stateChanged(FormElement e) {
				data.put(id, e.getContentAsString());

			}
		});
	}

	@Override
	public String getTitle() {
		return I18N.t("Feedback");
	}

	@Override
	public String getIcon() {
		return "mail_web";
	}
}

/*
 * Location: /Users/abby/Desktop/YAamsRGSSPlayer.jar Qualified Name:
 * de.yaams.rgssplayer.helper.gui.config.ConfigTab JD-Core Version: 0.6.0
 */