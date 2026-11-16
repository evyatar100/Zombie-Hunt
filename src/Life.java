import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;


public class Life implements ActionListener
{
	double xReal[];
	double yReal[];

	int xInt[];
	int yInt[];
	Color c;

	int myLife;
	boolean isAlive;

	Timer timer;
	
	int numOfPoint = 4;

	public Life (Point3D magozPoint)
	{
		myLife = 0;
		isAlive=true;
		timer = new Timer(3000,this);

		c = new Color(255, 0, 0, 0);
		
		xReal= new double[numOfPoint];
		yReal= new double[numOfPoint];
		xInt = new int [numOfPoint];
		yInt = new int [numOfPoint];

		xReal[0] = 0;					yReal[0] = 0;
		xReal[1] = magozPoint.x*2;		yReal[1] = 0;
		xReal[2] = magozPoint.x*2;		yReal[2] = magozPoint.y*2;
		xReal[3] = 0;					yReal[3] = magozPoint.y*2;
	}
	
	public void restart()
	{
		isAlive=true;
		myLife = 0;
		setTransparency();
		
	}
	public void convert(double xr[],double yr[],int xi[],int yi[],int aNum)
	{
		for(int i=0;i<aNum;i++)
		{
			xi[i]=(int)xr[i];
			yi[i]=(int)yr[i];
		}
	}
	public void convertAndShow (Graphics page)
	{
		convert(xReal, yReal, xInt, yInt, numOfPoint);
		page.setColor(c);
		page.fillPolygon(xInt, yInt, numOfPoint);
	}
	public void setTransparency()
	{
		c = new Color(255, 0, 0,myLife);
	}

	public void hit()
	{
		myLife+=20;
		if(myLife>=150)
			isAlive=false;
		else
			setTransparency();
		timer.restart();
	}
	public void actionPerformed(ActionEvent e) 
	{
		myLife-=50;
		if(myLife<0) myLife=0;
		setTransparency();
	}
}
