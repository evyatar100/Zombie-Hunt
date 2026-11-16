import java.awt.Color;
import java.awt.Graphics;

public class Galil
implements Comparable<Galil> {
    double[] xReal;
    double[] yReal;
    double[] zReal;
    private double[][] xGuf;
    private double[][] yGuf;
    private double[][] zGuf;
    int[] xInt;
    int[] yInt;
    Color color;
    int numOfPoints;
    int mish;
    Point3D magozPoint;
    Point3D xyz;
    double radius;
    double w;

    public Galil(Point3D magozPoint, int numOfPoints) {
        this.magozPoint = magozPoint;
        this.xReal = new double[numOfPoints * 2];
        this.yReal = new double[numOfPoints * 2];
        this.zReal = new double[numOfPoints * 2];
        this.xInt = new int[numOfPoints];
        this.yInt = new int[numOfPoints];
        this.numOfPoints = numOfPoints;
        this.mish = numOfPoints + 2;
        this.xGuf = new double[this.mish][numOfPoints];
        this.yGuf = new double[this.mish][numOfPoints];
        this.zGuf = new double[this.mish][numOfPoints];
        this.xyz = new Point3D();
    }

    public void buildGalil(double x, double y, double z, double radius, double PI, double w, Color color) {
        double teta = 0.0;
        double dTeta = PI / (double)(this.numOfPoints - 1);
        int i = 0;
        while (i < this.numOfPoints) {
            this.xReal[i] = x + radius * Math.sin(teta);
            this.xReal[i + this.numOfPoints] = this.xReal[i];
            this.yReal[i] = y + radius * Math.cos(teta);
            this.yReal[i + this.numOfPoints] = this.yReal[i];
            this.zReal[i] = z;
            this.zReal[i + this.numOfPoints] = z + w;
            teta += dTeta;
            ++i;
        }
        this.xyz.setXYZ(x, y, z);
        this.color = color;
    }

    public void buildHarut(double x, double y, double z, double radius1, double radius2, double PI, double w, Color color) {
        double teta = 0.0;
        double dTeta = PI / (double)(this.numOfPoints - 1);
        int i = 0;
        while (i < this.numOfPoints) {
            this.xReal[i] = x + radius1 * Math.sin(teta);
            this.yReal[i] = y + radius1 * Math.cos(teta);
            this.zReal[i] = z;
            this.xReal[i + this.numOfPoints] = x + radius2 * Math.sin(teta);
            this.yReal[i + this.numOfPoints] = y + radius2 * Math.cos(teta);
            this.zReal[i + this.numOfPoints] = z + w;
            teta += dTeta;
            ++i;
        }
        this.xyz.setXYZ(x, y, z);
        this.color = color;
    }

    public void fromPointToGuf() {
        int i = 0;
        while (i < this.numOfPoints) {
            this.xGuf[0][i] = this.xReal[i];
            this.yGuf[0][i] = this.yReal[i];
            this.zGuf[0][i] = this.zReal[i];
            this.xGuf[1][i] = this.xReal[this.numOfPoints * 2 - 1 - i];
            this.yGuf[1][i] = this.yReal[2 * this.numOfPoints - 1 - i];
            this.zGuf[1][i] = this.zReal[2 * this.numOfPoints - 1 - i];
            ++i;
        }
        i = 2;
        while (i < this.mish - 1) {
            this.xGuf[i][0] = this.xReal[i - 2];
            this.yGuf[i][0] = this.yReal[i - 2];
            this.zGuf[i][0] = this.zReal[i - 2];
            this.xGuf[i][1] = this.xReal[i - 2 + this.numOfPoints];
            this.yGuf[i][1] = this.yReal[i - 2 + this.numOfPoints];
            this.zGuf[i][1] = this.zReal[i - 2 + this.numOfPoints];
            this.xGuf[i][2] = this.xReal[i - 2 + this.numOfPoints + 1];
            this.yGuf[i][2] = this.yReal[i - 2 + this.numOfPoints + 1];
            this.zGuf[i][2] = this.zReal[i - 2 + this.numOfPoints + 1];
            this.xGuf[i][3] = this.xReal[i - 2 + 1];
            this.yGuf[i][3] = this.yReal[i - 2 + 1];
            this.zGuf[i][3] = this.zReal[i - 2 + 1];
            ++i;
        }
        this.xGuf[this.mish - 1][0] = this.xReal[0];
        this.yGuf[this.mish - 1][0] = this.yReal[0];
        this.zGuf[this.mish - 1][0] = this.zReal[0];
        this.xGuf[this.mish - 1][1] = this.xReal[this.numOfPoints - 1];
        this.yGuf[this.mish - 1][1] = this.yReal[this.numOfPoints - 1];
        this.zGuf[this.mish - 1][1] = this.zReal[this.numOfPoints - 1];
        this.xGuf[this.mish - 1][2] = this.xReal[2 * this.numOfPoints - 1];
        this.yGuf[this.mish - 1][2] = this.yReal[2 * this.numOfPoints - 1];
        this.zGuf[this.mish - 1][2] = this.zReal[2 * this.numOfPoints - 1];
        this.xGuf[this.mish - 1][3] = this.xReal[this.numOfPoints];
        this.yGuf[this.mish - 1][3] = this.yReal[this.numOfPoints];
        this.zGuf[this.mish - 1][3] = this.zReal[this.numOfPoints];
    }

    public void mullMat(Matrix3D mat) {
        mat.mullAllPoints(this.xReal, this.yReal, this.zReal, this.numOfPoints * 2);
        this.xyz.mullMat(mat);
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
        int i = 0;
        while (i < 2) {
            this.convert(this.xGuf[i], this.yGuf[i], this.zGuf[i], this.xInt, this.yInt, this.numOfPoints);
            if (this.Check(this.xInt[0], this.yInt[0], this.xInt[1], this.yInt[1], this.xInt[2], this.yInt[2]).booleanValue() && this.getCloseZ() > -MainPanel.p00.z) {
                if (isLines) {
                    page.setColor(Color.black);
                    page.drawPolygon(this.xInt, this.yInt, this.numOfPoints);
                }
                page.setColor(this.color);
                page.fillPolygon(this.xInt, this.yInt, this.numOfPoints);
            }
            ++i;
        }
        i = 2;
        while (i < this.mish) {
            this.convert(this.xGuf[i], this.yGuf[i], this.zGuf[i], this.xInt, this.yInt, 4);
            if (this.Check(this.xInt[0], this.yInt[0], this.xInt[1], this.yInt[1], this.xInt[2], this.yInt[2]).booleanValue() && this.getCloseZ() > -MainPanel.p00.z) {
                page.setColor(this.color);
                page.fillPolygon(this.xInt, this.yInt, 4);
            }
            ++i;
        }
    }

    public Boolean Check(int x1, int y1, int x2, int y2, int x3, int y3) {
        if (x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2) >= 0) {
            return true;
        }
        return false;
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

    @Override
    public int compareTo(Galil o) {
        if (this.getCloseZ() > o.getCloseZ()) {
            return 1;
        }
        if (this.getCloseZ() < o.getCloseZ()) {
            return -1;
        }
        return 0;
    }
}
