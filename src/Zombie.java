import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Zombie
extends Shape {
    ArrayList<BodyPart> parts;
    Random rand = new Random();
    Color color;
    Color feetColor;
    Color bigFeetColor = new Color(125, 0, 0);
    double dx;
    double dy;
    double dz;
    int red;
    int green;
    int blue;
    Point place = new Point();
    int spawn;
    int destinationIndex;
    Point destination;
    boolean legUpOrDown;
    int numOfLegStates = 10;
    int legsState;
    Matrix3D[] matiRightLegFront;
    Matrix3D[] matiLeftLegFront;
    Matrix3D[] matiRightLegBack;
    Matrix3D[] matiLeftLegBack;
    Matrix3D mati;
    boolean handUpOrDown;
    final int numOfHandStates = 10;
    int handState;
    Matrix3D[] matiRightHand;
    Matrix3D[] matiLeftHand;
    final int numOfFallStates = 11;
    int fallState;
    Matrix3D[] matiFall;
    Point3D xyz;
    Point3D center;
    Point3D bottomXYZ;
    double look;
    Point3D magozPoint;
    int life = 100;
    int id;
    boolean isAlive;
    boolean isBig;

    public Zombie(Point3D magozPoint) {
        this.parts = new ArrayList();
        this.parts.add(new Head(magozPoint));
        this.parts.add(new Body(magozPoint));
        this.parts.add(new Hand(magozPoint, true));
        this.parts.add(new Hand(magozPoint, false));
        this.parts.add(new Leg(magozPoint, true));
        this.parts.add(new Leg(magozPoint, false));
        this.xyz = new Point3D();
        this.center = new Point3D();
        this.bottomXYZ = new Point3D();
        this.legsState = this.numOfLegStates;
        this.legUpOrDown = true;
        this.matiRightLegFront = new Matrix3D[this.numOfLegStates * 2 + 1];
        this.matiLeftLegFront = new Matrix3D[this.numOfLegStates * 2 + 1];
        this.matiRightLegBack = new Matrix3D[this.numOfLegStates * 2 + 1];
        this.matiLeftLegBack = new Matrix3D[this.numOfLegStates * 2 + 1];
        this.mati = new Matrix3D();
        this.handUpOrDown = true;
        this.matiRightHand = new Matrix3D[21];
        this.matiLeftHand = new Matrix3D[21];
        this.matiFall = new Matrix3D[11];
        this.look = 0.0;
        this.isAlive = true;
        this.destination = new Point();
    }

    public void resetZombie(double XSize, double YSize, double ZSize, Point place, int ID, int spawn) {
        this.isAlive = true;
        this.life = 100;
        this.dx = XSize;
        this.dy = YSize;
        this.dz = ZSize;
        this.red = 170;
        this.green = 188;
        this.blue = 70;
        this.feetColor = Color.red;
        this.setPlace(place);
        this.id = ID;
        this.spawn = spawn;
        this.destinationIndex = 1;
        this.handState = 10;
        this.fallState = 0;
        this.isBig = false;
    }

    public void makeBig() {
        this.isBig = true;
        this.feetColor = this.bigFeetColor;
    }

    public void buildShape(double x, double z) {
        double y = 1000.0 - 2.0 * this.dy;
        this.color = new Color(this.red, this.green, this.blue);
        for (BodyPart b : this.parts) {
            if (b.name.compareTo("head") == 0) {
                ((Head)b).buildShape(x + this.dx / 4.0, y - this.dy / 3.0, z + this.dz / 4.0, this.dx / 2.0, this.dy / 3.0, this.dz / 3.0 * 2.0, this.color);
                continue;
            }
            if (b.name.compareTo("body") == 0) {
                ((Body)b).buildShape(x, y, z, this.dx, this.dy, this.dz, this.color);
                continue;
            }
            if (b.name.compareTo("rHand") == 0) {
                ((Hand)b).buildShape(x - this.dx / 3.0, y, z, this.dx / 3.0, this.dy + this.dy / 6.0, this.dz, this.color, this.feetColor);
                continue;
            }
            if (b.name.compareTo("lHand") == 0) {
                ((Hand)b).buildShape(x + this.dx, y, z, this.dx / 3.0, this.dy + this.dy / 6.0, this.dz, this.color, this.feetColor);
                continue;
            }
            if (b.name.compareTo("rLeg") == 0) {
                ((Leg)b).buildShape(x, y + this.dy, z, this.dx / 2.0, this.dy, this.dz, this.color, this.feetColor);
                continue;
            }
            if (b.name.compareTo("lLeg") != 0) continue;
            ((Leg)this.parts.get(5)).buildShape(x + this.dx / 2.0, y + this.dy, z, this.dx / 2.0, this.dy, this.dz, this.color, this.feetColor);
        }
        this.xyz.setXYZ(x, y, z);
        this.center.setXYZ(x + this.dx / 2.0, y + this.dy / 2.0, z + this.dz / 2.0);
        this.mati.setMatRotateXFix(-1.5707963267948966, this.parts.get((int)2).axis.x, this.parts.get((int)2).axis.y, this.parts.get((int)2).axis.z);
        this.parts.get(2).mullMat(this.mati);
        this.mati.setMatRotateXFix(-1.5707963267948966, this.parts.get((int)3).axis.x, this.parts.get((int)3).axis.y, this.parts.get((int)3).axis.z);
        this.parts.get(3).mullMat(this.mati);
        double HandAngel = 0.031415926535897934;
        int i = 0;
        while (i < this.matiLeftHand.length - 1) {
            this.matiRightHand[i] = new Matrix3D();
            this.matiLeftHand[i] = new Matrix3D();
            this.matiRightHand[i].setMatRotateXFix(HandAngel * (double)(-(i - this.numOfLegStates)), this.parts.get((int)2).axis.x, this.parts.get((int)2).axis.y, this.parts.get((int)2).axis.z);
            this.matiLeftHand[i].setMatRotateXFix(HandAngel * (double)(i - this.numOfLegStates), this.parts.get((int)3).axis.x, this.parts.get((int)3).axis.y, this.parts.get((int)3).axis.z);
            ++i;
        }
        double legAngel = 0.031415926535897934;
        int i2 = 0;
        while (i2 < this.matiLeftLegFront.length - 1) {
            this.matiLeftLegFront[i2] = new Matrix3D();
            this.matiRightLegFront[i2] = new Matrix3D();
            this.matiLeftLegBack[i2] = new Matrix3D();
            this.matiRightLegBack[i2] = new Matrix3D();
            this.matiRightLegFront[i2].setMatRotateXFix(legAngel * (double)(-(i2 - this.numOfLegStates)), this.parts.get((int)4).xyzFront.x, this.parts.get((int)4).xyzFront.y, this.parts.get((int)4).xyzFront.z);
            this.matiLeftLegFront[i2].setMatRotateXFix(legAngel * (double)(i2 - this.numOfLegStates), this.parts.get((int)5).xyzFront.x, this.parts.get((int)5).xyzFront.y, this.parts.get((int)5).xyzFront.z);
            this.matiRightLegBack[i2].setMatRotateXFix(legAngel * (double)(-(i2 - this.numOfLegStates)), this.parts.get((int)4).xyzBack.x, this.parts.get((int)4).xyzBack.y, this.parts.get((int)4).xyzBack.z);
            this.matiLeftLegBack[i2].setMatRotateXFix(legAngel * (double)(i2 - this.numOfLegStates), this.parts.get((int)5).xyzBack.x, this.parts.get((int)5).xyzBack.y, this.parts.get((int)5).xyzBack.z);
            ++i2;
        }
        this.bottomXYZ.setXYZ(this.parts.get((int)4).parts.get((int)1).xReal[5], this.parts.get((int)4).parts.get((int)1).yReal[5], this.parts.get((int)4).parts.get((int)1).zReal[5]);
    }

    public void fromPointToGuf() {
        for (BodyPart b : this.parts) {
            b.fromPointToGuf();
        }
    }

    public void mullMat(Matrix3D mat) {
        for (BodyPart b : this.parts) {
            b.mullMat(mat);
        }
        this.xyz.mullMat(mat);
        this.center.mullMat(mat);
    }

    public void convertAndShow(Graphics page, boolean isLines) {
        ArrayList<BodyPart> temp = new ArrayList<BodyPart>();
        temp.addAll(this.parts);
        Collections.sort(temp);
        Collections.reverse(temp);
        for (BodyPart b : temp) {
            b.convertAndShow(page, isLines);
        }
    }

    public void setPlace(int xPlace, int zPlace) {
        this.place.x = xPlace;
        this.place.y = zPlace;
    }

    public void setPlace(Point point) {
        this.place.x = point.x;
        this.place.y = point.y;
    }

    public void marchZomb() {
        this.parts.get(4).mullMat(this.matiRightLegFront[this.legsState], this.matiRightLegBack[this.legsState]);
        this.parts.get(5).mullMat(this.matiLeftLegFront[this.legsState], this.matiLeftLegBack[this.legsState]);
    }

    public void moveHands() {
        this.parts.get(2).mullMat(this.matiRightHand[this.handState]);
        this.parts.get(3).mullMat(this.matiLeftHand[this.handState]);
    }

    public void turnZomb() {
        this.mati.setMatRotateYFix(this.look, this.center.x, this.center.y, this.center.z);
        this.mullMat(this.mati);
    }

    public void fallBack() {
        this.mullMat(this.matiFall[this.fallState]);
        if (this.fallState < 10) {
            ++this.fallState;
        }
        if (this.handState > 10) {
            --this.handState;
        }
        if (this.handState < 10) {
            ++this.handState;
        }
        if (this.handState == 10) {
            this.mati.setMatRotateXFix(1.5707963267948966, this.parts.get((int)2).axis.x, this.parts.get((int)2).axis.y, this.parts.get((int)2).axis.z);
            this.parts.get(2).mullMat(this.mati);
            this.mati.setMatRotateXFix(1.5707963267948966, this.parts.get((int)3).axis.x, this.parts.get((int)3).axis.y, this.parts.get((int)3).axis.z);
            this.parts.get(3).mullMat(this.mati);
        }
        if (this.legsState > this.numOfLegStates) {
            --this.legsState;
        }
        if (this.legsState < this.numOfLegStates) {
            ++this.legsState;
        }
    }

    public void setFallMats() {
        double fallAngel = -0.15707963267948966;
        int i = 0;
        while (i < 11) {
            this.matiFall[i] = new Matrix3D();
            this.matiFall[i].setMatRotateXFix(fallAngel * (double)i, this.bottomXYZ.x, this.bottomXYZ.y, this.bottomXYZ.z);
            ++i;
        }
    }

    public boolean hit(int hit) {
        if (this.isBig) {
            hit /= 5;
        }
        this.life -= hit;
        this.red += hit / 2;
        if (this.red > 255) {
            this.red = 255;
        }
        this.green -= hit;
        if (this.green < 0) {
            this.green = 0;
        }
        this.blue -= hit / 2;
        if (this.blue < 0) {
            this.blue = 0;
        }
        return this.life <= 0;
    }

    public double getCloseZ() {
        double min = Double.MAX_VALUE;
        for (BodyPart p : this.parts) {
            if (!(p.getCloseZ() < min)) continue;
            min = p.getCloseZ();
        }
        return min;
    }
}
