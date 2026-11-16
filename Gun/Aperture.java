import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Aperture 
{
	Random generator = new Random();

	Box [] parts;

	Color color=Color.orange;

	Point3D xyz;
	
	Point3D aperturePoint;

	int numOfParts=3;
	int numOfPoint=3;

	Point3D magozPoint;

	public Aperture(Point3D magozPoint) 
	{
		parts = new Box [numOfParts];
		for (int i = 0; i < numOfParts; i++) 
		{
			parts[i] = new Box(magozPoint);
		}
		xyz= new Point3D();
		aperturePoint = new Point3D();
	}


	public void buildShape(double x,double y,double z) 
	{
		double[] xReal={110,125,0};
		double[] yReal={-9.5,-8,-7};
		double[] zReal={1,2.5,4};
		
		for (int i = 0; i < numOfPoint; i++)
		{
			xReal[i]+=x;
			yReal[i]+=y;
			zReal[i]+=z;
		}
		for (int i = 0; i < numOfParts; i++)
		{
			parts[i].buildShape(xReal[0], yReal[i%2], xReal[1], yReal[i%2], xReal[1],
					yReal[2], xReal[0], yReal[2], zReal[i], 1.5, color);
		}
		
		xyz.setXYZ(xReal[0], xReal[1], xReal[2]);
		
		aperturePoint.setXYZ((parts[0].xReal[0]+parts[2].xReal[6])/2,
				parts[1].yReal[0], 
				(parts[0].zReal[0]+parts[2].zReal[6])/2);
	}

	public void fromPointToGuf()
	{
		for (int i = 0; i < numOfParts; i++)
		{
			parts[i].fromPointToGuf();
		}	
	}

	public void mullMat(Matrix3D mat)
	{
		for (int i = 0; i < numOfParts; i++)
		{
			parts[i].mullMat(mat);
		}	
		xyz.mullMat(mat);
		aperturePoint.mullMat(mat);
	}

	public void convertAndShow(Graphics page)
	{
		if(parts[0].xReal[0]>=parts[0].xReal[0])
		{
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
		}
	}
}
