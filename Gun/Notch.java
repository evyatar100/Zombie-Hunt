import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Notch 
{
	Random generator = new Random();

	Box notch;

	Color color=Color.RED;
		
	Point3D xyz;
	
	Point3D notchPoint;
	
	Point3D magozPoint;

	public Notch(Point3D magozPoint) 
	{
		notch = new Box(magozPoint);
		xyz = new Point3D();
		notchPoint = new Point3D();
	}
	public void buildShape(double x,double y,double z) 
	{
		notch.buildShape(x+240, y-7, z+2.7, 1, -2.5, 0.6, color);
		
		xyz.setXYZ(x+240, y-7, z+2.7);
		notchPoint.setXYZ((notch.xReal[0]+notch.xReal[1])/2,
				notch.yReal[6], 
				(notch.zReal[0]+notch.zReal[4])/2);
	}

	public void fromPointToGuf()
	{
		notch.fromPointToGuf();
	}

	public void mullMat(Matrix3D mat)
	{
		notch.mullMat(mat);
		xyz.mullMat(mat);
		notchPoint.mullMat(mat);
	}

	public void convertAndShow(Graphics page)
	{
		notch.convertAndShow(page, true);
	}
}
