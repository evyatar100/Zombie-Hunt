import java.awt.Graphics;

import javax.swing.JOptionPane;


public abstract class Shape implements Comparable<Shape> 
{
	Point3D [][][] platot;
	public int compareTo(Shape o) 
	{
		if(this.getCloseZ()>o.getCloseZ())
			return 1;
		else if(this.getCloseZ()<o.getCloseZ())
			return -1;
		return 0;
	}

	public boolean isShoot(Point3D a, Point3D b)
	{
		Point3D aM;
		Point3D bM;
		Point3D cM;
		Point3D dM;

		for (int i = 0; i < platot.length; i++)
		{
			for(int j=0; j<platot[i].length; j++)
			{
				aM=platot[i][j][0];
				bM=platot[i][j][1];
				cM=platot[i][j][2];
				dM=platot[i][j][3];

				if(aM!=null && bM!=null && cM!=null && dM!=null) 
				{
					if(MyMath.YasharAndRectangle(a, b, aM, bM, cM, dM)) 
						return true;
				}
			}}
		return false;
	}

	private double getCloseZ()
	{
		if (this instanceof Zombie)
			return ((Zombie)this).getCloseZ();
		else if(this instanceof Column)
			return ((Column)this).getCloseZ();
		else if(this instanceof Gader)
			return ((Gader)this).getCloseZ();
		else if(this instanceof BodyPart)
			return ((BodyPart)this).getCloseZ();
		else if(this instanceof Bullet)
			return ((Bullet)this).getCloseZ();
		return 0;
	}
	public void convertAndShow(Graphics page)
	{
		if (this instanceof Zombie)
			((Zombie)this).convertAndShow(page, true);
		else if (this instanceof Column)
			((Column)this).convertAndShow(page);
		else if (this instanceof Gader)
			((Gader)this).convertAndShow(page);
		else if (this instanceof Bullet)
			((Bullet)this).convertAndShow(page);
		else if (this instanceof BodyPart)
			((Column)this).convertAndShow(page);
	}
}
