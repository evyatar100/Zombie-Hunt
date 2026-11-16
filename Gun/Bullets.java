
import java.awt.Point;
import java.util.ArrayList;


public class Bullets
{
	ArrayList<Bullet> bullets;

	Point3D magozPoint;

	Map map;

	int numOfBullets;
	
	Point3D pStart = MainPanel.pStart;
	
	public Bullets (Point3D magozPoint, Map map) 
	{		
		this.magozPoint=magozPoint;

		this.map=map;

		bullets = new ArrayList<Bullet>();
	}

	public synchronized void restart()
	{	
		bullets.clear();
	}
	public synchronized void addBullet(Point place, int ammo)
	{
		Bullet temp = new Bullet(magozPoint);

		temp.setBullet(place, ammo);

		bullets.add(temp);
		numOfBullets++;
	}
	public synchronized void removeBullet (Bullet b)
	{
		bullets.remove(b);
	}
	public synchronized void buildShape()
	{
		for (Bullet b : bullets) 
		{
			b.buildShape(pStart.x+b.place.x, pStart.y+500, pStart.z+b.place.y);
		}
	}
	public synchronized void fromPointToGuf()
	{
		for (Bullet b : bullets) 
		{
			b.fromPointToGuf();
		}
	}


	public synchronized void mullMat(Matrix3D mat)
	{
		for (Bullet b : bullets) 
		{
			b.mullMat(mat);
		}
	}
	public synchronized ArrayList<Bullet> getBullets()
	{
		return new ArrayList<Bullet>(bullets);
	}
	public synchronized void spin()
	{
		for (Bullet b : bullets) 
		{
			b.spin();
		}
	}


}
