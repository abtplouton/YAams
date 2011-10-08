/**
 * 
 */
package de.yaams.maker.helper.gui;

import java.awt.BorderLayout;
import java.awt.HeadlessException;

import javax.swing.JFrame;

/**
 * @author abby
 * 
 */
public class TestFrame extends JFrame {

	private static final long serialVersionUID = -3053007977255362769L;

	/**
	 * @throws HeadlessException
	 */
	public TestFrame() throws HeadlessException {
		setLayout(new BorderLayout());
	}

	public void start() {
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
