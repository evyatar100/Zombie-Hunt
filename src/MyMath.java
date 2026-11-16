public class MyMath {
    public static Point3D Kramer2(double a, double b, double c, double d, double r1, double r2) {
        Point3D result = new Point3D();
        double katsav = a * d - b * c;
        result.setX((r1 * d - b * r2) / katsav);
        result.setY((a * r2 - r1 * c) / katsav);
        result.setZ(0.0);
        return result;
    }

    public static Point3D Kramer3(double a, double b, double c, double d, double e, double f, double g, double h, double i, double r1, double r2, double r3) {
        Point3D result = new Point3D();
        double katsav = a * (e * i - f * h) - b * (d * i - f * g) + c * (d * h - e * g);
        result.setX((r1 * (e * i - f * h) - b * (r2 * i - f * r3) + c * (r2 * h - e * r3)) / katsav);
        result.setY((a * (r2 * i - f * r3) - r1 * (d * i - f * g) + c * (d * r3 - r2 * g)) / katsav);
        result.setZ((a * (e * r3 - r2 * h) - b * (d * r3 - r2 * g) + r1 * (d * h - e * g)) / katsav);
        return result;
    }

    public static boolean YasharAndRectangle(Point3D y1, Point3D y2, Point3D a, Point3D b, Point3D c, Point3D d) {
        Point3D hit = new Point3D();
        hit = MyMath.YasharAndMish(y1, y2, a, b, c);
        boolean check = MyMath.PointAndRectangle(hit, a, b, c, d);
        return check && hit.x >= 1.0;
    }

    public static Point3D YasharAndMish(Point3D a1, Point3D b1, Point3D a2, Point3D b2, Point3D c2) {
        Point3D ab1 = new Point3D(b1.x - a1.x, b1.y - a1.y, b1.z - a1.z);
        Point3D ab2 = new Point3D(b2.x - a2.x, b2.y - a2.y, b2.z - a2.z);
        Point3D ac2 = new Point3D(c2.x - a2.x, c2.y - a2.y, c2.z - a2.z);
        Point3D temp = MyMath.Kramer3(ab1.x, -ab2.x, -ac2.x, ab1.y, -ab2.y, -ac2.y, ab1.z, -ab2.z, -ac2.z, a2.x - a1.x, a2.y - a1.y, a2.z - a1.z);
        return temp;
    }

    public static boolean PointAndRectangle(Point3D p, Point3D a, Point3D b, Point3D c, Point3D d) {
        if (MyMath.YasharAndKeta(a, p, b, c)) {
            return true;
        }
        return MyMath.YasharAndKeta(a, p, d, c);
    }

    public static boolean YasharAndKeta(Point3D aY, Point3D bY, Point3D aK, Point3D bK) {
        Point3D abY = new Point3D(bY.x - aY.x, bY.y - aY.y, bY.z - aY.z);
        Point3D abK = new Point3D(bK.x - aK.x, bY.y - aK.y, bK.z - aK.z);
        double t1 = MyMath.Yashars(aY, abY, aK, abK, true);
        double t2 = MyMath.Yashars(aY, abY, aK, abK, false);
        return t2 >= 0.0 && t2 <= 1.0 && t1 >= 1.0;
    }

    public static double Yashars(Point3D a1, Point3D ab1, Point3D a2, Point3D ab2, boolean getFirst) {
        Point3D temp = new Point3D();
        temp = MyMath.Kramer2(ab1.x, -ab2.x, ab1.y, -ab2.y, a2.x - a1.x, a2.y - a1.y);
        if (getFirst) {
            return temp.x;
        }
        return temp.y;
    }
}
