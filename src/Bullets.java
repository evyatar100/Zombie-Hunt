import java.awt.Point;
import java.util.ArrayList;

public class Bullets {
    ArrayList<Bullet> bullets;
    Point3D magozPoint;
    Map map;
    int numOfBullets;
    Point3D pStart = MainPanel.pStart;
    boolean isForing = false;

    public Bullets(Point3D magozPoint, Map map) {
        this.magozPoint = magozPoint;
        this.map = map;
        this.bullets = new ArrayList();
    }

    public void restart() {
        this.bullets.clear();
    }

    public void addBullet(Point place, int ammo) {
        Bullet temp = new Bullet(this.magozPoint);
        temp.setBullet(place, ammo);
        this.bullets.add(temp);
        ++this.numOfBullets;
    }

    public void removeBullet(Bullet b) {
        this.bullets.remove(b);
    }

    public void buildShape() {
        for (Bullet b : this.bullets) {
            b.buildShape(this.pStart.x + (double)b.place.x, this.pStart.y + 500.0, this.pStart.z + (double)b.place.y);
        }
    }

    public void fromPointToGuf() {
        if (this.isForing) {
            return;
        }
        this.isForing = true;
        for (Bullet b : this.bullets) {
            b.fromPointToGuf();
        }
        this.isForing = false;
    }

    public void mullMat(Matrix3D mat) {
        if (this.isForing) {
            return;
        }
        this.isForing = true;
        for (Bullet b : this.bullets) {
            b.mullMat(mat);
        }
        this.isForing = false;
    }

    public ArrayList<Bullet> getBullets() {
        return this.bullets;
    }

    public void spin() {
        while (this.isForing) {
        }
        this.isForing = true;
        for (Bullet b : this.bullets) {
            b.spin();
        }
        this.isForing = false;
    }
}
