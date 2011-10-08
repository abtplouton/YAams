/**
 * 
 */
package de.yaams.maker.helper.gui.newForm;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.HashMap;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jidesoft.list.DefaultGroupableListModel;
import com.jidesoft.list.GroupList;

import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.gui.YDialog;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.YHeader;
import de.yaams.maker.helper.gui.icons.IconCache;
import de.yaams.maker.helper.helpcenter.HelpViewer;

/**
 * @author abt
 * 
 */
public class NewForm {
	
	protected DefaultGroupableListModel listModel;
	
	/**
	 * Create a new NewForm
	 */
	public NewForm() {
		listModel = new DefaultGroupableListModel();
	}
	
	/**
	 * Add a element to the form
	 * 
	 * @param form
	 */
	public void addElement(INewFormElement form) {
		// add it
		listModel.addElement(form);
		listModel.setGroupAt(form.getGroup(), listModel.size() - 1);
	}
	
	/**
	 * Show it and return the selected element or nothing
	 * 
	 * @param title
	 * @param icon
	 */
	public INewFormElement show(String title, String icon, String helpID) {
		// build objects
		HashMap<String, Object> objects = new HashMap<String, Object>();
		objects.put("newform", this);
		objects.put("id", helpID);
		// inform
		ExtentionManagement.work("form.new", objects);
		
		// build help
		final HelpViewer h = new HelpViewer(helpID);
		
		// build list
		final GroupList gl = new GroupList(listModel);
		gl.setCellRenderer(new IconRenderer());
		gl.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		gl.addListSelectionListener(new ListSelectionListener() {
			
			@Override public void valueChanged(ListSelectionEvent lse) {
				if (lse.getValueIsAdjusting()) {
					return;
				}
				
				// update help
				h.setId(((INewFormElement) gl.getSelectedValue()).getHelpID());
				
			}
		});
		
		// build layout
		JPanel left = new JPanel(new BorderLayout());
		left.add(new YHeader(title, icon), BorderLayout.NORTH);
		left.add(gl, BorderLayout.CENTER);
		
		// show
		if (YDialog.show(title, YFactory.createHorizontPanel(left, h, "newform." + helpID), true) == false) {
			return null;
		}
		
		// add it?
		if (gl.getSelectedValue() != null) {
			return (INewFormElement) gl.getSelectedValue();
		}
		return null;
	}
	
	private static class IconRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = -1567070349418754699L;
		
		@Override public Component getListCellRendererComponent(JList list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, "", index, isSelected, cellHasFocus);
			INewFormElement b = (INewFormElement) value;
			
			label.setIcon(IconCache.get(b.getIcon(), 32));
			label.setToolTipText(b.getTitle());
			return label;
		}
		
	}
	
}
