/**
 * 
 */
package de.yaams.extensions.rgssproject.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jruby.RubyArray;
import org.jruby.RubyHash;
import org.jruby.RubyObject;
import org.jruby.runtime.builtin.IRubyObject;

import de.yaams.extensions.jruby.RubyHelper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper;
import de.yaams.extensions.rgssproject.database.RGSS1Helper.Type;
import de.yaams.extensions.rgssproject.database.RGSS1Load;
import de.yaams.extensions.rgssproject.database.SystemGObject;
import de.yaams.maker.helper.gui.IZoom;
import de.yaams.maker.helper.gui.YFactory;
import de.yaams.maker.helper.gui.YToolBar;
import de.yaams.maker.helper.gui.icons.IconCache;
import de.yaams.maker.helper.gui.list.YArrayList;
import de.yaams.maker.programm.project.Project;
import de.yaams.maker.programm.ress.RessRess;

/**
 * @author abt
 * 
 */
public class YMapView extends JPanel {

	private static final long serialVersionUID = -8177415082549356097L;

	protected RubyObject map;
	protected Project project;
	protected int selectX, selectY, mapID;
	protected MapView view;
	protected boolean withEvents;

	/**
	 * Create a new MapView
	 */
	public YMapView(int mapID, Project project) {
		this(RGSS1Helper.get(project, Type.MAP).get(mapID).getObject(), project);
	}

	/**
	 * Create a new MapView
	 */
	public YMapView(RubyObject map, Project project) {
		super(new BorderLayout());
		this.project = project;
		view = new MapView();
		selectX = -1;

		// create image
		setMap(map);

		// build gui
		JScrollPane j = new JScrollPane(view);
		j.getViewport().setBackground(SystemColor.window);
		add(j, BorderLayout.CENTER);

		installToolbar();
	}

	/**
	 * Helpermethod to install support, to chance the map
	 */
	public YMapView installMapChance() {

		// create list
		MapList list = new MapList(RGSS1Helper.get(project, Type.MAP));

		add(YFactory.createHorizontPanel(list, (JComponent) getComponent(0), "map"), BorderLayout.CENTER);

		// get it
		return this;
	}

	/**
	 * Update the map view
	 */
	public void recreateEventImage() {
		view.recreateEventImage();
		withEvents = true;
	}

	/**
	 * Helpermethod to install support, for select an tile
	 */
	protected YMapView installToolbar() {
		// set toolbar
		YToolBar bar = YFactory.installZoomlevel(new YToolBar(), view);

		add(bar, BorderLayout.NORTH);

		// get it
		return this;
	}

	/**
	 * Helpermethod to install support, for select an tile
	 */
	public YMapView installClickSupport() {
		view.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				selectX = e.getX() / 32;
				selectY = e.getY() / 32;
				view.repaint();
				// invalidate();
				// revalidate();
			}
		});

		// get it
		return this;
	}

	/**
	 * Update the map view
	 */
	public void recreateImage() {
		view.recreateImage();
		view.invalidate();
		view.revalidate();
	}

	/**
	 * @return the selectX
	 */
	public int getSelectX() {
		return selectX;
	}

	/**
	 * @return the selectY
	 */
	public int getSelectY() {
		return selectY;
	}

	/**
	 * @return the view
	 */
	public MapView getView() {
		return view;
	}

	/**
	 * @author abt
	 * 
	 */
	public class MapView extends JComponent implements IZoom {

		private static final long serialVersionUID = -8177415082549356097L;

		protected BufferedImage cache;
		protected BufferedImage ecache;
		protected ArrayList<BufferedImage> tileset;
		protected double zoom;
		protected static final int TILED = 32;

		/**
		 * Create a new MapView
		 */
		public MapView() {
			zoom = 1;
		}

		/**
		 * Helpermethod to install support, for select an tile
		 */
		public void installClickSupport() {
			addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Zoom Support
					selectX = (int) (e.getX() / (TILED * zoom));
					selectY = (int) (e.getY() / (TILED * zoom));
					repaint();
					// invalidate();
					// revalidate();
				}
			});
			zoom = 1;
		}

		/**
		 * Paint map, overwrite it, to add something
		 */
		@Override
		public void paintComponent(Graphics g) {
			g.drawImage(cache, 0, 0, (int) (cache.getWidth() * zoom), (int) (cache.getHeight() * zoom), null);

			if (ecache != null) {
				g.drawImage(ecache, 0, 0, (int) (ecache.getWidth() * zoom), (int) (ecache.getHeight() * zoom), null);
			}

			int t = (int) (TILED * zoom);

			// draw select?
			if (selectX >= 0) {
				g.setColor(Color.white);
				g.drawRect(selectX * TILED, selectY * TILED, t, t);
				g.setColor(Color.black);
				g.drawRect(selectX * TILED + 1, selectY * TILED + 1, t - 2, t - 2);
			}
		}

		/**
		 * Update the map view
		 */
		public void recreateImage() {

			cache = new BufferedImage(RubyHelper.toInt(map.getInstanceVariable("@width")) * TILED, RubyHelper.toInt(map
					.getInstanceVariable("@height")) * TILED, BufferedImage.TYPE_4BYTE_ABGR);
			setPreferredSize(new Dimension((int) (cache.getWidth() * zoom), (int) (cache.getHeight() * zoom)));

			// paint background
			cache.getGraphics().setColor(Color.white);
			cache.getGraphics().fillRect(0, 0, cache.getWidth(), cache.getHeight());

			// load tileset
			tileset = RGSS1Load.loadAutotilesAsImage(project, RubyHelper.toInt(map.getInstanceVariable("@tileset_id")));
			tileset.addAll(RGSS1Load.loadTilesetAsImage(project, RubyHelper.toInt(map.getInstanceVariable("@tileset_id"))));

			// load map data
			IRubyObject[] data = ((RubyArray) ((RubyObject) map.getInstanceVariable("@data")).getInstanceVariable("@data")).toJavaArray();

			// reset color
			cache.getGraphics().setColor(Color.black);

			// draw
			for (int z = 0; z < 3; z++) {
				// set tiles
				for (int x = 0, u = cache.getWidth() / TILED; x < u; x++) {
					for (int y = 0, v = cache.getHeight() / TILED; y < v; y++) {
						int id = Integer.valueOf(data[x + y * u + z * u * v].toString());
						// exist graphic?
						if (id < 0 || id >= tileset.size()) {
							cache.getGraphics().drawString("!" + id + "!", x * TILED + 2, y * TILED + TILED - 4);
						} else if (tileset.get(id) == null) {
						} else {
							cache.getGraphics().drawImage(tileset.get(id), x * TILED, y * TILED, null);
						}

					}
				}
			}

		}

		/**
		 * Update the map view
		 */
		public void recreateEventImage() {
			ecache = new BufferedImage((Integer) map.getInstanceVariable("@width").toJava(Integer.class) * TILED, (Integer) map
					.getInstanceVariable("@height").toJava(Integer.class) * TILED, BufferedImage.TYPE_4BYTE_ABGR);

			Image eventRaw = IconCache.getImage("event", TILED);

			// paint events
			RubyHash events = (RubyHash) map.getInstanceVariable("@events");
			for (Object id : events.keySet()) {
				// load event
				RubyObject event = (RubyObject) events.get(id);
				int x = (Integer) event.getInstanceVariable("@x").toJava(Integer.class);
				int y = (Integer) event.getInstanceVariable("@y").toJava(Integer.class);
				// get first page for image
				RubyObject page = (RubyObject) ((RubyArray) event.getInstanceVariable("@pages")).get(0);
				// get image
				RubyObject graphic = (RubyObject) page.getInstanceVariable("@graphic");
				BufferedImage img = null;
				int tile = (Integer) graphic.getInstanceVariable("@tile_id").toJava(Integer.class);
				String cname = (String) graphic.getInstanceVariable("@character_name").toJava(String.class);
				// tile, graphic or nothing?
				if (tile > 0) {
					img = tileset.get(tile);
				} else if (cname != null && cname.length() > 0) {
					img = RessRess.getGraphic(project, "Characters", cname);
				}

				// draw bottom
				ecache.getGraphics().drawImage(eventRaw, x * TILED, y * TILED, null);

				// draw event
				if (img != null) {
					ecache.getGraphics().drawImage(img, x * TILED, y * TILED, x * TILED + TILED, y * TILED + TILED, 0, 0, TILED, TILED,
							null);
				}
			}
		}

		@Override
		public double getZoomLevel() {
			return zoom;
		}

		@Override
		public void setZoomLevel(double d) {
			zoom = d;
			setPreferredSize(new Dimension((int) (cache.getWidth() * zoom), (int) (cache.getHeight() * zoom)));
			repaint();

		}

		@Override
		public Dimension getViewDimension() {
			return YMapView.this.getSize();
		}

		@Override
		public Dimension getObjectDimension() {
			return new Dimension(cache.getWidth(), cache.getHeight());
		}

	}

	public class MapList extends YArrayList<SystemGObject> {

		private static final long serialVersionUID = 8860060681534647786L;

		public MapList(ArrayList<SystemGObject> ary) {
			super(ary);
			offSet = 1;
		}

		@Override
		public void add() {
		}

		@Override
		public Object getIcon(SystemGObject o) {
			return "map";
		}

		@Override
		public boolean isModified(SystemGObject o) {
			return false;
		}

		@Override
		protected void info() {
		}

		@Override
		public String getDesc(SystemGObject o) {
			return null;
		}

		@Override
		protected void configIntern() {
			// select new map
			setMap(getSelectedObject().getObject());
		}

		@Override
		public Object getText(final SystemGObject value) {
			SystemGObject o = value;
			return RubyHelper.toInt(o.getObject(), "@id") + ". " + o.getName();
		}

	}

	/**
	 * @return the mapID
	 */
	public int getMapID() {
		return mapID;
	}

	/**
	 * @param map
	 *            the map to set
	 */
	public void setMap(RubyObject map) {
		this.map = map;
		mapID = (Integer) map.getInstanceVariable("@id").toJava(Integer.class);

		recreateImage();
		if (withEvents) {
			recreateEventImage();
		}
		view.repaint();
	}

	/**
	 * @param selectX
	 *            the selectX to set
	 */
	public void setSelectX(int selectX) {
		this.selectX = selectX;
		repaint();
	}

	/**
	 * @param selectY
	 *            the selectY to set
	 */
	public void setSelectY(int selectY) {
		this.selectY = selectY;
		repaint();
	}

}
