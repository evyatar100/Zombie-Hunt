import java.awt.Color;
import java.awt.Graphics;

public class Floor {
    Brick[] brick;
    Color color;
    Point3D magozPoint;
    int numX;
    int numY;
    int num;
    double xSize;
    double ySize;

    public Floor(Point3D magozPoint, int numX, int numY) {
        this.numX = numX;
        this.numY = numY;
        this.num = numX * numY;
        this.magozPoint = magozPoint;
        this.brick = new Brick[this.num];
        int i = 0;
        while (i < this.num) {
            this.brick[i] = new Brick(magozPoint);
            ++i;
        }
    }

    public void buildShape(double x, double y, double z, double dx, double dy, int state, Color color) {
        this.xSize = dx / (double)this.numX;
        this.ySize = dy / (double)this.numY;
        if (state == 0) {
            int i = 0;
            while (i < this.num) {
                this.brick[i].buildShape(x + (double)(i % this.numX) * this.xSize, y, z + (double)(i / this.numX) * this.ySize, this.xSize, this.ySize, 0, color);
                ++i;
            }
        } else {
            int i = 0;
            while (i < this.num) {
                if (i % this.numX == this.numX - 1 && i / this.numX % 2 == 1) {
                    this.brick[i].buildShape(x + (double)(i % this.numX) * this.xSize + (double)(i / this.numX % 2) * 0.5 * this.xSize, y + (double)(i / this.numX) * this.ySize, z, this.xSize / 2.0, this.ySize, 1, color);
                } else if (i % this.numX == 0 && i / this.numX % 2 == 1) {
                    this.brick[i].buildShape(x + (double)(i % this.numX) * this.xSize, y + (double)(i / this.numX) * this.ySize, z, this.xSize * 1.5, this.ySize, 1, color);
                } else {
                    this.brick[i].buildShape(x + (double)(i % this.numX) * this.xSize + (double)(i / this.numX % 2) * 0.5 * this.xSize, y + (double)(i / this.numX) * this.ySize, z, this.xSize, this.ySize, 1, color);
                }
                ++i;
            }
            Matrix3D mati = new Matrix3D();
            mati.setMatRotateYFix(-1.5707963267948966 * (double)(state - 1), x, y, z);
            this.mullMat(mati);
        }
    }

    public void mullMat(Matrix3D mat) {
        int i = 0;
        while (i < this.num) {
            this.brick[i].mullMat(mat);
            ++i;
        }
    }

    public void convertAndShow(Graphics page) {
        int i = 0;
        while (i < this.num) {
            this.brick[i].convertAndShow(page, true);
            ++i;
        }
    }
}
