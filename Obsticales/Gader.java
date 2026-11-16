import java.awt.Color;
import java.awt.Graphics;


public class Gader extends Shape
{
	Brick basis;
	Galil top;

	Brick bar1, bar2;

	Color color=Color.white, barColor = new Color(240,240,240);

	Point3D magozPoint;

	Matrix3D mati= new Matrix3D();

	Point3D [][] platot;

	public Gader (Point3D magozPoint) 
	{
		this.magozPoint=magozPoint;

		basis = new Brick(magozPoint);
		top = new Galil(magozPoint, 8);		

		bar1 = new Brick(magozPoint);
		bar2 = new Brick(magozPoint);

		platot = new Point3D[3][];
		
		setSuperPlatot();
	}


	public void buildShape(double x, double y, double z, 
			double dx, double dy, double dz, double dis) 
	{
		basis.buildShape(x, y, z, dx, dy, 1, color);
		top.buildGalil(x+dx/2, y, z, dx/2, Math.PI, dz, color);
		mati.setMatRotateZFix(-Math.PI/2, top.xyz.x, top.xyz.y, top.xyz.z);
		top.mullMat(mati);

		bar1.buildShape(x-dis/2, y+dy*0.05, z+dz, dx+dis, 50, 1, barColor);
		bar2.buildShape(x-dis/2, y+dy*0.8, 	 z+dz, dx+dis, 50, 1, barColor);
	}

	public void fromPointToGuf()
	{
		top.fromPointToGuf();
	}


	public void mullMat(Matrix3D mat)
	{
		basis.mullMat(mat);
		top.mullMat(mat);

		bar1.mullMat(mat);
		bar2.mullMat(mat);
	}

	public void convertAndShow(Graphics page)
	{
		if(basis.Check())
		{
			bar1.convertAndShow(page, false);
			bar2.convertAndShow(page, false);

			basis.convertAndShow(page, false);
			top.convertAndShow(page, false);
		}
	}
	public double getCloseZ()
	{
		return Math.min(basis.getCloseZ(), top.getCloseZ());
	}
	private void setSuperPlatot() 
	{
		super.platot = new Point3D[1][1][];
		super.platot[0][0]=basis.getPlatot();
	}
}
