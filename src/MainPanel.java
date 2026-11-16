/*
 * Created on Eiar 5767
 * update Av 5768
 * @author levian
 * for me
 */
import java.awt.Color;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.JPanel;

public class MainPanel extends JPanel implements ActionListener
{	
	BufferedImage cursorImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");

	Toolkit toolkit = Toolkit.getDefaultToolkit();
	Image cImg = toolkit.getImage("files/images/Cursor.PNG");

	Cursor defult = toolkit.createCustomCursor(cImg, new Point(5,0), "defult");

	Image startImg;

	Font scoreFont;

	JButton newGame, exit, resume;
	JSlider sensitivity;

	static Point3D p00, pMe, pStart;
	Point3D magozPoint;
	Point myPoint, locationOnScreen=new Point(0, 0);

	Animation animation;
	BackSound backSound;

	Random rand = new Random();

	Timer repaintTimer;
	Timer timer;

	Text text;

	Listener listener;

	Map map;

	Life life;

	Gun gun;

	Aim aim;

	Arena arena;

	Zombies zombies;

	Bullets bullets;

	Moon moon;

	ArrayList<Shape> shape;
	ArrayList<BodyPart> tempBodyParts;

	final int xSize=20000, ySize= 200, zSize=20000;
	final int zombSizeX=300, zombSizeY = 500, zombSizeZ = 100;
	final int moveSpeed = 75;

	Matrix3D tempTurnX, tempTurnY, tempMove;
	Matrix3D mabatTurnX, mabatTurnY, mabatMove;

	Matrix3D matiZomb;

	Color wallColor = new Color(230, 230, 230);

	boolean isLookFor;
	double zivaReal;
	int ziva;
	double zombSpeed;

	boolean isUsingCheats = false, isShowHits = false, isOneShoot = false, isShowMap = false;
	boolean isDebugMode;
	private final AtomicInteger pendingDebugKills = new AtomicInteger();
	private volatile int debugKillCount;

	boolean isGameStarted;

	public MainPanel()
	{
		super();

		setBackground(new Color(0,0,5));
		biuldElem();

		listener = new Listener(this);

		setCursor(defult);
	}

	public void paintComponent (Graphics page)
	{
		super.paintComponent(page);

		setSensitivity();

		if(isGameStarted)
		{
			makeShapes();

			convertAndShow(page);
		}
		else
		{
			page.drawImage(startImg, 0, 0, this);
			if (isDebugMode)
			{
				page.setColor(Color.orange);
				page.setFont(new Font("Arial", Font.BOLD, 16));
				page.drawString("DEBUG MODE ON", 510, 82);
			}

			page.setColor(new Color(240,240,240));
			page.setFont(new Font(getName(), 0, 20));
			page.drawString("Sensitivity: "+listener.now, 510, 52);

			page.setColor(Color.orange);
			page.setFont(scoreFont);
			if(!isUsingCheats)
				text.setHighScore();
			page.drawString("High Score: "+text.getHighScore(), 15, 35);
			if(text.score>0)
				page.drawString("Your Score: "+text.score, 15, 65);
		}
	}


	public void biuldElem()
	{
		try {
			startImg = ImageIO.read(new File("files/images/backGround.png"));
			scoreFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("files/fonts/score.ttf")).deriveFont(Font.PLAIN, 25);
		} catch (Exception e) {}

		makeBottons();
		magozPoint=new Point3D(950/2, 650/2, 1000);
		p00=new Point3D(0,0,1000);
		pMe = new Point3D(p00.x, p00.y, 0);
		pStart= new Point3D(p00.x-xSize/2, p00.y, pMe.z-5000);

		myPoint = new Point();

		repaintTimer = new Timer(20,this);
		repaintTimer.start();

		timer = new Timer(1000,this);

		text = new Text(magozPoint);

		map = new Map(xSize, zSize, 200, 200);

		life = new Life(magozPoint);

		gun = new Gun(magozPoint);

		aim = new Aim(magozPoint);
		aim.buildShape(pMe.x, pMe.y, pMe.z+500, 25, gun);

		arena = new Arena(magozPoint, map);

		zombies = new Zombies(this);

		bullets = new Bullets(magozPoint, map);

		moon = new Moon(magozPoint);

		shape = new ArrayList<Shape>();
		tempBodyParts = new ArrayList<BodyPart>();

		tempTurnX 	= new Matrix3D();
		mabatTurnX 	= new Matrix3D();

		tempTurnY 	= new Matrix3D();
		mabatTurnY 	= new Matrix3D();

		tempMove 	= new Matrix3D();
		mabatMove 	= new Matrix3D();

		matiZomb 	= new Matrix3D();

		isGameStarted=false;
	}

	private void makeBottons() 
	{		
		newGame = new JButton("New Game");
		newGame.setFocusable(false);
		newGame.addActionListener(this);
		add(newGame);

		exit = new JButton("Exit");
		exit.setFocusable(false);
		exit.addActionListener(this);
		add(exit);

		resume = new JButton("Resume");
		resume.setFocusable(false);
		resume.addActionListener(this);
		add(resume);
		resume.setEnabled(false);

		sensitivity = new JSlider(10,200,100);
		sensitivity.setMajorTickSpacing(25);
		sensitivity.setMinorTickSpacing(5);
		sensitivity.setPaintTicks(true);
		sensitivity.setFocusable(false);
		add(sensitivity);

	}

	public void newGame()
	{
		setCursor(blankCursor);
		stopAnimaition();
		if(isGameStarted)
		{
			map.clearObject(myPoint.x, myPoint.y);
			timer.stop();
			life.restart();
		}
		myPoint.setLocation(10000, 5000);

		gun.makeNew();
		gun.buildShape(p00.x, p00.y, p00.z-900);
		gun.fromPointToGuf();

		life.restart();

		text.score=0;

		arena.restart();
		zombies.restart();
		bullets.restart();

		mabatTurnX.setIdentity();
		mabatTurnY.setIdentity();
		mabatMove.setIdentity();

		zivaReal=0;
		isLookFor=true;
		setZiva(0);
		map.setObject(1, myPoint);

		seconds=0;
		numOfSpawns = 0;
		chanceOfNewZomb = 10;
		zombSpeed =10;
		debugKillCount = 0;

		newGame.hide();
		exit.hide();
		resume.hide();
		resume.setEnabled(true);
		sensitivity.hide();

		isGameStarted=true;
		listener.game(true);
		requestFocusInWindow();
		text.stopShowingScore();

		timer.restart();
		startAnimaition();
	}
	public void pauseGame()
	{
		setCursor(defult);

		listener.game(false);

		newGame.show();
		exit.show();
		resume.show();
		sensitivity.show();

		pauseAnimaition();

		timer.stop();
		life.timer.stop();
	}
	public void resumeGame()
	{
		if(!isGameStarted)
			return;
		setBackground(new Color(0,0,5));
		setCursor(blankCursor);
		listener.game(true);

		newGame.hide();
		exit.hide();
		resume.hide();
		sensitivity.hide();

		timer.restart();
		life.timer.restart();
		resumeAnimation();
		requestFocusInWindow();
	}

	public void endGame()
	{
		isGameStarted = false;
		setCursor(defult);

		listener.game(false);

		newGame.show();
		exit.show();
		resume.show();
		resume.enable(false);
		sensitivity.show();

		timer.stop();
		life.timer.stop();
	}

	public void shoot()
	{
		if(gun.clip==0 && gun.ammo==0)
		{
			text.addText("No Ammo", Color.red);
			return;
		}
		if(gun.isCantShot)
		{
			return;
		}

		new Sound(Sound.type.GUN);
		gun.shoot();

		Point3D aS = pMe;
		Point3D bS = aim.xyz;

		int hitPoints = 0;

		shape.clear();
		shape.addAll(zombies.getZombies());
		shape.addAll(arena.getList());
		Collections.sort(shape);

		for (Shape s : shape)
		{
			if(s instanceof Zombie)
			{
				tempBodyParts.clear();
				tempBodyParts.addAll(((Zombie)s).parts);
				Collections.sort(tempBodyParts);
				for (BodyPart b : tempBodyParts)
				{
					if(b.isShoot(aS, bS))
					{
						if(b instanceof Head)
						{
							text.addScore(50);
							hitPoints = 100;
						}
						else if(b instanceof Body)
						{
							text.addScore(10);
							hitPoints = 40;
						}
						else if(b instanceof Hand || b instanceof Leg)
						{
							text.addScore(5);
							hitPoints = 30;
						}

						if(isOneShoot)
							hitPoints=100;
						if(((Zombie)s).hit(hitPoints))
						{
							killZomb((Zombie)s);
							if(((Zombie)s).isBig)
								text.addScore(500);
							else
								text.addScore(100);
						}
						return;
					}}}	


			else
			{
				if(s.isShoot(aS, bS))
				{
					if(isShowHits)
						text.addText("wall", Color.red);
					return;
				}}}
		if(isShowHits)
			text.addText("missed", Color.red);
	}
	public void hit()
	{
		Zombie temp;
		if(gun.isCantShot)
			return;

		gun.hit();

		int hitPoints;
		temp = zombies.hitZomb();
		if(temp==null) 
			return;

		new Sound(Sound.type.GUN_PUNCH);
		text.addScore(50);
		if(isOneShoot)
			hitPoints=100;
		else
			hitPoints=50;
		if(temp.hit(hitPoints))
		{
			killZomb(temp);
			if(temp.isBig)
				text.addScore(500);
			else
				text.addScore(100);
			text.addScore(100);
		}
	}
	
	public void play()
	{
		applyPendingDebugKills();
		bullets.spin();
		listener.move();
		arena.openDoor();
		
		int i=0;
		Zombie z;
		while(i < zombies.getNumOfZomb())
		{
			if(!zombies.isForring)
			{
				z = zombies.getZomb(i);
				turnZomb(z);
				if(isClose(z))
				{
					hitMe(z);
				}
				else
				{
					z.handState=z.numOfHandStates;
					moveZomb(z);
					marchZomb(z);
				}
				i++;
			}
			else
			{
				try
				{
					Thread.sleep(1);
				}
				catch (InterruptedException e) {}
			}
		}
	}

	private void applyPendingDebugKills()
	{
		int requests = pendingDebugKills.getAndSet(0);
		for (int i = 0; i < requests; i++)
		{
			int living = zombies.getNumOfZomb();
			if (living == 0)
			{
				text.addText("No live zombies", Color.orange);
				continue;
			}

			Zombie target = zombies.getZomb(rand.nextInt(living));
			killZomb(target);
			isUsingCheats = true;
			debugKillCount++;
			text.addText("Debug kill", Color.orange);
		}
	}

	public void toggleDebugMode()
	{
		isDebugMode = !isDebugMode;
		if (isDebugMode)
		{
			isUsingCheats = true;
			text.addText("Debug mode on", Color.orange);
		}
		else
		{
			text.addText("Debug mode off", Color.orange);
		}
		repaint();
	}

	public void requestDebugKill()
	{
		pendingDebugKills.incrementAndGet();
	}

	public void moveZomb(Zombie zomb)
	{
		double temp;

		Point target = zomb.destination;
		Point xz = new Point(zomb.place.x-target.x, zomb.place.y-target.y);

		temp=-Math.max(Math.abs(xz.x), Math.abs(xz.y));
		if (temp==0)
			xz.setLocation(0, 0);
		else
			xz.setLocation((xz.x/temp)*zombSpeed, (xz.y/temp)*zombSpeed);


		map.clearObject(zomb.place.x, zomb.place.y);

		if(map.canZombieMove(zomb.place.x+xz.x, zomb.place.y+xz.y, zomb.id))
		{
			zomb.place.x+=xz.x;
			zomb.place.y+=xz.y;
		}
		else if (map.canZombieMove(zomb.place.x+xz.x, zomb.place.y, zomb.id))
		{
			zomb.place.x+=xz.x;
		}
		else if (map.canZombieMove(zomb.place.x, zomb.place.y+xz.y, zomb.id))
		{
			zomb.place.y+=xz.y;
		}

		map.setObject(zomb.id, zomb.place);
	}


	public void turnZomb(Zombie zomb)
	{
		Point tempP = zomb.place;
		Point target = zomb.destination;

		if(tempP.y==target.y)
		{
			if(tempP.x>target.x)
			{
				zomb.look = -Math.PI/2;
			}
			else
			{
				zomb.look = Math.PI/2;
			}
		}
		else if(tempP.y<target.y)
		{
			zomb.look = Math.PI-Math.atan((target.x-tempP.x)/(target.y-tempP.y));
		}
		else
		{
			zomb.look = Math.atan((tempP.x-target.x)/(target.y-tempP.y));
		}
	}

	public void marchZomb(Zombie zomb)
	{
		if(zomb.legUpOrDown)
		{
			if(zomb.legsState==zomb.numOfLegStates*2-1) 
			{
				zomb.legUpOrDown=false;
			}
			else
			{
				zomb.legsState++;
			}
		}
		else
		{
			if(zomb.legsState==0)
			{
				zomb.legUpOrDown=true;
			}
			else
			{
				zomb.legsState--;
			}
		}
	}
	public void hitMe(Zombie zomb)
	{
		if(zomb.handUpOrDown)
		{
			if(zomb.handState==zomb.numOfHandStates*2-1) 
			{
				zomb.handUpOrDown=false;
			}
			else
			{
				zomb.handState++;
			}
		}
		else
		{
			if(zomb.handState==0)
			{
				zomb.handUpOrDown=true;
			}
			else
			{
				zomb.handState--;
			}
		}
		if(zomb.handState==zomb.numOfHandStates)
		{
			life.hit();
			new Sound(Sound.type.PUNCH);
		}
	}

	public boolean isClose (Zombie z)
	{
		if (Math.sqrt(Math.pow(z.place.x-myPoint.x, 2)+ Math.pow(z.place.y-myPoint.y, 2))<500)
			return true;
		return false;
	}
	public void takeAmmo()
	{
		if(gun.isAmmoFull)
			return;
		Bullet temp=null;
		for (Bullet b : bullets.getBullets())
		{
			if (Math.sqrt(Math.pow(b.place.x-myPoint.x, 2)+
					Math.pow(b.place.y-myPoint.y, 2))<750)
			{
				new Sound(Sound.type.AMMO);
				gun.addAmmo(b.ammo);
				temp=b;
				continue;
			}
		}
		bullets.removeBullet(temp);
	}
	public void move(int dx, int dz)
	{
		if(map.isEmpty(myPoint.x-dx, myPoint.y-dz,1))
		{
			map.clearObject(myPoint.x, myPoint.y);
			myPoint.x-=dx;
			myPoint.y-=dz;
			map.setObject(1, myPoint);
			tempMove.setMatMove(dx, 0, dz);
			mabatMove.mullMatMat(tempMove);
		}
		else if(map.isEmpty(myPoint.x, myPoint.y-dz,1))
		{
			map.clearObject(myPoint.x, myPoint.y);
			myPoint.y-=dz;
			map.setObject(1, myPoint);
			tempMove.setMatMove(0, 0, dz);
			mabatMove.mullMatMat(tempMove);
		}
		else if(map.isEmpty(myPoint.x-dx, myPoint.y,1))
		{
			map.clearObject(myPoint.x, myPoint.y);
			myPoint.x-=dx;
			map.setObject(1, myPoint);
			tempMove.setMatMove(dx, 0, 0);
			mabatMove.mullMatMat(tempMove);
		}
	}
	
	public void moveForward()
	{
		if(isLookFor)
			move(ziva, -moveSpeed+Math.abs(ziva));
		else
			move(-ziva, moveSpeed-Math.abs(ziva));
	}
	public void moveBackward()
	{
		if(isLookFor)
			move(-ziva, moveSpeed-Math.abs(ziva));
		else
			move(ziva, -moveSpeed+Math.abs(ziva));
	}
	public void moveRight()
	{
		if(isLookFor)
			move(moveSpeed-Math.abs(ziva), ziva);
		else
			move(-moveSpeed+Math.abs(ziva), -ziva);
	}
	public void moveLeft() 
	{
		if(isLookFor)
			move(-moveSpeed+Math.abs(ziva), -ziva);
		else
			move(moveSpeed-Math.abs(ziva), ziva);
	}

	double rotateXMax = Math.PI/2;
	public void rotateXFix(double angel, double xReal, double yReal, double zReal)
	{
		if(rotateXMax+angel > Math.PI  ||  rotateXMax+angel < 0)
			return;
		tempTurnX.setMatRotateXFix(angel,xReal, yReal, zReal);
		rotateXMax+=angel;
		mabatTurnX.mullMatMat(tempTurnX);
	}

	public void rotateFixXUp()
	{
		rotateXFix(Math.PI/500,pMe.x,pMe.y,pMe.z);
	}

	public void rotateFixXDown()
	{
		rotateXFix(-Math.PI/500,pMe.x,pMe.y,pMe.z);
	}

	public void rotateYFix(double angel, double xReal, double yReal, double zReal)
	{
		tempTurnY.setMatRotateYFix(angel,xReal ,yReal, zReal);
		setZiva(angel);
		mabatTurnY.mullMatMat(tempTurnY);

	}
	public void setZiva(double angel)
	{
		final double  PI = Math.PI;
		double zivaTemp;
		zivaReal+=angel;
		zivaTemp=zivaReal;

		while(zivaTemp<-PI/2)
		{
			zivaTemp += PI;
		}
		while(zivaTemp>PI/2)
		{
			zivaTemp -= PI;
		}
		ziva = (int) (-zivaTemp/(PI/2)*moveSpeed);

		int temp = (int) (zivaReal/PI*4);
		temp-=2;
		temp/=4;
		if(temp%2==0)
			isLookFor=true;
		else
			isLookFor=false;
		if(zivaReal>PI/2)
			isLookFor=!isLookFor;

	}
	public void rotateFixYLeft()
	{
		rotateYFix(-Math.PI/100,pMe.x,pMe.y,pMe.z);
	}

	public void rotateFixYRight()
	{
		rotateYFix(Math.PI/100,pMe.x,pMe.y,pMe.z);
	}

	public void rotateZFix(double angel, double xReal, double yReal, double zReal)
	{
		tempTurnX.setMatRotateZFix(angel,xReal,yReal,zReal);
		mabatTurnX.mullMatMat(tempTurnX);
	}

	public void rotateFixZFor()
	{
		rotateZFix(Math.PI/100,pMe.x,pMe.y+5000,pMe.z);
	}

	public void rotateFixZBack()
	{
		rotateZFix(-Math.PI/100,pMe.x,pMe.y+600,pMe.z);
	}

	public void startAnimaition()
	{
		if (animation==null)
		{
			animation = new Animation(this);
		}
		if (backSound==null)
		{
			backSound = new BackSound(this);
		}
	}
	public void stopAnimaition()
	{
		if (animation!=null)
		{
			animation.halt();
		}
		animation=null;

		if (backSound!=null)
		{
			backSound.halt();
		}
		backSound=null;
	}
	public void pauseAnimaition()
	{
		if (animation!=null)
		{
			animation.pauseLoop();
		}
		if (backSound!=null)
		{
			backSound.pauseLoop();
		}
	}
	public void resumeAnimation()
	{
		if (animation!=null)
		{
			animation.resumeLoop();
		}
		if (backSound!=null)
		{
			backSound.resumeLoop();
		}
	}

	public void convertAndShow(Graphics page)
	{
		moon.convertAndShow(page);
		arena.convertAndShow(page);

		shape.clear();
		shape.addAll(zombies.getAllZombies());
		shape.addAll(arena.getList());
		shape.addAll(bullets.getBullets());
		Collections.sort(shape);
		Collections.reverse(shape);

		for (Shape s : shape)
		{
			s.convertAndShow(page);
		}

		gun.convertAndShow(page);
		aim.convertAndShow(page);

		text.show(page);
		life.convertAndShow(page);
		map.printMap(page);
		if (isDebugMode)
		{
			page.setColor(new Color(0, 0, 0, 180));
			page.fillRoundRect(getWidth()-270, 12, 258, 64, 10, 10);
			page.setColor(Color.orange);
			page.setFont(new Font("Arial", Font.BOLD, 15));
			page.drawString("DEBUG MODE", getWidth()-256, 32);
			page.setColor(Color.white);
			page.setFont(new Font("Arial", Font.PLAIN, 13));
			page.drawString("Click to kill a random live zombie", getWidth()-256, 49);
			page.drawString("Kills: "+debugKillCount+"  Live: "+zombies.getNumOfZomb(), getWidth()-256, 65);
		}
	}

	public void makeShapes()
	{
		takeAmmo();

		buildShapes();

		for (Zombie z : zombies.getZombies())
		{
			z.marchZomb();
			z.moveHands();
			z.turnZomb();
		}
		for (Zombie z : zombies.getDeadZombies())
		{
			z.fallBack();
			z.turnZomb();
		}

		mullMat(mabatMove);
		mullMat(mabatTurnY);
		mullMat(mabatTurnX);
		fromPointToGuf();

	}
	public void buildShapes()
	{
		arena.buildShape(xSize, ySize, zSize);
		zombies.buildShape();
		bullets.buildShape();
		moon.buildShape(3000, -150000, 500000, 15000);
	}

	public void mullMat(Matrix3D mat)
	{
		arena.mullMat(mat);
		zombies.mullMat(mat);
		bullets.mullMat(mat);
		moon.mullMat(mat);
	}
	public void fromPointToGuf()
	{
		arena.fromPointToGuf();
		zombies.fromPointToGuf();
		bullets.fromPointToGuf();
		gun.fromPointToGuf();
	}

	int spareBullets=0;
	public void killZomb (Zombie z)
	{
		zombies.killZomb(z);

		if(z.isBig && z.destinationIndex>2)
			bullets.addBullet(z.place, 15);
		else
		{
			if(rand.nextInt(5)==0)
			{
				spareBullets++;
			}
			if(spareBullets>0 && z.destinationIndex>2)
			{
				bullets.addBullet(z.place, 10);
				spareBullets--;
			}
		}
	}

	float seconds=0;
	int numOfSpawns = 0;
	int chanceOfNewZomb = 10;
	public void actionPerformed (ActionEvent e)
	{
		if(e.getSource() == timer)
		{
			seconds++;
			if(seconds==1)
			{
				text.addBigText("North door opened");
				new Sound(Sound.type.SIRAN);
				openDoors(0);
			}
			else if(seconds==30)
			{
				text.addBigText("East door opened");
				new Sound(Sound.type.SIRAN);
				openDoors(1);
			}
			else if(seconds==60)
			{
				text.addBigText("South door opened");
				new Sound(Sound.type.SIRAN);
				openDoors(2);
			}
			else if(seconds==120)
			{
				text.addBigText("West door opened");
				new Sound(Sound.type.SIRAN);
				openDoors(3);
			}
			else if(seconds%100==0)
			{
				text.addBigText("Zombie Speed Increased");
				new Sound(Sound.type.SIRAN);
				zombSpeed+=3;
			}
			if((seconds*2)%chanceOfNewZomb==0 && (seconds<62 || seconds>82))
			{
				zombies.addZombie(rand.nextInt(numOfSpawns));
			}
			if(seconds%60==0 && chanceOfNewZomb>1)
			{
				chanceOfNewZomb--;
			}
			else if(seconds%70==0)
			{
				text.addBigText("A Huge Zombie Is Coming");
				new Sound(Sound.type.SIRAN);
				zombies.addBigZombie();
			}
		}

		else if(e.getSource() == repaintTimer)
			repaint();

		else if(e.getSource() == exit)
			System.exit(0);
		else if(e.getSource() == newGame)
			newGame();
		else if(e.getSource() == resume)
			resumeGame();
	}

	public void openDoors(int index)
	{
		arena.sides.get(index).openDoors();
		numOfSpawns+=2;
		chanceOfNewZomb-=2;
	}

	public void setSensitivity()
	{
		listener.setSensitivity(sensitivity.getValue());
	}

	public void showMap()
	{
		if(isShowMap)
			map.showMap();
	}

	public void cheat()
	{
		String code;
		code = JOptionPane.showInputDialog(null, null, null, JOptionPane.QUESTION_MESSAGE);
		if(code==null)
			return;
		if(code.trim().equalsIgnoreCase("debug mode"))
		{
			toggleDebugMode();
		}
		else if(code.toLowerCase().compareTo("reset score")==0)
		{
			text.restartScore();
		}
		else if(code.toLowerCase().compareTo("show hits")==0)
		{
			isShowHits = !isShowHits;
			if(isShowHits)
				text.addText("show hits", Color.white);
			else
				text.addText("hide hits", Color.white);
		}
		else if(code.toLowerCase().compareTo("add ammo")==0)
		{
			gun.addAmmo(20);
			text.addText("+20 bullets", Color.white);
			if(!isUsingCheats)
			{
				text.addText("Highscore is disabled!", Color.white);
				isUsingCheats=true;
			}
		}
		else if(code.toLowerCase().compareTo("one shoot")==0)
		{
			isOneShoot = !isOneShoot;
			if(isOneShoot)
				text.addText("One Shoot on!", Color.white);
			else
				text.addText("One Shoot off!", Color.white);
			if(!isUsingCheats)
			{
				text.addText("Highscore is disabled!", Color.white);
				isUsingCheats=true;
			}
		}
		else if(code.toLowerCase().compareTo("die")==0)
		{
			endGame();
		}
		else if(code.toLowerCase().compareTo("show map")==0)
		{
			isShowMap=!isShowMap;
			if(isShowMap)
				text.addText("Map is showable!", Color.white);
			else
				text.addText("Map isn't showable!", Color.white);
		}
	}
}


