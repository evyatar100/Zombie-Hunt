import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.io.InputStream;
import java.util.Random;

public class Gun extends Thread
{
	Random generator = new Random();

	Handle handle;
	Barrel barrel;
	Notch notch;
	Aperture aperture;
	
	Point3D backP, forP;

	Matrix3D mati;

	Point3D xyz;
	
	Point3D magozPoint;

	int ammo, clip = 1;
	final int clipSize = 12, maxAmmo = 60;
	
	boolean isAmmoFull;
	boolean isCantShot;
	
	int reloadState;
	final int numOfStates=17;
	
	int hitState;
	final int numOfHitStates=11;
	
	int recoilState;
	final int numOfRecoilStates=5;
	
	Font playbill;
	Color ammoColor = new Color(0,78,155);
	
	public Gun(Point3D magozPoint) 
	{
		handle = new Handle(magozPoint);
		barrel =new Barrel(magozPoint);
		notch =new Notch(magozPoint);
		aperture = new Aperture(magozPoint);
		mati = new Matrix3D();

		xyz= new Point3D();

		backP = new Point3D();
		forP = new Point3D();

		this.magozPoint=magozPoint;
		
		playbill = new Font(Font.SANS_SERIF, Font.PLAIN, 50);
		try (InputStream font = Resources.open("fonts/Playbill.ttf")) {
			playbill = Font.createFont(Font.TRUETYPE_FONT, font).deriveFont(Font.PLAIN, 50);
		} catch (Exception e) {System.err.println("Playbill font not found: " + e);}

		start();
	}
	
	public void makeNew()
	{
		ammo=20;
		clip=0;
		load();
		if(ammo+clip<maxAmmo)
			isAmmoFull=false;
		else
			isAmmoFull=true;

		isCantShot = false;
		reloadState = 0;
		hitState = 0;
	}


	public void buildShape(double x,double y,double z) 
	{
		handle.buildShape(x, y, z);
		barrel.buildShape(x, y, z);
		notch.buildShape(x, y, z);
		aperture.buildShape(x, y, z);

		xyz.setXYZ(x, y, z);
		
		mati.setMatRotateYFix(Math.PI/2, xyz.x, xyz.y, xyz.z);
		this.mullMat(mati);
		mati.setMatScaleFix(3, 3, 3, xyz.x, xyz.y, xyz.z);
		this.mullMat(mati);

		mati.setMatMove(75, 65, -1120);
		this.mullMat(mati);

		this.backP.setXYZ(xyz);
		this.forP.setXYZ(xyz);
		forP.z+=100;
	}

	public void fromPointToGuf()
	{
		handle.fromPointToGuf();
		barrel.fromPointToGuf();
		notch.fromPointToGuf();
		aperture.fromPointToGuf();
	}

	public void mullMat(Matrix3D mat)
	{
		handle.mullMat(mat);
		barrel.mullMat(mat);
		notch.mullMat(mat);
		aperture.mullMat(mat);

		xyz.mullMat(mat);
	}

	public void convertAndShow(Graphics page)
	{
		handle.convertAndShow(page);
		barrel.convertAndShow(page);
		notch.convertAndShow(page);
		aperture.convertAndShow(page);
		
		page.setFont(playbill);
		page.setColor(ammoColor);
		page.drawString(ammo+"/"+clip, 5, 635);
	}
	public void addAmmo(int a)
	{
		ammo+=a;
		if(clip+ammo>maxAmmo)
		{
			ammo=60-clip;
			isAmmoFull=true;
		}
	}
	public void shoot()
	{
		if(isCantShot)
			return;
		
		isCantShot=true;
		recoilState = numOfRecoilStates;
		isCantShot=true;
		
		
		clip--;
		
		isAmmoFull=false;
	}
	private void load()
	{
		ammo+=clip;
		if(ammo<clipSize)
		{
			clip=ammo;
			ammo=0;
		}
		else
		{
			clip=clipSize;
			ammo-=clipSize;
		}
	}
	
	public void reload()
	{
		if(ammo==0||clip==clipSize||isCantShot)
			return;
		
		isCantShot=true;
		reloadState = numOfStates;
		isCantShot=true;
	}
	
	public void hit()
	{
		if(isCantShot)
			return;
		
		isCantShot=true;
		hitState = numOfHitStates;
		isCantShot=true;
	}
	
	public void run()
	{
		for(;;)
		{
			if(clip==0)
				reload();
			
			//reload
			if(reloadState < numOfStates/2  &&  reloadState > 0)
			{
				mati.setMatRotateXFix(Math.PI/2/numOfStates, xyz.x, xyz.y, xyz.z);
				mullMat(mati);
			}
			else if(reloadState==(numOfStates/2)+1)
			{
				new Sound(Sound.type.RELOAD);
				reloadState--;
				sleepy(1500);
				load();
			}
			else if(reloadState < numOfStates  &&  reloadState > (numOfStates/2)+1)
			{
				mati.setMatRotateXFix(-Math.PI/2/numOfStates, xyz.x, xyz.y, xyz.z);
				mullMat(mati);
			}
			if(reloadState!=0)
			{
				reloadState--;
				if(reloadState==0)
				{
					isCantShot = false;
				}
			}
			
			//hit
			else if(hitState < numOfHitStates/2  &&  hitState > 0)
			{
				mati.setMatRotateYFix(-Math.PI/2/numOfHitStates, backP.x, backP.y, backP.z);
				mullMat(mati);
				sleepy(30);
			}
			else if(hitState==(numOfHitStates/2)+1)
			{
				hitState--;
				sleepy(30);
			}
			else if(hitState < numOfHitStates  &&  hitState > (numOfHitStates/2)+1)
			{
				mati.setMatRotateYFix(Math.PI/2/numOfHitStates, backP.x, backP.y, backP.z);
				mullMat(mati);
				sleepy(30);
			}
			if(hitState!=0)
			{
				hitState--;
				if(hitState==0)
				{
					isCantShot = false;
				}
			}
			
			//recoil
			else if(recoilState < numOfRecoilStates/2  &&  recoilState > 0)
			{
				mati.setMatRotateXFix(-Math.PI/40/numOfRecoilStates, backP.x, backP.y, backP.z);
				mullMat(mati);
				sleepy(40);
			}
			else if(recoilState==(numOfRecoilStates/2)+1)
			{
				recoilState--;
				sleepy(40);
			}
			else if(recoilState < numOfRecoilStates  &&  recoilState > (numOfRecoilStates/2)+1)
			{
				mati.setMatRotateXFix(Math.PI/40/numOfRecoilStates, backP.x, backP.y, backP.z);
				mullMat(mati);
				sleepy(40);
			}
			if(recoilState!=0)
			{
				recoilState--;
				if(recoilState==0)
				{
					isCantShot = false;
				}
			}
			sleepy(20);
		}
	}
	private void sleepy(int delay)
	{
		try 
		{
			sleep(delay);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
}
