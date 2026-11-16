import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;

import java.io.*;

public class Aim 
{
	double[] xReal;
	double[] yReal;
	double[] zReal;

	int[] xInt;
	int[] yInt;

	Point3D xyz;

	int numOfPoints = 30;
	Point3D magozPoint;

	Gun gun;
	
	Color color = new Color(0,78,155);
	public Aim(Point3D magozPoint) 
	{
		this.magozPoint=magozPoint;
		xReal= new double[numOfPoints+4];
		yReal= new double[numOfPoints+4];
		zReal= new double[numOfPoints+4];		
		xInt= new int[numOfPoints+4];
		yInt= new int[numOfPoints+4];

		xyz = new Point3D();
	}


	public void buildShape(double x, double y, double z, double radius, Gun gun) 
	{
		double teta=0;
		double dTeta = Math.PI*2/numOfPoints;
		for(int i=0;i<numOfPoints;i++)
		{
			xReal[i]=x+radius*Math.sin(teta); yReal[i]=y+radius*Math.cos(teta); zReal[i]=z;
			teta+=dTeta;
		}
		xReal[numOfPoints]=x-radius*1.3;     		yReal[numOfPoints]=y;     						zReal[numOfPoints]=z;  
		xReal[numOfPoints+1]=x+radius*1.3;   	   	yReal[numOfPoints+1]=y;    	  					zReal[numOfPoints+1]=z;
		xReal[numOfPoints+2]=x; 					yReal[numOfPoints+2]=y-radius*1.3;  			zReal[numOfPoints+2]=z;
		xReal[numOfPoints+3]=x;  					yReal[numOfPoints+3]=y+radius*1.3;  			zReal[numOfPoints+3]=z;

		xyz.setXYZ(x, y, z);
		
		this.gun=gun;
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
		page.setColor(color);
		if(!gun.isCantShot)
		{
			convert(xReal, yReal, zReal, xInt, yInt, numOfPoints+4);
			page.drawPolygon(xInt, yInt, numOfPoints);

			page.drawLine(xInt[numOfPoints], yInt[numOfPoints], xInt[numOfPoints+1], yInt[numOfPoints+1]);
			page.drawLine(xInt[numOfPoints+2],yInt[numOfPoints+2], xInt[numOfPoints+3],yInt[numOfPoints+3]);
		}
	}


}