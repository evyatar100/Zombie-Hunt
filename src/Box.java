import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Box implements Comparable<Box>
{
	Random generator = new Random();

	double[] xReal;
	double[] yReal;
	double[] zReal;
	private double[][] xGuf;
	private double[][] yGuf;
	private double[][] zGuf;
	int[] xInt;
	int[] yInt;
	int[] zInt;
	Color color;

	Point3D [][] platot;

	boolean whatToDraw[];

	Point3D xyz;

	Point3D magozPoint;

	private int numOfPoint=8;
	int mish=6;

	boolean drawChange = false;

	public Box(Point3D magozPoint) 
	{
		this.magozPoint=magozPoint;
		xReal=new double[numOfPoint];
		yReal=new double[numOfPoint];
		zReal=new double[numOfPoint];
		xGuf=new double[mish][4];
		yGuf=new double[mish][4];
		zGuf=new double[mish][4];
		xInt=new int[numOfPoint];
		yInt=new int[numOfPoint];
		zInt=new int[numOfPoint];

		xyz=new Point3D();
		platot = new Point3D[mish][4];
		whatToDraw = new boolean[mish];
		for (int i = 0; i < mish; i++)
			for (int j = 0; j < 4; j++)
			{
				whatToDraw[i]=true;
				{
					platot[i][j] = new Point3D();
				}
			}

	}

	public void buildShape(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double z,double dz, Color color) 
	{
		xReal[0]=x1;     	yReal[0]=y1;    zReal[0]=z;  
		xReal[1]=x2;   		yReal[1]=y2;    zReal[1]=z;
		xReal[2]=x3; 		yReal[2]=y3;  	zReal[2]=z;
		xReal[3]=x4;  		yReal[3]=y4;  	zReal[3]=z;
		xReal[4]=x1;  		yReal[4]=y1;  	zReal[4]=z+dz;
		xReal[5]=x4;   		yReal[5]=y4;  	zReal[5]=z+dz;
		xReal[6]=x3;    	yReal[6]=y3;  	zReal[6]=z+dz;
		xReal[7]=x2;     	yReal[7]=y2;    zReal[7]=z+dz;  
		this.color=color;

		xyz.setXYZ(x1, y1, z);

	}

	public void buildShape(double x,double y,double z,
			double dx,double dy,double dz, Color color) 
	{
		xReal[0]=x;     	yReal[0]=y;     	zReal[0]=z;  
		xReal[1]=x+dx;   	yReal[1]=y;    	  	zReal[1]=z;
		xReal[2]=x+dx; 		yReal[2]=y+dy;  	zReal[2]=z;
		xReal[3]=x;  		yReal[3]=y+dy;  	zReal[3]=z;
		xReal[4]=x;  		yReal[4]=y;  		zReal[4]=z+dz;
		xReal[5]=x;   		yReal[5]=y+dy;  	zReal[5]=z+dz;
		xReal[6]=x+dx;    	yReal[6]=y+dy;  	zReal[6]=z+dz;
		xReal[7]=x+dx;     	yReal[7]=y;     	zReal[7]=z+dz;  
		this.color=color;

		xyz.setXYZ(x, y, z);

	}
	public void buildShape(double x,double y,double z,
			double dx,double dy,double dz, Color color, Color frontColor) 
	{
		xReal[0]=x;     	yReal[0]=y;     	zReal[0]=z;  
		xReal[1]=x+dx;   	yReal[1]=y;    	  	zReal[1]=z;
		xReal[2]=x+dx; 		yReal[2]=y+dy;  	zReal[2]=z;
		xReal[3]=x;  		yReal[3]=y+dy;  	zReal[3]=z;
		xReal[4]=x;  		yReal[4]=y;  		zReal[4]=z+dz;
		xReal[5]=x;   		yReal[5]=y+dy;  	zReal[5]=z+dz;
		xReal[6]=x+dx;    	yReal[6]=y+dy;  	zReal[6]=z+dz;
		xReal[7]=x+dx;     	yReal[7]=y;     	zReal[7]=z+dz;  
		this.color=color;

		xyz.setXYZ(x, y, z);

	}
	public void fromPointToGuf()
	{
		int i;

		i=0;
		xGuf[i][0]=xReal[0];     yGuf[i][0]=yReal[0];  	zGuf[i][0]=zReal[0];
		xGuf[i][1]=xReal[1];     yGuf[i][1]=yReal[1];   zGuf[i][1]=zReal[1];
		xGuf[i][2]=xReal[2];     yGuf[i][2]=yReal[2];   zGuf[i][2]=zReal[2];
		xGuf[i][3]=xReal[3];     yGuf[i][3]=yReal[3];   zGuf[i][3]=zReal[3];

		i=1;
		xGuf[i][0]=xReal[4];     yGuf[i][0]=yReal[4];   zGuf[i][0]=zReal[4];
		xGuf[i][1]=xReal[5];     yGuf[i][1]=yReal[5];   zGuf[i][1]=zReal[5];
		xGuf[i][2]=xReal[6];     yGuf[i][2]=yReal[6];   zGuf[i][2]=zReal[6];
		xGuf[i][3]=xReal[7];     yGuf[i][3]=yReal[7];   zGuf[i][3]=zReal[7];

		i=2;
		xGuf[i][0]=xReal[0];     yGuf[i][0]=yReal[0];   zGuf[i][0]=zReal[0];
		xGuf[i][1]=xReal[4];     yGuf[i][1]=yReal[4];   zGuf[i][1]=zReal[4];
		xGuf[i][2]=xReal[7];     yGuf[i][2]=yReal[7];   zGuf[i][2]=zReal[7];
		xGuf[i][3]=xReal[1];     yGuf[i][3]=yReal[1];   zGuf[i][3]=zReal[1];

		i=3;
		xGuf[i][0]=xReal[1];     yGuf[i][0]=yReal[1];   zGuf[i][0]=zReal[1];
		xGuf[i][1]=xReal[7];     yGuf[i][1]=yReal[7];   zGuf[i][1]=zReal[7];
		xGuf[i][2]=xReal[6];     yGuf[i][2]=yReal[6];   zGuf[i][2]=zReal[6];
		xGuf[i][3]=xReal[2];     yGuf[i][3]=yReal[2];   zGuf[i][3]=zReal[2];

		i=4;
		xGuf[i][0]=xReal[2];     yGuf[i][0]=yReal[2];   zGuf[i][0]=zReal[2];
		xGuf[i][1]=xReal[6];     yGuf[i][1]=yReal[6];   zGuf[i][1]=zReal[6];
		xGuf[i][2]=xReal[5];     yGuf[i][2]=yReal[5];   zGuf[i][2]=zReal[5];
		xGuf[i][3]=xReal[3];     yGuf[i][3]=yReal[3];   zGuf[i][3]=zReal[3];

		i=5;
		xGuf[i][0]=xReal[3];     yGuf[i][0]=yReal[3];   zGuf[i][0]=zReal[3];
		xGuf[i][1]=xReal[5];     yGuf[i][1]=yReal[5];   zGuf[i][1]=zReal[5];
		xGuf[i][2]=xReal[4];     yGuf[i][2]=yReal[4];   zGuf[i][2]=zReal[4];
		xGuf[i][3]=xReal[0];     yGuf[i][3]=yReal[0];   zGuf[i][3]=zReal[0];

		for (i = 0; i < mish; i++)
		{
			platot[i][0].setXYZ(xGuf[i][0], yGuf[i][0], zGuf[i][0]);
			platot[i][1].setXYZ(xGuf[i][1], yGuf[i][1], zGuf[i][1]);
			platot[i][2].setXYZ(xGuf[i][2], yGuf[i][2], zGuf[i][2]);
			platot[i][3].setXYZ(xGuf[i][3], yGuf[i][3], zGuf[i][3]);
		}
	}

	public void mullMat(Matrix3D mat)
	{
		mat.mullAllPoints(xReal, yReal, zReal, numOfPoint);
		xyz.mullMat(mat);
	}
	public void mullMat(Matrix3D matFront, Matrix3D matBack) 
	{
		double[] xtempF=new double[4];
		double[] ytempF=new double[4];
		double[] ztempF=new double[4];

		double[] xtempB=new double[4];
		double[] ytempB=new double[4];
		double[] ztempB=new double[4];

		for (int i = 0; i < 4; i++) 
		{
			xtempF[i] = xReal[i];
			ytempF[i] = yReal[i];
			ztempF[i] = zReal[i];

			xtempB[i] = xReal[i+4];
			ytempB[i] = yReal[i+4];
			ztempB[i] = zReal[i+4];
		}
		matFront.mullAllPoints(xtempF, ytempF, ztempF, 4);
		matBack.mullAllPoints(xtempB, ytempB, ztempB, 4);
		for (int i = 0; i < 4; i++) 
		{
			xReal[i] = xtempF[i];
			yReal[i] = ytempF[i];
			zReal[i] = ztempF[i];

			xReal[i+4] = xtempB[i];
			yReal[i+4] = ytempB[i];
			zReal[i+4] = ztempB[i];
		}
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
		for (int i = 0; i < mish; i++)
		{
			convert(xGuf[i], yGuf[i], zGuf[i], xInt, yInt, 4);

			if (check(xInt[0], yInt[0], xInt[1], yInt[1],xInt[2], yInt[2])&&
					getCloseZ()>-MainPanel.p00.z)
			{

				page.setColor(color);
				page.fillPolygon(xInt, yInt, 4);
				if(isLines&&whatToDraw[i])
				{
					page.setColor(Color.black);
					page.drawPolygon(xInt, yInt, 4);
				}
			}
		}
		convert(xGuf[0], yGuf[0], zGuf[0], xInt, yInt, 4);

	}

	public Boolean check(int x1, int y1, int x2, int y2, int x3, int y3)
	{
		return x1*(y2-y3)+x2*(y3-y1)+x3*(y1-y2) >=0;
	}

	public boolean wallCheck()
	{
		convert(xGuf[0], yGuf[0], zGuf[0], xInt, yInt, 4);
		return check(xInt[0], yInt[0], xInt[1], yInt[1],xInt[2], yInt[2]);
	}
	public void setLines(boolean front, boolean back, boolean top, boolean right,
			boolean bottom, boolean left)
	{
		drawChange = false;
		whatToDraw[0] = front;
		whatToDraw[1] = back;
		whatToDraw[2] = top;
		whatToDraw[3] = right;
		whatToDraw[4] = bottom;
		whatToDraw[5] = left;
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
	public double getFarZ()
	{
		double maxZ=Double.MIN_VALUE;
		for (double i : zReal) 
		{
			if(i>maxZ) maxZ=i;
		}
		return maxZ;
	}
	public Point3D getFarPoint()
	{
		double maxZ=getFarZ();
		int index=4;

		for (int i = 0; i < numOfPoint; i++) 
		{
			if(maxZ==zReal[i])
				index=i;
		}
		return new Point3D(xReal[index], yReal[index], zReal[index]);
	}
	public Point3D [][] getPlatot()
	{
		return platot;
	}
	public int compareTo(Box o) 
	{
		return (int) (this.getCloseZ()-o.getCloseZ());
	}

}
