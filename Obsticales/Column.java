import java.awt.Color;
import java.awt.Graphics;


public class Column extends Shape
{
	Box basis;

	Color color;

	Point3D magozPoint;

	Point3D place, xyz1;

	boolean isBorder;

	public Column (Point3D magozPoint) 
	{
		this.magozPoint=magozPoint;

		basis = new Box(magozPoint);
		
		setSuperPlatot();
	}


	public void buildShape(double x, double y, double z, 
			double dx, double dy, double dz, Color color, boolean isBorder) 
	{
		place=new Point3D(x,y,z);
		xyz1=new Point3D(x+dx,y,z);

		basis.buildShape(x, y, z, dx, dy, dz, color);

		this.isBorder=isBorder;
	}

	public void fromPointToGuf()
	{
		basis.fromPointToGuf();
	}


	public void mullMat(Matrix3D mat)
	{
		basis.mullMat(mat);
	}

	public void convertAndShow(Graphics page)
	{
		if (isBorder && basis.wallCheck()) 
		{
			basis.convertAndShow(page, false);
		}
		else if(!isBorder)
			basis.convertAndShow(page, false);
	}
	public double getCloseZ()
	{
		return basis.getCloseZ();
	}
	private void setSuperPlatot() 
	{
		super.platot = new Point3D[1][][];
		super.platot[0]=basis.getPlatot();
	}
}
