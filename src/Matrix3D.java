public class Matrix3D {
    final int size = 4;
    double[][] mat;

    public Matrix3D() {
        this.mat = new double[4][4];
        this.setIdentity();
    }

    public Matrix3D(Matrix3D temp) {
        this.mat = temp.mat;
    }

    public void setIdentity() {
        this.mat[0][0] = 1.0;
        this.mat[0][1] = 0.0;
        this.mat[0][2] = 0.0;
        this.mat[0][3] = 0.0;
        this.mat[1][0] = 0.0;
        this.mat[1][1] = 1.0;
        this.mat[1][2] = 0.0;
        this.mat[1][3] = 0.0;
        this.mat[2][0] = 0.0;
        this.mat[2][1] = 0.0;
        this.mat[2][2] = 1.0;
        this.mat[2][3] = 0.0;
        this.mat[3][0] = 0.0;
        this.mat[3][1] = 0.0;
        this.mat[3][2] = 0.0;
        this.mat[3][3] = 1.0;
    }

    public void setMatMove(double dx, double dy, double dz) {
        this.mat[0][0] = 1.0;
        this.mat[0][1] = 0.0;
        this.mat[0][2] = 0.0;
        this.mat[0][3] = 0.0;
        this.mat[1][0] = 0.0;
        this.mat[1][1] = 1.0;
        this.mat[1][2] = 0.0;
        this.mat[1][3] = 0.0;
        this.mat[2][0] = 0.0;
        this.mat[2][1] = 0.0;
        this.mat[2][2] = 1.0;
        this.mat[2][3] = 0.0;
        this.mat[3][0] = dx;
        this.mat[3][1] = dy;
        this.mat[3][2] = dz;
        this.mat[3][3] = 1.0;
    }

    public void setMatScale(double dx, double dy, double dz) {
        this.mat[0][0] = dx;
        this.mat[0][1] = 0.0;
        this.mat[0][2] = 0.0;
        this.mat[0][3] = 0.0;
        this.mat[1][0] = 0.0;
        this.mat[1][1] = dy;
        this.mat[1][2] = 0.0;
        this.mat[1][3] = 0.0;
        this.mat[2][0] = 0.0;
        this.mat[2][1] = 0.0;
        this.mat[2][2] = dz;
        this.mat[2][3] = 0.0;
        this.mat[3][0] = 0.0;
        this.mat[3][1] = 0.0;
        this.mat[3][2] = 0.0;
        this.mat[3][3] = 1.0;
    }

    public void setMatScaleFix(double sx, double sy, double sz, double xf, double yf, double zf) {
        this.mat[0][0] = sx;
        this.mat[0][1] = 0.0;
        this.mat[0][2] = 0.0;
        this.mat[0][3] = 0.0;
        this.mat[1][0] = 0.0;
        this.mat[1][1] = sy;
        this.mat[1][2] = 0.0;
        this.mat[1][3] = 0.0;
        this.mat[2][0] = 0.0;
        this.mat[2][1] = 0.0;
        this.mat[2][2] = sz;
        this.mat[2][3] = 0.0;
        this.mat[3][0] = (1.0 - sx) * xf;
        this.mat[3][1] = (1.0 - sy) * yf;
        this.mat[3][2] = (1.0 - sz) * zf;
        this.mat[3][3] = 1.0;
    }

    public void setMatRotateX(double angel) {
        this.mat[0][0] = 1.0;
        this.mat[0][1] = 0.0;
        this.mat[0][2] = 0.0;
        this.mat[0][3] = 0.0;
        this.mat[1][0] = 0.0;
        this.mat[1][1] = Math.cos(angel);
        this.mat[1][2] = Math.sin(angel);
        this.mat[1][3] = 0.0;
        this.mat[2][0] = 0.0;
        this.mat[2][1] = -Math.sin(angel);
        this.mat[2][2] = Math.cos(angel);
        this.mat[2][3] = 0.0;
        this.mat[3][0] = 0.0;
        this.mat[3][1] = 0.0;
        this.mat[3][2] = 0.0;
        this.mat[3][3] = 1.0;
    }

    public void setMatRotateXFix(double angel, double xReal, double yReal, double zReal) {
        Matrix3D temp = new Matrix3D();
        this.setMatMove(-xReal, -yReal, -zReal);
        temp.setMatRotateX(angel);
        this.mullMatMat(temp);
        temp.setMatMove(xReal, yReal, zReal);
        this.mullMatMat(temp);
    }

    public void setMatRotateY(double angel) {
        this.mat[0][0] = Math.cos(angel);
        this.mat[0][1] = 0.0;
        this.mat[0][2] = Math.sin(angel);
        this.mat[0][3] = 0.0;
        this.mat[1][0] = 0.0;
        this.mat[1][1] = 1.0;
        this.mat[1][2] = 0.0;
        this.mat[1][3] = 0.0;
        this.mat[2][0] = -Math.sin(angel);
        this.mat[2][1] = 0.0;
        this.mat[2][2] = Math.cos(angel);
        this.mat[2][3] = 0.0;
        this.mat[3][0] = 0.0;
        this.mat[3][1] = 0.0;
        this.mat[3][2] = 0.0;
        this.mat[3][3] = 1.0;
    }

    public void setMatRotateYFix(double angel, double xReal, double yReal, double zReal) {
        Matrix3D temp = new Matrix3D();
        this.setMatMove(-xReal, -yReal, -zReal);
        temp.setMatRotateY(angel);
        this.mullMatMat(temp);
        temp.setMatMove(xReal, yReal, zReal);
        this.mullMatMat(temp);
    }

    public void setMatRotateZ(double angel) {
        this.mat[0][0] = Math.cos(angel);
        this.mat[0][1] = Math.sin(angel);
        this.mat[0][2] = 0.0;
        this.mat[0][3] = 0.0;
        this.mat[1][0] = -Math.sin(angel);
        this.mat[1][1] = Math.cos(angel);
        this.mat[1][2] = 0.0;
        this.mat[1][3] = 0.0;
        this.mat[2][0] = 0.0;
        this.mat[2][1] = 0.0;
        this.mat[2][2] = 1.0;
        this.mat[2][3] = 0.0;
        this.mat[3][0] = 0.0;
        this.mat[3][1] = 0.0;
        this.mat[3][2] = 0.0;
        this.mat[3][3] = 1.0;
    }

    public void setMatRotateZFix(double angel, double xReal, double yReal, double zReal) {
        Matrix3D temp = new Matrix3D();
        this.setMatMove(-xReal, -yReal, -zReal);
        temp.setMatRotateZ(angel);
        this.mullMatMat(temp);
        temp.setMatMove(xReal, yReal, zReal);
        this.mullMatMat(temp);
    }

    public void setMatRotateAxis(Point3D p1, Point3D p2, double teta) {
        double d;
        Matrix3D m1 = new Matrix3D();
        double a = p2.x - p1.x;
        double b = p2.y - p1.y;
        double c = p2.z - p2.z;
        double l = (float)Math.sqrt(a * a + b * b + c * c);
        a /= l;
        if ((d = (double)((float)Math.sqrt((b /= l) * b + (c /= l) * c))) == 0.0) {
            this.setMatRotateXFix(p1.x, p1.y, p1.z, teta);
        } else {
            this.setMatMove(-p1.x, -p1.y, -p1.z);
            m1.setIdentity();
            m1.mat[1][1] = c / d;
            m1.mat[1][2] = b / d;
            m1.mat[2][1] = -b / d;
            m1.mat[2][2] = c / d;
            this.mullMatMat(m1);
            m1.setIdentity();
            m1.mat[0][0] = d;
            m1.mat[0][2] = a;
            m1.mat[2][0] = -a;
            m1.mat[2][2] = d;
            this.mullMatMat(m1);
            m1.setMatRotateZ(teta);
            this.mullMatMat(m1);
            m1.setIdentity();
            m1.mat[0][0] = d;
            m1.mat[0][2] = -a;
            m1.mat[2][0] = a;
            m1.mat[2][2] = d;
            this.mullMatMat(m1);
            m1.setIdentity();
            m1.mat[1][1] = c / d;
            m1.mat[1][2] = -b / d;
            m1.mat[2][1] = b / d;
            m1.mat[2][2] = c / d;
            this.mullMatMat(m1);
            m1.setMatMove(p1.x, p1.y, p1.z);
            this.mullMatMat(m1);
        }
    }

    public void mullMatMat(Matrix3D aMat) {
        Matrix3D temp = new Matrix3D();
        int i = 0;
        while (i < 4) {
            int j = 0;
            while (j < 4) {
                temp.mat[i][j] = 0.0;
                int k = 0;
                while (k < 4) {
                    double[] dArray = temp.mat[i];
                    int n = j;
                    dArray[n] = dArray[n] + this.mat[i][k] * aMat.mat[k][j];
                    ++k;
                }
                ++j;
            }
            ++i;
        }
        this.mat = temp.mat;
    }

    public void mullAllPoints(double[] xr, double[] yr, double[] zr, int aNum) {
        int i = 0;
        while (i < aNum) {
            double xTemp = xr[i];
            double yTemp = yr[i];
            double zTemp = zr[i];
            xr[i] = xTemp * this.mat[0][0] + yTemp * this.mat[1][0] + zTemp * this.mat[2][0] + 1.0 * this.mat[3][0];
            yr[i] = xTemp * this.mat[0][1] + yTemp * this.mat[1][1] + zTemp * this.mat[2][1] + 1.0 * this.mat[3][1];
            zr[i] = xTemp * this.mat[0][2] + yTemp * this.mat[1][2] + zTemp * this.mat[2][2] + 1.0 * this.mat[3][2];
            ++i;
        }
    }
}
