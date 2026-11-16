public class Point3D {
    double x;
    double y;
    double z;

    public Point3D() {
    }

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setXYZ(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setXYZ(Point3D xyz) {
        this.x = xyz.x;
        this.y = xyz.y;
        this.z = xyz.z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void mullMat(Matrix3D mat) {
        double xTemp = this.x;
        double yTemp = this.y;
        double zTemp = this.z;
        this.x = xTemp * mat.mat[0][0] + yTemp * mat.mat[1][0] + zTemp * mat.mat[2][0] + 1.0 * mat.mat[3][0];
        this.y = xTemp * mat.mat[0][1] + yTemp * mat.mat[1][1] + zTemp * mat.mat[2][1] + 1.0 * mat.mat[3][1];
        this.z = xTemp * mat.mat[0][2] + yTemp * mat.mat[1][2] + zTemp * mat.mat[2][2] + 1.0 * mat.mat[3][2];
    }
}
