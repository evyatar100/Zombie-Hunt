import java.awt.Color;
import java.awt.Graphics;


public class Floor 
{
	Brick [] brick;

	Color color;

	Point3D magozPoint;

	int numX;
	int numY;
	int num;
	double xSize, ySize;

	public Floor (Point3D magozPoint, int numX, int numY) 
	{
		this.numX=numX;
		this.numY=numY;
		num= (int) numX*numY;

		this.magozPoint=magozPoint;

		brick = new Brick[num];
		for (int i=0; i<num; i++)
		{
			brick[i] = new Brick(magozPoint);
		}
	}


	public void buildShape(double x,double y,double z,
			double dx, double dy, int state, Color color) 
	{
		xSize = dx/numX;
		ySize = dy/numY;

		if(state==0)//floor
		{
			for (int i=0; i<num; i++)
			{
				brick[i].buildShape(x+i%numX*xSize, y, z+i/numX*ySize, xSize, ySize, 0, color);
			}
		}
		else
		{
			for (int i=0; i<num; i++)
			{
				if(i%numX==numX-1 && i/numX%2==1)
					brick[i].buildShape(x+i%numX*xSize  +(i/numX%2*0.5*xSize), y+i/numX*ySize, z, xSize/2, ySize, 1, color);
				else if(i%numX==0 && i/numX%2==1)
					brick[i].buildShape(x+i%numX*xSize, y+i/numX*ySize, z, xSize*1.5, ySize, 1, color);
				else
					brick[i].buildShape(x+i%numX*xSize  +(i/numX%2*0.5*xSize), y+i/numX*ySize, z, xSize, ySize, 1, color);
			}
			Matrix3D mati = new Matrix3D();
			mati.setMatRotateYFix(-(Math.PI/2)*(state-1), x, y, z);
			mullMat(mati);
		}
	}


	public void mullMat(Matrix3D mat)
	{
		for (int i=0; i<num ; i++)
		{
			brick[i].mullMat(mat);
		}
	}

	public void convertAndShow(Graphics page)
	{
		for (int i=0; i<num ; i++)
		{
			brick[i].convertAndShow(page,true);
		}
	}
}
