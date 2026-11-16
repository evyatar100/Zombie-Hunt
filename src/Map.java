import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;


public class Map //0=nothing, 1=me, 2+=zombie, -1=object, 
{
	int [][] map;
	double xSize, ySize, xBox, yBox;
	int xNum, yNum;
	boolean showMap;

	Point3D pStart;

	public Map(double xSize, double ySize, double xBox, double yBox)
	{
		this.xSize=xSize;
		this.ySize=ySize;
		this.xBox=xBox;
		this.yBox=yBox;
		this.pStart=MainPanel.pStart;

		xNum = indexX(xSize);
		yNum = indexY(ySize); 
		map = new int[xNum][yNum];

		showMap = false;
	}
	public boolean isInRange (double x, double z)
	{
		if(x>xBox*2 && x<xSize-xBox*2 && z>yBox*2 && z<ySize-yBox*2)
			return true;
		return false;
	}
	public boolean isEmpty (double x, double z, int object)
	{
		int tempX = indexX(x);
		int tempY = indexY(z);
		if( 
				(map[tempX-1][tempY-1]==0 || map[tempX-1][tempY-1]==object)
				&& (map[tempX-1][tempY]==0   || map[tempX-1][tempY]==object)
				&& (map[tempX-1][tempY+1]==0 || map[tempX-1][tempY+1]==object)
				&& (map[tempX][tempY-1]==0   || map[tempX][tempY-1]==object)
				&& (map[tempX][tempY]==0     || map[tempX][tempY]==object)
				&& (map[tempX][tempY+1]==0   || map[tempX][tempY+1]==object)
				&& (map[tempX+1][tempY-1]==0 || map[tempX+1][tempY-1]==object)
				&& (map[tempX+1][tempY]==0   || map[tempX+1][tempY]==object)
				&& (map[tempX+1][tempY+1]==0 || map[tempX+1][tempY+1]==object))
			return true;
		return false;
	}
	public boolean canZombieMove (double x, double z, int object)
	{
		int tempX = indexX(x);
		int tempY = indexY(z);
		if( 
					(map[tempX-1][tempY-1]==0 || map[tempX-1][tempY-1]==object	|| map[tempX-1][tempY-1]==-2)
				&& 	(map[tempX-1][tempY]==0   || map[tempX-1][tempY]==object	|| map[tempX-1][tempY]==-2)
				&& 	(map[tempX-1][tempY+1]==0 || map[tempX-1][tempY+1]==object	|| map[tempX-1][tempY+1]==-2)
				&& 	(map[tempX][tempY-1]==0   || map[tempX][tempY-1]==object	|| map[tempX][tempY-1]==-2)
				&& 	(map[tempX][tempY]==0     || map[tempX][tempY]==object		|| map[tempX][tempY]==-2)
				&& 	(map[tempX][tempY+1]==0   || map[tempX][tempY+1]==object	|| map[tempX][tempY+1]==-2)
				&& 	(map[tempX+1][tempY-1]==0 || map[tempX+1][tempY-1]==object	|| map[tempX+1][tempY-1]==-2)
				&&	(map[tempX+1][tempY]==0   || map[tempX+1][tempY]==object	|| map[tempX+1][tempY]==-2)
				&&	(map[tempX+1][tempY+1]==0 || map[tempX+1][tempY+1]==object	|| map[tempX+1][tempY+1]==-2)
				                                                                            
		)
			return true;
		return false;
	}
	public void setObjectIndex(int object, int indexX, int indexY)
	{
		map[indexX][indexY]=object;
	}
	public void setObject(int object, Point point)
	{
		int tempX = indexX(point.x);
		int tempY = indexY(point.y);
		setObjectIndex(object, tempX, tempY);
	}
	public void clearObject(double x, double z)
	{
		int tempX = indexX(x);
		int tempY = indexY(z);

		setObjectIndex(0, tempX, tempY);
	}
	public void setBigObject(double x, double z, double dx, double dz)
	{
		int x1=indexX(x-pStart.x);
		int y1=indexY(z-pStart.z);
		int dx1=indexX(dx);
		if(dx1==0) dx1=1;
		int dy1=indexY(dz);
		if(dy1==0) dy1=1;
		for(int i=x1; i<x1+dx1; i++)
		{
			for(int j=y1; j<=y1+dy1; j++)
			{
				setObjectIndex(-1, i, j);
			}
		}
	}
	public void setDoor(double x, double z, int state, boolean isOpen)//0=for, 1=right, 2=back, 3=left;
	{
		int object;
		if(isOpen) 	object=-2;
		else 		object=-3;

		int x1 = 0;
		int y1 = 0;
		int dx1 = 0;
		int dy1 = 0;

		switch (state)
		{
		case 0:
			x1=indexX(x-pStart.x);
			y1=indexY(z-pStart.z);
			dx1=indexX(Door.dx);
			dy1=1;
			break;

		case 1:
			x1=indexX(x-pStart.x);
			y1=indexY(z-pStart.z-Door.dx);
			dx1=1;
			dy1=indexY(Door.dx);
			break;

		case 2:
			x1=indexX(x-pStart.x-Door.dx);
			y1=indexY(z-pStart.z);
			dx1=indexX(Door.dx);
			dy1=1;
			break;
		case 3:
			x1=indexX(x-pStart.x);
			y1=indexY(z-pStart.z);
			dx1=1;
			dy1=indexY(Door.dx);
			break;
		}

		for(int i=x1; i<x1+dx1; i++)
		{
			for(int j=y1; j<=y1+dy1; j++)
			{
				setObjectIndex(object, i, j);
			}
		}
	}
	public boolean isBigObject(double x, double z, double dx, double dz)
	{
		int x1=indexX(x);
		int y1=indexY(z);
		int dx1=indexX(dx);
		int dy1=indexY(dz);
		for(int i=x1; i<=x1+dx1; i++)
		{
			for(int j=y1; j<=y1+dy1; j++)
			{
				if(isInRange(i, j) && !isEmpty(i, j, -1))
					return false;
			}
		}
		return true;
	}
	public boolean isBigObject(int x, int y, int dx, int dy)
	{
		for(int i=x; i<=x+dx; i++)
		{
			for(int j=y; j<=y+dy; j++)
			{
				if(!isEmpty(i, j, -1))
					return false;
			}
		}
		return true;
	}
	public int indexX(double size)
	{
		return (int) (size/xBox);
	}
	public int indexY(double size)
	{
		return (int) (size/yBox);
	}
	public void showMap()
	{
		showMap=!showMap;
	}
	public void printMap(Graphics page)
	{
		if(showMap)
		{
			page.setColor(Color.red);
			page.setFont(new Font("0", 0, 10));
			for(int i=0; i<xNum; i++)
			{
				for(int j=0; j<yNum; j++)
				{
					if(map[i][j]==0)
						page.setColor(Color.yellow);
					else if(map[i][j]==1)
						page.setColor(Color.blue);
					else if(map[i][j]>=2)
						page.setColor(Color.red);
					else if(map[i][j]==-1)
						page.setColor(Color.black);
					else if(map[i][j]==-2)
						page.setColor(new Color(128, 0, 255));
					else if(map[i][j]==-3)
						page.setColor(new Color(185, 122, 87));
					else
						page.setColor(Color.orange);
					page.drawString(map[i][j]+"", j*5, (i+1)*10);
				}
			}
		}
	}

}

