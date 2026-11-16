import java.util.Random;

public class Animation
extends Thread {
    MainPanel myPanel;
    Random rand = new Random();
    BackSound backSound;

    public Animation(MainPanel myPanel) {
        this.myPanel = myPanel;
        this.setDaemon(true);
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            this.myPanel.play();
            if (!this.myPanel.life.isAlive) {
                this.myPanel.gun.isCantShot = true;
                int i = 0;
                while (i < 48) {
                    this.myPanel.rotateFixZBack();
                    this.sleepy(25);
                    ++i;
                }
                this.sleepy(1500);
                this.myPanel.isGameStarted = false;
                this.myPanel.endGame();
            }
            this.sleepy(15);
        }
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
