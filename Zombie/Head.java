import java.awt.Color;


public class Head extends BodyPart
{	
	public Head(Point3D magozPoint) 
	{
		super(magozPoint);
		parts.add(new Box(magozPoint));//head
		
		platot = new Point3D[1][6][4];
		name = "head";
		
		setSuperPlatot();
	}


	public void buildShape(double x, double y, double z,
			double dx, double dy ,double dz, Color color) 
	{
		parts.get(0).buildShape(x, y, z, dx, dy, dz, color);
		
		xyz.setXYZ(x, y, z);
		
		platot[0]=parts.get(0).platot;
	}
}
