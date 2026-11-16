
import java.awt.Color;
import java.util.Random;
import java.util.Timer;

public class BackSound extends Thread
{
	MainPanel myPanel;
	Random rand = new Random();
	Timer tmr;
	Color lightning = new Color(255, 255, 255);
	volatile boolean running = true;
	volatile boolean paused = false;

	public BackSound(MainPanel myPanel)
	{
		this.myPanel=myPanel;
		setDaemon(true);
		start();
	}

	public void run()
	{
		while (running)
		{
			if (paused)
			{
				sleepy(200);
				continue;
			}
			switch(rand.nextInt(20))	
			{
			case 0:
				new Sound(Sound.type.THUNDER);
				myPanel.setBackground(lightning);
				sleepy(100);
				
				myPanel.setBackground(new Color(0,0,5));
				sleepy(100);
				
				myPanel.setBackground(lightning);
				sleepy(100);
				
				myPanel.setBackground(new Color(0,0,5));
				break;
			case 1:
				new Sound(Sound.type.ZOMBIE);
				break;
			case 2:
				new Sound(Sound.type.ZOMBIE);
				break;
				
			default:
				break;
			}
			sleepy(1000);
		}
	}

	public void halt()
	{
		running = false;
	}

	public void pauseLoop()
	{
		paused = true;
	}

	public void resumeLoop()
	{
		paused = false;
	}

	private void sleepy(int delay)
	{
		try 
		{
			sleep(delay);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}

	}

}

