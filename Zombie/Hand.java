import java.awt.Color;


public class Hand extends BodyPart
{	
	boolean RL;
			
	public Hand(Point3D magozPoint, boolean RL) 
	{
		super(magozPoint);
		parts.add(new Box(magozPoint));//shoulder
		parts.add(new Box(magozPoint));//arm
		parts.add(new Box(magozPoint));//hand
		parts.add(new Box(magozPoint));//toe

		platot = new Point3D[4][6][4];
		
		this.RL=RL;

		if(RL)
			name = "rHand";
		else
			name = "lHand";
		
		setSuperPlatot();
		
	}


	public void buildShape(double x,double y,double z,
			double dx,double dy,double dz, Color armColor, Color handColor) 
	{
		parts.get(0).buildShape(x, y, z, dx, dy/6*2, dz, armColor);
		parts.get(1).buildShape(x, y+dy/6*2, z, dx, dy/6*4, dz, armColor);
		parts.get(2).buildShape(x, y+dy, z, dx, dy/12, dz*1.3, handColor);
		
		if(RL)
			parts.get(3).buildShape(x+dx, y+dy, z+dz/10, dx/4, dy/12/5*3, dz*0.9, handColor);
		else
			parts.get(3).buildShape(x-dx/4, y+dy, z+dz/10, dx/4, dy/12/5*3, dz*0.9, handColor);
		
		axis.setXYZ(x+dx/2, y+dy/12, z+dz/2);
		xyz.setXYZ(x, y, z);
		
		platot[0]=parts.get(0).platot;
		platot[1]=parts.get(1).platot;
		platot[2]=parts.get(2).platot;
		platot[3]=parts.get(3).platot;
		
	}

}
