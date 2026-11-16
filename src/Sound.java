import java.applet.Applet;
import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class Sound
extends Thread {
    AudioClip clip;
    String URL;
    int clipLength;
    int delay = 0;
    Random rand = new Random();

    public Sound(type t) {
        switch (t) {
            case GUN: {
                this.URL = "file:files/sounds/GunFire.wav";
                this.clipLength = 2000;
                break;
            }
            case THUNDER: {
                this.URL = "file:files/sounds/Thunder.wav";
                this.clipLength = 3000;
                break;
            }
            case PUNCH: {
                this.URL = "file:files/sounds/Punch.wav";
                this.clipLength = 1000;
                break;
            }
            case RELOAD: {
                this.URL = "file:files/sounds/Reload.wav";
                this.clipLength = 1000;
                break;
            }
            case AMMO: {
                this.URL = "file:files/sounds/Ammo.wav";
                this.clipLength = 1000;
                break;
            }
            case SIRAN: {
                this.URL = "file:files/sounds/Siran.wav";
                this.clipLength = 1000;
                break;
            }
            case ZOMBIE: {
                int r2 = this.rand.nextInt(7);
                this.URL = "file:files/sounds/Back_Sounds/Sound" + r2 + ".wav";
                this.clipLength = 3000;
                break;
            }
            case GUN_PUNCH: {
                this.URL = "file:files/sounds/GunPunch.wav";
                this.clipLength = 500;
            }
        }
        try {
            this.clip = Applet.newAudioClip(new URL(this.URL));
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.start();
    }

    @Override
    public void run() {
        try {
            Sound.sleep(this.delay);
            this.clip.play();
            Sound.sleep(this.clipLength);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    static enum type {
        GUN,
        THUNDER,
        PUNCH,
        RELOAD,
        AMMO,
        ZOMBIE,
        GUN_PUNCH,
        SIRAN;

    }
}
