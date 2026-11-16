import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Zombie extends Shape
{
	ArrayList <BodyPart> parts;
	
	Random rand = new Random();

	Color color, feetColor, bigFeetColor=new Color(125,0,0);
	
	double dx, dy, dz;
	
	int red;
	int green;
	int blue;

	Point place;
	int spawn;
	int destinationIndex;
	Point destination;

	boolean legUpOrDown;
	int numOfLegStates = 10;
	int legsState;

	Matrix3D matiRightLegFront[];
	Matrix3D matiLeftLegFront[];

	Matrix3D matiRightLegBack[];
	Matrix3D matiLeftLegBack[];

	Matrix3D mati;
	
	boolean handUpOrDown;
	final int numOfHandStates = 10;
	int handState;

	Matrix3D matiRightHand[];
	Matrix3D matiLeftHand[];
	
	
	final int numOfFallStates = 11;
	int fallState;
	
	Matrix3D matiFall[];

	Point3D xyz;
	Point3D center;
	Point3D bottomXYZ;
	
	double look;

	Point3D magozPoint;

	int life;
	
	int id;
	
	boolean isAlive;
	
	boolean isBig;

	public Zombie(Point3D magozPoint) 
	{		
		place = new Point();
		life=100;
		
		parts = new ArrayList<BodyPart>();

		parts.add(new Head(magozPoint));//head
		parts.add(new Body(magozPoint));//head

		parts.add(new Hand(magozPoint, true));//rHand
		parts.add(new Hand(magozPoint, false));//lHand

		parts.add(new Leg(magozPoint, true));//rLeg
		parts.add(new Leg(magozPoint, false));//lLeg

		xyz= new Point3D();
		center = new Point3D();
		bottomXYZ = new Point3D();

		legsState=numOfLegStates;

		legUpOrDown=true;

		matiRightLegFront = new Matrix3D[numOfLegStates*2+1];
		matiLeftLegFront = new Matrix3D[numOfLegStates*2+1];

		matiRightLegBack = new Matrix3D[numOfLegStates*2+1];
		matiLeftLegBack = new Matrix3D[numOfLegStates*2+1];

		mati = new Matrix3D();
		
		handUpOrDown = true;
		
		matiRightHand = new Matrix3D[numOfHandStates*2+1];
		matiLeftHand = new Matrix3D[numOfHandStates*2+1];
		
		matiFall = new Matrix3D[numOfFallStates];
		
		look = 0;
		
		isAlive=true;
		
		destination = new Point();
	}
	
	public void resetZombie(double XSize, double YSize, double ZSize, Point place, int ID, int spawn)
	{
		isAlive=true;
		life=100;
		
		this.dx=XSize;
		this.dy=YSize;
		this.dz=ZSize;
		
		red=170;
		green=188;
		blue=70;
		feetColor = Color.red;
		
		setPlace(place);
		id = ID;
		this.spawn=spawn;
		destinationIndex = 1;
		
		handState = numOfHandStates;
		fallState = 0;
		
		isBig=false;
	}
	public void makeBig()
	{	
		isBig=true;
		feetColor=bigFeetColor;
	}

	public void buildShape(double x ,double z) 
	{
		double y=1000-2*dy;
		color = new Color(red, green, blue);
		for (BodyPart b : parts) 
		{
			if (b.name.compareTo("head")==0)
				((Head)b).buildShape(x+dx/4, y-dy/3, z+dz/4, dx/2, dy/3, dz/3*2, color);
			else if(b.name.compareTo("body")==0)
				((Body)b).buildShape(x, y, z, dx, dy, dz, color);
			else if(b.name.compareTo("rHand")==0)
				((Hand)b).buildShape(x-dx/3, y, z, dx/3, dy+dy/6, dz, color, feetColor);
			else if(b.name.compareTo("lHand")==0)
				((Hand)b).buildShape(x+dx, y, z, dx/3, dy+dy/6, dz, color, feetColor);
			else if(b.name.compareTo("rLeg")==0)
				((Leg)b).buildShape(x, y+dy, z, dx/2, dy, dz, color, feetColor);
			else if(b.name.compareTo("lLeg")==0)
				((Leg)parts.get(5)).buildShape(x+dx/2, y+dy, z, dx/2, dy, dz, color, feetColor);
		}
		xyz.setXYZ(x, y, z);

		center.setXYZ(x+dx/2, y+dy/2, z+dz/2);

		mati.setMatRotateXFix(-Math.PI/2, parts.get(2).axis.x, parts.get(2).axis.y, parts.get(2).axis.z);
		parts.get(2).mullMat(mati);
		mati.setMatRotateXFix(-Math.PI/2, parts.get(3).axis.x, parts.get(3).axis.y, parts.get(3).axis.z);
		parts.get(3).mullMat(mati);
		
		double HandAngel = Math.PI/100;
		for (int i = 0; i < matiLeftHand.length-1; i++)
		{
			matiRightHand[i] = new Matrix3D();
			matiLeftHand[i] = new Matrix3D();
			
			matiRightHand[i].setMatRotateXFix(HandAngel*-(i-numOfLegStates), parts.get(2).axis.x, parts.get(2).axis.y, parts.get(2).axis.z);
			matiLeftHand[i].setMatRotateXFix(HandAngel*(i-numOfLegStates), parts.get(3).axis.x, parts.get(3).axis.y, parts.get(3).axis.z);
		}

		double legAngel = Math.PI/100;
		for (int i = 0; i < matiLeftLegFront.length-1; i++) 
		{
			matiLeftLegFront[i] = new Matrix3D();
			matiRightLegFront[i] = new Matrix3D();

			matiLeftLegBack[i] = new Matrix3D();
			matiRightLegBack[i] = new Matrix3D();

			matiRightLegFront[i].setMatRotateXFix(legAngel*-(i-numOfLegStates), parts.get(4).xyzFront.x, parts.get(4).xyzFront.y, parts.get(4).xyzFront.z);
			matiLeftLegFront[i].setMatRotateXFix(legAngel*(i-numOfLegStates), parts.get(5).xyzFront.x, parts.get(5).xyzFront.y, parts.get(5).xyzFront.z);

			matiRightLegBack[i].setMatRotateXFix(legAngel*-(i-numOfLegStates), parts.get(4).xyzBack.x, parts.get(4).xyzBack.y, parts.get(4).xyzBack.z);
			matiLeftLegBack[i].setMatRotateXFix(legAngel*(i-numOfLegStates), parts.get(5).xyzBack.x, parts.get(5).xyzBack.y, parts.get(5).xyzBack.z);

		}
		
		bottomXYZ.setXYZ(parts.get(4).parts.get(1).xReal[5], parts.get(4).parts.get(1).yReal[5], parts.get(4).parts.get(1).zReal[5]);
	}


	public void fromPointToGuf()
	{
		for (BodyPart b : parts) 
		{
			b.fromPointToGuf();
		}
	}

	public void mullMat(Matrix3D mat)
	{
		for (BodyPart b : parts) 
		{
			b.mullMat(mat);
		}

		xyz.mullMat(mat);
		center.mullMat(mat);
	}

	public void convertAndShow(Graphics page, boolean isLines)
	{
		ArrayList <BodyPart> temp = new ArrayList<BodyPart>();
		temp.addAll(parts);
		Collections.sort(temp);
		Collections.reverse(temp);
		for (BodyPart b : temp) 
		{
			b.convertAndShow(page, isLines);
		}
	}

	public void setPlace(int xPlace, int zPlace)
	{
		place.x=xPlace;
		place.y=zPlace;
	}

	public void setPlace(Point point)
	{
		place.x=point.x;
		place.y=point.y;
	}

	public void marchZomb()
	{
		parts.get(4).mullMat(matiRightLegFront[legsState], matiRightLegBack[legsState]);
		parts.get(5).mullMat(matiLeftLegFront[legsState], matiLeftLegBack[legsState]);
	}
	
	public void moveHands()
	{
		parts.get(2).mullMat(matiRightHand[handState]);
		parts.get(3).mullMat(matiLeftHand[handState]);
	}
	
	public void turnZomb()
	{
		mati.setMatRotateYFix(look, center.x, center.y, center.z);
		mullMat(mati);
	}
	public void fallBack()
	{
		mullMat(matiFall[fallState]);
		if(fallState<numOfFallStates-1)//all
		{
			fallState++;
		}
		
		if(handState>numOfHandStates)//hands
			handState--;
		if(handState<numOfHandStates)
			handState++;
		if(handState==numOfHandStates)
		{
			mati.setMatRotateXFix(Math.PI/2, parts.get(2).axis.x, parts.get(2).axis.y, parts.get(2).axis.z);
			parts.get(2).mullMat(mati);
			mati.setMatRotateXFix(Math.PI/2, parts.get(3).axis.x, parts.get(3).axis.y, parts.get(3).axis.z);
			parts.get(3).mullMat(mati);
		}
		
		if(legsState>numOfLegStates)//legs
			legsState--;
		if(legsState<numOfLegStates)
			legsState++;
	}
	public void setFallMats()
	{
		double fallAngel = -Math.PI/20;
		for(int i = 0; i < numOfFallStates; i++)
		{
			matiFall[i] = new Matrix3D();
			matiFall[i].setMatRotateXFix(fallAngel*i, bottomXYZ.x, bottomXYZ.y, bottomXYZ.z);
		}
		
	}

	public boolean hit(int hit)
	{
		if(isBig)
			hit/=5;
		life-=hit;
		red+=hit/2;
		if(red>255) red=255;
		green-=hit;
		if(green<0) green=0;
		blue-=hit/2;
		if(blue<0) blue=0;
		if(life<=0)
			return true;
		return false;
	}
	public double getCloseZ()
	{
		double min=Double.MAX_VALUE;
		for (BodyPart p : parts) 
		{
			if(p.getCloseZ()<min) min=p.getCloseZ();
		}
		return min;
	}
}
