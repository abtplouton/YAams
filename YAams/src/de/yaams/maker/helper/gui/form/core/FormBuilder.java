/**
 * 
 */
package de.yaams.maker.helper.gui.form.core;

import java.awt.BorderLayout;
import java.awt.SystemColor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.commons.lang.Validate;

import com.jgoodies.forms.builder.ButtonBarBuilder2;
import com.jidesoft.pane.CollapsiblePanes;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.extensions.ExtentionManagement;
import de.yaams.maker.helper.gui.AE;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.YMessagesDialog;
import de.yaams.maker.helper.gui.YScrollPane;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormSaveElement;

/**
 * @author Nebli
 * 
 */
public class FormBuilder {

	protected HashMap<String, JComponent> buttons;
	protected HashMap<String, FormHeader> headers;
	protected JComponent center;
	protected String id;

	/**
	 * 
	 */
	public FormBuilder(String id) {
		headers = new HashMap<String, FormHeader>();
		buttons = new HashMap<String, JComponent>();
		this.id = id;

		// add basic
		addHeader("basic", new FormHeader("", null));
	}

	/**
	 * Return the element or nothing
	 * 
	 * @param id
	 * @return
	 */
	public boolean existElement(String id) {
		// check it
		Validate.notEmpty(id, "Exist Element is empty");

		// get it
		return getHeader(id).existElement(getElementID(id));
	}

	/**
	 * Return the element or nothing
	 * 
	 * @param id
	 * @return
	 */
	public FormElement getElement(String id) {
		// check it
		Validate.notEmpty(id, "Element ID is empty");

		// get it
		return getHeader(id).get(getElementID(id));
	}

	/**
	 * Get ID of the element
	 * 
	 * @param id
	 * @return
	 */
	protected String getElementID(String id) {
		// check it
		Validate.notEmpty(id, "ElementID is empty");

		// get it
		return id.substring(id.indexOf(".") + 1);
	}

	/**
	 * Get the headerelement
	 * 
	 * @param id
	 * @return
	 */
	public FormHeader getHeader(String id) {
		return headers.get(existHeader(id));
	}

	/**
	 * Get only the header id of the element
	 * 
	 * @param id
	 * @return
	 */
	protected String getHeaderID(String id) {
		if (id.indexOf(".") != -1) {
			return id.substring(0, id.indexOf("."));
		} else {
			return id;
		}
	}

	/**
	 * Exist an element? return throw, if not exist
	 * 
	 * @param id
	 */
	public boolean containsHeader(String id) {
		return headers.containsKey(getHeaderID(id));
	}

	/**
	 * Exist an element? return throw, if not exist
	 * 
	 * @param id
	 */
	protected String existHeader(String id) {
		// get
		id = getHeaderID(id);

		// check
		Validate.notNull(headers.get(id), "Header " + id + " is missing");

		return id;
	}

	/**
	 * @param elements
	 *            the elements to set
	 */
	public FormElement addElement(String id, FormElement element) {
		try {
			getHeader(id).add(getElementID(id), element);
		} catch (Throwable t) {
			YEx.info("Can not add Element " + id, t);
		}
		return element;
	}

	/**
	 * @param elements
	 *            the elements to set
	 */
	public FormHeader addHeader(String id, FormHeader element) {
		headers.put(id, element);
		return element;
	}

	/**
	 * @param elements
	 *            the elements to set
	 */
	public void addButton(String id, JComponent element) {
		buttons.put(id, element);
	}

	/**
	 * Build the button bar, or null
	 * 
	 * @return
	 */
	protected JComponent getButtonBar(boolean withAutoSaveFunction) {
		// add buttons?
		if (!withAutoSaveFunction || buttons.size() > 0) {
			ButtonBarBuilder2 b = new ButtonBarBuilder2();

			b.addGlue();
			// add buttons
			for (String key : buttons.keySet()) {
				b.addRelatedGap();
				b.addButton(buttons.get(key));
			}

			// add save button?
			if (!withAutoSaveFunction) {
				b.addUnrelatedGap();
				b.addButton(YFactory.b(I18N.t("Speichern"), "disk", new AE() {

					@Override
					public void run() {

						// save all
						for (String key : headers.keySet()) {
							for (String keye : headers.get(key).getElements().keySet()) {
								FormElement e = headers.get(key).get(keye);
								if (e instanceof FormSaveElement) {
									((FormSaveElement) e).informListeners();
								}
							}
						}

					}
				}));
			}

			// build layout
			return b.getPanel();

		} else {
			return null;
		}
	}

	/**
	 * Build the main bar
	 * 
	 * @param withAutoSaveFunction
	 * @return
	 */
	protected JComponent getMainPanel(boolean withAutoSaveFunction) {
		// one or more?
		if (headers.size() == 1) {
			return new YScrollPane(headers.entrySet().iterator().next().getValue().getPanelFromBuilder(withAutoSaveFunction));
		} else {
			// build basics
			CollapsiblePanes cp = new CollapsiblePanes();
			cp.setGap(0);
			cp.setBorder(BorderFactory.createEmptyBorder());
			cp.setBackground(SystemColor.window);

			// get elements
			Object[] keys = headers.keySet().toArray();
			Arrays.sort(keys);
			LinkedList<Object> obj = new LinkedList<Object>(Arrays.asList(keys));

			// build order
			LinkedList<String> erg = new LinkedList<String>();
			String cid;
			int sort;
			while (obj.size() > 0) {
				sort = headers.get(obj.get(0)).getSorting();
				cid = (String) obj.get(0);
				// add all elements
				for (final Object key : obj) {
					if (headers.get(key).getSorting() < sort) {
						sort = headers.get(key).getSorting();
						cid = (String) key;
					}
				}

				// remove it
				erg.add(cid);
				obj.remove(cid);
			}

			// add all elements
			for (final Object key : erg) {
				cp.add(headers.get(key).getPane(withAutoSaveFunction, this));
			}
			cp.addExpansion();

			return new YScrollPane(cp);
		}
	}

	/**
	 * Generate the panel with the form
	 */
	public JComponent getPanel(final boolean withAutoSaveFunction) {

		// build objects
		HashMap<String, Object> objects = new HashMap<String, Object>();
		objects.put("form", this);

		// inform
		ExtentionManagement.work("form." + id, objects);

		// inform all listerns
		for (String head : headers.keySet()) {
			for (String ele : headers.get(head).getElements().keySet()) {
				headers.get(head).get(ele).informListeners();
			}
		}

		// collect
		JComponent main = getMainPanel(withAutoSaveFunction);
		JComponent buttons = getButtonBar(withAutoSaveFunction);

		// build layout
		if (buttons != null || center != null) {
			final JPanel ges = new JPanel(new BorderLayout());
			// add center?
			if (center == null) {
				ges.add(main, BorderLayout.CENTER);
			} else {
				ges.add(main, BorderLayout.NORTH);
				ges.add(center, BorderLayout.CENTER);
			}
			// add buttons?
			if (buttons != null) {
				ges.add(buttons, BorderLayout.SOUTH);
			}
			return ges;
		} else {
			return main;
		}

		// if ("1".equals("1")) {
		// return builder.getPanel();
		// }
		// // build layout
		// final JPanel p = new JPanel();
		// if (true) {
		// p.setLayout(new ParagraphLayout(3, 3, 3, 3, 1, 1));
		// } else {
		// p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		// }
		//
		// // add all elements
		// for (final FormElement f : elements) {
		// f.addToContainer(p, withAutoSaveFunction, true);
		// }
		//
		// // add buttons?
		// if (!withAutoSaveFunction || buttons.size() > 0) {
		// final JPanel button = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		// // add save button?
		// if (!withAutoSaveFunction) {
		// button.add(b(I18N.t("Save"), "disk", new ActionListener() {
		//
		// @Override public void actionPerformed(final ActionEvent ae) {
		//
		// // save all
		// for (final FormElement f : elements) {
		// if (f instanceof FormSaveElement) {
		// ((FormSaveElement) f).informListeners();
		// }
		// }
		//
		// // save add?
		// if (saveAction != null) {
		// saveAction.actionPerformed(ae);
		// }
		//
		// }
		// }));
		// }
		//
		// // add buttons
		// for (final JComponent j : buttons) {
		// button.add(j);
		// }
		//
		// // build layout
		// final JPanel ges = new JPanel(new BorderLayout());
		// if (true) {
		// ges.add(new YScrollPane(p), BorderLayout.CENTER);
		// } else {
		// // set layout
		// final JPanel northbox = new JPanel(new BorderLayout());
		// northbox.add(BorderLayout.NORTH, p);
		//
		// final YScrollPane j = new YScrollPane(northbox);
		// j.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		// ges.add(new YScrollPane(j), BorderLayout.CENTER);
		// }
		// ges.add(button, BorderLayout.SOUTH);
		// return ges;
		//
		// } else {
		// return new YScrollPane(p);
		// }

	}

	/**
	 * Add a new chance listener to all elements;
	 * 
	 * @param c
	 */
	public FormBuilder addChangeListener(FormElementChangeListener c) {
		// add listener
		for (String key : headers.keySet()) {
			for (String keye : headers.get(key).getElements().keySet()) {
				headers.get(key).get(keye).addChangeListener(c);
			}
		}

		return this;
	}

	/**
	 * Set for all headers the same columns
	 * 
	 * @param col
	 * @return
	 */
	public FormBuilder setColumn(int col) {
		// set
		for (String key : headers.keySet()) {
			headers.get(key).setColumn(col);
		}

		return this;
	}

	/**
	 * @return the center
	 */
	public JComponent getCenter() {
		return center;
	}

	/**
	 * @param center
	 *            the center to set
	 */
	public void setCenter(JComponent center) {
		this.center = center;
	}

	/**
	 * @return the buttons
	 */
	public HashMap<String, JComponent> getButtons() {
		return buttons;
	}

	/**
	 * Check if all elements valide
	 * 
	 * @return
	 */
	public boolean isValidate() {
		YMessagesDialog y = new YMessagesDialog(I18N.t("Fehler beim Ausfüllen"), "validate." + id);
		y.setFooter(I18N.t("Trotzdem fortfahren?"));

		// ask all
		for (String key : headers.keySet()) {
			for (String keye : headers.get(key).getElements().keySet()) {
				headers.get(key).get(keye).isValidate(y);
			}
		}

		return y.showQuestion();
	}

	/**
	 * @return the id
	 */
	public String getIdofHeader(FormHeader header) {
		// check it
		for (String key : headers.keySet()) {
			if (headers.get(key) == header) {
				return key;
			}
		}
		return null;
	}
}
