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
	
	final String [] arr = {"Very High", "High", "Normal", "Low", "Very Low"}; 

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
		now = arr[x/250-2];
	}
	public void resetMouse()
	{
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


	boolean isMove = true;
	public void mouseMoved(MouseEvent e)
	{
		if(!gameOn) return;
		if(!isMove)
			return;
		mp.rotateYFix(Math.PI/sensitivity*(e.getXOnScreen()-(mp.locationOnScreen.x+(int)mp.magozPoint.x)), mp.pMe.x, mp.pMe.y, mp.pMe.z);
		mp.rotateXFix(Math.PI/(sensitivity*2)*(e.getYOnScreen()-(mp.locationOnScreen.y+(int)mp.magozPoint.y)), mp.pMe.x, mp.pMe.y, mp.pMe.z);

		isMove=false;
		resetMouse();
		isMove=true;
	}

	public void mouseClicked(MouseEvent e) 
	{
		if(!gameOn) return;
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
		if(!gameOn) return;
		isMove=false;
		resetMouse();
		isMove=true;

	}
	
	public void game(boolean a)
	{
		if(a)
		{
			gameOn=true;
			isMove=false;
			resetMouse();
			isMove=true;
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

	}

	public void mouseReleased(MouseEvent e) 
	{

	}

	public void mouseDragged(MouseEvent e) 
	{

	}

}
