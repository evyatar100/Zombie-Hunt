import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Robot;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Listener extends JPanel implements MouseMotionListener, MouseListener, KeyListener
{
	boolean gameOn;
	MainPanel mp;

	Robot robot;
	
	int sensitivity;
	String now;
	final boolean remotePointer = "vnc".equalsIgnoreCase(System.getenv("ZH_DISPLAY_MODE"));
	
	boolean [] moveState = new boolean[4];

	public Listener(MainPanel mp)
	{
		super();

		this.mp=mp;

		try {
			robot = new Robot();}
		catch (Exception e){}
		resetMouse();

		mp.addMouseMotionListener(this);
		mp.addMouseListener(this);
		mp.addKeyListener(this);
		mp.setFocusable(true);

		for (int i = 0; i < 4; i++) 
		{
			moveState[i]=false;
		}
		gameOn=false;
	}
	public void setSensitivity(int x)
	{
		sensitivity=x;
		now = x + "%";
	}
	public void resetMouse()
	{
		if (robot==null)
			return;
		try
		{
			Point onScreen = mp.getLocationOnScreen();
			mp.locationOnScreen.setLocation(onScreen);
		}
		catch (IllegalComponentStateException e) {}
		robot.mouseMove(mp.locationOnScreen.x+(int)mp.magozPoint.x, mp.locationOnScreen.y+(int)mp.magozPoint.y);
	}
	public void keyPressed(KeyEvent e) 
	{
		if(!gameOn) 
			switch (e.getKeyCode())
			{
			case 27:
				mp.resumeGame();
				return;
				
			case 10:
				mp.newGame();
				return;
				
			case 113:
				mp.cheat();
				return;
				
			default:
				return;
			}
		switch (e.getKeyCode())
		{
		case 27:
			mp.pauseGame();
			return;
		default:
			break;
		}
		if(!mp.life.isAlive)
			return;
		
		switch (e.getKeyChar())
		{
		case 's':
			if(!moveState[1])
				moveState[0]=true;
			return;

		case 'w':
			if(!moveState[0])
				moveState[1]=true;
			return;

		case 'd':
			if(!moveState[3])
				moveState[2]=true;
			return;

		case 'a':
			if(!moveState[2])
				moveState[3]=true;
			return;

		default:
			break;
		}
	}
	public void keyReleased(KeyEvent e) 
	{
		if(!gameOn) return;
		switch (e.getKeyChar())
		{

		case 's':
			moveState[0]=false;
			break;

		case 'w':
			moveState[1]=false;
			break;

		case 'd':
			moveState[2]=false;
			break;

		case 'a':
			moveState[3]=false;
			break;
			
		default:
			break;
		}
	}
	public void keyTyped(KeyEvent e) 
	{
		if(!gameOn || !mp.life.isAlive) return;
		switch (e.getKeyChar())
		{
		case 'r':
			mp.gun.reload();
			return;
		case 'm':
			mp.showMap();
			return;
		default:
			break;
		}
	}


	public void mouseMoved(MouseEvent e)
	{
		int mouseX = e.getXOnScreen();
		int mouseY = e.getYOnScreen();
		int centerX = mp.locationOnScreen.x+(int)mp.magozPoint.x;
		int centerY = mp.locationOnScreen.y+(int)mp.magozPoint.y;
		int deltaX = mouseX-centerX;
		int deltaY = mouseY-centerY;

		if(!gameOn || (deltaX==0 && deltaY==0))
			return;

		mp.rotateYFix(Math.PI*sensitivity/100000*deltaX, mp.pMe.x, mp.pMe.y, mp.pMe.z);
		mp.rotateXFix(Math.PI*sensitivity/200000*deltaY, mp.pMe.x, mp.pMe.y, mp.pMe.z);
		if(!remotePointer)
			resetMouse();
	}

	public void mouseClicked(MouseEvent e) 
	{
		if(!gameOn) return;
		if (mp.isDebugMode) return;
		switch (e.getButton())
		{
		case MouseEvent.BUTTON1:
			mp.shoot();
			break;
			
		case MouseEvent.BUTTON2:
			mp.shoot();
			break;
			
		case MouseEvent.BUTTON3:
			mp.hit();
			break;

		default:
			break;
		}
	}

	public void move()
	{	
		if(!mp.life.isAlive || !gameOn)
		{
			for (int i = 0; i < moveState.length; i++) 
			{
				moveState[i]=false;
			}
			return;
		}
		if(moveState[0])
			mp.moveBackward();
		if(moveState[1])
			mp.moveForward();
		if(moveState[2])
			mp.moveLeft();	
		if(moveState[3])
			mp.moveRight();
	}
	
	public void mouseEntered(MouseEvent e) 
	{
		if(gameOn && !remotePointer)
			resetMouse();

	}
	
	public void game(boolean a)
	{
		if(a)
		{
			gameOn=true;
			if(!remotePointer)
				resetMouse();
		}
		else
		{
			gameOn=false;
		}
	}
	
	//************************************************//
	
	public void mouseExited(MouseEvent e) 
	{
	}

	public void mousePressed(MouseEvent e) 
	{
		if (gameOn && mp.isDebugMode)
			mp.requestDebugKill();

	}

	public void mouseReleased(MouseEvent e) 
	{

	}

	public void mouseDragged(MouseEvent e) 
	{
		mouseMoved(e);
	}

}
