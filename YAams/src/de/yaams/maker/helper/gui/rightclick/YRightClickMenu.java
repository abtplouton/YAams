/**
 * 
 */
package de.yaams.maker.helper.gui.rightclick;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

/**
 * @author abt
 * 
 */
public class YRightClickMenu {
	
	/**
	 * Create a new RightClickMenu
	 */
	public static void installRightClickMenu(final JTextComponent tc) {
		
		tc.addMouseListener(new MouseAdapter() {
			
			@Override public void mouseReleased(MouseEvent me) {
				mousePressed(me);
			}
			
			@Override public void mousePressed(MouseEvent me) {
				// right click?
				if (me.isPopupTrigger()) {
					// create popup menu and show
					JPopupMenu menu = new JPopupMenu();
					menu.add(new CutAction(tc));
					menu.add(new CopyAction(tc));
					menu.add(new PasteAction(tc));
					menu.add(new DeleteAction(tc));
					menu.addSeparator();
					menu.add(new SelectAllAction(tc));
					
					// show it
					Point pt = SwingUtilities.convertPoint(me.getComponent(), me.getPoint(), tc);
					menu.show(tc, pt.x, pt.y);
					
				}
				
			}
		});
		
	}
	
}
