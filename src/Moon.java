import java.awt.Color;
import java.awt.Graphics;

public class Moon {
    double[] xReal;
    double[] yReal;
    double[] zReal;
    int[] xInt;
    int[] yInt;
    int numOfPoints = 50;
    Point3D magozPoint;
    Color color = new Color(230, 230, 230);
    double radius;

    public Moon(Point3D magozPoint) {
        this.magozPoint = magozPoint;
        this.xReal = new double[this.numOfPoints];
        this.yReal = new double[this.numOfPoints];
        this.zReal = new double[this.numOfPoints];
        this.xInt = new int[this.numOfPoints];
        this.yInt = new int[this.numOfPoints];
    }

    public void buildShape(double x, double y, double z, double radius) {
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
        this.radius = radius;
    }

    public void mullMat(Matrix3D mat) {
        mat.mullAllPoints(this.xReal, this.yReal, this.zReal, this.numOfPoints);
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
        if (this.zReal[0] > this.radius) {
            this.convert(this.xReal, this.yReal, this.zReal, this.xInt, this.yInt, this.numOfPoints);
            page.setColor(this.color);
            page.fillPolygon(this.xInt, this.yInt, this.numOfPoints);
        }
    }
}
