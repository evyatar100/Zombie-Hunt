import java.awt.Color;
import java.awt.Graphics;


public class Brick 
{

	double[] xReal;
	double[] yReal;
	double[] zReal;

	int[] xInt;
	int[] yInt;

	Point3D [] platot;

	Color color;

	Point3D magozPoint;

	double maxSize;


	private int numOfPoint=4;

	public Brick (Point3D magozPoint) 
	{
		this.magozPoint=magozPoint;
		xReal=new double[numOfPoint];
		yReal=new double[numOfPoint];
		zReal=new double[numOfPoint];

		xInt=new int[numOfPoint];
		yInt=new int[numOfPoint];

		platot = new Point3D[4];
		for (int i = 0; i < 4; i++)
		{
			platot[i] = new Point3D();
		}
	}


	public void buildShape(double x,double y,double z, double dx, double dy, int state, Color color) 
	{

		if(state == 0)// floor
		{
			xReal[0]=x;     	yReal[0]=y;     zReal[0]=z;  
			xReal[1]=x;   		yReal[1]=y;     zReal[1]=z+dy;
			xReal[2]=x+dx; 		yReal[2]=y;  	zReal[2]=z+dy;
			xReal[3]=x+dx;  	yReal[3]=y;  	zReal[3]=z;
		}
		else if (state == 1)// forward
		{
			xReal[0]=x;     	yReal[0]=y;     	zReal[0]=z;  
			xReal[1]=x+dx;   	yReal[1]=y;    	  	zReal[1]=z;
			xReal[2]=x+dx; 		yReal[2]=y+dy;  	zReal[2]=z;
			xReal[3]=x;  		yReal[3]=y+dy;  	zReal[3]=z;
		}
		else if (state == 2)// left
		{
			xReal[0]=x;     	yReal[0]=y;     	zReal[0]=z;  
			xReal[1]=x;   		yReal[1]=y;    	  	zReal[1]=z+dx;
			xReal[2]=x; 		yReal[2]=y+dy;  	zReal[2]=z+dx;
			xReal[3]=x;  		yReal[3]=y+dy;  	zReal[3]=z;
		}
		this.color=color;
	}

	public void mullMat(Matrix3D mat)
	{
		mat.mullAllPoints(xReal, yReal, zReal, numOfPoint);
		
		platot[0].setXYZ(xReal[0], yReal[0], zReal[0]);
		platot[1].setXYZ(xReal[1], yReal[1], zReal[1]);
		platot[2].setXYZ(xReal[2], yReal[2], zReal[2]);
		platot[3].setXYZ(xReal[3], yReal[3], zReal[3]);
	}

	public void convert(double xr[],double yr[],double zr[],int xi[],int yi[],int aNum)
	{
		for (int i = 0; i < aNum; i++)
		{
			xi[i]=(int)(xr[i]*(magozPoint.z/(magozPoint.z+zr[i]))+magozPoint.x);
			yi[i]=(int)(yr[i]*(magozPoint.z/(magozPoint.z+zr[i]))+magozPoint.y);
		}

	}

	public void convertAndShow(Graphics page, boolean isLines)
	{

		convert(xReal, yReal, zReal, xInt, yInt, numOfPoint);

		if (Check(xInt[0], yInt[0], xInt[1], yInt[1],xInt[2], yInt[2])&& getCloseZ()>-MainPanel.p00.z)
		{
			page.setColor(color);
			page.fillPolygon(xInt, yInt, 4);
			if(isLines)
			{
				page.setColor(Color.black);
				page.drawPolygon(xInt, yInt, 4);
			}
		}
	}

	public Boolean Check(int x1, int y1, int x2, int y2, int x3, int y3)
	{
		return x1*(y2-y3)+x2*(y3-y1)+x3*(y1-y2) >=0;
	}
	public Boolean Check()
	{
		convert(xReal, yReal, zReal, xInt, yInt, numOfPoint);
		return Check(xInt[0], yInt[0], xInt[1], yInt[1],xInt[2], yInt[2]);
	}
	public double getCloseZ()
	{
		double min=Double.MAX_VALUE;
		for (double i : zReal) 
		{
			if(i<min) min=i;
		}
		return min;
	}
	public Point3D [] getPlatot()
	{
		return platot;
	}
}
