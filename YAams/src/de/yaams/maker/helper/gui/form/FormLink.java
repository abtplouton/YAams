/**
 * 
 */
package de.yaams.maker.helper.gui.form;

import java.awt.event.ActionEvent;

import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.hyperlink.AbstractHyperlinkAction;

import de.yaams.maker.helper.SystemHelper;

/**
 * @author Nebli
 * 
 */
public class FormLink extends FormElement {

	protected JXHyperlink link;

	/**
	 * @param title
	 */
	public FormLink(final String content, final String url) {
		super("");
		if (content != null) {
			// final String s = content.toString();

			AbstractHyperlinkAction<Object> linkAction = new AbstractHyperlinkAction<Object>(content) {
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e) {
					SystemHelper.openUrl(content);
					setVisited(true);
				}
			};
			link = new JXHyperlink(linkAction);
		}
		element = link;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.packandgo.helper.gui.form.FormElement#getContentAsString()
	 */
	@Override
	public String getContentAsString() {
		return link != null ? link.getText() : null;
	}

}
