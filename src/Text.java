
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.InputStream;


public class Text extends Thread
{
	int score;
	
	Integer highScore = 0;
	
	String str1, str2, bigText, scoreStr;
	int delay1=0, delay2=0, delay3=0;;
	Color c1, c2, Bc;

	int tempScore;
	boolean isSrt1Score, showScore = false;

	Point3D magozPoint;

	Font bigTextFont, scoreFont;
	public Text(Point3D magozPoint) 
	{
		score = 0;
		this.magozPoint=magozPoint;
		str1=null;
		str2=null;
		bigText=null;
		
		Bc = Color.orange;
		
		bigTextFont = new Font(Font.SANS_SERIF, Font.BOLD, 40);
		scoreFont = new Font(Font.SANS_SERIF, Font.PLAIN, 50);
		try (InputStream font = Resources.open("fonts/door.TTF")) {
			bigTextFont = Font.createFont(Font.TRUETYPE_FONT, font).deriveFont(Font.PLAIN, 40);
		} catch (Exception e) {System.err.println("Door font not found: " + e);}
		try (InputStream font = Resources.open("fonts/Playbill.ttf")) {
			scoreFont = Font.createFont(Font.TRUETYPE_FONT, font).deriveFont(Font.PLAIN, 50);
		} catch (Exception e) {System.err.println("Score font not found: " + e);}
		
		loadScoreFile();
		start();
	}
	public void run()
	{
		for(;;)
		{
			sleepy(100);
			delay1-=100;
			delay2-=100;
			delay3-=100;
		}
	}
	public void addText(String text, Color color)
	{
		str2=str1;
		delay2=delay1;
		c2=c1;

		str1=text;
		delay1=2000;
		c1=color;

		isSrt1Score=false;
	}
	public void addScore(int a)
	{
		score+=a;
		if(isSrt1Score)
		{
			tempScore+=a;
			str1 = "+"+tempScore;
			delay1=2000;
			c1=Color.orange;
		}
		else
		{
			str2=str1;
			delay2=delay1;
			c2=c1;

			tempScore=a;
			str1 = "+"+tempScore;
			delay1=2000;
			c1=Color.orange;
		}
		isSrt1Score=true;
	}
	public void addBigText(String text)
	{
		bigText=text;
		delay3=5000;
	}
	public void showScore(int score)
	{
		this.scoreStr = ""+score;
		showScore = true;
	}
	public void stopShowingScore()
	{
		showScore = false;
	}
	public void show(Graphics page)
	{
		if(delay1>0)
		{
			page.setFont(new Font("Arial", 0, 15));
			page.setColor(c1);
			page.drawString(str1, (int) magozPoint.x, (int) (magozPoint.y-125));
		}
		else
			tempScore=0;
		if(delay2>0)
		{
			page.setFont(new Font("Arial", 0, 15));
			page.setColor(c2);
			page.drawString(str2, (int) magozPoint.x, (int) (magozPoint.y-140));
		}
		if(delay3>0)
		{
			page.setFont(bigTextFont);
			page.setColor(Bc);
			page.drawString(bigText, (int) magozPoint.x-150, (int) (magozPoint.y-200));
		}
		if(showScore)
		{
			page.setFont(new Font("Arial", 0, 40));
			page.setColor(Bc);
			page.drawString("Score: "+scoreStr+".", (int) magozPoint.x-150, (int) (magozPoint.y));
		}
		page.setFont(scoreFont);
		page.setColor(Color.orange);
		page.drawString(score+"", 5, 35);
	}
	public void setHighScore()
	{
		if(score>highScore)
		{
			highScore=score;
			updateScoreFile();
		}
	}
	public int getHighScore()
	{
		return highScore;
	}
	public void restartScore()
	{
		highScore = 0;
		updateScoreFile();
	}
	public void loadScoreFile() 
	{
		highScore = HighScoreStore.load();
	}
	public void updateScoreFile() 
	{
		HighScoreStore.save(highScore);
	}
	
	private void sleepy(int wait)
	{
		try 
		{
			sleep(wait);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}

}
