import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


public class DoorWall
{
	ArrayList<Gader> leftWall, rightWall;
	
	Door door;

	Color color = new Color(230, 230, 230,100);

	Point3D magozPoint;

	Map map;

	boolean isSetOnMap;

	int numX;

	public DoorWall (Point3D magozPoint, Map map) 
	{
		this.magozPoint=magozPoint;
		leftWall = new ArrayList<Gader>();
		rightWall = new ArrayList<Gader>();

		numX=30;

		door = new Door(magozPoint, map);
		for (int i=0; i<numX; i++)
		{
			leftWall.add(new Gader(magozPoint));
			rightWall.add(new Gader(magozPoint));
		}

		this.map=map;
		isSetOnMap=false;
	}

	public void buildShape(double x, double y, double z, 
			double dx, double dy, double dz, int state) 
	{
		double wallSize = (dx-Door.dx)/2;
		double xSize = wallSize/numX;

		door.buildShape(x+wallSize, y-Door.dy+dy, z);

		for (int i=0; i<numX; i++)
		{
			leftWall.get(i).buildShape(x+i*xSize, y, z, xSize-20, dy, dz, 20);
			rightWall.get(i).buildShape(x+wallSize+Door.dx+i*xSize, y, z, xSize-20, dy, dz, 20);
		}

		Matrix3D temp = new Matrix3D();
		temp.setMatRotateYFix(-(Math.PI/2)*state, x, y, z);
		mullMat(temp);

		if(!isSetOnMap)
		{
			switch(state)
			{
			case 0:
				map.setBigObject(x, z, wallSize, dz);
				map.setBigObject(x+wallSize+Door.dx, z, wallSize, dz);
				map.setDoor(x+wallSize, z, 0, door.isOpen);
				break;

			case 1:
				map.setBigObject(x, z-wallSize, dz, wallSize);
				map.setBigObject(x, z-dx, dz, wallSize);
				map.setDoor(x, z-wallSize, 1, door.isOpen);
				break;

			case 2:
				map.setBigObject(x-wallSize, z-dz, wallSize, dz);
				map.setBigObject(x-dx, z-dz, wallSize, dz);
				map.setDoor(x-wallSize, z-Door.dz, 2, door.isOpen);
				break;

			case 3:
				map.setBigObject(x-dz, z, dz, wallSize);
				map.setBigObject(x-dz, z+wallSize+Door.dx, dz, wallSize);
				map.setDoor(x-Door.dz, z+wallSize, 3, door.isOpen);
				break;

			default:
				break;
			}
			isSetOnMap=true;
		}
	}

	public void fromPointToGuf()
	{
		for (int i=0; i<numX; i++)
		{
			leftWall.get(i).fromPointToGuf();
			rightWall.get(i).fromPointToGuf();
		}
		door.fromPointToGuf();
	}

	public void mullMat(Matrix3D mat)
	{
		for (int i=0; i<numX; i++)
		{
			leftWall.get(i).mullMat(mat);
			rightWall.get(i).mullMat(mat);
		}
		door.mullMat(mat);
	}

	public void doorsState()
	{
		door.doorsState();
	}
	
	public ArrayList<Shape> getList()
	{
		ArrayList<Shape> temp = new ArrayList<Shape>();

		temp.addAll(leftWall);
		temp.addAll(rightWall);
		temp.addAll(door.getColumns());
		return temp;
	}
	public void openDoors()
	{
		door.openDoors();
		isSetOnMap=false;
	}
}
