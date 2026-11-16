import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


public class Arena extends Shape
{
	Floor floor;

	ArrayList<DoorWall> sides;// 0=for, 1=right, 2=back, 3=left.

	ArrayList<Floor> outWalls;//0=for, 1=right, 2=back, 3=left.
	
	Point3D magozPoint;

	Color wallColor = new Color(230, 230, 230);

	Map map;
	
	Point3D pStart=MainPanel.pStart;

	public Arena (Point3D magozPoint, Map map) 
	{
		this.magozPoint=magozPoint;

		floor = new Floor(magozPoint, 50, 50);

		sides = new ArrayList<DoorWall>();
		outWalls = new ArrayList<Floor>();
		
		for (int i = 0; i < 4; i++) 
		{
			sides.add(new DoorWall(magozPoint, map));
			outWalls.add(new Floor(magozPoint, 45, 8));
		}

		this.map=map;
	}

	public void restart()
	{
		for (DoorWall side : sides) 
		{
			side.door.openState=side.door.numOfStates-1;
			side.door.isOpen=false;
		}
	}

	public void buildShape(double dx, double dy, double dz) 
	{
		double hall=2000;
		floor.buildShape(pStart.x, pStart.y+1000, pStart.z, dx, dz, 0, new Color(0,50,0));		

		sides.get(0).buildShape(pStart.x+hall, pStart.y-dy, pStart.z+dz-hall, dx-hall*2, dy+1000, 1, 0);//for

		sides.get(1).buildShape(pStart.x+dx-hall, pStart.y-dy, pStart.z+dz-hall, dz-hall*2, dy+1000, 1, 1);//right

		sides.get(2).buildShape(pStart.x+dx-hall, pStart.y-dy, pStart.z+hall, dx-hall*2, dy+1000, 1, 2);//back

		sides.get(3).buildShape(pStart.x+hall, pStart.y-dy, pStart.z+hall, dz-hall*2, dy+1000, 1, 3);//left

		dy -=250;
		outWalls.get(0).buildShape(pStart.x, pStart.y-dy, pStart.z+dz, dx, dy+1000, 1, wallColor);//for

		outWalls.get(1).buildShape(pStart.x+dx, pStart.y-dy, pStart.z+dz, dz, dy+1000, 2, wallColor);//right

		outWalls.get(2).buildShape(pStart.x+dx, pStart.y-dy, pStart.z, dx, dy+1000, 3, wallColor);//back

		outWalls.get(3).buildShape(pStart.x, pStart.y-dy, pStart.z, dz, dy+1000, 4, wallColor);//left

		for (int i=0; i<map.xNum; i++)//-1 on outWalls
		{
			map.setObjectIndex(-1, i, 0);
			map.setObjectIndex(-1, i, map.yNum-1);
		}
		for (int i=0; i<map.yNum; i++)//-1 on outWalls
		{
			map.setObjectIndex(-1, 0, i);
			map.setObjectIndex(-1, map.xNum-1, i);
		}

	}
	public void fromPointToGuf()
	{
		for (DoorWall wd : sides) 
		{
			wd.fromPointToGuf();
		}
	}


	public void mullMat(Matrix3D mat)
	{
		floor.mullMat(mat);
		for (DoorWall wd : sides) 
		{
			wd.mullMat(mat);
		}
		for (Floor w: outWalls)
		{
			w.mullMat(mat);
		}
	}

	public void convertAndShow(Graphics page)
	{
		floor.convertAndShow(page);
		for (Floor w: outWalls)
		{
			w.convertAndShow(page);
		}

	}

	public ArrayList<Shape> getList()
	{
		ArrayList<Shape> temp = new ArrayList<Shape>();
		for (DoorWall wd : sides) 
		{
			temp.addAll(wd.getList());
		}
		return temp;

	}
	public void openDoor()
	{
		for (DoorWall wd : sides) 
		{
			wd.doorsState();
		}
	}

}
