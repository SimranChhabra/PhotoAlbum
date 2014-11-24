package edu.usc.multimedia;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.image.*;

public class Util {

	public static void log(Object str) {
		System.out.println(str);
	}

	public static void displayImage(BufferedImage image) {
		JFrame frame = new JFrame();
		JLabel label = new JLabel(new ImageIcon(image));
		frame.getContentPane().removeAll();
		frame.getContentPane().add(label, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
}
