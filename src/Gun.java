import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.FileInputStream;
import java.util.Random;

public class Gun
extends Thread {
    Random generator = new Random();
    Handle handle;
    Barrel barrel;
    Notch notch;
    Aperture aperture;
    Point3D backP;
    Point3D forP;
    Matrix3D mati;
    Point3D xyz;
    Point3D magozPoint;
    int ammo;
    int clip = 1;
    final int clipSize = 12;
    final int maxAmmo = 60;
    boolean isAmmoFull;
    boolean isCantShot;
    int reloadState;
    final int numOfStates = 17;
    int hitState;
    final int numOfHitStates = 11;
    int recoilState;
    final int numOfRecoilStates = 5;
    Font playbill;
    Color ammoColor = new Color(0, 78, 155);

    public Gun(Point3D magozPoint) {
        this.handle = new Handle(magozPoint);
        this.barrel = new Barrel(magozPoint);
        this.notch = new Notch(magozPoint);
        this.aperture = new Aperture(magozPoint);
        this.mati = new Matrix3D();
        this.xyz = new Point3D();
        this.backP = new Point3D();
        this.forP = new Point3D();
        this.magozPoint = magozPoint;
        try {
            this.playbill = Font.createFont(0, new FileInputStream("files/fonts/Playbill.ttf")).deriveFont(0, 50.0f);
        }
        catch (Exception e) {
            System.out.println("playbill font not found");
        }
        this.start();
    }

    public void makeNew() {
        this.ammo = 20;
        this.clip = 0;
        this.load();
        this.isAmmoFull = this.ammo + this.clip >= 60;
        this.isCantShot = false;
        this.reloadState = 0;
        this.hitState = 0;
    }

    public void buildShape(double x, double y, double z) {
        this.handle.buildShape(x, y, z);
        this.barrel.buildShape(x, y, z);
        this.notch.buildShape(x, y, z);
        this.aperture.buildShape(x, y, z);
        this.xyz.setXYZ(x, y, z);
        this.mati.setMatRotateYFix(1.5707963267948966, this.xyz.x, this.xyz.y, this.xyz.z);
        this.mullMat(this.mati);
        this.mati.setMatScaleFix(3.0, 3.0, 3.0, this.xyz.x, this.xyz.y, this.xyz.z);
        this.mullMat(this.mati);
        this.mati.setMatMove(75.0, 65.0, -1120.0);
        this.mullMat(this.mati);
        this.backP.setXYZ(this.xyz);
        this.forP.setXYZ(this.xyz);
        this.forP.z += 100.0;
    }

    public void fromPointToGuf() {
        this.handle.fromPointToGuf();
        this.barrel.fromPointToGuf();
        this.notch.fromPointToGuf();
        this.aperture.fromPointToGuf();
    }

    public void mullMat(Matrix3D mat) {
        this.handle.mullMat(mat);
        this.barrel.mullMat(mat);
        this.notch.mullMat(mat);
        this.aperture.mullMat(mat);
        this.xyz.mullMat(mat);
    }

    public void convertAndShow(Graphics page) {
        this.handle.convertAndShow(page);
        this.barrel.convertAndShow(page);
        this.notch.convertAndShow(page);
        this.aperture.convertAndShow(page);
        page.setFont(this.playbill);
        page.setColor(this.ammoColor);
        page.drawString(String.valueOf(this.ammo) + "/" + this.clip, 5, 635);
    }

    public void addAmmo(int a) {
        this.ammo += a;
        if (this.clip + this.ammo > 60) {
            this.ammo = 60 - this.clip;
            this.isAmmoFull = true;
        }
    }

    public void shoot() {
        if (this.isCantShot) {
            return;
        }
        this.isCantShot = true;
        this.recoilState = 5;
        this.isCantShot = true;
        --this.clip;
        this.isAmmoFull = false;
    }

    private void load() {
        this.ammo += this.clip;
        if (this.ammo < 12) {
            this.clip = this.ammo;
            this.ammo = 0;
        } else {
            this.clip = 12;
            this.ammo -= 12;
        }
    }

    public void reload() {
        if (this.ammo == 0 || this.clip == 12 || this.isCantShot) {
            return;
        }
        this.isCantShot = true;
        this.reloadState = 17;
        this.isCantShot = true;
    }

    public void hit() {
        if (this.isCantShot) {
            return;
        }
        this.isCantShot = true;
        this.hitState = 11;
        this.isCantShot = true;
    }

    @Override
    public void run() {
        while (true) {
            if (this.clip == 0) {
                this.reload();
            }
            if (this.reloadState < 8 && this.reloadState > 0) {
                this.mati.setMatRotateXFix(0.09239978392911156, this.xyz.x, this.xyz.y, this.xyz.z);
                this.mullMat(this.mati);
            } else if (this.reloadState == 9) {
                new Sound(Sound.type.RELOAD);
                --this.reloadState;
                this.sleepy(1500);
                this.load();
            } else if (this.reloadState < 17 && this.reloadState > 9) {
                this.mati.setMatRotateXFix(-0.09239978392911156, this.xyz.x, this.xyz.y, this.xyz.z);
                this.mullMat(this.mati);
            }
            if (this.reloadState != 0) {
                --this.reloadState;
                if (this.reloadState == 0) {
                    this.isCantShot = false;
                }
            } else if (this.hitState < 5 && this.hitState > 0) {
                this.mati.setMatRotateYFix(-0.14279966607226333, this.backP.x, this.backP.y, this.backP.z);
                this.mullMat(this.mati);
                this.sleepy(30);
            } else if (this.hitState == 6) {
                --this.hitState;
                this.sleepy(30);
            } else if (this.hitState < 11 && this.hitState > 6) {
                this.mati.setMatRotateYFix(0.14279966607226333, this.backP.x, this.backP.y, this.backP.z);
                this.mullMat(this.mati);
                this.sleepy(30);
            }
            if (this.hitState != 0) {
                --this.hitState;
                if (this.hitState == 0) {
                    this.isCantShot = false;
                }
            } else if (this.recoilState < 2 && this.recoilState > 0) {
                this.mati.setMatRotateXFix(-0.015707963267948967, this.backP.x, this.backP.y, this.backP.z);
                this.mullMat(this.mati);
                this.sleepy(40);
            } else if (this.recoilState == 3) {
                --this.recoilState;
                this.sleepy(40);
            } else if (this.recoilState < 5 && this.recoilState > 3) {
                this.mati.setMatRotateXFix(0.015707963267948967, this.backP.x, this.backP.y, this.backP.z);
                this.mullMat(this.mati);
                this.sleepy(40);
            }
            if (this.recoilState != 0) {
                --this.recoilState;
                if (this.recoilState == 0) {
                    this.isCantShot = false;
                }
            }
            this.sleepy(20);
        }
    }

    private void sleepy(int delay) {
        try {
            Gun.sleep(delay);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
