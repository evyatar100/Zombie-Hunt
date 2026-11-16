import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

public class Bullet
extends Shape {
    ArrayList<Galil> first;
    ArrayList<Galil> second;
    ArrayList<Galil> temp;
    Color color = new Color(192, 170, 40);
    Color topColor = new Color(161, 126, 32);
    Point3D xyz;
    Point place;
    int numOfParts = 4;
    int numOfPoint = 20;
    Point3D magozPoint;
    Matrix3D matiTemp = new Matrix3D();
    Matrix3D matiRotate;
    int ammo;
    int largeR = 30;
    int mediumR = 20;
    int smallR = 2;
    int firstS = 250;
    int secondS = 30;
    int thirdS = 100;
    int spinTimes = 0;

    public Bullet(Point3D magozPoint) {
        this.first = new ArrayList();
        this.second = new ArrayList();
        this.temp = new ArrayList();
        int i = 0;
        while (i < this.numOfParts) {
            this.first.add(new Galil(magozPoint, this.numOfPoint));
            this.second.add(new Galil(magozPoint, this.numOfPoint));
            ++i;
        }
        this.xyz = new Point3D();
        this.matiRotate = new Matrix3D();
    }

    public void setBullet(Point place, int ammo) {
        this.place = place;
        this.ammo = ammo;
    }

    public void buildShape(double x, double y, double z) {
        this.first.get(0).buildGalil(x, y, z, this.largeR, Math.PI * 2, this.firstS, this.color);
        this.first.get(1).buildHarut(x, y, z + 250.0, this.largeR, this.mediumR, Math.PI * 2, this.secondS, this.color);
        this.first.get(2).buildGalil(x, y, z + 280.0, this.mediumR, Math.PI * 2, this.secondS, this.color);
        this.first.get(3).buildHarut(x, y, z + 310.0, this.mediumR, this.smallR, Math.PI * 2, this.thirdS, this.topColor);
        this.second.get(0).buildGalil(x, y, z, this.largeR, Math.PI * 2, this.firstS, this.color);
        this.second.get(1).buildHarut(x, y, z + 250.0, this.largeR, this.mediumR, Math.PI * 2, this.secondS, this.color);
        this.second.get(2).buildGalil(x, y, z + 280.0, this.mediumR, Math.PI * 2, this.secondS, this.color);
        this.second.get(3).buildHarut(x, y, z + 310.0, this.mediumR, this.smallR, Math.PI * 2, this.thirdS, this.topColor);
        this.xyz.setXYZ(x, y, z + (double)((this.firstS + this.secondS + this.secondS + this.thirdS) / 2));
        this.matiTemp.setMatRotateXFix(1.5707963267948966, this.xyz.x, this.xyz.y, this.xyz.z);
        this.mullMat(this.matiTemp);
        this.matiTemp.setMatRotateZFix(0.7853981633974483, this.xyz.x, this.xyz.y, this.xyz.z);
        this.mullMatOne(this.matiTemp, true);
        this.matiTemp.setMatRotateZFix(-0.7853981633974483, this.xyz.x, this.xyz.y, this.xyz.z);
        this.mullMatOne(this.matiTemp, false);
        this.matiTemp.setMatMove(0.0, 0.0, this.largeR * 2 + 5);
        this.mullMatOne(this.matiTemp, false);
        this.matiTemp.setMatMove(0.0, 0.0, (this.largeR * 2 + 5) / 2);
        this.xyz.mullMat(this.matiTemp);
        this.matiRotate.setMatRotateYFix(0.07853981633974483 * (double)this.spinTimes, this.xyz.x, this.xyz.y, this.xyz.z);
        this.mullMat(this.matiRotate);
    }

    public void fromPointToGuf() {
        int i = 0;
        while (i < this.numOfParts) {
            this.first.get(i).fromPointToGuf();
            this.second.get(i).fromPointToGuf();
            ++i;
        }
    }

    public void mullMat(Matrix3D mat) {
        int i = 0;
        while (i < this.numOfParts) {
            this.first.get(i).mullMat(mat);
            this.second.get(i).mullMat(mat);
            ++i;
        }
        this.xyz.mullMat(mat);
    }

    public void mullMats(Matrix3D mabatMove, Matrix3D mabatTurnY, Matrix3D mabatTurnX) {
        this.mullMat(mabatMove);
        this.mullMat(mabatTurnY);
        this.mullMat(mabatTurnX);
    }

    public void mullMatOne(Matrix3D mat, boolean firstOrSecond) {
        int i = 0;
        while (i < this.numOfParts) {
            if (firstOrSecond) {
                this.first.get(i).mullMat(mat);
            } else {
                this.second.get(i).mullMat(mat);
            }
            ++i;
        }
    }

    @Override
    public void convertAndShow(Graphics page) {
        this.temp.clear();
        this.temp.addAll(this.first);
        this.temp.addAll(this.second);
        Collections.sort(this.temp);
        Collections.reverse(this.temp);
        for (Galil g : this.temp) {
            g.convertAndShow(page, true);
        }
    }

    public double getCloseZ() {
        this.temp.clear();
        this.temp.addAll(this.first);
        this.temp.addAll(this.second);
        double min = Double.MAX_VALUE;
        for (Galil g : this.temp) {
            if (!(g.getCloseZ() < min)) continue;
            min = g.getCloseZ();
        }
        return min;
    }

    public void spin() {
        ++this.spinTimes;
        if (this.spinTimes == 80) {
            this.spinTimes = 0;
        }
    }
}
