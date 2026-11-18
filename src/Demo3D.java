import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.plaf.PanelUI;

public class Demo3D
{
	public static void main(String[] args) 
	{
		Sound.preload();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		final int width = (int) screenSize.getWidth();
		final int height = (int) screenSize.getHeight();
		final int panelWidth = 950;
		final int panelHeight = 650;
		
		JFrame myFrame=new JFrame("Evyatar's Project!");
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setResizable(false);
		myFrame.setLocation((width-panelWidth)/2, (height-panelHeight)/3);
		MainPanel mainPanel=new MainPanel();
		mainPanel.locationOnScreen.setLocation((width-panelWidth)/2, (height-panelHeight)/3);
		mainPanel.setOpaque(true);
		mainPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
		myFrame.add(mainPanel,BorderLayout.CENTER);
		myFrame.pack();
		myFrame.setVisible(true);
		mainPanel.requestFocusInWindow();
	}
}

