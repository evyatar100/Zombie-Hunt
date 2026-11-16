import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

public class Map {
    int[][] map;
    double xSize;
    double ySize;
    double xBox;
    double yBox;
    int xNum;
    int yNum;
    boolean showMap;
    Point3D pStart;

    public Map(double xSize, double ySize, double xBox, double yBox) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.xBox = xBox;
        this.yBox = yBox;
        this.pStart = MainPanel.pStart;
        this.xNum = this.indexX(xSize);
        this.yNum = this.indexY(ySize);
        this.map = new int[this.xNum][this.yNum];
        this.showMap = false;
    }

    public boolean isInRange(double x, double z) {
        return x > this.xBox * 2.0 && x < this.xSize - this.xBox * 2.0 && z > this.yBox * 2.0 && z < this.ySize - this.yBox * 2.0;
    }

    public boolean isEmpty(double x, double z, int object) {
        int tempY;
        int tempX = this.indexX(x);
        return !(this.map[tempX - 1][(tempY = this.indexY(z)) - 1] != 0 && this.map[tempX - 1][tempY - 1] != object || this.map[tempX - 1][tempY] != 0 && this.map[tempX - 1][tempY] != object || this.map[tempX - 1][tempY + 1] != 0 && this.map[tempX - 1][tempY + 1] != object || this.map[tempX][tempY - 1] != 0 && this.map[tempX][tempY - 1] != object || this.map[tempX][tempY] != 0 && this.map[tempX][tempY] != object || this.map[tempX][tempY + 1] != 0 && this.map[tempX][tempY + 1] != object || this.map[tempX + 1][tempY - 1] != 0 && this.map[tempX + 1][tempY - 1] != object || this.map[tempX + 1][tempY] != 0 && this.map[tempX + 1][tempY] != object || this.map[tempX + 1][tempY + 1] != 0 && this.map[tempX + 1][tempY + 1] != object);
    }

    public boolean canZombieMove(double x, double z, int object) {
        int tempY;
        int tempX = this.indexX(x);
        return !(this.map[tempX - 1][(tempY = this.indexY(z)) - 1] != 0 && this.map[tempX - 1][tempY - 1] != object && this.map[tempX - 1][tempY - 1] != -2 || this.map[tempX - 1][tempY] != 0 && this.map[tempX - 1][tempY] != object && this.map[tempX - 1][tempY] != -2 || this.map[tempX - 1][tempY + 1] != 0 && this.map[tempX - 1][tempY + 1] != object && this.map[tempX - 1][tempY + 1] != -2 || this.map[tempX][tempY - 1] != 0 && this.map[tempX][tempY - 1] != object && this.map[tempX][tempY - 1] != -2 || this.map[tempX][tempY] != 0 && this.map[tempX][tempY] != object && this.map[tempX][tempY] != -2 || this.map[tempX][tempY + 1] != 0 && this.map[tempX][tempY + 1] != object && this.map[tempX][tempY + 1] != -2 || this.map[tempX + 1][tempY - 1] != 0 && this.map[tempX + 1][tempY - 1] != object && this.map[tempX + 1][tempY - 1] != -2 || this.map[tempX + 1][tempY] != 0 && this.map[tempX + 1][tempY] != object && this.map[tempX + 1][tempY] != -2 || this.map[tempX + 1][tempY + 1] != 0 && this.map[tempX + 1][tempY + 1] != object && this.map[tempX + 1][tempY + 1] != -2);
    }

    public void setObjectIndex(int object, int indexX, int indexY) {
        this.map[indexX][indexY] = object;
    }

    public void setObject(int object, Point point) {
        int tempX = this.indexX(point.x);
        int tempY = this.indexY(point.y);
        this.setObjectIndex(object, tempX, tempY);
    }

    public void clearObject(double x, double z) {
        int tempX = this.indexX(x);
        int tempY = this.indexY(z);
        this.setObjectIndex(0, tempX, tempY);
    }

    public void setBigObject(double x, double z, double dx, double dz) {
        int dy1;
        int x1 = this.indexX(x - this.pStart.x);
        int y1 = this.indexY(z - this.pStart.z);
        int dx1 = this.indexX(dx);
        if (dx1 == 0) {
            dx1 = 1;
        }
        if ((dy1 = this.indexY(dz)) == 0) {
            dy1 = 1;
        }
        int i = x1;
        while (i < x1 + dx1) {
            int j = y1;
            while (j <= y1 + dy1) {
                this.setObjectIndex(-1, i, j);
                ++j;
            }
            ++i;
        }
    }

    public void setDoor(double x, double z, int state, boolean isOpen) {
        int object = isOpen ? -2 : -3;
        int x1 = 0;
        int y1 = 0;
        int dx1 = 0;
        int dy1 = 0;
        switch (state) {
            case 0: {
                x1 = this.indexX(x - this.pStart.x);
                y1 = this.indexY(z - this.pStart.z);
                dx1 = this.indexX(2000.0);
                dy1 = 1;
                break;
            }
            case 1: {
                x1 = this.indexX(x - this.pStart.x);
                y1 = this.indexY(z - this.pStart.z - 2000.0);
                dx1 = 1;
                dy1 = this.indexY(2000.0);
                break;
            }
            case 2: {
                x1 = this.indexX(x - this.pStart.x - 2000.0);
                y1 = this.indexY(z - this.pStart.z);
                dx1 = this.indexX(2000.0);
                dy1 = 1;
                break;
            }
            case 3: {
                x1 = this.indexX(x - this.pStart.x);
                y1 = this.indexY(z - this.pStart.z);
                dx1 = 1;
                dy1 = this.indexY(2000.0);
            }
        }
        int i = x1;
        while (i < x1 + dx1) {
            int j = y1;
            while (j <= y1 + dy1) {
                this.setObjectIndex(object, i, j);
                ++j;
            }
            ++i;
        }
    }

    public boolean isBigObject(double x, double z, double dx, double dz) {
        int x1 = this.indexX(x);
        int y1 = this.indexY(z);
        int dx1 = this.indexX(dx);
        int dy1 = this.indexY(dz);
        int i = x1;
        while (i <= x1 + dx1) {
            int j = y1;
            while (j <= y1 + dy1) {
                if (this.isInRange(i, j) && !this.isEmpty(i, j, -1)) {
                    return false;
                }
                ++j;
            }
            ++i;
        }
        return true;
    }

    public boolean isBigObject(int x, int y, int dx, int dy) {
        int i = x;
        while (i <= x + dx) {
            int j = y;
            while (j <= y + dy) {
                if (!this.isEmpty(i, j, -1)) {
                    return false;
                }
                ++j;
            }
            ++i;
        }
        return true;
    }

    public int indexX(double size) {
        return (int)(size / this.xBox);
    }

    public int indexY(double size) {
        return (int)(size / this.yBox);
    }

    public void showMap() {
        this.showMap = !this.showMap;
    }

    public void printMap(Graphics page) {
        if (this.showMap) {
            page.setColor(Color.red);
            page.setFont(new Font("0", 0, 10));
            int i = 0;
            while (i < this.xNum) {
                int j = 0;
                while (j < this.yNum) {
                    if (this.map[i][j] == 0) {
                        page.setColor(Color.yellow);
                    } else if (this.map[i][j] == 1) {
                        page.setColor(Color.blue);
                    } else if (this.map[i][j] >= 2) {
                        page.setColor(Color.red);
                    } else if (this.map[i][j] == -1) {
                        page.setColor(Color.black);
                    } else if (this.map[i][j] == -2) {
                        page.setColor(new Color(128, 0, 255));
                    } else if (this.map[i][j] == -3) {
                        page.setColor(new Color(185, 122, 87));
                    } else {
                        page.setColor(Color.orange);
                    }
                    page.drawString(String.valueOf(this.map[i][j]), j * 5, (i + 1) * 10);
                    ++j;
                }
                ++i;
            }
        }
    }
}
