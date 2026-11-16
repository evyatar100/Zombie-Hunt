import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Aperture {
    Random generator = new Random();
    Box[] parts;
    Color color = Color.orange;
    Point3D xyz;
    Point3D aperturePoint;
    int numOfParts = 3;
    int numOfPoint = 3;
    Point3D magozPoint;

    public Aperture(Point3D magozPoint) {
        this.parts = new Box[this.numOfParts];
        int i = 0;
        while (i < this.numOfParts) {
            this.parts[i] = new Box(magozPoint);
            ++i;
        }
        this.xyz = new Point3D();
        this.aperturePoint = new Point3D();
    }

    public void buildShape(double x, double y, double z) {
        double[] xReal = new double[]{110.0, 125.0, 0.0};
        double[] yReal = new double[]{-9.5, -8.0, -7.0};
        double[] zReal = new double[]{1.0, 2.5, 4.0};
        int i = 0;
        while (i < this.numOfPoint) {
            int n = i;
            xReal[n] = xReal[n] + x;
            int n2 = i;
            yReal[n2] = yReal[n2] + y;
            int n3 = i++;
            zReal[n3] = zReal[n3] + z;
        }
        i = 0;
        while (i < this.numOfParts) {
            this.parts[i].buildShape(xReal[0], yReal[i % 2], xReal[1], yReal[i % 2], xReal[1], yReal[2], xReal[0], yReal[2], zReal[i], 1.5, this.color);
            ++i;
        }
        this.xyz.setXYZ(xReal[0], xReal[1], xReal[2]);
        this.aperturePoint.setXYZ((this.parts[0].xReal[0] + this.parts[2].xReal[6]) / 2.0, this.parts[1].yReal[0], (this.parts[0].zReal[0] + this.parts[2].zReal[6]) / 2.0);
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
        this.aperturePoint.mullMat(mat);
    }

    public void convertAndShow(Graphics page) {
        if (this.parts[0].xReal[0] >= this.parts[0].xReal[0]) {
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
