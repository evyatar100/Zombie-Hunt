import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Barrel {
    Random generator = new Random();
    Box[] parts;
    Galil barrel;
    double[] xReal = new double[]{70.0, 140.0, 142.0, 152.0, 152.0, 142.0, 140.0, 70.0};
    double[] yReal = new double[]{-7.0, -7.0, -7.0, -7.0, -5.0, -5.0, -3.0, -3.0};
    Color color = new Color(50, 50, 50);
    Point3D xyz;
    int numOfParts = 3;
    int numOfPoint = 8;
    Point3D magozPoint;

    public Barrel(Point3D magozPoint) {
        this.barrel = new Galil(magozPoint, 20);
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
        this.barrel.buildGalil(x + 152.0, y + -6.0, z + 2.5, 1.0, Math.PI * 2, 93.0, this.color);
        Matrix3D mati = new Matrix3D();
        mati.setMatRotateYFix(-1.5707963267948966, this.barrel.xyz.x, this.barrel.xyz.y, this.barrel.xyz.z);
        this.barrel.mullMat(mati);
        this.xyz.setXYZ(x, y, z);
    }

    public void fromPointToGuf() {
        this.barrel.fromPointToGuf();
        int i = 0;
        while (i < this.numOfParts) {
            this.parts[i].fromPointToGuf();
            ++i;
        }
    }

    public void mullMat(Matrix3D mat) {
        this.barrel.mullMat(mat);
        int i = 0;
        while (i < this.numOfParts) {
            this.parts[i].mullMat(mat);
            ++i;
        }
        this.xyz.mullMat(mat);
    }

    public void convertAndShow(Graphics page) {
        if (this.parts[0].xyz.z > this.parts[1].xyz.z) {
            this.barrel.convertAndShow(page, true);
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
            this.barrel.convertAndShow(page, true);
        }
    }
}
