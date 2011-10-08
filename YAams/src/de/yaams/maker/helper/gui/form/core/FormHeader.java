/**
 * 
 */
package de.yaams.maker.helper.gui.form.core;

import java.awt.BorderLayout;
import java.beans.PropertyVetoException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.pane.event.CollapsiblePaneAdapter;
import com.jidesoft.pane.event.CollapsiblePaneEvent;

import de.yaams.maker.helper.Setting;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.icons.IconCache;

/**
 * @author abt
 * 
 */
public class FormHeader {

	protected HashMap<String, FormElement> elements;
	protected int column;
	protected String id, title, icon;
	protected boolean collapsed;
	protected int sorting;

	/**
	 * Create a new FormHeader
	 */
	public FormHeader(String title, String icon) {
		elements = new HashMap<String, FormElement>();
		column = 2;
		this.title = title;
		this.icon = icon;
		collapsed = false;
	}

	/**
	 * Get the collapspane
	 */
	public CollapsiblePane getPane(boolean withAutoSaveFunction, FormBuilder form) {
		// build it
		final String id = "form.header.collapsed." + form.getIdofHeader(this);

		// build it
		CollapsiblePane ac = icon == null ? new CollapsiblePane(title) : new CollapsiblePane(title, IconCache.get(icon));
		ac.setStyle(CollapsiblePane.DROPDOWN_STYLE);
		ac.addCollapsiblePaneListener(new CollapsiblePaneAdapter() {

			@Override
			public void paneExpanded(CollapsiblePaneEvent event) {
				Setting.set(id, false);

			}

			@Override
			public void paneCollapsed(CollapsiblePaneEvent event) {
				Setting.set(id, true);

			}
		});

		// load setting
		try {
			ac.setCollapsed(Setting.exist(id) ? Setting.get(id, false) : collapsed);
		} catch (PropertyVetoException e) {
			YEx.info("Can't collapsed " + title, e);
		}
		// acp.setContentAreaFilled(true);
		JPanel p = new JPanel(new BorderLayout());
		p.add(getPanelFromBuilder(withAutoSaveFunction), BorderLayout.CENTER);
		p.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		ac.setContentPane(p);
		return ac;
	}

	/**
	 * Build Panel
	 * 
	 * @return
	 */
	public JPanel getPanelFromBuilder(boolean withAutoSaveFunction) {
		// build layout
		StringBuilder sb = new StringBuilder("right:p");
		for (int i = 1; i < column; i++) {
			if (i % 2 == 0) {
				sb.append(", 7dlu, right:p");
			} else {
				sb.append(", 4dlu, min(10dlu;p):grow");
			}
		}

		// build it
		DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout(sb.toString(), ""));
		builder.setDefaultDialogBorder();

		// get elements
		Object[] keys = elements.keySet().toArray();
		Arrays.sort(keys);
		LinkedList<Object> obj = new LinkedList<Object>(Arrays.asList(keys));

		// build order
		LinkedList<String> erg = new LinkedList<String>();
		String cid;
		int sort;
		while (obj.size() > 0) {
			sort = elements.get(obj.get(0)).getSorting();
			cid = (String) obj.get(0);
			// add all elements
			for (final Object key : obj) {
				if (elements.get(key).getSorting() < sort) {
					sort = elements.get(key).getSorting();
					cid = (String) key;
				}
			}

			// remove it
			erg.add(cid);
			obj.remove(cid);
		}

		// add all elements
		for (final Object key : erg) {
			elements.get(key).addToContainer(builder, withAutoSaveFunction);
		}

		return builder.getPanel();
	}

	/**
	 * Exist an element? return throw, if not exist
	 * 
	 * @param id
	 */
	public void existElementThrow(String id) {
		if (!elements.containsKey(id)) {
			throw new IllegalAccessError(id + " is missing");
		}
	}

	/**
	 * Exist an element? return true/false
	 * 
	 * @param id
	 */
	public boolean existElement(String id) {
		if (!elements.containsKey(id)) {
			return false;
		}
		return true;
	}

	/**
	 * Add a new element
	 * 
	 * @param id
	 * @param e
	 */
	public void add(String id, FormElement e) {
		elements.put(id, e);
	}

	/**
	 * Get a element
	 * 
	 * @param id
	 */
	public FormElement get(String id) {
		existElementThrow(id);
		return elements.get(id);
	}

	/**
	 * Remove a element
	 * 
	 * @param id
	 */
	public void remove(String id) {
		elements.remove(id);
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public FormHeader setColumn(int column) {
		this.column = column;
		return this;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public FormHeader setTitle(String title) {
		this.title = title;
		return this;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public FormHeader setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public FormHeader setId(String id) {
		this.id = id;
		return this;
	}

	/**
	 * @return the elements
	 */
	public HashMap<String, FormElement> getElements() {
		return elements;
	}

	/**
	 * @return the collapsed
	 */
	public boolean isCollapsed() {
		return collapsed;
	}

	/**
	 * @param collapsed
	 *            the collapsed to set
	 */
	public FormHeader setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
		return this;
	}

	/**
	 * @return the sorting
	 */
	public int getSorting() {
		return sorting;
	}

	/**
	 * @param sorting
	 *            the sorting to set
	 */
	public FormHeader setSorting(int sorting) {
		this.sorting = sorting;
		return this;
	}
}
