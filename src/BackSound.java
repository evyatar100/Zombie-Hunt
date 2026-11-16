import java.awt.Color;
import java.util.Random;
import java.util.Timer;

public class BackSound
extends Thread {
    MainPanel myPanel;
    Random rand = new Random();
    Timer tmr;
    Color lightning = new Color(255, 255, 255);
    volatile boolean running = true;
    volatile boolean paused = false;

    public BackSound(MainPanel myPanel) {
        this.myPanel = myPanel;
        this.setDaemon(true);
        this.start();
    }

    @Override
    public void run() {
        while (this.running) {
            if (this.paused) {
                this.sleepy(200);
                continue;
            }
            switch (this.rand.nextInt(20)) {
                case 0: {
                    new Sound(Sound.type.THUNDER);
                    this.myPanel.setBackground(this.lightning);
                    this.sleepy(100);
                    this.myPanel.setBackground(new Color(0, 0, 5));
                    this.sleepy(100);
                    this.myPanel.setBackground(this.lightning);
                    this.sleepy(100);
                    this.myPanel.setBackground(new Color(0, 0, 5));
                    break;
                }
                case 1: {
                    new Sound(Sound.type.ZOMBIE);
                    break;
                }
                case 2: {
                    new Sound(Sound.type.ZOMBIE);
                    break;
                }
            }
            this.sleepy(1000);
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
            BackSound.sleep(delay);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
