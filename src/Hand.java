import java.awt.Color;

public class Hand
extends BodyPart {
    boolean RL;

    public Hand(Point3D magozPoint, boolean RL) {
        super(magozPoint);
        this.parts.add(new Box(magozPoint));
        this.parts.add(new Box(magozPoint));
        this.parts.add(new Box(magozPoint));
        this.parts.add(new Box(magozPoint));
        this.platot = new Point3D[4][6][4];
        this.RL = RL;
        this.name = RL ? "rHand" : "lHand";
        this.setSuperPlatot();
    }

    public void buildShape(double x, double y, double z, double dx, double dy, double dz, Color armColor, Color handColor) {
        ((Box)this.parts.get(0)).buildShape(x, y, z, dx, dy / 6.0 * 2.0, dz, armColor);
        ((Box)this.parts.get(1)).buildShape(x, y + dy / 6.0 * 2.0, z, dx, dy / 6.0 * 4.0, dz, armColor);
        ((Box)this.parts.get(2)).buildShape(x, y + dy, z, dx, dy / 12.0, dz * 1.3, handColor);
        if (this.RL) {
            ((Box)this.parts.get(3)).buildShape(x + dx, y + dy, z + dz / 10.0, dx / 4.0, dy / 12.0 / 5.0 * 3.0, dz * 0.9, handColor);
        } else {
            ((Box)this.parts.get(3)).buildShape(x - dx / 4.0, y + dy, z + dz / 10.0, dx / 4.0, dy / 12.0 / 5.0 * 3.0, dz * 0.9, handColor);
        }
        this.axis.setXYZ(x + dx / 2.0, y + dy / 12.0, z + dz / 2.0);
        this.xyz.setXYZ(x, y, z);
        this.platot[0] = ((Box)this.parts.get((int)0)).platot;
        this.platot[1] = ((Box)this.parts.get((int)1)).platot;
        this.platot[2] = ((Box)this.parts.get((int)2)).platot;
        this.platot[3] = ((Box)this.parts.get((int)3)).platot;
    }
}
