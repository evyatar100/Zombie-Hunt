import java.awt.Color;
import java.util.ArrayList;

public class Door {
    ArrayList<Column> right;
    ArrayList<Column> left;
    Column leftBar;
    Column rightBar;
    Column upBar;
    Point3D magozPoint;
    Map map;
    int numOfStates = 250;
    Point3D rightAxis;
    Point3D leftAxis;
    int openState;
    Matrix3D[] matiRight;
    Matrix3D[] matiLeft;
    int numX = 10;
    int numY = 10;
    static final double dx = 2000.0;
    static final double dy = 2500.0;
    static final double dz = 100.0;
    boolean isOpen;

    public Door(Point3D magozPoint, Map map) {
        this.magozPoint = magozPoint;
        this.right = new ArrayList();
        this.left = new ArrayList();
        int i = 0;
        while (i < this.numX) {
            this.right.add(new Column(magozPoint));
            this.left.add(new Column(magozPoint));
            ++i;
        }
        this.leftBar = new Column(magozPoint);
        this.rightBar = new Column(magozPoint);
        this.upBar = new Column(magozPoint);
        this.map = map;
        this.matiRight = new Matrix3D[this.numOfStates];
        this.matiLeft = new Matrix3D[this.numOfStates];
        this.rightAxis = new Point3D();
        this.leftAxis = new Point3D();
        this.openState = this.numOfStates - 1;
        this.isOpen = false;
    }

    public void buildShape(double x, double y, double z) {
        Color color = new Color(80, 40, 0);
        Color barColor = new Color(40, 20, 0);
        double xSize = 2000.0 / (double)this.numX;
        double ySize = 2500.0;
        double zSize = 100.0;
        int i = 0;
        while (i < this.numX) {
            this.right.get(i).buildShape(x + (double)i * xSize / 2.0, y, z, xSize / 2.0, ySize, zSize, color, false);
            this.left.get(i).buildShape(x + 1000.0 + (double)i * xSize / 2.0, y, z, xSize / 2.0, ySize, zSize, color, false);
            ++i;
        }
        double barLength = 100.0;
        this.rightBar.buildShape(x - barLength, y, z, barLength, ySize, zSize, barColor, true);
        this.leftBar.buildShape(x + 2000.0, y, z, barLength, ySize, zSize, barColor, true);
        this.upBar.buildShape(x - barLength, y - barLength, z, 2000.0 + barLength * 2.0, barLength, zSize, barColor, true);
        this.rightAxis.setXYZ(this.right.get((int)0).place);
        this.leftAxis.setXYZ(this.left.get((int)(this.numX - 1)).xyz1);
        double openAngel = 0.010471975511965976;
        int i2 = 0;
        while (i2 < this.numOfStates) {
            this.matiRight[i2] = new Matrix3D();
            this.matiLeft[i2] = new Matrix3D();
            this.matiRight[i2].setMatRotateYFix(openAngel * (double)(i2 - this.numOfStates + 1), this.rightAxis.x, this.rightAxis.y, this.rightAxis.z);
            this.matiLeft[i2].setMatRotateYFix(openAngel * (double)(-(i2 - this.numOfStates + 1)), this.leftAxis.x, this.leftAxis.y, this.leftAxis.z);
            ++i2;
        }
        for (Column r : this.right) {
            r.mullMat(this.matiRight[this.openState]);
        }
        for (Column l : this.left) {
            l.mullMat(this.matiLeft[this.openState]);
        }
    }

    public void fromPointToGuf() {
        for (Column column : this.right) {
            column.fromPointToGuf();
        }
        for (Column column : this.left) {
            column.fromPointToGuf();
        }
        this.rightBar.fromPointToGuf();
        this.leftBar.fromPointToGuf();
        this.upBar.fromPointToGuf();
    }

    public void mullMat(Matrix3D mat) {
        for (Column column : this.right) {
            column.mullMat(mat);
        }
        for (Column column : this.left) {
            column.mullMat(mat);
        }
        this.rightBar.mullMat(mat);
        this.leftBar.mullMat(mat);
        this.upBar.mullMat(mat);
        this.rightAxis.mullMat(mat);
        this.leftAxis.mullMat(mat);
    }

    public ArrayList<Column> getColumns() {
        ArrayList<Column> temp = new ArrayList<Column>();
        temp.addAll(this.right);
        temp.addAll(this.left);
        temp.add(this.rightBar);
        temp.add(this.leftBar);
        temp.add(this.upBar);
        return temp;
    }

    public void doorsState() {
        if (this.isOpen && this.openState > 0) {
            --this.openState;
        }
    }

    public void openDoors() {
        this.isOpen = true;
    }
}
