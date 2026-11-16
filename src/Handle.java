import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Handle {
    Random generator = new Random();
    Box[] parts;
    double[] xReal = new double[]{0.0, 47.0, 54.0, 70.0, 95.0, 140.0, 142.0, 160.0, 160.0, 155.0, 140.0, 95.0, 70.0, 54.0, 47.0, 0.0};
    double[] yReal = new double[]{0.0, 0.0, 2.0, -3.0, -3.0, -3.0, -5.0, -5.0, 3.0, 5.0, 5.0, 10.0, 10.0, 20.0, 17.0, 25.0};
    Color color = new Color(154, 105, 75);
    Point3D xyz;
    int numOfParts = 7;
    int numOfPoint = 16;
    Point3D magozPoint;

    public Handle(Point3D magozPoint) {
        this.parts = new Box[this.numOfParts];
        int i = 0;
        while (i < this.numOfParts) {
            this.parts[i] = new Box(magozPoint);
            ++i;
        }
        this.xyz = new Point3D();
    }

    public void buildShape(double x, double y, double z) {
        int i = 0;
        while (i < this.numOfPoint) {
            int n = i;
            this.xReal[n] = this.xReal[n] + x;
            int n2 = i++;
            this.yReal[n2] = this.yReal[n2] + y;
        }
        i = 0;
        while (i < this.numOfParts) {
            this.parts[i].buildShape(this.xReal[i], this.yReal[i], this.xReal[i + 1], this.yReal[i + 1], this.xReal[this.numOfPoint - i - 2], this.yReal[this.numOfPoint - i - 2], this.xReal[this.numOfPoint - i - 1], this.yReal[this.numOfPoint - i - 1], z, 6.0, this.color);
            ++i;
        }
        this.xyz.setXYZ(x, y, z);
        this.parts[0].setLines(false, false, true, false, true, true);
        this.parts[1].setLines(false, false, true, false, true, false);
        this.parts[2].setLines(false, false, true, false, true, false);
        this.parts[3].setLines(false, false, true, false, true, false);
        this.parts[4].setLines(false, false, true, false, true, false);
        this.parts[5].setLines(false, false, true, false, true, false);
        this.parts[6].setLines(false, false, true, true, true, false);
    }

    public void fromPointToGuf() {
        int i = 0;
        while (i < this.numOfParts) {
            this.parts[i].fromPointToGuf();
            ++i;
        }
    }

    public void mullMat(Matrix3D mat) {
        int i = 0;
        while (i < this.numOfParts) {
            this.parts[i].mullMat(mat);
            ++i;
        }
        this.xyz.mullMat(mat);
    }

    public void convertAndShow(Graphics page) {
        if (this.parts[0].xyz.z > this.parts[1].xyz.z) {
            int i = 0;
            while (i < this.numOfParts) {
                this.parts[i].convertAndShow(page, true);
                ++i;
            }
        } else {
            int i = this.numOfParts - 1;
            while (i > -1) {
                this.parts[i].convertAndShow(page, true);
                --i;
            }
        }
    }
}
