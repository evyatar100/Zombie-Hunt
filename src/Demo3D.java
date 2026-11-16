import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class Demo3D {
    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)screenSize.getWidth();
        int height = (int)screenSize.getHeight();
        int panelWidth = 950;
        int panelHeight = 650;
        JFrame myFrame = new JFrame("Evyatar's Project!");
        myFrame.setDefaultCloseOperation(3);
        myFrame.setResizable(false);
        myFrame.setLocation((width - 950) / 2, (height - 650) / 3);
        MainPanel mainPanel = new MainPanel();
        mainPanel.locationOnScreen.setLocation((width - 950) / 2, (height - 650) / 3);
        mainPanel.setOpaque(true);
        mainPanel.setPreferredSize(new Dimension(950, 650));
        myFrame.add((Component)mainPanel, "Center");
        myFrame.pack();
        myFrame.setVisible(true);
    }
}
