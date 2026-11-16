import java.util.Random;

public class Animation extends Thread
{
	MainPanel myPanel;
	Random rand = new Random();
	BackSound backSound;

	public Animation(MainPanel myPanel)
	{
		this.myPanel=myPanel;
		setDaemon(true);
		start();
	}
	

	public void run()
	{
		for (;;)
		{
			myPanel.play();
			if(!myPanel.life.isAlive)
			{
				myPanel.gun.isCantShot=true;
				for (int i = 0; i < 48; i++) 
				{
					myPanel.rotateFixZBack();
					sleepy(25);
				}
				sleepy(1500);
				myPanel.isGameStarted=false;
				myPanel.endGame();
			}
			sleepy(15);
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

