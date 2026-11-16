import java.util.Random;

public class Animation
extends Thread {
    MainPanel myPanel;
    Random rand = new Random();
    BackSound backSound;
    volatile boolean running = true;
    volatile boolean paused = false;

    public Animation(MainPanel myPanel) {
        this.myPanel = myPanel;
        this.setDaemon(true);
        this.start();
    }

    @Override
    public void run() {
        while (this.running) {
            if (this.paused) {
                this.sleepy(15);
                continue;
            }
            this.myPanel.play();
            if (!this.running) {
                break;
            }
            if (!this.myPanel.life.isAlive) {
                this.myPanel.gun.isCantShot = true;
                int i = 0;
                while (i < 48 && this.running) {
                    this.myPanel.rotateFixZBack();
                    this.sleepy(25);
                    ++i;
                }
                if (!this.running) {
                    break;
                }
                this.sleepy(1500);
                if (!this.running) {
                    break;
                }
                this.myPanel.isGameStarted = false;
                this.myPanel.endGame();
            }
            this.sleepy(15);
        }
    }

    public void halt() {
        this.running = false;
    }

    public void pauseLoop() {
        this.paused = true;
    }

    public void resumeLoop() {
        this.paused = false;
    }

    private void sleepy(int delay) {
        try {
            Animation.sleep(delay);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
