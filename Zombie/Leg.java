import java.awt.Color;

public class Leg extends BodyPart
{	
	boolean RL;
	public Leg(Point3D magozPoint, boolean RL) 
	{
		super(magozPoint);
		parts.add(new Box(magozPoint));//leg
		parts.add(new Box(magozPoint));//feet
		
		platot = new Point3D[2][6][4];
		
		this.RL=RL;
		
		if(RL)
			name = "rLeg";
		else
			name = "lLeg";
		
		setSuperPlatot();
	}


	public void buildShape(double x, double y, double z,
			double dx, double dy ,double dz, Color legColor, Color feetColor) 
	{
		parts.get(0).buildShape(x, y, z, dx, dy/6*5, dz, legColor);
		parts.get(1).buildShape(x, y+dy/6*5, z, dx, dy/6, dz, feetColor);
		
		xyz.setXYZ(x, y, z);
		xyzFront.setXYZ(x, y, z);
		xyzBack.setXYZ(x, y, z+dz);
		
		platot[0]=parts.get(0).platot;
		platot[1]=parts.get(1).platot;
		
	}
}
