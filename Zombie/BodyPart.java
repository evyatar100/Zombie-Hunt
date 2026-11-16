import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class BodyPart extends Shape

{
	Random generator = new Random();

	ArrayList<Box> parts;

	Color color;
	
	Point3D xyz;
	Point3D xyzFront, xyzBack;
	Point3D axis;
	
	Point3D magozPoint;
		
	String name;
	
	boolean isBig = false;
		
	public BodyPart(Point3D magozPoint)
	{
		this.magozPoint=magozPoint;
		parts = new ArrayList<Box>();
		
		xyz = new Point3D();
		xyzFront = new Point3D();
		xyzBack = new Point3D();
		axis = new Point3D();
	}

	public void fromPointToGuf()
	{
		for (Box m : parts) 
		{
			m.fromPointToGuf();
		}
	}

	public void mullMat(Matrix3D mat)
	{
		for (Box m : parts) 
		{
			m.mullMat(mat);
		}
		xyz.mullMat(mat);
		xyzFront.mullMat(mat);
		xyzBack.mullMat(mat);
		axis.mullMat(mat);
	}
	
	public void mullMat(Matrix3D matFront, Matrix3D matBack)
	{
		for (Box m : parts) 
		{
			m.mullMat(matFront, matBack);
		}
	}

	public void convertAndShow(Graphics page, boolean isLines)
	{
		Collections.sort(parts);
		Collections.reverse(parts);
		for (Box m : parts) 
		{
			m.convertAndShow(page, isLines);
		}
	}
	public double getCloseZ()
	{
		double min=Double.MAX_VALUE;
		for (Box p : parts) 
		{
			if(p.getCloseZ()<min)
				min=p.getCloseZ();
		}
		return min;
	}
	public void setSuperPlatot() 
	{
		super.platot=this.platot;
	}
}
