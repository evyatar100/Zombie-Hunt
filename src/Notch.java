import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Notch {
    Random generator = new Random();
    Box notch;
    Color color = Color.RED;
    Point3D xyz;
    Point3D notchPoint;
    Point3D magozPoint;

    public Notch(Point3D magozPoint) {
        this.notch = new Box(magozPoint);
        this.xyz = new Point3D();
        this.notchPoint = new Point3D();
    }

    public void buildShape(double x, double y, double z) {
        this.notch.buildShape(x + 240.0, y - 7.0, z + 2.7, 1.0, -2.5, 0.6, this.color);
        this.xyz.setXYZ(x + 240.0, y - 7.0, z + 2.7);
        this.notchPoint.setXYZ((this.notch.xReal[0] + this.notch.xReal[1]) / 2.0, this.notch.yReal[6], (this.notch.zReal[0] + this.notch.zReal[4]) / 2.0);
    }

    public void fromPointToGuf() {
        this.notch.fromPointToGuf();
    }

    public void mullMat(Matrix3D mat) {
        this.notch.mullMat(mat);
        this.xyz.mullMat(mat);
        this.notchPoint.mullMat(mat);
    }

    public void convertAndShow(Graphics page) {
        this.notch.convertAndShow(page, true);
    }
}
