import java.awt.Color;
import java.awt.Graphics;

public class Column
extends Shape {
    Box basis;
    Color color;
    Point3D magozPoint;
    Point3D place;
    Point3D xyz1;
    boolean isBorder;

    public Column(Point3D magozPoint) {
        this.magozPoint = magozPoint;
        this.basis = new Box(magozPoint);
        this.setSuperPlatot();
    }

    public void buildShape(double x, double y, double z, double dx, double dy, double dz, Color color, boolean isBorder) {
        this.place = new Point3D(x, y, z);
        this.xyz1 = new Point3D(x + dx, y, z);
        this.basis.buildShape(x, y, z, dx, dy, dz, color);
        this.isBorder = isBorder;
    }

    public void fromPointToGuf() {
        this.basis.fromPointToGuf();
    }

    public void mullMat(Matrix3D mat) {
        this.basis.mullMat(mat);
    }

    @Override
    public void convertAndShow(Graphics page) {
        if (this.isBorder && this.basis.wallCheck()) {
            this.basis.convertAndShow(page, false);
        } else if (!this.isBorder) {
            this.basis.convertAndShow(page, false);
        }
    }

    public double getCloseZ() {
        return this.basis.getCloseZ();
    }

    private void setSuperPlatot() {
        this.platot = new Point3D[1][][];
        this.platot[0] = this.basis.getPlatot();
    }
}
