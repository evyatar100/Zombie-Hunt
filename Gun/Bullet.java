import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

public class Bullet extends Shape
{
	ArrayList<Galil> first, second;
	ArrayList<Galil> temp;

	Color color=new Color(192, 170, 40);
	Color topColor = new Color(161, 126, 32);

	Point3D xyz;
	
	Point place;

	int numOfParts=4;
	int numOfPoint=20;

	Point3D magozPoint;

	Matrix3D matiTemp = new Matrix3D();
	Matrix3D matiRotate;
	
	int ammo;

	public Bullet(Point3D magozPoint) 
	{
		first = new ArrayList<Galil>();
		second = new ArrayList<Galil>();

		temp = new ArrayList<Galil>();

		for (int i = 0; i < numOfParts; i++)
		{
			first.add(new Galil(magozPoint, numOfPoint));
			second.add(new Galil(magozPoint, numOfPoint));
		}
		xyz = new Point3D();
		matiRotate = new Matrix3D();
	}
	
	public void setBullet(Point place, int ammo)
	{
		this.place=place;
		this.ammo = ammo;
		
	}
	int largeR=30, mediumR=20, smallR=2, firstS=250, secondS=30, thirdS=100;
	public void buildShape(double x, double y, double z) 
	{
		first.get(0).buildGalil(x, y, z, largeR, Math.PI*2, firstS, color);
		first.get(1).buildHarut(x, y, z+250, largeR, mediumR, Math.PI*2, secondS, color);
		first.get(2).buildGalil(x, y, z+280, mediumR, Math.PI*2, secondS, color);
		first.get(3).buildHarut(x, y, z+310, mediumR, smallR, Math.PI*2, thirdS, topColor);

		second.get(0).buildGalil(x, y, z, largeR, Math.PI*2, firstS, color);
		second.get(1).buildHarut(x, y, z+250, largeR, mediumR, Math.PI*2, secondS, color);
		second.get(2).buildGalil(x, y, z+280, mediumR, Math.PI*2, secondS, color);
		second.get(3).buildHarut(x, y, z+310, mediumR, smallR, Math.PI*2, thirdS, topColor);

		xyz.setXYZ(x, y, z+(firstS+secondS+secondS+thirdS)/2);

		matiTemp.setMatRotateXFix(Math.PI/2, xyz.x, xyz.y, xyz.z);
		mullMat(matiTemp);
		matiTemp.setMatRotateZFix(Math.PI/4, xyz.x, xyz.y, xyz.z);
		mullMatOne(matiTemp, true);
		matiTemp.setMatRotateZFix(-Math.PI/4, xyz.x, xyz.y, xyz.z);
		mullMatOne(matiTemp, false);
		matiTemp.setMatMove(0, 0, largeR*2+5);
		mullMatOne(matiTemp, false);
		matiTemp.setMatMove(0, 0, (largeR*2+5)/2);
		xyz.mullMat(matiTemp);
		
		matiRotate.setMatRotateYFix((Math.PI/40)*spinTimes, xyz.x, xyz.y, xyz.z);
		mullMat(matiRotate);
	}

	public void fromPointToGuf()
	{
		for (int i = 0; i < numOfParts; i++) 
		{
			first.get(i).fromPointToGuf();
			second.get(i).fromPointToGuf();
		}
	}

	public void mullMat(Matrix3D mat)
	{
		for (int i = 0; i < numOfParts; i++) 
		{
			first.get(i).mullMat(mat);
			second.get(i).mullMat(mat);
		}
		xyz.mullMat(mat);
	}
	public void mullMats(Matrix3D mabatMove, Matrix3D mabatTurnY, Matrix3D mabatTurnX)
	{
		mullMat(mabatMove);
		mullMat(mabatTurnY);
		mullMat(mabatTurnX);
	}
	public void mullMatOne(Matrix3D mat, boolean firstOrSecond)
	{
		for (int i = 0; i < numOfParts; i++) 
		{
			if(firstOrSecond)
				first.get(i).mullMat(mat);
			else
				second.get(i).mullMat(mat);
		}
	}

	public void convertAndShow(Graphics page)
	{
		temp.clear();
		temp.addAll(first);
		temp.addAll(second);
		Collections.sort(temp);
		Collections.reverse(temp);
		for (Galil g : temp) 
		{
			g.convertAndShow(page, true);
		}
	}
	public double getCloseZ()
	{
		temp.clear();
		temp.addAll(first);
		temp.addAll(second);

		double min=Double.MAX_VALUE;
		for (Galil g : temp) 
		{
			if(g.getCloseZ()<min) min=g.getCloseZ();
		}
		return min;
	}
	int spinTimes=0;
	public void spin()
	{
		spinTimes++;
		if(spinTimes==80)
			spinTimes=0;
	}
}
