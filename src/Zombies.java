import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Zombies {
    Random rand = new Random();
    boolean isForring = false;
    MainPanel mP;
    ArrayList<Zombie> zombies;
    ArrayList<Zombie> deadZombies;
    ArrayList<Zombie> waitList;
    Point3D magozPoint;
    Map map;
    Point3D pStart = MainPanel.pStart;
    int numOfZomb;
    final int maxZomb = 35;
    Point[][] points;
    double smallScale = 0.85;
    double bigScale = 1.5;

    public Zombies(MainPanel mP) {
        this.mP = mP;
        this.map = mP.map;
        this.magozPoint = mP.magozPoint;
        this.zombies = new ArrayList();
        this.deadZombies = new ArrayList();
        this.waitList = new ArrayList();
        this.setSpawnPoints();
    }

    public void restart() {
        this.isForring = true;
        for (Zombie z : this.zombies) {
            this.map.clearObject(z.place.x, z.place.y);
        }
        this.isForring = false;
        this.waitList.addAll(this.zombies);
        this.waitList.addAll(this.deadZombies);
        this.zombies.clear();
        this.deadZombies.clear();
    }

    public boolean addZombie(int spawn) {
        if (!this.map.isEmpty(this.points[spawn][0].x, this.points[spawn][0].y, 0) || this.zombies.size() > 35) {
            return false;
        }
        Zombie temp = !this.waitList.isEmpty() ? this.waitList.remove(0) : new Zombie(this.magozPoint);
        boolean size = this.rand.nextBoolean();
        if (size) {
            temp.resetZombie((double)this.mP.zombSizeX * this.smallScale, (double)this.mP.zombSizeY * this.smallScale, (double)this.mP.zombSizeZ * this.smallScale, this.points[spawn][0], this.numOfZomb + 2, spawn);
        } else {
            temp.resetZombie(this.mP.zombSizeX, this.mP.zombSizeY, this.mP.zombSizeZ, this.points[spawn][0], this.numOfZomb + 2, spawn);
        }
        this.map.setObject(temp.id, temp.place);
        this.zombies.add(temp);
        ++this.numOfZomb;
        return true;
    }

    public boolean addBigZombie() {
        if (!this.map.isEmpty(this.points[8][0].x, this.points[8][0].y, 0) || this.zombies.size() > 40) {
            return false;
        }
        Zombie temp = !this.waitList.isEmpty() ? this.waitList.remove(0) : new Zombie(this.magozPoint);
        temp.resetZombie((double)this.mP.zombSizeX * this.bigScale * 1.1, (double)this.mP.zombSizeY * this.bigScale, (double)this.mP.zombSizeZ * this.bigScale * 1.3, this.points[8][0], this.numOfZomb + 2, 8);
        temp.makeBig();
        this.map.setObject(temp.id, temp.place);
        this.zombies.add(temp);
        ++this.numOfZomb;
        return true;
    }

    public void buildShape() {
        this.isForring = true;
        for (Zombie z : this.zombies) {
            z.buildShape(this.pStart.x + (double)z.place.x, this.pStart.z + (double)z.place.y);
        }
        for (Zombie z : this.deadZombies) {
            z.buildShape(this.pStart.x + (double)z.place.x, this.pStart.z + (double)z.place.y);
        }
        this.isForring = false;
        this.setDestinations();
    }

    public void fromPointToGuf() {
        this.isForring = true;
        for (Zombie z : this.zombies) {
            z.fromPointToGuf();
        }
        for (Zombie z : this.deadZombies) {
            z.fromPointToGuf();
        }
        this.isForring = false;
    }

    public void mullMat(Matrix3D mat) {
        this.isForring = true;
        for (Zombie z : this.zombies) {
            z.mullMat(mat);
        }
        for (Zombie z : this.deadZombies) {
            z.mullMat(mat);
        }
        this.isForring = false;
    }

    public Zombie hitZomb() {
        Collections.sort(this.zombies);
        for (Zombie z : this.zombies) {
            if (!(Math.sqrt(Math.pow(z.place.x - this.mP.myPoint.x, 2.0) + Math.pow(z.place.y - this.mP.myPoint.y, 2.0)) < 1000.0) || !(z.xyz.z > -MainPanel.p00.z)) continue;
            return z;
        }
        return null;
    }

    public ArrayList<Zombie> getAllZombies() {
        ArrayList<Zombie> temp = new ArrayList<Zombie>();
        temp.addAll(this.zombies);
        temp.addAll(this.deadZombies);
        return temp;
    }

    public ArrayList<Zombie> getZombies() {
        return this.zombies;
    }

    public ArrayList<Zombie> getDeadZombies() {
        return this.deadZombies;
    }

    public void killZomb(Zombie zomb) {
        this.map.clearObject(zomb.place.x, zomb.place.y);
        this.zombies.remove(zomb);
        this.deadZombies.add(zomb);
        zomb.isAlive = false;
        zomb.setFallMats();
        if (this.deadZombies.size() > 15) {
            Zombie temp = this.deadZombies.remove(0);
            this.waitList.add(temp);
        }
    }

    public int getNumOfZomb() {
        return this.zombies.size();
    }

    public Zombie getZomb(int index) {
        return this.zombies.get(index);
    }

    public void setDestinations() {
        this.isForring = true;
        for (Zombie z : this.zombies) {
            if (Math.sqrt(Math.pow(z.place.x - z.destination.x, 2.0) + Math.pow(z.place.y - z.destination.y, 2.0)) < 50.0 && z.destinationIndex < 3) {
                ++z.destinationIndex;
            }
            z.destination = z.destinationIndex < 3 ? this.points[z.spawn][z.destinationIndex] : this.mP.myPoint;
        }
        this.isForring = false;
    }

    private void setSpawnPoints() {
        this.mP.getClass();
        int xSize = 20000;
        this.mP.getClass();
        int zSize = 20000;
        this.points = new Point[9][3];
        int a = 600;
        int b = 2500;
        int c = 3000;
        this.points[0][0] = new Point(b, zSize - a);
        this.points[0][1] = new Point(xSize / 2 - a, zSize - a);
        this.points[0][2] = new Point(xSize / 2 - a, zSize - c);
        this.points[1][0] = new Point(xSize - b, zSize - a);
        this.points[1][1] = new Point(xSize / 2 + a, zSize - a);
        this.points[1][2] = new Point(xSize / 2 + a, zSize - c);
        this.points[2][0] = new Point(xSize - a, zSize - b);
        this.points[2][1] = new Point(xSize - a, zSize / 2 + a);
        this.points[2][2] = new Point(xSize - c, zSize / 2 + a);
        this.points[3][0] = new Point(xSize - a, b);
        this.points[3][1] = new Point(xSize - a, zSize / 2 - a);
        this.points[3][2] = new Point(xSize - c, zSize / 2 - a);
        this.points[4][0] = new Point(b, a);
        this.points[4][1] = new Point(xSize / 2 - a, a);
        this.points[4][2] = new Point(xSize / 2 - a, c);
        this.points[5][0] = new Point(xSize - b, a);
        this.points[5][1] = new Point(xSize / 2 + a, a);
        this.points[5][2] = new Point(xSize / 2 + a, c);
        this.points[6][0] = new Point(a, zSize - b);
        this.points[6][1] = new Point(a, zSize / 2 + a);
        this.points[6][2] = new Point(c, zSize / 2 + a);
        this.points[7][0] = new Point(a, b);
        this.points[7][1] = new Point(a, zSize / 2 - a);
        this.points[7][2] = new Point(c, zSize / 2 - a);
        this.points[8][0] = new Point(xSize - b, a);
        this.points[8][1] = new Point(xSize / 2, a);
        this.points[8][2] = new Point(xSize / 2, c);
    }
}
