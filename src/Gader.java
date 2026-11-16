import java.awt.Color;
import java.awt.Graphics;

public class Gader
extends Shape {
    Brick basis;
    Galil top;
    Brick bar1;
    Brick bar2;
    Color color = Color.white;
    Color barColor = new Color(240, 240, 240);
    Point3D magozPoint;
    Matrix3D mati = new Matrix3D();
    Point3D[][] platot;

    public Gader(Point3D magozPoint) {
        this.magozPoint = magozPoint;
        this.basis = new Brick(magozPoint);
        this.top = new Galil(magozPoint, 8);
        this.bar1 = new Brick(magozPoint);
        this.bar2 = new Brick(magozPoint);
        this.platot = new Point3D[3][];
        this.setSuperPlatot();
    }

    public void buildShape(double x, double y, double z, double dx, double dy, double dz, double dis) {
        this.basis.buildShape(x, y, z, dx, dy, 1, this.color);
        this.top.buildGalil(x + dx / 2.0, y, z, dx / 2.0, Math.PI, dz, this.color);
        this.mati.setMatRotateZFix(-1.5707963267948966, this.top.xyz.x, this.top.xyz.y, this.top.xyz.z);
        this.top.mullMat(this.mati);
        this.bar1.buildShape(x - dis / 2.0, y + dy * 0.05, z + dz, dx + dis, 50.0, 1, this.barColor);
        this.bar2.buildShape(x - dis / 2.0, y + dy * 0.8, z + dz, dx + dis, 50.0, 1, this.barColor);
    }

    public void fromPointToGuf() {
        this.top.fromPointToGuf();
    }

    public void mullMat(Matrix3D mat) {
        this.basis.mullMat(mat);
        this.top.mullMat(mat);
        this.bar1.mullMat(mat);
        this.bar2.mullMat(mat);
    }

    @Override
    public void convertAndShow(Graphics page) {
        if (this.basis.Check().booleanValue()) {
            this.bar1.convertAndShow(page, false);
            this.bar2.convertAndShow(page, false);
            this.basis.convertAndShow(page, false);
            this.top.convertAndShow(page, false);
        }
    }

    public double getCloseZ() {
        return Math.min(this.basis.getCloseZ(), this.top.getCloseZ());
    }

    private void setSuperPlatot() {
        ((Shape)this).platot = new Point3D[1][1][];
        ((Shape)this).platot[0][0] = this.basis.getPlatot();
    }
}
