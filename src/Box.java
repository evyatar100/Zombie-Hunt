import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Box
implements Comparable<Box> {
    Random generator = new Random();
    double[] xReal;
    double[] yReal;
    double[] zReal;
    private double[][] xGuf;
    private double[][] yGuf;
    private double[][] zGuf;
    int[] xInt;
    int[] yInt;
    int[] zInt;
    Color color;
    Point3D[][] platot;
    boolean[] whatToDraw;
    Point3D xyz;
    Point3D magozPoint;
    private int numOfPoint = 8;
    int mish = 6;
    boolean drawChange = false;

    public Box(Point3D magozPoint) {
        this.magozPoint = magozPoint;
        this.xReal = new double[this.numOfPoint];
        this.yReal = new double[this.numOfPoint];
        this.zReal = new double[this.numOfPoint];
        this.xGuf = new double[this.mish][4];
        this.yGuf = new double[this.mish][4];
        this.zGuf = new double[this.mish][4];
        this.xInt = new int[this.numOfPoint];
        this.yInt = new int[this.numOfPoint];
        this.zInt = new int[this.numOfPoint];
        this.xyz = new Point3D();
        this.platot = new Point3D[this.mish][4];
        this.whatToDraw = new boolean[this.mish];
        int i = 0;
        while (i < this.mish) {
            int j = 0;
            while (j < 4) {
                this.whatToDraw[i] = true;
                this.platot[i][j] = new Point3D();
                ++j;
            }
            ++i;
        }
    }

    public void buildShape(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double z, double dz, Color color) {
        this.xReal[0] = x1;
        this.yReal[0] = y1;
        this.zReal[0] = z;
        this.xReal[1] = x2;
        this.yReal[1] = y2;
        this.zReal[1] = z;
        this.xReal[2] = x3;
        this.yReal[2] = y3;
        this.zReal[2] = z;
        this.xReal[3] = x4;
        this.yReal[3] = y4;
        this.zReal[3] = z;
        this.xReal[4] = x1;
        this.yReal[4] = y1;
        this.zReal[4] = z + dz;
        this.xReal[5] = x4;
        this.yReal[5] = y4;
        this.zReal[5] = z + dz;
        this.xReal[6] = x3;
        this.yReal[6] = y3;
        this.zReal[6] = z + dz;
        this.xReal[7] = x2;
        this.yReal[7] = y2;
        this.zReal[7] = z + dz;
        this.color = color;
        this.xyz.setXYZ(x1, y1, z);
    }

    public void buildShape(double x, double y, double z, double dx, double dy, double dz, Color color) {
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
        this.xReal[4] = x;
        this.yReal[4] = y;
        this.zReal[4] = z + dz;
        this.xReal[5] = x;
        this.yReal[5] = y + dy;
        this.zReal[5] = z + dz;
        this.xReal[6] = x + dx;
        this.yReal[6] = y + dy;
        this.zReal[6] = z + dz;
        this.xReal[7] = x + dx;
        this.yReal[7] = y;
        this.zReal[7] = z + dz;
        this.color = color;
        this.xyz.setXYZ(x, y, z);
    }

    public void buildShape(double x, double y, double z, double dx, double dy, double dz, Color color, Color frontColor) {
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
        this.xReal[4] = x;
        this.yReal[4] = y;
        this.zReal[4] = z + dz;
        this.xReal[5] = x;
        this.yReal[5] = y + dy;
        this.zReal[5] = z + dz;
        this.xReal[6] = x + dx;
        this.yReal[6] = y + dy;
        this.zReal[6] = z + dz;
        this.xReal[7] = x + dx;
        this.yReal[7] = y;
        this.zReal[7] = z + dz;
        this.color = color;
        this.xyz.setXYZ(x, y, z);
    }

    public void fromPointToGuf() {
        int i = 0;
        this.xGuf[i][0] = this.xReal[0];
        this.yGuf[i][0] = this.yReal[0];
        this.zGuf[i][0] = this.zReal[0];
        this.xGuf[i][1] = this.xReal[1];
        this.yGuf[i][1] = this.yReal[1];
        this.zGuf[i][1] = this.zReal[1];
        this.xGuf[i][2] = this.xReal[2];
        this.yGuf[i][2] = this.yReal[2];
        this.zGuf[i][2] = this.zReal[2];
        this.xGuf[i][3] = this.xReal[3];
        this.yGuf[i][3] = this.yReal[3];
        this.zGuf[i][3] = this.zReal[3];
        i = 1;
        this.xGuf[i][0] = this.xReal[4];
        this.yGuf[i][0] = this.yReal[4];
        this.zGuf[i][0] = this.zReal[4];
        this.xGuf[i][1] = this.xReal[5];
        this.yGuf[i][1] = this.yReal[5];
        this.zGuf[i][1] = this.zReal[5];
        this.xGuf[i][2] = this.xReal[6];
        this.yGuf[i][2] = this.yReal[6];
        this.zGuf[i][2] = this.zReal[6];
        this.xGuf[i][3] = this.xReal[7];
        this.yGuf[i][3] = this.yReal[7];
        this.zGuf[i][3] = this.zReal[7];
        i = 2;
        this.xGuf[i][0] = this.xReal[0];
        this.yGuf[i][0] = this.yReal[0];
        this.zGuf[i][0] = this.zReal[0];
        this.xGuf[i][1] = this.xReal[4];
        this.yGuf[i][1] = this.yReal[4];
        this.zGuf[i][1] = this.zReal[4];
        this.xGuf[i][2] = this.xReal[7];
        this.yGuf[i][2] = this.yReal[7];
        this.zGuf[i][2] = this.zReal[7];
        this.xGuf[i][3] = this.xReal[1];
        this.yGuf[i][3] = this.yReal[1];
        this.zGuf[i][3] = this.zReal[1];
        i = 3;
        this.xGuf[i][0] = this.xReal[1];
        this.yGuf[i][0] = this.yReal[1];
        this.zGuf[i][0] = this.zReal[1];
        this.xGuf[i][1] = this.xReal[7];
        this.yGuf[i][1] = this.yReal[7];
        this.zGuf[i][1] = this.zReal[7];
        this.xGuf[i][2] = this.xReal[6];
        this.yGuf[i][2] = this.yReal[6];
        this.zGuf[i][2] = this.zReal[6];
        this.xGuf[i][3] = this.xReal[2];
        this.yGuf[i][3] = this.yReal[2];
        this.zGuf[i][3] = this.zReal[2];
        i = 4;
        this.xGuf[i][0] = this.xReal[2];
        this.yGuf[i][0] = this.yReal[2];
        this.zGuf[i][0] = this.zReal[2];
        this.xGuf[i][1] = this.xReal[6];
        this.yGuf[i][1] = this.yReal[6];
        this.zGuf[i][1] = this.zReal[6];
        this.xGuf[i][2] = this.xReal[5];
        this.yGuf[i][2] = this.yReal[5];
        this.zGuf[i][2] = this.zReal[5];
        this.xGuf[i][3] = this.xReal[3];
        this.yGuf[i][3] = this.yReal[3];
        this.zGuf[i][3] = this.zReal[3];
        i = 5;
        this.xGuf[i][0] = this.xReal[3];
        this.yGuf[i][0] = this.yReal[3];
        this.zGuf[i][0] = this.zReal[3];
        this.xGuf[i][1] = this.xReal[5];
        this.yGuf[i][1] = this.yReal[5];
        this.zGuf[i][1] = this.zReal[5];
        this.xGuf[i][2] = this.xReal[4];
        this.yGuf[i][2] = this.yReal[4];
        this.zGuf[i][2] = this.zReal[4];
        this.xGuf[i][3] = this.xReal[0];
        this.yGuf[i][3] = this.yReal[0];
        this.zGuf[i][3] = this.zReal[0];
        i = 0;
        while (i < this.mish) {
            this.platot[i][0].setXYZ(this.xGuf[i][0], this.yGuf[i][0], this.zGuf[i][0]);
            this.platot[i][1].setXYZ(this.xGuf[i][1], this.yGuf[i][1], this.zGuf[i][1]);
            this.platot[i][2].setXYZ(this.xGuf[i][2], this.yGuf[i][2], this.zGuf[i][2]);
            this.platot[i][3].setXYZ(this.xGuf[i][3], this.yGuf[i][3], this.zGuf[i][3]);
            ++i;
        }
    }

    public void mullMat(Matrix3D mat) {
        mat.mullAllPoints(this.xReal, this.yReal, this.zReal, this.numOfPoint);
        this.xyz.mullMat(mat);
    }

    public void mullMat(Matrix3D matFront, Matrix3D matBack) {
        double[] xtempF = new double[4];
        double[] ytempF = new double[4];
        double[] ztempF = new double[4];
        double[] xtempB = new double[4];
        double[] ytempB = new double[4];
        double[] ztempB = new double[4];
        int i = 0;
        while (i < 4) {
            xtempF[i] = this.xReal[i];
            ytempF[i] = this.yReal[i];
            ztempF[i] = this.zReal[i];
            xtempB[i] = this.xReal[i + 4];
            ytempB[i] = this.yReal[i + 4];
            ztempB[i] = this.zReal[i + 4];
            ++i;
        }
        matFront.mullAllPoints(xtempF, ytempF, ztempF, 4);
        matBack.mullAllPoints(xtempB, ytempB, ztempB, 4);
        i = 0;
        while (i < 4) {
            this.xReal[i] = xtempF[i];
            this.yReal[i] = ytempF[i];
            this.zReal[i] = ztempF[i];
            this.xReal[i + 4] = xtempB[i];
            this.yReal[i + 4] = ytempB[i];
            this.zReal[i + 4] = ztempB[i];
            ++i;
        }
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
        while (i < this.mish) {
            this.convert(this.xGuf[i], this.yGuf[i], this.zGuf[i], this.xInt, this.yInt, 4);
            if (this.check(this.xInt[0], this.yInt[0], this.xInt[1], this.yInt[1], this.xInt[2], this.yInt[2]).booleanValue() && this.getCloseZ() > -MainPanel.p00.z) {
                page.setColor(this.color);
                page.fillPolygon(this.xInt, this.yInt, 4);
                if (isLines && this.whatToDraw[i]) {
                    page.setColor(Color.black);
                    page.drawPolygon(this.xInt, this.yInt, 4);
                }
            }
            ++i;
        }
        this.convert(this.xGuf[0], this.yGuf[0], this.zGuf[0], this.xInt, this.yInt, 4);
    }

    public Boolean check(int x1, int y1, int x2, int y2, int x3, int y3) {
        if (x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2) >= 0) {
            return true;
        }
        return false;
    }

    public boolean wallCheck() {
        this.convert(this.xGuf[0], this.yGuf[0], this.zGuf[0], this.xInt, this.yInt, 4);
        return this.check(this.xInt[0], this.yInt[0], this.xInt[1], this.yInt[1], this.xInt[2], this.yInt[2]);
    }

    public void setLines(boolean front, boolean back, boolean top, boolean right, boolean bottom, boolean left) {
        this.drawChange = false;
        this.whatToDraw[0] = front;
        this.whatToDraw[1] = back;
        this.whatToDraw[2] = top;
        this.whatToDraw[3] = right;
        this.whatToDraw[4] = bottom;
        this.whatToDraw[5] = left;
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

    public double getFarZ() {
        double maxZ = Double.MIN_VALUE;
        double[] dArray = this.zReal;
        int n = this.zReal.length;
        int n2 = 0;
        while (n2 < n) {
            double i = dArray[n2];
            if (i > maxZ) {
                maxZ = i;
            }
            ++n2;
        }
        return maxZ;
    }

    public Point3D getFarPoint() {
        double maxZ = this.getFarZ();
        int index = 4;
        int i = 0;
        while (i < this.numOfPoint) {
            if (maxZ == this.zReal[i]) {
                index = i;
            }
            ++i;
        }
        return new Point3D(this.xReal[index], this.yReal[index], this.zReal[index]);
    }

    public Point3D[][] getPlatot() {
        return this.platot;
    }

    @Override
    public int compareTo(Box o) {
        return (int)(this.getCloseZ() - o.getCloseZ());
    }
}
