import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;

public class MainPanel
extends JPanel
implements ActionListener {
    BufferedImage cursorImg = new BufferedImage(1, 1, 2);
    Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(this.cursorImg, new Point(0, 0), "blank cursor");
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Image cImg = this.toolkit.getImage("files/images/Cursor.PNG");
    Cursor defult = this.toolkit.createCustomCursor(this.cImg, new Point(5, 0), "defult");
    Image startImg;
    Font scoreFont;
    JButton newGame;
    JButton exit;
    JButton resume;
    JSlider sensitivity;
    static Point3D p00;
    static Point3D pMe;
    static Point3D pStart;
    Point3D magozPoint;
    Point myPoint;
    Point locationOnScreen = new Point(0, 0);
    Thread animation;
    Thread backSound;
    Random rand = new Random();
    Timer repaintTimer;
    Timer timer;
    Text text;
    Listener listener;
    Map map;
    Life life;
    Gun gun;
    Aim aim;
    Arena arena;
    Zombies zombies;
    Bullets bullets;
    Moon moon;
    ArrayList<Shape> shape;
    ArrayList<BodyPart> tempBodyParts;
    final int xSize = 20000;
    final int ySize = 200;
    final int zSize = 20000;
    final int zombSizeX = 300;
    final int zombSizeY = 500;
    final int zombSizeZ = 100;
    final int moveSpeed = 75;
    Matrix3D tempTurnX;
    Matrix3D tempTurnY;
    Matrix3D tempMove;
    Matrix3D mabatTurnX;
    Matrix3D mabatTurnY;
    Matrix3D mabatMove;
    Matrix3D matiZomb;
    Color wallColor = new Color(230, 230, 230);
    boolean isLookFor;
    double zivaReal;
    int ziva;
    double zombSpeed;
    boolean isUsingCheats = false;
    boolean isShowHits = false;
    boolean isOneShoot = false;
    boolean isShowMap = false;
    boolean isGameStarted;
    double rotateXMax = 1.5707963267948966;
    int spareBullets = 0;
    float seconds = 0.0f;
    int numOfSpawns = 0;
    int chanceOfNewZomb = 10;

    public MainPanel() {
        this.setBackground(new Color(0, 0, 5));
        this.biuldElem();
        this.listener = new Listener(this);
        this.setCursor(this.defult);
    }

    @Override
    public void paintComponent(Graphics page) {
        super.paintComponent(page);
        this.setSensitivity();
        if (this.isGameStarted) {
            this.makeShapes();
            this.convertAndShow(page);
        } else {
            page.drawImage(this.startImg, 0, 0, this);
            page.setColor(new Color(240, 240, 240));
            page.setFont(new Font(this.getName(), 0, 20));
            page.drawString("Sensitivity: " + this.listener.now, 510, 52);
            page.setColor(Color.orange);
            page.setFont(this.scoreFont);
            if (!this.isUsingCheats) {
                this.text.setHighScore();
            }
            page.drawString("High Score: " + this.text.getHighScore(), 15, 35);
            if (this.text.score > 0) {
                page.drawString("Your Score: " + this.text.score, 15, 65);
            }
        }
    }

    public void biuldElem() {
        try {
            this.startImg = ImageIO.read(new File("files/images/backGround.PNG"));
            this.scoreFont = Font.createFont(0, new FileInputStream("files/fonts/score.ttf")).deriveFont(0, 25.0f);
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.makeBottons();
        this.magozPoint = new Point3D(475.0, 325.0, 1000.0);
        p00 = new Point3D(0.0, 0.0, 1000.0);
        pMe = new Point3D(MainPanel.p00.x, MainPanel.p00.y, 0.0);
        pStart = new Point3D(MainPanel.p00.x - 10000.0, MainPanel.p00.y, MainPanel.pMe.z - 5000.0);
        this.myPoint = new Point();
        this.repaintTimer = new Timer(20, this);
        this.repaintTimer.start();
        this.timer = new Timer(1000, this);
        this.text = new Text(this.magozPoint);
        this.map = new Map(20000.0, 20000.0, 200.0, 200.0);
        this.life = new Life(this.magozPoint);
        this.gun = new Gun(this.magozPoint);
        this.aim = new Aim(this.magozPoint);
        this.aim.buildShape(MainPanel.pMe.x, MainPanel.pMe.y, MainPanel.pMe.z + 500.0, 25.0, this.gun);
        this.arena = new Arena(this.magozPoint, this.map);
        this.zombies = new Zombies(this);
        this.bullets = new Bullets(this.magozPoint, this.map);
        this.moon = new Moon(this.magozPoint);
        this.shape = new ArrayList();
        this.tempBodyParts = new ArrayList();
        this.tempTurnX = new Matrix3D();
        this.mabatTurnX = new Matrix3D();
        this.tempTurnY = new Matrix3D();
        this.mabatTurnY = new Matrix3D();
        this.tempMove = new Matrix3D();
        this.mabatMove = new Matrix3D();
        this.matiZomb = new Matrix3D();
        this.isGameStarted = false;
    }

    private void makeBottons() {
        this.newGame = new JButton("New Game");
        this.newGame.setFocusable(false);
        this.newGame.addActionListener(this);
        this.add(this.newGame);
        this.exit = new JButton("Exit");
        this.exit.setFocusable(false);
        this.exit.addActionListener(this);
        this.add(this.exit);
        this.resume = new JButton("Resume");
        this.resume.setFocusable(false);
        this.resume.addActionListener(this);
        this.add(this.resume);
        this.resume.setEnabled(false);
        this.sensitivity = new JSlider(500, 1500, 1000);
        this.sensitivity.setMajorTickSpacing(250);
        this.sensitivity.setPaintTicks(true);
        this.sensitivity.setSnapToTicks(true);
        this.sensitivity.setFocusable(false);
        this.add(this.sensitivity);
    }

    public void newGame() {
        this.setCursor(this.blankCursor);
        this.stopAnimaition();
        if (this.isGameStarted) {
            this.map.clearObject(this.myPoint.x, this.myPoint.y);
            this.timer.stop();
            this.life.restart();
        }
        this.myPoint.setLocation(10000, 5000);
        this.gun.makeNew();
        this.gun.buildShape(MainPanel.p00.x, MainPanel.p00.y, MainPanel.p00.z - 900.0);
        this.gun.fromPointToGuf();
        this.life.restart();
        this.text.score = 0;
        this.arena.restart();
        this.zombies.restart();
        this.bullets.restart();
        this.mabatTurnX.setIdentity();
        this.mabatTurnY.setIdentity();
        this.mabatMove.setIdentity();
        this.zivaReal = 0.0;
        this.isLookFor = true;
        this.setZiva(0.0);
        this.map.setObject(1, this.myPoint);
        this.seconds = 0.0f;
        this.numOfSpawns = 0;
        this.chanceOfNewZomb = 10;
        this.zombSpeed = 10.0;
        this.newGame.hide();
        this.exit.hide();
        this.resume.hide();
        this.resume.setEnabled(true);
        this.sensitivity.hide();
        this.isGameStarted = true;
        this.listener.game(true);
        this.text.stopShowingScore();
        this.timer.restart();
        this.startAnimaition();
    }

    public void pauseGame() {
        this.setCursor(this.defult);
        this.listener.game(false);
        this.newGame.show();
        this.exit.show();
        this.resume.show();
        this.sensitivity.show();
        this.pauseAnimaition();
        this.timer.stop();
        this.life.timer.stop();
    }

    public void resumeGame() {
        if (!this.isGameStarted) {
            return;
        }
        this.setBackground(new Color(0, 0, 5));
        this.setCursor(this.blankCursor);
        this.listener.game(true);
        this.newGame.hide();
        this.exit.hide();
        this.resume.hide();
        this.sensitivity.hide();
        this.timer.restart();
        this.life.timer.restart();
        this.resumeAnimation();
    }

    public void endGame() {
        this.isGameStarted = false;
        this.setCursor(this.defult);
        this.listener.game(false);
        this.newGame.show();
        this.exit.show();
        this.resume.show();
        this.resume.enable(false);
        this.sensitivity.show();
        this.timer.stop();
        this.life.timer.stop();
    }

    public void shoot() {
        if (this.gun.clip == 0 && this.gun.ammo == 0) {
            this.text.addText("No Ammo", Color.red);
            return;
        }
        if (this.gun.isCantShot) {
            return;
        }
        new Sound(Sound.type.GUN);
        this.gun.shoot();
        Point3D aS = pMe;
        Point3D bS = this.aim.xyz;
        int hitPoints = 0;
        this.shape.clear();
        this.shape.addAll(this.zombies.getZombies());
        this.shape.addAll(this.arena.getList());
        Collections.sort(this.shape);
        for (Shape s : this.shape) {
            if (s instanceof Zombie) {
                this.tempBodyParts.clear();
                this.tempBodyParts.addAll(((Zombie)s).parts);
                Collections.sort(this.tempBodyParts);
                for (BodyPart b : this.tempBodyParts) {
                    if (!b.isShoot(aS, bS)) continue;
                    if (b instanceof Head) {
                        this.text.addScore(50);
                        hitPoints = 100;
                    } else if (b instanceof Body) {
                        this.text.addScore(10);
                        hitPoints = 40;
                    } else if (b instanceof Hand || b instanceof Leg) {
                        this.text.addScore(5);
                        hitPoints = 30;
                    }
                    if (this.isOneShoot) {
                        hitPoints = 100;
                    }
                    if (((Zombie)s).hit(hitPoints)) {
                        this.killZomb((Zombie)s);
                        if (((Zombie)s).isBig) {
                            this.text.addScore(500);
                        } else {
                            this.text.addScore(100);
                        }
                    }
                    return;
                }
                continue;
            }
            if (!s.isShoot(aS, bS)) continue;
            if (this.isShowHits) {
                this.text.addText("wall", Color.red);
            }
            return;
        }
        if (this.isShowHits) {
            this.text.addText("missed", Color.red);
        }
    }

    public void hit() {
        if (this.gun.isCantShot) {
            return;
        }
        this.gun.hit();
        Zombie temp = this.zombies.hitZomb();
        if (temp == null) {
            return;
        }
        new Sound(Sound.type.GUN_PUNCH);
        this.text.addScore(50);
        int hitPoints = this.isOneShoot ? 100 : 50;
        if (temp.hit(hitPoints)) {
            this.killZomb(temp);
            if (temp.isBig) {
                this.text.addScore(500);
            } else {
                this.text.addScore(100);
            }
            this.text.addScore(100);
        }
    }

    public void play() {
        this.bullets.spin();
        this.listener.move();
        this.arena.openDoor();
        int i = 0;
        while (i < this.zombies.getNumOfZomb()) {
            if (this.zombies.isForring) continue;
            Zombie z = this.zombies.getZomb(i);
            this.turnZomb(z);
            if (this.isClose(z)) {
                this.hitMe(z);
            } else {
                z.handState = z.numOfHandStates;
                this.moveZomb(z);
                this.marchZomb(z);
            }
            ++i;
        }
    }

    public void moveZomb(Zombie zomb) {
        Point target = zomb.destination;
        Point xz = new Point(zomb.place.x - target.x, zomb.place.y - target.y);
        double temp = -Math.max(Math.abs(xz.x), Math.abs(xz.y));
        if (temp == 0.0) {
            xz.setLocation(0, 0);
        } else {
            xz.setLocation((double)xz.x / temp * this.zombSpeed, (double)xz.y / temp * this.zombSpeed);
        }
        this.map.clearObject(zomb.place.x, zomb.place.y);
        if (this.map.canZombieMove(zomb.place.x + xz.x, zomb.place.y + xz.y, zomb.id)) {
            zomb.place.x += xz.x;
            zomb.place.y += xz.y;
        } else if (this.map.canZombieMove(zomb.place.x + xz.x, zomb.place.y, zomb.id)) {
            zomb.place.x += xz.x;
        } else if (this.map.canZombieMove(zomb.place.x, zomb.place.y + xz.y, zomb.id)) {
            zomb.place.y += xz.y;
        }
        this.map.setObject(zomb.id, zomb.place);
    }

    public void turnZomb(Zombie zomb) {
        Point tempP = zomb.place;
        Point target = zomb.destination;
        zomb.look = tempP.y == target.y ? (tempP.x > target.x ? -1.5707963267948966 : 1.5707963267948966) : (tempP.y < target.y ? Math.PI - Math.atan((target.x - tempP.x) / (target.y - tempP.y)) : Math.atan((tempP.x - target.x) / (target.y - tempP.y)));
    }

    public void marchZomb(Zombie zomb) {
        if (zomb.legUpOrDown) {
            if (zomb.legsState == zomb.numOfLegStates * 2 - 1) {
                zomb.legUpOrDown = false;
            } else {
                ++zomb.legsState;
            }
        } else if (zomb.legsState == 0) {
            zomb.legUpOrDown = true;
        } else {
            --zomb.legsState;
        }
    }

    public void hitMe(Zombie zomb) {
        if (zomb.handUpOrDown) {
            if (zomb.handState == zomb.numOfHandStates * 2 - 1) {
                zomb.handUpOrDown = false;
            } else {
                ++zomb.handState;
            }
        } else if (zomb.handState == 0) {
            zomb.handUpOrDown = true;
        } else {
            --zomb.handState;
        }
        if (zomb.handState == zomb.numOfHandStates) {
            this.life.hit();
            new Sound(Sound.type.PUNCH);
        }
    }

    public boolean isClose(Zombie z) {
        return Math.sqrt(Math.pow(z.place.x - this.myPoint.x, 2.0) + Math.pow(z.place.y - this.myPoint.y, 2.0)) < 500.0;
    }

    public void takeAmmo() {
        if (this.gun.isAmmoFull) {
            return;
        }
        Bullet temp = null;
        for (Bullet b : this.bullets.getBullets()) {
            if (!(Math.sqrt(Math.pow(b.place.x - this.myPoint.x, 2.0) + Math.pow(b.place.y - this.myPoint.y, 2.0)) < 750.0)) continue;
            new Sound(Sound.type.AMMO);
            this.gun.addAmmo(b.ammo);
            temp = b;
        }
        this.bullets.removeBullet(temp);
    }

    @Override
    public void move(int dx, int dz) {
        if (this.map.isEmpty(this.myPoint.x - dx, this.myPoint.y - dz, 1)) {
            this.map.clearObject(this.myPoint.x, this.myPoint.y);
            this.myPoint.x -= dx;
            this.myPoint.y -= dz;
            this.map.setObject(1, this.myPoint);
            this.tempMove.setMatMove(dx, 0.0, dz);
            this.mabatMove.mullMatMat(this.tempMove);
        } else if (this.map.isEmpty(this.myPoint.x, this.myPoint.y - dz, 1)) {
            this.map.clearObject(this.myPoint.x, this.myPoint.y);
            this.myPoint.y -= dz;
            this.map.setObject(1, this.myPoint);
            this.tempMove.setMatMove(0.0, 0.0, dz);
            this.mabatMove.mullMatMat(this.tempMove);
        } else if (this.map.isEmpty(this.myPoint.x - dx, this.myPoint.y, 1)) {
            this.map.clearObject(this.myPoint.x, this.myPoint.y);
            this.myPoint.x -= dx;
            this.map.setObject(1, this.myPoint);
            this.tempMove.setMatMove(dx, 0.0, 0.0);
            this.mabatMove.mullMatMat(this.tempMove);
        }
    }

    public void moveForward() {
        if (this.isLookFor) {
            this.move(this.ziva, -75 + Math.abs(this.ziva));
        } else {
            this.move(-this.ziva, 75 - Math.abs(this.ziva));
        }
    }

    public void moveBackward() {
        if (this.isLookFor) {
            this.move(-this.ziva, 75 - Math.abs(this.ziva));
        } else {
            this.move(this.ziva, -75 + Math.abs(this.ziva));
        }
    }

    public void moveRight() {
        if (this.isLookFor) {
            this.move(75 - Math.abs(this.ziva), this.ziva);
        } else {
            this.move(-75 + Math.abs(this.ziva), -this.ziva);
        }
    }

    public void moveLeft() {
        if (this.isLookFor) {
            this.move(-75 + Math.abs(this.ziva), -this.ziva);
        } else {
            this.move(75 - Math.abs(this.ziva), this.ziva);
        }
    }

    public void rotateXFix(double angel, double xReal, double yReal, double zReal) {
        if (this.rotateXMax + angel > Math.PI || this.rotateXMax + angel < 0.0) {
            return;
        }
        this.tempTurnX.setMatRotateXFix(angel, xReal, yReal, zReal);
        this.rotateXMax += angel;
        this.mabatTurnX.mullMatMat(this.tempTurnX);
    }

    public void rotateFixXUp() {
        this.rotateXFix(0.006283185307179587, MainPanel.pMe.x, MainPanel.pMe.y, MainPanel.pMe.z);
    }

    public void rotateFixXDown() {
        this.rotateXFix(-0.006283185307179587, MainPanel.pMe.x, MainPanel.pMe.y, MainPanel.pMe.z);
    }

    public void rotateYFix(double angel, double xReal, double yReal, double zReal) {
        this.tempTurnY.setMatRotateYFix(angel, xReal, yReal, zReal);
        this.setZiva(angel);
        this.mabatTurnY.mullMatMat(this.tempTurnY);
    }

    public void setZiva(double angel) {
        double PI = Math.PI;
        this.zivaReal += angel;
        double zivaTemp = this.zivaReal;
        while (zivaTemp < -1.5707963267948966) {
            zivaTemp += Math.PI;
        }
        while (zivaTemp > 1.5707963267948966) {
            zivaTemp -= Math.PI;
        }
        this.ziva = (int)(-zivaTemp / 1.5707963267948966 * 75.0);
        int temp = (int)(this.zivaReal / Math.PI * 4.0);
        temp -= 2;
        this.isLookFor = (temp /= 4) % 2 == 0;
        if (this.zivaReal > 1.5707963267948966) {
            this.isLookFor = !this.isLookFor;
        }
    }

    public void rotateFixYLeft() {
        this.rotateYFix(-0.031415926535897934, MainPanel.pMe.x, MainPanel.pMe.y, MainPanel.pMe.z);
    }

    public void rotateFixYRight() {
        this.rotateYFix(0.031415926535897934, MainPanel.pMe.x, MainPanel.pMe.y, MainPanel.pMe.z);
    }

    public void rotateZFix(double angel, double xReal, double yReal, double zReal) {
        this.tempTurnX.setMatRotateZFix(angel, xReal, yReal, zReal);
        this.mabatTurnX.mullMatMat(this.tempTurnX);
    }

    public void rotateFixZFor() {
        this.rotateZFix(0.031415926535897934, MainPanel.pMe.x, MainPanel.pMe.y + 5000.0, MainPanel.pMe.z);
    }

    public void rotateFixZBack() {
        this.rotateZFix(-0.031415926535897934, MainPanel.pMe.x, MainPanel.pMe.y + 600.0, MainPanel.pMe.z);
    }

    public void startAnimaition() {
        if (this.animation == null) {
            this.animation = new Animation(this);
        }
        if (this.backSound == null) {
            this.backSound = new BackSound(this);
        }
    }

    public void stopAnimaition() {
        if (this.animation != null) {
            this.animation.stop();
        }
        this.animation = null;
        if (this.backSound != null) {
            this.backSound.stop();
        }
        this.backSound = null;
    }

    public void pauseAnimaition() {
        if (this.animation != null) {
            this.animation.suspend();
        }
        if (this.backSound != null) {
            this.backSound.suspend();
        }
    }

    public void resumeAnimation() {
        if (this.animation != null) {
            this.animation.resume();
        }
        if (this.backSound != null) {
            this.backSound.resume();
        }
    }

    public void convertAndShow(Graphics page) {
        this.moon.convertAndShow(page);
        this.arena.convertAndShow(page);
        this.shape.clear();
        this.shape.addAll(this.zombies.getAllZombies());
        this.shape.addAll(this.arena.getList());
        this.shape.addAll(this.bullets.getBullets());
        Collections.sort(this.shape);
        Collections.reverse(this.shape);
        for (Shape s : this.shape) {
            s.convertAndShow(page);
        }
        this.gun.convertAndShow(page);
        this.aim.convertAndShow(page);
        this.text.show(page);
        this.life.convertAndShow(page);
        this.map.printMap(page);
    }

    public void makeShapes() {
        this.takeAmmo();
        this.buildShapes();
        for (Zombie z : this.zombies.getZombies()) {
            z.marchZomb();
            z.moveHands();
            z.turnZomb();
        }
        for (Zombie z : this.zombies.getDeadZombies()) {
            z.fallBack();
            z.turnZomb();
        }
        this.mullMat(this.mabatMove);
        this.mullMat(this.mabatTurnY);
        this.mullMat(this.mabatTurnX);
        this.fromPointToGuf();
    }

    public void buildShapes() {
        this.arena.buildShape(20000.0, 200.0, 20000.0);
        this.zombies.buildShape();
        this.bullets.buildShape();
        this.moon.buildShape(3000.0, -150000.0, 500000.0, 15000.0);
    }

    public void mullMat(Matrix3D mat) {
        this.arena.mullMat(mat);
        this.zombies.mullMat(mat);
        this.bullets.mullMat(mat);
        this.moon.mullMat(mat);
    }

    public void fromPointToGuf() {
        this.arena.fromPointToGuf();
        this.zombies.fromPointToGuf();
        this.bullets.fromPointToGuf();
        this.gun.fromPointToGuf();
    }

    public void killZomb(Zombie z) {
        this.zombies.killZomb(z);
        if (z.isBig && z.destinationIndex > 2) {
            this.bullets.addBullet(z.place, 15);
        } else {
            if (this.rand.nextInt(5) == 0) {
                ++this.spareBullets;
            }
            if (this.spareBullets > 0 && z.destinationIndex > 2) {
                this.bullets.addBullet(z.place, 10);
                --this.spareBullets;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.timer) {
            this.seconds += 1.0f;
            if (this.seconds == 1.0f) {
                this.text.addBigText("North door opened");
                new Sound(Sound.type.SIRAN);
                this.openDoors(0);
            } else if (this.seconds == 30.0f) {
                this.text.addBigText("East door opened");
                new Sound(Sound.type.SIRAN);
                this.openDoors(1);
            } else if (this.seconds == 60.0f) {
                this.text.addBigText("South door opened");
                new Sound(Sound.type.SIRAN);
                this.openDoors(2);
            } else if (this.seconds == 120.0f) {
                this.text.addBigText("West door opened");
                new Sound(Sound.type.SIRAN);
                this.openDoors(3);
            } else if (this.seconds % 100.0f == 0.0f) {
                this.text.addBigText("Zombie Speed Increased");
                new Sound(Sound.type.SIRAN);
                this.zombSpeed += 3.0;
            }
            if (this.seconds * 2.0f % (float)this.chanceOfNewZomb == 0.0f && (this.seconds < 62.0f || this.seconds > 82.0f)) {
                this.zombies.addZombie(this.rand.nextInt(this.numOfSpawns));
            }
            if (this.seconds % 60.0f == 0.0f && this.chanceOfNewZomb > 1) {
                --this.chanceOfNewZomb;
            } else if (this.seconds % 70.0f == 0.0f) {
                this.text.addBigText("A Huge Zombie Is Coming");
                new Sound(Sound.type.SIRAN);
                this.zombies.addBigZombie();
            }
        } else if (e.getSource() == this.repaintTimer) {
            this.repaint();
        } else if (e.getSource() == this.exit) {
            System.exit(0);
        } else if (e.getSource() == this.newGame) {
            this.newGame();
        } else if (e.getSource() == this.resume) {
            this.resumeGame();
        }
    }

    public void openDoors(int index) {
        this.arena.sides.get(index).openDoors();
        this.numOfSpawns += 2;
        this.chanceOfNewZomb -= 2;
    }

    public void setSensitivity() {
        this.listener.setSensitivity(this.sensitivity.getValue());
    }

    public void showMap() {
        if (this.isShowMap) {
            this.map.showMap();
        }
    }

    public void cheat() {
        String code = JOptionPane.showInputDialog(null, null, null, 3);
        if (code == null) {
            return;
        }
        if (code.toLowerCase().compareTo("reset score") == 0) {
            this.text.restartScore();
        } else if (code.toLowerCase().compareTo("show hits") == 0) {
            boolean bl = this.isShowHits = !this.isShowHits;
            if (this.isShowHits) {
                this.text.addText("show hits", Color.white);
            } else {
                this.text.addText("hide hits", Color.white);
            }
        } else if (code.toLowerCase().compareTo("add ammo") == 0) {
            this.gun.addAmmo(20);
            this.text.addText("+20 bullets", Color.white);
            if (!this.isUsingCheats) {
                this.text.addText("Highscore is disabled!", Color.white);
                this.isUsingCheats = true;
            }
        } else if (code.toLowerCase().compareTo("one shoot") == 0) {
            boolean bl = this.isOneShoot = !this.isOneShoot;
            if (this.isOneShoot) {
                this.text.addText("One Shoot on!", Color.white);
            } else {
                this.text.addText("One Shoot off!", Color.white);
            }
            if (!this.isUsingCheats) {
                this.text.addText("Highscore is disabled!", Color.white);
                this.isUsingCheats = true;
            }
        } else if (code.toLowerCase().compareTo("die") == 0) {
            this.endGame();
        } else if (code.toLowerCase().compareTo("show map") == 0) {
            boolean bl = this.isShowMap = !this.isShowMap;
            if (this.isShowMap) {
                this.text.addText("Map is showable!", Color.white);
            } else {
                this.text.addText("Map isn't showable!", Color.white);
            }
        }
    }
}
