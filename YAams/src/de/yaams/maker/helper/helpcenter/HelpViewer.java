/**
 * 
 */
package de.yaams.maker.helper.helpcenter;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXEditorPane;

import com.ezware.common.Strings;

import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.gui.YHeader;
import de.yaams.maker.helper.gui.rightclick.YRightClickMenuText;

/**
 * @author abt
 * 
 */
public class HelpViewer extends JPanel {
	private static final long serialVersionUID = -8488848346473249006L;
	
	protected YHeader header;
	protected JXEditorPane editor;
	protected HelpFile help;
	
	/**
	 * Create a new HelpViewer
	 */
	public HelpViewer(String id) {
		super(new BorderLayout());
		
		setId(id);
	}
	
	/**
	 * Set a new content
	 * 
	 * @param id
	 */
	public void setId(String id) {
		// clear
		removeAll();
		
		// set
		try {
			
			// read
			help = HelpCenterManagement.get(id);
			
			// build layout
			header = new YHeader(help.getTitle(), help.getIcon() + "_help");
			add(header, BorderLayout.NORTH);
			
			editor = new JXEditorPane(help.getUrl());
			editor.setEditable(false);
			YRightClickMenuText.install(editor);
			
			add(new JScrollPane(editor), BorderLayout.CENTER);
		} catch (Throwable t) {
			editor = new JXEditorPane();
			editor.setText(t.getClass() + " " + t.getMessage() + " " + Strings.stackStraceAsString(t));
			add(editor);
			YEx.info("Can not display helpfile " + id + " (" + HelpCenterManagement.get(id) + ")", t);
		}
		
		invalidate();
		revalidate();
	}
	
}
