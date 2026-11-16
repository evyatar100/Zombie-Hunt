import java.awt.Color;
import java.awt.Graphics;

public class Brick {
    double[] xReal;
    double[] yReal;
    double[] zReal;
    int[] xInt;
    int[] yInt;
    Point3D[] platot;
    Color color;
    Point3D magozPoint;
    double maxSize;
    private int numOfPoint = 4;

    public Brick(Point3D magozPoint) {
        this.magozPoint = magozPoint;
        this.xReal = new double[this.numOfPoint];
        this.yReal = new double[this.numOfPoint];
        this.zReal = new double[this.numOfPoint];
        this.xInt = new int[this.numOfPoint];
        this.yInt = new int[this.numOfPoint];
        this.platot = new Point3D[4];
        int i = 0;
        while (i < 4) {
            this.platot[i] = new Point3D();
            ++i;
        }
    }

    public void buildShape(double x, double y, double z, double dx, double dy, int state, Color color) {
        if (state == 0) {
            this.xReal[0] = x;
            this.yReal[0] = y;
            this.zReal[0] = z;
            this.xReal[1] = x;
            this.yReal[1] = y;
            this.zReal[1] = z + dy;
            this.xReal[2] = x + dx;
            this.yReal[2] = y;
            this.zReal[2] = z + dy;
            this.xReal[3] = x + dx;
            this.yReal[3] = y;
            this.zReal[3] = z;
        } else if (state == 1) {
            this.xReal[0] = x;
            this.yReal[0] = y;
            this.zReal[0] = z;
            this.xReal[1] = x + dx;
            this.yReal[1] = y;
            this.zReal[1] = z;
            this.xReal[2] = x + dx;
            this.yReal[2] = y + dy;
            this.zReal[2] = z;
            this.xReal[3] = x;
            this.yReal[3] = y + dy;
            this.zReal[3] = z;
        } else if (state == 2) {
            this.xReal[0] = x;
            this.yReal[0] = y;
            this.zReal[0] = z;
            this.xReal[1] = x;
            this.yReal[1] = y;
            this.zReal[1] = z + dx;
            this.xReal[2] = x;
            this.yReal[2] = y + dy;
            this.zReal[2] = z + dx;
            this.xReal[3] = x;
            this.yReal[3] = y + dy;
            this.zReal[3] = z;
        }
        this.color = color;
    }

    public void mullMat(Matrix3D mat) {
        mat.mullAllPoints(this.xReal, this.yReal, this.zReal, this.numOfPoint);
        this.platot[0].setXYZ(this.xReal[0], this.yReal[0], this.zReal[0]);
        this.platot[1].setXYZ(this.xReal[1], this.yReal[1], this.zReal[1]);
        this.platot[2].setXYZ(this.xReal[2], this.yReal[2], this.zReal[2]);
        this.platot[3].setXYZ(this.xReal[3], this.yReal[3], this.zReal[3]);
    }

    public void convert(double[] xr, double[] yr, double[] zr, int[] xi, int[] yi, int aNum) {
        int i = 0;
        while (i < aNum) {
            xi[i] = (int)(xr[i] * (this.magozPoint.z / (this.magozPoint.z + zr[i])) + this.magozPoint.x);
            yi[i] = (int)(yr[i] * (this.magozPoint.z / (this.magozPoint.z + zr[i])) + this.magozPoint.y);
            ++i;
        }
    }

    public void convertAndShow(Graphics page, boolean isLines) {
        this.convert(this.xReal, this.yReal, this.zReal, this.xInt, this.yInt, this.numOfPoint);
        if (this.Check(this.xInt[0], this.yInt[0], this.xInt[1], this.yInt[1], this.xInt[2], this.yInt[2]).booleanValue() && this.getCloseZ() > -MainPanel.p00.z) {
            page.setColor(this.color);
            page.fillPolygon(this.xInt, this.yInt, 4);
            if (isLines) {
                page.setColor(Color.black);
                page.drawPolygon(this.xInt, this.yInt, 4);
            }
        }
    }

    public Boolean Check(int x1, int y1, int x2, int y2, int x3, int y3) {
        if (x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2) >= 0) {
            return true;
        }
        return false;
    }

    public Boolean Check() {
        this.convert(this.xReal, this.yReal, this.zReal, this.xInt, this.yInt, this.numOfPoint);
        return this.Check(this.xInt[0], this.yInt[0], this.xInt[1], this.yInt[1], this.xInt[2], this.yInt[2]);
    }

    public double getCloseZ() {
        double min = Double.MAX_VALUE;
        double[] dArray = this.zReal;
        int n = this.zReal.length;
        int n2 = 0;
        while (n2 < n) {
            double i = dArray[n2];
            if (i < min) {
                min = i;
            }
            ++n2;
        }
        return min;
    }

    public Point3D[] getPlatot() {
        return this.platot;
    }
}
