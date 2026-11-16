import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class Life
implements ActionListener {
    double[] xReal;
    double[] yReal;
    int[] xInt;
    int[] yInt;
    Color c;
    int myLife = 0;
    boolean isAlive = true;
    Timer timer = new Timer(3000, this);
    int numOfPoint = 4;

    public Life(Point3D magozPoint) {
        this.c = new Color(255, 0, 0, 0);
        this.xReal = new double[this.numOfPoint];
        this.yReal = new double[this.numOfPoint];
        this.xInt = new int[this.numOfPoint];
        this.yInt = new int[this.numOfPoint];
        this.xReal[0] = 0.0;
        this.yReal[0] = 0.0;
        this.xReal[1] = magozPoint.x * 2.0;
        this.yReal[1] = 0.0;
        this.xReal[2] = magozPoint.x * 2.0;
        this.yReal[2] = magozPoint.y * 2.0;
        this.xReal[3] = 0.0;
        this.yReal[3] = magozPoint.y * 2.0;
    }

    public void restart() {
        this.isAlive = true;
        this.myLife = 0;
        this.setTransparency();
    }

    public void convert(double[] xr, double[] yr, int[] xi, int[] yi, int aNum) {
        int i = 0;
        while (i < aNum) {
            xi[i] = (int)xr[i];
            yi[i] = (int)yr[i];
            ++i;
        }
    }

    public void convertAndShow(Graphics page) {
        this.convert(this.xReal, this.yReal, this.xInt, this.yInt, this.numOfPoint);
        page.setColor(this.c);
        page.fillPolygon(this.xInt, this.yInt, this.numOfPoint);
    }

    public void setTransparency() {
        this.c = new Color(255, 0, 0, this.myLife);
    }

    public void hit() {
        this.myLife += 20;
        if (this.myLife >= 150) {
            this.isAlive = false;
        } else {
            this.setTransparency();
        }
        this.timer.restart();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.myLife -= 50;
        if (this.myLife < 0) {
            this.myLife = 0;
        }
        this.setTransparency();
    }
}
