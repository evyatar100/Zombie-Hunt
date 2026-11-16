import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Arena
extends Shape {
    Floor floor;
    ArrayList<DoorWall> sides;
    ArrayList<Floor> outWalls;
    Point3D magozPoint;
    Color wallColor = new Color(230, 230, 230);
    Map map;
    Point3D pStart = MainPanel.pStart;

    public Arena(Point3D magozPoint, Map map) {
        this.magozPoint = magozPoint;
        this.floor = new Floor(magozPoint, 50, 50);
        this.sides = new ArrayList();
        this.outWalls = new ArrayList();
        int i = 0;
        while (i < 4) {
            this.sides.add(new DoorWall(magozPoint, map));
            this.outWalls.add(new Floor(magozPoint, 45, 8));
            ++i;
        }
        this.map = map;
    }

    public void restart() {
        for (DoorWall side : this.sides) {
            side.door.openState = side.door.numOfStates - 1;
            side.door.isOpen = false;
        }
    }

    public void buildShape(double dx, double dy, double dz) {
        double hall = 2000.0;
        this.floor.buildShape(this.pStart.x, this.pStart.y + 1000.0, this.pStart.z, dx, dz, 0, new Color(0, 50, 0));
        this.sides.get(0).buildShape(this.pStart.x + hall, this.pStart.y - dy, this.pStart.z + dz - hall, dx - hall * 2.0, dy + 1000.0, 1.0, 0);
        this.sides.get(1).buildShape(this.pStart.x + dx - hall, this.pStart.y - dy, this.pStart.z + dz - hall, dz - hall * 2.0, dy + 1000.0, 1.0, 1);
        this.sides.get(2).buildShape(this.pStart.x + dx - hall, this.pStart.y - dy, this.pStart.z + hall, dx - hall * 2.0, dy + 1000.0, 1.0, 2);
        this.sides.get(3).buildShape(this.pStart.x + hall, this.pStart.y - dy, this.pStart.z + hall, dz - hall * 2.0, dy + 1000.0, 1.0, 3);
        this.outWalls.get(0).buildShape(this.pStart.x, this.pStart.y - (dy -= 250.0), this.pStart.z + dz, dx, dy + 1000.0, 1, this.wallColor);
        this.outWalls.get(1).buildShape(this.pStart.x + dx, this.pStart.y - dy, this.pStart.z + dz, dz, dy + 1000.0, 2, this.wallColor);
        this.outWalls.get(2).buildShape(this.pStart.x + dx, this.pStart.y - dy, this.pStart.z, dx, dy + 1000.0, 3, this.wallColor);
        this.outWalls.get(3).buildShape(this.pStart.x, this.pStart.y - dy, this.pStart.z, dz, dy + 1000.0, 4, this.wallColor);
        int i = 0;
        while (i < this.map.xNum) {
            this.map.setObjectIndex(-1, i, 0);
            this.map.setObjectIndex(-1, i, this.map.yNum - 1);
            ++i;
        }
        i = 0;
        while (i < this.map.yNum) {
            this.map.setObjectIndex(-1, 0, i);
            this.map.setObjectIndex(-1, this.map.xNum - 1, i);
            ++i;
        }
    }

    public void fromPointToGuf() {
        for (DoorWall wd : this.sides) {
            wd.fromPointToGuf();
        }
    }

    public void mullMat(Matrix3D mat) {
        this.floor.mullMat(mat);
        for (DoorWall wd : this.sides) {
            wd.mullMat(mat);
        }
        for (Floor w : this.outWalls) {
            w.mullMat(mat);
        }
    }

    @Override
    public void convertAndShow(Graphics page) {
        this.floor.convertAndShow(page);
        for (Floor w : this.outWalls) {
            w.convertAndShow(page);
        }
    }

    public ArrayList<Shape> getList() {
        ArrayList<Shape> temp = new ArrayList<Shape>();
        for (DoorWall wd : this.sides) {
            temp.addAll(wd.getList());
        }
        return temp;
    }

    public void openDoor() {
        for (DoorWall wd : this.sides) {
            wd.doorsState();
        }
    }
}
