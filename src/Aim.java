import java.awt.Color;
import java.awt.Graphics;

public class Aim {
    double[] xReal;
    double[] yReal;
    double[] zReal;
    int[] xInt;
    int[] yInt;
    Point3D xyz;
    int numOfPoints = 30;
    Point3D magozPoint;
    Gun gun;
    Color color = new Color(0, 78, 155);

    public Aim(Point3D magozPoint) {
        this.magozPoint = magozPoint;
        this.xReal = new double[this.numOfPoints + 4];
        this.yReal = new double[this.numOfPoints + 4];
        this.zReal = new double[this.numOfPoints + 4];
        this.xInt = new int[this.numOfPoints + 4];
        this.yInt = new int[this.numOfPoints + 4];
        this.xyz = new Point3D();
    }

    public void buildShape(double x, double y, double z, double radius, Gun gun) {
        double teta = 0.0;
        double dTeta = Math.PI * 2 / (double)this.numOfPoints;
        int i = 0;
        while (i < this.numOfPoints) {
            this.xReal[i] = x + radius * Math.sin(teta);
            this.yReal[i] = y + radius * Math.cos(teta);
            this.zReal[i] = z;
            teta += dTeta;
            ++i;
        }
        this.xReal[this.numOfPoints] = x - radius * 1.3;
        this.yReal[this.numOfPoints] = y;
        this.zReal[this.numOfPoints] = z;
        this.xReal[this.numOfPoints + 1] = x + radius * 1.3;
        this.yReal[this.numOfPoints + 1] = y;
        this.zReal[this.numOfPoints + 1] = z;
        this.xReal[this.numOfPoints + 2] = x;
        this.yReal[this.numOfPoints + 2] = y - radius * 1.3;
        this.zReal[this.numOfPoints + 2] = z;
        this.xReal[this.numOfPoints + 3] = x;
        this.yReal[this.numOfPoints + 3] = y + radius * 1.3;
        this.zReal[this.numOfPoints + 3] = z;
        this.xyz.setXYZ(x, y, z);
        this.gun = gun;
    }

    public void convert(double[] xr, double[] yr, double[] zr, int[] xi, int[] yi, int aNum) {
        int i = 0;
        while (i < aNum) {
            xi[i] = (int)(xr[i] * (this.magozPoint.z / (this.magozPoint.z + zr[i])) + this.magozPoint.x);
            yi[i] = (int)(yr[i] * (this.magozPoint.z / (this.magozPoint.z + zr[i])) + this.magozPoint.y);
            ++i;
        }
    }

    public void convertAndShow(Graphics page) {
        page.setColor(this.color);
        if (!this.gun.isCantShot) {
            this.convert(this.xReal, this.yReal, this.zReal, this.xInt, this.yInt, this.numOfPoints + 4);
            page.drawPolygon(this.xInt, this.yInt, this.numOfPoints);
            page.drawLine(this.xInt[this.numOfPoints], this.yInt[this.numOfPoints], this.xInt[this.numOfPoints + 1], this.yInt[this.numOfPoints + 1]);
            page.drawLine(this.xInt[this.numOfPoints + 2], this.yInt[this.numOfPoints + 2], this.xInt[this.numOfPoints + 3], this.yInt[this.numOfPoints + 3]);
        }
    }
}
