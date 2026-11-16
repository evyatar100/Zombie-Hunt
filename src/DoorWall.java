import java.awt.Color;
import java.util.ArrayList;

public class DoorWall {
    ArrayList<Gader> leftWall;
    ArrayList<Gader> rightWall;
    Door door;
    Color color = new Color(230, 230, 230, 100);
    Point3D magozPoint;
    Map map;
    boolean isSetOnMap;
    int numX;

    public DoorWall(Point3D magozPoint, Map map) {
        this.magozPoint = magozPoint;
        this.leftWall = new ArrayList();
        this.rightWall = new ArrayList();
        this.numX = 30;
        this.door = new Door(magozPoint, map);
        int i = 0;
        while (i < this.numX) {
            this.leftWall.add(new Gader(magozPoint));
            this.rightWall.add(new Gader(magozPoint));
            ++i;
        }
        this.map = map;
        this.isSetOnMap = false;
    }

    public void buildShape(double x, double y, double z, double dx, double dy, double dz, int state) {
        double wallSize = (dx - 2000.0) / 2.0;
        double xSize = wallSize / (double)this.numX;
        this.door.buildShape(x + wallSize, y - 2500.0 + dy, z);
        int i = 0;
        while (i < this.numX) {
            this.leftWall.get(i).buildShape(x + (double)i * xSize, y, z, xSize - 20.0, dy, dz, 20.0);
            this.rightWall.get(i).buildShape(x + wallSize + 2000.0 + (double)i * xSize, y, z, xSize - 20.0, dy, dz, 20.0);
            ++i;
        }
        Matrix3D temp = new Matrix3D();
        temp.setMatRotateYFix(-1.5707963267948966 * (double)state, x, y, z);
        this.mullMat(temp);
        if (!this.isSetOnMap) {
            switch (state) {
                case 0: {
                    this.map.setBigObject(x, z, wallSize, dz);
                    this.map.setBigObject(x + wallSize + 2000.0, z, wallSize, dz);
                    this.map.setDoor(x + wallSize, z, 0, this.door.isOpen);
                    break;
                }
                case 1: {
                    this.map.setBigObject(x, z - wallSize, dz, wallSize);
                    this.map.setBigObject(x, z - dx, dz, wallSize);
                    this.map.setDoor(x, z - wallSize, 1, this.door.isOpen);
                    break;
                }
                case 2: {
                    this.map.setBigObject(x - wallSize, z - dz, wallSize, dz);
                    this.map.setBigObject(x - dx, z - dz, wallSize, dz);
                    this.map.setDoor(x - wallSize, z - 100.0, 2, this.door.isOpen);
                    break;
                }
                case 3: {
                    this.map.setBigObject(x - dz, z, dz, wallSize);
                    this.map.setBigObject(x - dz, z + wallSize + 2000.0, dz, wallSize);
                    this.map.setDoor(x - 100.0, z + wallSize, 3, this.door.isOpen);
                    break;
                }
            }
            this.isSetOnMap = true;
        }
    }

    public void fromPointToGuf() {
        int i = 0;
        while (i < this.numX) {
            this.leftWall.get(i).fromPointToGuf();
            this.rightWall.get(i).fromPointToGuf();
            ++i;
        }
        this.door.fromPointToGuf();
    }

    public void mullMat(Matrix3D mat) {
        int i = 0;
        while (i < this.numX) {
            this.leftWall.get(i).mullMat(mat);
            this.rightWall.get(i).mullMat(mat);
            ++i;
        }
        this.door.mullMat(mat);
    }

    public void doorsState() {
        this.door.doorsState();
    }

    public ArrayList<Shape> getList() {
        ArrayList<Shape> temp = new ArrayList<Shape>();
        temp.addAll(this.leftWall);
        temp.addAll(this.rightWall);
        temp.addAll(this.door.getColumns());
        return temp;
    }

    public void openDoors() {
        this.door.openDoors();
        this.isSetOnMap = false;
    }
}
