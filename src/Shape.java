import java.awt.Graphics;

public abstract class Shape
implements Comparable<Shape> {
    Point3D[][][] platot;

    @Override
    public int compareTo(Shape o) {
        if (this.getCloseZ() > o.getCloseZ()) {
            return 1;
        }
        if (this.getCloseZ() < o.getCloseZ()) {
            return -1;
        }
        return 0;
    }

    public boolean isShoot(Point3D a, Point3D b) {
        int i = 0;
        while (i < this.platot.length) {
            int j = 0;
            while (j < this.platot[i].length) {
                Point3D aM = this.platot[i][j][0];
                Point3D bM = this.platot[i][j][1];
                Point3D cM = this.platot[i][j][2];
                Point3D dM = this.platot[i][j][3];
                if (aM != null && bM != null && cM != null && dM != null && MyMath.YasharAndRectangle(a, b, aM, bM, cM, dM)) {
                    return true;
                }
                ++j;
            }
            ++i;
        }
        return false;
    }

    private double getCloseZ() {
        if (this instanceof Zombie) {
            return ((Zombie)this).getCloseZ();
        }
        if (this instanceof Column) {
            return ((Column)this).getCloseZ();
        }
        if (this instanceof Gader) {
            return ((Gader)this).getCloseZ();
        }
        if (this instanceof BodyPart) {
            return ((BodyPart)this).getCloseZ();
        }
        if (this instanceof Bullet) {
            return ((Bullet)this).getCloseZ();
        }
        return 0.0;
    }

    public void convertAndShow(Graphics page) {
        if (this instanceof Zombie) {
            ((Zombie)this).convertAndShow(page, true);
        } else if (this instanceof Column) {
            ((Column)this).convertAndShow(page);
        } else if (this instanceof Gader) {
            ((Gader)this).convertAndShow(page);
        } else if (this instanceof Bullet) {
            ((Bullet)this).convertAndShow(page);
        } else if (this instanceof BodyPart) {
            ((Column)this).convertAndShow(page);
        }
    }
}
