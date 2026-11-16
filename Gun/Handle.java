import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Handle 
{
	Random generator = new Random();

	Box [] parts;

	double[] xReal={0,47,54,70,95,140,142,160,160,155,140,95,70,54,47,0};
	double[] yReal={0,0,2,-3,-3,-3,-5,-5,3,5,5,10,10,20,17,25};

	Color color=new Color(154, 105, 75);

	Point3D xyz;

	int numOfParts=7;
	int numOfPoint=16;

	Point3D magozPoint;

	public Handle(Point3D magozPoint) 
	{
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
					xReal[numOfPoint-i-2], yReal[numOfPoint-i-2], 
					xReal[numOfPoint-i-1], yReal[numOfPoint-i-1], z, 6, color);
		}
		xyz.setXYZ(x, y, z);
		parts[0].setLines(false, false, true, false, true, true);
		parts[1].setLines(false, false, true, false, true, false);
		parts[2].setLines(false, false, true, false, true, false);
		parts[3].setLines(false, false, true, false, true, false);
		parts[4].setLines(false, false, true, false, true, false);
		parts[5].setLines(false, false, true, false, true, false);
		parts[6].setLines(false, false, true, true, true, false);
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
	}

	public void convertAndShow(Graphics page)
	{
		if(parts[0].xyz.z>parts[1].xyz.z)
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
