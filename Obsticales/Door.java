import java.awt.Color;
import java.util.ArrayList;


public class Door
{
	ArrayList<Column> right, left;
	Column leftBar, rightBar, upBar;

	Point3D magozPoint;

	Map map;

	int numOfStates=250;

	Point3D rightAxis, leftAxis;

	int openState;
	Matrix3D matiRight[];
	Matrix3D matiLeft[];

	int numX=10;
	int numY=10;

	static final double dx=2000, dy=2500, dz=100;

	boolean isOpen;

	public Door (Point3D magozPoint, Map map) 
	{		
		this.magozPoint=magozPoint;

		right = new ArrayList<Column>();
		left = new ArrayList<Column>();

		for (int i=0; i<numX; i++)
		{
			right.add(new Column(magozPoint));
			left.add(new Column(magozPoint));
		}
		
		leftBar = new Column(magozPoint);
		rightBar = new Column(magozPoint);
		upBar = new Column(magozPoint);

		this.map=map;

		matiRight = new Matrix3D[numOfStates];
		matiLeft = new Matrix3D[numOfStates];

		rightAxis = new Point3D();
		leftAxis = new Point3D();
		openState=numOfStates-1;

		isOpen=false;
	}


	public void buildShape(double x, double y, double z)
	{
		Color color = new Color(80, 40, 0);
		Color barColor = new Color(40, 20, 0);
		
		double xSize = dx/numX;;
		double ySize = dy;
		double zSize = dz;

		for (int i=0; i<numX; i++)
		{
			right.get(i).buildShape(x+(i*xSize)/2, y, z, xSize/2, ySize, zSize, color, false);
			left.get(i).buildShape(x+dx/2+(i*xSize)/2, y, z, xSize/2, ySize, zSize, color, false);
		}
		double barLength=100;
		rightBar.buildShape(x-barLength, y, z, barLength, ySize, zSize, barColor, true);
		leftBar.buildShape(x+dx, y, z, barLength, ySize, zSize, barColor, true);
		upBar.buildShape(x-barLength, y-barLength, z, dx+barLength*2, barLength, zSize, barColor, true);

		rightAxis.setXYZ(right.get(0).place);
		leftAxis.setXYZ(left.get(numX-1).xyz1);

		double openAngel = Math.PI/300;
		for (int i = 0; i < numOfStates; i++)
		{
			matiRight[i] = new Matrix3D();
			matiLeft[i] = new Matrix3D();

			matiRight[i].setMatRotateYFix(openAngel*(i-numOfStates+1), rightAxis.x, rightAxis.y, rightAxis.z);
			matiLeft[i].setMatRotateYFix(openAngel*-(i-numOfStates+1), leftAxis.x, leftAxis.y, leftAxis.z);
		}

		for (Column r : right) 
		{
			r.mullMat(matiRight[openState]);
		}
		for (Column l : left) 
		{
			l.mullMat(matiLeft[openState]);
		}
	}
	public void fromPointToGuf()
	{
		for (Column column : right) 
		{
			column.fromPointToGuf();
		}
		for (Column column : left) 
		{
			column.fromPointToGuf();
		}

		rightBar.fromPointToGuf();
		leftBar.fromPointToGuf();
		upBar.fromPointToGuf();
	}


	public void mullMat(Matrix3D mat)
	{
		for (Column column : right) 
		{
			column.mullMat(mat);
		}
		for (Column column : left) 
		{
			column.mullMat(mat);
		}

		rightBar.mullMat(mat);
		leftBar.mullMat(mat);
		upBar.mullMat(mat);

		rightAxis.mullMat(mat);
		leftAxis.mullMat(mat);
	}
	public ArrayList<Column> getColumns()
	{
		ArrayList<Column> temp = new ArrayList<Column>();
		temp.addAll(right);
		temp.addAll(left);
		temp.add(rightBar);
		temp.add(leftBar);
		temp.add(upBar);
		return temp;
	}
	public void doorsState()
	{
		if(isOpen&&openState>0)
		{
			openState--;
		}
	}
	public void openDoors()
	{
		isOpen=true;
	}

}
