import java.awt.Color;
import java.awt.Graphics;

public class Galil implements Comparable<Galil>
{
	double[] xReal;
	double[] yReal;
	double[] zReal;
	private double[][] xGuf;
	private double[][] yGuf;
	private double[][] zGuf;
	int[] xInt;
	int[] yInt;

	Color color;

	int numOfPoints, mish;
	Point3D magozPoint;

	Point3D xyz;
	double  radius, w;

	public Galil(Point3D magozPoint, int numOfPoints) 
	{
		this.magozPoint=magozPoint;
		xReal= new double[numOfPoints*2];
		yReal= new double[numOfPoints*2];
		zReal= new double[numOfPoints*2];		
		xInt= new int[numOfPoints];
		yInt= new int[numOfPoints];
		this.numOfPoints=numOfPoints;
		mish = numOfPoints+2;

		xGuf = new double [mish][numOfPoints];
		yGuf = new double [mish][numOfPoints];
		zGuf = new double [mish][numOfPoints];

		xyz = new Point3D();
	}


	public void buildGalil(double x, double y, double z, double radius, double PI, double w, Color color) 
	{
		double teta=0;
		double dTeta = PI/(numOfPoints-1);
		for(int i=0;i<numOfPoints;i++)
		{
			xReal[i]=x+radius*Math.sin(teta); xReal[i+numOfPoints]= xReal[i];
			yReal[i]=y+radius*Math.cos(teta); yReal[i+numOfPoints]= yReal[i];
			zReal[i]=z; 					  zReal[i+numOfPoints]= z+w;
			teta+=dTeta;

		}
		xyz.setXYZ(x, y, z);
		this.color=color;
	}
	
	public void buildHarut(double x, double y, double z, double radius1, double radius2 , double PI, double w, Color color) 
	{
		double teta=0;
		double dTeta = PI/(numOfPoints-1);
		for(int i=0;i<numOfPoints;i++)
		{
			xReal[i] = x+radius1*Math.sin(teta); 
			yReal[i] = y+radius1*Math.cos(teta);
			zReal[i] = z; 					  
			
			xReal[i+numOfPoints] = x+radius2*Math.sin(teta);
			yReal[i+numOfPoints] = y+radius2*Math.cos(teta);
			zReal[i+numOfPoints] = z+w;
			
			teta+=dTeta;
		}
		
		xyz.setXYZ(x, y, z);
		this.color=color;
	}
	public void fromPointToGuf()
	{
		for (int i = 0; i < numOfPoints; i++)
		{
			xGuf[0][i]=xReal[i]; 					yGuf[0][i]=yReal[i]; 					zGuf[0][i]=zReal[i];
			xGuf[1][i]=xReal[numOfPoints*2-1-i]; 	yGuf[1][i]=yReal[2*numOfPoints-1-i];	zGuf[1][i]=zReal[2*numOfPoints-1-i];
		}
		for (int i = 2; i < mish-1; i++)
		{
			xGuf[i][0]=xReal[i-2]; 					yGuf[i][0]=yReal[i-2]; 					zGuf[i][0]=zReal[i-2];
			xGuf[i][1]=xReal[i-2+numOfPoints]; 		yGuf[i][1]=yReal[i-2+numOfPoints]; 		zGuf[i][1]=zReal[i-2+numOfPoints];
			xGuf[i][2]=xReal[i-2+numOfPoints+1]; 	yGuf[i][2]=yReal[i-2+numOfPoints+1];	zGuf[i][2]=zReal[i-2+numOfPoints+1];
			xGuf[i][3]=xReal[i-2+1]; 				yGuf[i][3]=yReal[i-2+1]; 				zGuf[i][3]=zReal[i-2+1];
		}
		xGuf[mish-1][0]=xReal[0]; 				 yGuf[mish-1][0]=yReal[0];  				zGuf[mish-1][0]=zReal[0];
		xGuf[mish-1][1]=xReal[numOfPoints-1]; 	 yGuf[mish-1][1]=yReal[numOfPoints-1];  	zGuf[mish-1][1]=zReal[numOfPoints-1];
		xGuf[mish-1][2]=xReal[2*numOfPoints-1];  yGuf[mish-1][2]=yReal[2*numOfPoints-1]; 	zGuf[mish-1][2]=zReal[2*numOfPoints-1];
		xGuf[mish-1][3]=xReal[numOfPoints];  	 yGuf[mish-1][3]=yReal[numOfPoints];    	zGuf[mish-1][3]=zReal[numOfPoints];
	}


	public void mullMat(Matrix3D mat)
	{
		mat.mullAllPoints(xReal, yReal, zReal, numOfPoints*2);
		xyz.mullMat(mat);
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
		for (int i = 0; i < 2; i++)
		{
			convert(xGuf[i], yGuf[i], zGuf[i], xInt, yInt, numOfPoints);

			if (Check(xInt[0], yInt[0], xInt[1], yInt[1],xInt[2], yInt[2])  &&  getCloseZ() > -MainPanel.p00.z)
			{
				if(isLines)
				{
					page.setColor(Color.black);
					page.drawPolygon(xInt, yInt, numOfPoints);
				}
				page.setColor(color);
				page.fillPolygon(xInt, yInt, numOfPoints);
			}
		}
		for (int i = 2; i < mish; i++)
		{
			convert(xGuf[i], yGuf[i], zGuf[i], xInt, yInt, 4);
			
			if (Check(xInt[0], yInt[0], xInt[1], yInt[1],xInt[2], yInt[2])  &&  getCloseZ() > -MainPanel.p00.z)
			{
				page.setColor(color);
				page.fillPolygon(xInt, yInt, 4);
			}
		}
	}

	public Boolean Check(int x1, int y1, int x2, int y2, int x3, int y3)
	{
		return x1*(y2-y3)+x2*(y3-y1)+x3*(y1-y2) >=0;
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

	public int compareTo(Galil o) 
	{
		if(this.getCloseZ()>o.getCloseZ())
			return 1;
		if(this.getCloseZ()<o.getCloseZ())
			return -1;
		return 0;
	}
}