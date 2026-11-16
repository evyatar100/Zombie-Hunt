import java.awt.Color;
import java.awt.Graphics;

public class Moon 
{
	double[] xReal;
	double[] yReal;
	double[] zReal;

	int[] xInt;
	int[] yInt;

	int numOfPoints = 50;

	Point3D magozPoint;

	Color color = new Color(230,230,230);
	
	double radius;

	public Moon(Point3D magozPoint) 
	{
		this.magozPoint=magozPoint;

		xReal= new double[numOfPoints];
		yReal= new double[numOfPoints];
		zReal= new double[numOfPoints];		
		xInt= new int[numOfPoints];
		yInt= new int[numOfPoints];
	}


	public void buildShape(double x, double y, double z, double radius) 
	{
		double teta=0;
		double dTeta = Math.PI*2/numOfPoints;
		for(int i=0;i<numOfPoints;i++)
		{
			xReal[i]=x+radius*Math.sin(teta); yReal[i]=y+radius*Math.cos(teta); zReal[i]=z;
			teta+=dTeta;
		}
		this.radius=radius;
	}

	public void mullMat(Matrix3D mat)
	{
		mat.mullAllPoints(xReal, yReal, zReal, numOfPoints);
	}

	public void convert(double xr[],double yr[],double zr[],int xi[],int yi[],int aNum)
	{
		for (int i = 0; i < aNum; i++)
		{
			xi[i]=(int)(xr[i]*(magozPoint.z/(magozPoint.z+zr[i]))+magozPoint.x);
			yi[i]=(int)(yr[i]*(magozPoint.z/(magozPoint.z+zr[i]))+magozPoint.y);
		}

	}

	public void convertAndShow(Graphics page)
	{
		if(zReal[0]>radius)
		{
			convert(xReal, yReal, zReal, xInt, yInt, numOfPoints);
			page.setColor(color);
			page.fillPolygon(xInt, yInt, numOfPoints);
		}
	}
}