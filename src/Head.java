import java.awt.Color;

public class Head
extends BodyPart {
    public Head(Point3D magozPoint) {
        super(magozPoint);
        this.parts.add(new Box(magozPoint));
        this.platot = new Point3D[1][6][4];
        this.name = "head";
        this.setSuperPlatot();
    }

    public void buildShape(double x, double y, double z, double dx, double dy, double dz, Color color) {
        ((Box)this.parts.get(0)).buildShape(x, y, z, dx, dy, dz, color);
        this.xyz.setXYZ(x, y, z);
        this.platot[0] = ((Box)this.parts.get((int)0)).platot;
    }
}
