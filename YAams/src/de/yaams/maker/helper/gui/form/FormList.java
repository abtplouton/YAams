/**
 * 
 */
package de.yaams.maker.helper.gui.form;

import java.util.ArrayList;

import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.YDialog;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.list.YArrayList;

/**
 * @author abby
 * 
 */
public abstract class FormList<T> extends FormSaveElement {

	protected FList<T> list;
	protected String title, icon;

	/**
	 * Create a new
	 * 
	 * @param title
	 * @param ary
	 */
	public FormList() {
		super("");
	}

	/**
	 * Build the gui
	 * 
	 * @param title
	 * @param icon
	 * @param ary
	 * @param add
	 * @param open
	 * @param delete
	 */
	protected void buildGui(String title, String icon, ArrayList<T> ary, boolean add, boolean open, boolean delete) {
		this.title = title;
		this.icon = icon;

		// build list
		list = new FList<T>(ary);
		list.setAdd(add);
		list.setConfig(true);
		list.setOpen(open);
		list.setDelete(delete);

		list.buildToolbar(title, icon);

		// add it
		element = list;
	}

	/**
	 * Helpermethod to create a new element
	 * 
	 * @return
	 */
	protected abstract T getNewElement();

	/**
	 * Edit the element
	 * 
	 * @param f
	 */
	protected void configForm() {
		// can edit?
		if (!list.canEdit()) {
			return;
		}

		// build
		FormBuilder f = new FormBuilder("formlist." + list.getSelectedObject().getClass());
		configForm(f, list.getSelectedObject());

		// show
		YDialog.showForm(I18N.t("Edit {0}", title), icon, f);

	}

	/**
	 * Edit the element
	 * 
	 * @param f
	 */
	protected abstract void configForm(FormBuilder f, T element);

	/**
	 * Get the icon
	 * 
	 * @param f
	 */
	protected abstract Object getIcon(T element);

	/**
	 * Get the desc
	 * 
	 * @param f
	 */
	protected String getDesc(T element) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.gui.form.FormElement#getContentAsString()
	 */
	@Override
	public String getContentAsString() {
		return list.getSelectedObject().toString();
	}

	/**
	 * Helpermethod to get the text for the list element/title, normally
	 * toString.
	 * 
	 * @param value
	 * @return
	 */
	public Object getText(final Object value) {
		return list.getText(value);
	}

	/**
	 * Intern list
	 * 
	 * @author abby
	 * 
	 * @param <N>
	 */
	public class FList<N> extends YArrayList<T> {

		private static final long serialVersionUID = 3488084250133566553L;

		public FList(ArrayList<T> ary) {
			super(ary);
		}

		@Override
		public void add() {
			add(getNewElement());
			configForm();
			informListeners();

		}

		@Override
		public Object getIcon(T o) {
			return FormList.this.getIcon(o);
		}

		@Override
		public boolean isModified(T o) {
			return false;
		}

		@Override
		protected void info() {
		}

		@Override
		public String getDesc(T o) {
			return FormList.this.getDesc(o);
		}

		@Override
		protected void configIntern() {
			configForm();

			// update entry
			int id = list.getSelectedIndex();
			model.remove(id);
			model.add(id, getAry().get(id));
			list.setSelectedIndex(id);

			informListeners();

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.yaams.packandgo.helper.gui.list.YSimpleList#delObject(int)
		 */
		@Override
		public void delObject(final int id) {
			super.delObject(id);
			informListeners();
		}

		/**
		 * Helpermethod to get the text for the list element/title, normally
		 * toString.
		 * 
		 * @param value
		 * @return
		 */
		@Override
		public Object getText(final Object value) {
			return FormList.this.getText(value);
		}

	}
}
