import java.applet.Applet;
import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;


public class Sound extends Thread
{
	AudioClip clip;
	String URL;
	int clipLength;
	int delay;
	
	Random rand = new Random();
		
	enum type {GUN, THUNDER, PUNCH, RELOAD, AMMO, ZOMBIE, GUN_PUNCH, SIRAN};
	public Sound(type t) 
	{
		delay=0;
		switch (t)
		{
		case GUN:
			URL="file:files/sounds/GunFire.wav";
			clipLength=2000;
			break;
		case THUNDER:
			URL="file:files/sounds/Thunder.wav";
			clipLength=3000;
			break;
		case PUNCH:
			URL="file:files/sounds/Punch.wav";
			clipLength=1000;
			break;
		case RELOAD:
			URL="file:files/sounds/Reload.wav";
			clipLength=1000;
			break;
		case AMMO:
			URL="file:files/sounds/Ammo.wav";
			clipLength=1000;
			break;
			
		case SIRAN:
			URL="file:files/sounds/Siran.wav";
			clipLength=1000;
			break;
			
		case ZOMBIE:
			int r2 = rand.nextInt(7);
			URL="file:files/sounds/Back_Sounds/Sound"+r2+".wav";
			clipLength=3000;
			break;
			
		case GUN_PUNCH:
			URL="file:files/sounds/GunPunch.wav";
			clipLength=500;
			break;
		}
		try 
		{
			clip = Applet.newAudioClip(new URL(URL));
		}
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
		start();
	}
	public void run()
	{
		
		try 
		{
			sleep(delay);
			clip.play();
			sleep(clipLength);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
