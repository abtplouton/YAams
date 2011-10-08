/**
 * 
 */
package de.yaams.core.helper.gui.form.core;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import de.yaams.core.helper.gui.form.FormElement;
import de.yaams.core.helper.gui.icons.IconCache;

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
	public JPanel getPane(boolean withAutoSaveFunction) {
		// build it
		JPanel p = new JPanel(new BorderLayout());
		p.add(new JLabel(title, IconCache.get(icon), SwingConstants.LEADING), BorderLayout.NORTH);
		p.add(getPanelFromBuilder(withAutoSaveFunction), BorderLayout.CENTER);
		return p;
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
					cid = (String) (key);
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
	protected void existElement(String id) {
		if (!elements.containsKey(id)) {
			throw new IllegalAccessError(id + " is missing");
		}
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
		existElement(id);
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
