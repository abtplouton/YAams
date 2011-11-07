/**
 * 
 */
package de.yaams.extensions.rgssproject.map.nevent.core;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;

import de.yaams.extensions.rgssproject.map.nevent.YEventCommandList;
import de.yaams.maker.helper.I18N;
import de.yaams.maker.helper.gui.YEx;
import de.yaams.maker.helper.gui.form.FormElement;
import de.yaams.maker.helper.gui.form.FormElementChangeListener;
import de.yaams.maker.helper.gui.form.FormInfo;
import de.yaams.maker.helper.gui.form.core.FormBuilder;
import de.yaams.maker.helper.gui.form.core.FormHeader;
import de.yaams.maker.helper.gui.list.YArrayList;

/**
 * @author abt
 * 
 */
public class YEventCodeList extends YArrayList<EventCode> {

	private static final long serialVersionUID = 5395161197125590084L;
	protected YEventCommandList gui;
	protected EventCode oldCode;

	/**
	 * Create a new YEventCodeList
	 * 
	 * @param ary
	 */
	public YEventCodeList(ArrayList<EventCode> ary, YEventCommandList gui) {
		super(ary);

		this.gui = gui;

		// build toolbar
		delete = true;
		buildToolbar(I18N.t("Event Command"), "event");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.gui.list.YSimpleList#add()
	 */
	@Override
	public void add() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.gui.list.YSimpleList#getIcon(java.lang.Object)
	 */
	@Override
	public Object getIcon(EventCode o) {
		return o.getCommand().getIcon(o);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.yaams.maker.helper.gui.list.YSimpleList#isModified(java.lang.Object)
	 */
	@Override
	public boolean isModified(EventCode o) {
		return o.isModified();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.gui.list.YSimpleList#info()
	 */
	@Override
	protected void info() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("TODO - ");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.gui.list.YSimpleList#getDesc(java.lang.Object)
	 */
	@Override
	public String getDesc(EventCode o) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yaams.maker.helper.gui.list.YSimpleList#configIntern()
	 */
	@Override
	protected void configIntern() {
		final EventCode e = getSelectedObject();

		// save it?
		if (oldCode != null) {
			oldCode.saveBack();
		}
		oldCode = e;

		try {
			// build panel
			FormBuilder f = new FormBuilder("event.code.list." + e.getCommand().getIcon());
			f.addHeader("basic", new FormHeader(I18N.t("Grundlegenes"), e.getCommand().getIcon()));

			// build it
			getSelectedObject().buildPanel(f);
			// add modifikation
			f.addChangeListener(new FormElementChangeListener() {

				@Override
				public void stateChanged(FormElement form) {
					e.setModified(true);
					e.setTitleCache(null);

				}
			});

			f.addHeader("warn", new FormHeader(I18N.t("Hinweis für die Benutzung"), "warn").setSorting(5));
			f.addElement(
					"warn.info",
					new FormInfo(
							"",
							I18N.t("Die Unterstützung für Eventcommands ist noch sehr experimentell. Bitte sei vorsichtig, bei dem was du tust.")));

			// display right the infos
			gui.setRight(f.getPanel(true));
		} catch (Throwable t) {
			YEx.info("Can not create Panel", t);
		}

	}

	/**
	 * If the user select one element
	 */
	@Override
	protected void selected() {
		configIntern();
	}

	/**
	 * Format the output fpr the list renderer
	 * 
	 * @param label
	 * @param list
	 * @param value
	 * @param index
	 * @param isSelected
	 * @param cellHasFocus
	 * @return
	 */
	@Override
	public JLabel getListCellRendererComponent(JLabel label, final JList list, final Object value, final int index,
			final boolean isSelected, final boolean cellHasFocus) {
		JLabel l = super.getListCellRendererComponent(label, list, value, index, isSelected, cellHasFocus);
		// add border
		l.setBorder(BorderFactory.createEmptyBorder(0, ((EventCode) value).getIndent() * 8, 0, 0));

		return l;
	}
}
