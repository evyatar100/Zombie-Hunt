import java.awt.Color;

public class Leg
extends BodyPart {
    boolean RL;

    public Leg(Point3D magozPoint, boolean RL) {
        super(magozPoint);
        this.parts.add(new Box(magozPoint));
        this.parts.add(new Box(magozPoint));
        this.platot = new Point3D[2][6][4];
        this.RL = RL;
        this.name = RL ? "rLeg" : "lLeg";
        this.setSuperPlatot();
    }

    public void buildShape(double x, double y, double z, double dx, double dy, double dz, Color legColor, Color feetColor) {
        ((Box)this.parts.get(0)).buildShape(x, y, z, dx, dy / 6.0 * 5.0, dz, legColor);
        ((Box)this.parts.get(1)).buildShape(x, y + dy / 6.0 * 5.0, z, dx, dy / 6.0, dz, feetColor);
        this.xyz.setXYZ(x, y, z);
        this.xyzFront.setXYZ(x, y, z);
        this.xyzBack.setXYZ(x, y, z + dz);
        this.platot[0] = ((Box)this.parts.get((int)0)).platot;
        this.platot[1] = ((Box)this.parts.get((int)1)).platot;
    }
}
