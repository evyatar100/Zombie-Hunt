import java.util.Random;

public class Animation extends Thread
{
	MainPanel myPanel;
	Random rand = new Random();
	BackSound backSound;
	volatile boolean running = true;
	volatile boolean paused = false;

	public Animation(MainPanel myPanel)
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
				sleepy(15);
				continue;
			}
			myPanel.play();
			if (!running)
				break;
			if(!myPanel.life.isAlive)
			{
				myPanel.gun.isCantShot=true;
				for (int i = 0; i < 48 && running; i++) 
				{
					myPanel.rotateFixZBack();
					sleepy(25);
				}
				if (!running)
					break;
				sleepy(1500);
				if (!running)
					break;
				myPanel.isGameStarted=false;
				myPanel.endGame();
			}
			sleepy(15);
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

