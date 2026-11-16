
import java.awt.Point;
import java.util.ArrayList;


public class Bullets
{
	ArrayList<Bullet> bullets;

	Point3D magozPoint;

	Map map;

	int numOfBullets;
	
	Point3D pStart = MainPanel.pStart;
	
	boolean isForing = false;

	public Bullets (Point3D magozPoint, Map map) 
	{		
		this.magozPoint=magozPoint;

		this.map=map;

		bullets = new ArrayList<Bullet>();
	}

	public void restart()
	{	
		bullets.clear();
	}
	public void addBullet(Point place, int ammo)
	{
		Bullet temp = new Bullet(magozPoint);

		temp.setBullet(place, ammo);

		bullets.add(temp);
		numOfBullets++;
	}
	public void removeBullet (Bullet b)
	{
		bullets.remove(b);
	}
	public void buildShape()
	{
		for (Bullet b : bullets) 
		{
			b.buildShape(pStart.x+b.place.x, pStart.y+500, pStart.z+b.place.y);
		}
	}
	public void fromPointToGuf()
	{
		if(isForing)
			return;
		isForing=true;
		for (Bullet b : bullets) 
		{
			b.fromPointToGuf();
		}
		isForing=false;
	}


	public void mullMat(Matrix3D mat)
	{
		if(isForing)
			return;
		isForing=true;
		for (Bullet b : bullets) 
		{
			b.mullMat(mat);
		}
		isForing=false;
	}
	public ArrayList<Bullet> getBullets()
	{
		return bullets;
	}
	public void spin()
	{
		while(isForing){}
		isForing=true;
		for (Bullet b : bullets) 
		{
			b.spin();
		}
		isForing=false;
	}


}
