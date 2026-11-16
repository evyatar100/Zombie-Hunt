
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Zombies
{
	Random rand = new Random();
	
	volatile boolean isForring = false;
	MainPanel mP;
	ArrayList<Zombie> zombies;
	ArrayList<Zombie> deadZombies;
	ArrayList<Zombie> waitList;

	Point3D magozPoint;

	Map map;
	Point3D pStart = MainPanel.pStart;

	int numOfZomb;
	final int maxZomb = 35;

	Point [][] points;

	double smallScale = 0.85, bigScale = 1.5;

	public Zombies (MainPanel mP) 
	{		
		this.mP=mP;
		this.map=mP.map;
		this.magozPoint=mP.magozPoint;

		zombies = new ArrayList<Zombie>();
		deadZombies = new ArrayList<Zombie>();
		waitList = new ArrayList<Zombie>();
		setSpawnPoints();
	}

	public void restart()
	{
		isForring=true;
		for (Zombie z : zombies)
		{
			map.clearObject(z.place.x, z.place.y);
		}
		isForring=false;

		waitList.addAll(zombies);
		waitList.addAll(deadZombies);

		zombies.clear();
		deadZombies.clear();
	}

	public boolean addZombie(int spawn)
	{
		if(!map.isEmpty(points[spawn][0].x, points[spawn][0].y, 0) || zombies.size()>maxZomb)
			return false;
		Zombie temp;
		if(!waitList.isEmpty())
			temp = waitList.remove(0);
		else
			temp = new Zombie(magozPoint);
		
		boolean size = rand.nextBoolean();
		if(size)
			temp.resetZombie(mP.zombSizeX*smallScale, mP.zombSizeY*smallScale, 
					mP.zombSizeZ*smallScale, points[spawn][0], numOfZomb+2, spawn);
		else
			temp.resetZombie(mP.zombSizeX, mP.zombSizeY, 
					mP.zombSizeZ, points[spawn][0], numOfZomb+2, spawn);
		map.setObject(temp.id, temp.place);
		zombies.add(temp);
		numOfZomb++;
		return true;
	}

	public boolean addBigZombie()
	{
		if(!map.isEmpty(points[8][0].x, points[8][0].y, 0) || zombies.size()>40)
			return false;
		Zombie temp;
		if(!waitList.isEmpty())
			temp = waitList.remove(0);
		else
			temp = new Zombie(magozPoint);
		temp.resetZombie(mP.zombSizeX*bigScale*1.1, mP.zombSizeY*bigScale, 
				mP.zombSizeZ*bigScale*1.3, points[8][0], numOfZomb+2, 8);
		temp.makeBig();
		map.setObject(temp.id, temp.place);
		zombies.add(temp);
		numOfZomb++;
		return true;
	}
	public void buildShape()
	{
		isForring=true;
		for (Zombie z : zombies) 
		{
			z.buildShape(pStart.x+z.place.x, pStart.z+z.place.y);
		}
		for (Zombie z : deadZombies) 
		{
			z.buildShape(pStart.x+z.place.x, pStart.z+z.place.y);
		}
		isForring=false;

		setDestinations();
	}

	public void fromPointToGuf()
	{
		isForring=true;
		for (Zombie z : zombies) 
		{
			z.fromPointToGuf();
		}
		for (Zombie z : deadZombies) 
		{
			z.fromPointToGuf();
		}
		isForring=false;
	}

	public void mullMat(Matrix3D mat)
	{
		isForring=true;
		for (Zombie z : zombies) 
		{
			z.mullMat(mat);
		}
		for (Zombie z : deadZombies) 
		{
			z.mullMat(mat);
		}
		isForring=false;
	}

	public Zombie hitZomb()
	{
		Collections.sort(zombies);
		for (Zombie z : zombies) 
		{
			if(Math.sqrt(Math.pow(z.place.x-mP.myPoint.x, 2)+ Math.pow(z.place.y-mP.myPoint.y, 2))<1000 && z.xyz.z>-MainPanel.p00.z)
			{
				return z;
			}
		}
		return null;
	}

	public ArrayList<Zombie> getAllZombies()
	{
		ArrayList<Zombie> temp = new ArrayList<Zombie>();
		temp.addAll(zombies);
		temp.addAll(deadZombies);
		return temp;
	}
	public ArrayList<Zombie> getZombies()
	{
		return zombies;
	}
	public ArrayList<Zombie> getDeadZombies()
	{
		return deadZombies;
	}
	public void killZomb (Zombie zomb)
	{
		Zombie temp;
		map.clearObject(zomb.place.x, zomb.place.y);
		zombies.remove(zomb);

		deadZombies.add(zomb);

		zomb.isAlive=false;
		zomb.setFallMats();

		if(deadZombies.size()>15)
		{
			temp = deadZombies.remove(0);
			waitList.add(temp);
		}
	}
	public int getNumOfZomb()
	{
		return zombies.size();
	}
	public Zombie getZomb(int index)
	{
		return zombies.get(index);
	}
	public void setDestinations()
	{

		isForring=true;
		for (Zombie z : zombies) 
		{
			if(Math.sqrt(Math.pow(z.place.x-z.destination.x, 2)+ Math.pow(z.place.y-z.destination.y, 2))<50  &&  z.destinationIndex<3)
			{
				z.destinationIndex++;
			}
			if(z.destinationIndex<3)
				z.destination = points[z.spawn][z.destinationIndex];
			else
				z.destination=mP.myPoint;
		}
		isForring=false;

	}
	private void setSpawnPoints()
	{	
		int xSize=mP.xSize, zSize=mP.zSize;
		points = new Point[9][3];
		int a=600, b=2500, c=3000;
		//left
		//right

		//forward
		points[0][0] = new Point(b, 		zSize-a);
		points[0][1] = new Point(xSize/2-a, zSize-a);
		points[0][2] = new Point(xSize/2-a, zSize-c);

		points[1][0] = new Point(xSize-b, 	zSize-a);
		points[1][1] = new Point(xSize/2+a,	zSize-a);
		points[1][2] = new Point(xSize/2+a, zSize-c);

		//right
		points[2][0] = new Point(xSize-a, 	zSize-b);
		points[2][1] = new Point(xSize-a, 	zSize/2+a);
		points[2][2] = new Point(xSize-c, 	zSize/2+a);

		points[3][0] = new Point(xSize-a, 	b);
		points[3][1] = new Point(xSize-a, 	zSize/2-a);
		points[3][2] = new Point(xSize-c, 	zSize/2-a);

		//backward		
		points[4][0] = new Point(b, 		a);
		points[4][1] = new Point(xSize/2-a, a);
		points[4][2] = new Point(xSize/2-a, c);

		points[5][0] = new Point(xSize-b, 	a);
		points[5][1] = new Point(xSize/2+a, a);
		points[5][2] = new Point(xSize/2+a, c);

		//left		
		points[6][0] = new Point(a, 		zSize-b);
		points[6][1] = new Point(a, 		zSize/2+a);
		points[6][2] = new Point(c, 		zSize/2+a);

		points[7][0] = new Point(a, 		b);
		points[7][1] = new Point(a, 		zSize/2-a);
		points[7][2] = new Point(c, 		zSize/2-a);
		
		points[8][0] = new Point(xSize-b, 		a);
		points[8][1] = new Point(xSize/2, 		a);
		points[8][2] = new Point(xSize/2, 		c);
	}
}
