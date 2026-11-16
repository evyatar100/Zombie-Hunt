import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Barrel 
{
	Random generator = new Random();

	Box [] parts;
	Galil barrel;

	double[] xReal={70,140,142,152,152,142,140,70};
	double[] yReal={-7,-7,-7,-7,-5,-5,-3,-3};

	Color color=new Color(50, 50, 50);

	Point3D xyz;

	int numOfParts=3;
	int numOfPoint=8;

	Point3D magozPoint;

	public Barrel(Point3D magozPoint) 
	{
		barrel = new Galil(magozPoint, 20);
		parts = new Box [numOfParts];
		for (int i = 0; i < numOfParts; i++) 
		{
			parts[i] = new Box(magozPoint);
		}
		xyz= new Point3D();

	}


	public void buildShape(double x,double y,double z) 
	{
		for (int i = 0; i < numOfPoint; i++)
		{
			xReal[i]+=x;
			yReal[i]+=y;
		}
		for (int i = 0; i < numOfParts; i++)
		{
			parts[i].buildShape(xReal[i], yReal[i], xReal[i+1], yReal[i+1], 
					xReal[numOfPoint-i-2],yReal[numOfPoint-i-2], 
					xReal[numOfPoint-i-1], yReal[numOfPoint-i-1], z, 6, color);
		}
		barrel.buildGalil(x+152, y+-6, z+2.5, 1, Math.PI*2, 93, color);
		Matrix3D mati = new Matrix3D();
		mati.setMatRotateYFix(-Math.PI/2, barrel.xyz.x, barrel.xyz.y, barrel.xyz.z);
		barrel.mullMat(mati);

		xyz.setXYZ(x, y, z);
	}

	public void fromPointToGuf()
	{
		barrel.fromPointToGuf();
		for (int i = 0; i < numOfParts; i++)
		{
			parts[i].fromPointToGuf();
		}	
	}

	public void mullMat(Matrix3D mat)
	{
		barrel.mullMat(mat);
		for (int i = 0; i < numOfParts; i++)
		{
			parts[i].mullMat(mat);
		}	
		xyz.mullMat(mat);
	}

	public void convertAndShow(Graphics page)
	{
		if(parts[0].xyz.z>parts[1].xyz.z)
		{
			barrel.convertAndShow(page, true);
			for (int i = 0; i < numOfParts; i++)
			{
				parts[i].convertAndShow(page, true);
			}
		}
		else
		{
			for (int i = numOfParts-1; i > -1; i--)
			{
				parts[i].convertAndShow(page, true);
			}
			barrel.convertAndShow(page, true);
		}
	}
}
