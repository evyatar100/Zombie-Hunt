
import java.awt.Color;
import java.util.Random;
import java.util.Timer;

public class BackSound extends Thread
{
	MainPanel myPanel;
	Random rand = new Random();
	Timer tmr;
	Color lightning = new Color(255, 255, 255);

	public BackSound(MainPanel myPanel)
	{
		this.myPanel=myPanel;
		setDaemon(true);
		start();
	}

	public void run()
	{
		for (;;)
		{
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

