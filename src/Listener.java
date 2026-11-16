import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

public class Listener
extends JPanel
implements MouseMotionListener,
MouseListener,
KeyListener {
    boolean gameOn;
    MainPanel mp;
    Robot robot;
    int sensitivity;
    String now;
    final String[] arr = new String[]{"Very High", "High", "Normal", "Low", "Very Low"};
    boolean[] moveState = new boolean[4];
    boolean isMove = true;

    public Listener(MainPanel mp) {
        this.mp = mp;
        try {
            this.robot = new Robot();
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.resetMouse();
        mp.addMouseMotionListener(this);
        mp.addMouseListener(this);
        mp.addKeyListener(this);
        mp.setFocusable(true);
        int i = 0;
        while (i < 4) {
            this.moveState[i] = false;
            ++i;
        }
        this.gameOn = false;
    }

    public void setSensitivity(int x) {
        this.sensitivity = x;
        this.now = this.arr[x / 250 - 2];
    }

    public void resetMouse() {
        this.robot.mouseMove(this.mp.locationOnScreen.x + (int)this.mp.magozPoint.x, this.mp.locationOnScreen.y + (int)this.mp.magozPoint.y);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!this.gameOn) {
            switch (e.getKeyCode()) {
                case 27: {
                    this.mp.resumeGame();
                    return;
                }
                case 10: {
                    this.mp.newGame();
                    return;
                }
                case 113: {
                    this.mp.cheat();
                    return;
                }
            }
            return;
        }
        switch (e.getKeyCode()) {
            case 27: {
                this.mp.pauseGame();
                return;
            }
        }
        if (!this.mp.life.isAlive) {
            return;
        }
        switch (e.getKeyChar()) {
            case 's': {
                if (!this.moveState[1]) {
                    this.moveState[0] = true;
                }
                return;
            }
            case 'w': {
                if (!this.moveState[0]) {
                    this.moveState[1] = true;
                }
                return;
            }
            case 'd': {
                if (!this.moveState[3]) {
                    this.moveState[2] = true;
                }
                return;
            }
            case 'a': {
                if (!this.moveState[2]) {
                    this.moveState[3] = true;
                }
                return;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!this.gameOn) {
            return;
        }
        switch (e.getKeyChar()) {
            case 's': {
                this.moveState[0] = false;
                break;
            }
            case 'w': {
                this.moveState[1] = false;
                break;
            }
            case 'd': {
                this.moveState[2] = false;
                break;
            }
            case 'a': {
                this.moveState[3] = false;
                break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (!this.gameOn || !this.mp.life.isAlive) {
            return;
        }
        switch (e.getKeyChar()) {
            case 'r': {
                this.mp.gun.reload();
                return;
            }
            case 'm': {
                this.mp.showMap();
                return;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!this.gameOn) {
            return;
        }
        if (!this.isMove) {
            return;
        }
        this.mp.rotateYFix(Math.PI / (double)this.sensitivity * (double)(e.getXOnScreen() - (this.mp.locationOnScreen.x + (int)this.mp.magozPoint.x)), MainPanel.pMe.x, MainPanel.pMe.y, MainPanel.pMe.z);
        this.mp.rotateXFix(Math.PI / (double)(this.sensitivity * 2) * (double)(e.getYOnScreen() - (this.mp.locationOnScreen.y + (int)this.mp.magozPoint.y)), MainPanel.pMe.x, MainPanel.pMe.y, MainPanel.pMe.z);
        this.isMove = false;
        this.resetMouse();
        this.isMove = true;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!this.gameOn) {
            return;
        }
        switch (e.getButton()) {
            case 1: {
                this.mp.shoot();
                break;
            }
            case 2: {
                this.mp.shoot();
                break;
            }
            case 3: {
                this.mp.hit();
                break;
            }
        }
    }

    public void move() {
        if (!this.mp.life.isAlive || !this.gameOn) {
            int i = 0;
            while (i < this.moveState.length) {
                this.moveState[i] = false;
                ++i;
            }
            return;
        }
        if (this.moveState[0]) {
            this.mp.moveBackward();
        }
        if (this.moveState[1]) {
            this.mp.moveForward();
        }
        if (this.moveState[2]) {
            this.mp.moveLeft();
        }
        if (this.moveState[3]) {
            this.mp.moveRight();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (!this.gameOn) {
            return;
        }
        this.isMove = false;
        this.resetMouse();
        this.isMove = true;
    }

    public void game(boolean a) {
        if (a) {
            this.gameOn = true;
            this.isMove = false;
            this.resetMouse();
            this.isMove = true;
        } else {
            this.gameOn = false;
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }
}
