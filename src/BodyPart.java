import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class BodyPart
extends Shape {
    Random generator = new Random();
    ArrayList<Box> parts;
    Color color;
    Point3D xyz;
    Point3D xyzFront;
    Point3D xyzBack;
    Point3D axis;
    Point3D magozPoint;
    String name;
    boolean isBig = false;

    public BodyPart(Point3D magozPoint) {
        this.magozPoint = magozPoint;
        this.parts = new ArrayList();
        this.xyz = new Point3D();
        this.xyzFront = new Point3D();
        this.xyzBack = new Point3D();
        this.axis = new Point3D();
    }

    public void fromPointToGuf() {
        for (Box m : this.parts) {
            m.fromPointToGuf();
        }
    }

    public void mullMat(Matrix3D mat) {
        for (Box m : this.parts) {
            m.mullMat(mat);
        }
        this.xyz.mullMat(mat);
        this.xyzFront.mullMat(mat);
        this.xyzBack.mullMat(mat);
        this.axis.mullMat(mat);
    }

    public void mullMat(Matrix3D matFront, Matrix3D matBack) {
        for (Box m : this.parts) {
            m.mullMat(matFront, matBack);
        }
    }

    public void convertAndShow(Graphics page, boolean isLines) {
        Collections.sort(this.parts);
        Collections.reverse(this.parts);
        for (Box m : this.parts) {
            m.convertAndShow(page, isLines);
        }
    }

    public double getCloseZ() {
        double min = Double.MAX_VALUE;
        for (Box p : this.parts) {
            if (!(p.getCloseZ() < min)) continue;
            min = p.getCloseZ();
        }
        return min;
    }

    public void setSuperPlatot() {
        this.platot = this.platot;
    }
}
