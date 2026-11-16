/*
 * Created on Eiar 5767
 * upDate     Tamuz 5769
 * @author levian
 * for Student
 */
public class Matrix3D 
{
	final int size=4;
	double mat[][];

	public Matrix3D()
	{
		mat=new double[size][size];
		setIdentity();
	}
	public Matrix3D(Matrix3D temp)
	{
		this.mat = temp.mat;
	}
	
	public void setIdentity()
	{
		mat[0][0]=1.0;  mat[0][1]=0.0;  mat[0][2]=0.0; mat[0][3]=0.0;
		mat[1][0]=0.0;  mat[1][1]=1.0;  mat[1][2]=0.0; mat[1][3]=0.0;
		mat[2][0]=0.0;  mat[2][1]=0.0;  mat[2][2]=1.0; mat[2][3]=0.0;
		mat[3][0]=0.0;  mat[3][1]=0.0;  mat[3][2]=0.0; mat[3][3]=1.0;
	}
	
	public void setMatMove(double dx,double dy,double dz)
	{
		mat[0][0]=1.0;  mat[0][1]=0.0;  mat[0][2]=0.0; mat[0][3]=0.0;
		mat[1][0]=0.0;  mat[1][1]=1.0;  mat[1][2]=0.0; mat[1][3]=0.0;
		mat[2][0]=0.0;  mat[2][1]=0.0;  mat[2][2]=1.0; mat[2][3]=0.0;
		mat[3][0]=dx;   mat[3][1]=dy;   mat[3][2]=dz;  mat[3][3]=1.0;
	}

	public void setMatScale(double dx,double dy,double dz)
	{
		mat[0][0]=dx;   mat[0][1]=0.0;  mat[0][2]=0.0;  mat[0][3]=0.0;
		mat[1][0]=0.0;  mat[1][1]=dy;   mat[1][2]=0.0;  mat[1][3]=0.0;
		mat[2][0]=0.0;  mat[2][1]=0.0;  mat[2][2]=dz;   mat[2][3]=0.0;
		mat[3][0]=0.0;  mat[3][1]=0.0;  mat[3][2]=0.0;  mat[3][3]=1.0;
	}
	
	public void setMatScaleFix(double sx,double sy,double sz,double xf, double yf, double zf)
   {
      mat[0][0]=sx;         mat[0][1]=0.0;        mat[0][2]=0.0;       mat[0][3]=0.0;
      mat[1][0]=0.0;        mat[1][1]=sy;         mat[1][2]=0.0;       mat[1][3]=0.0;
      mat[2][0]=0.0;        mat[2][1]=0.0;        mat[2][2]=sz;        mat[2][3]=0.0;
      mat[3][0]=(1-sx)*xf;  mat[3][1]=(1-sy)*yf;  mat[3][2]=(1-sz)*zf; mat[3][3]=1.0;
   }

	public void setMatRotateX(double angel)
	{
		mat[0][0]=1.0;  mat[0][1]=0.0;               mat[0][2]=0.0;               mat[0][3]=0.0;
		mat[1][0]=0.0;  mat[1][1]=Math.cos(angel);   mat[1][2]=Math.sin(angel);   mat[1][3]=0.0;
		mat[2][0]=0.0;  mat[2][1]=-Math.sin(angel);  mat[2][2]=Math.cos(angel);   mat[2][3]=0.0;
		mat[3][0]=0.0;  mat[3][1]=0.0;               mat[3][2]=0.0;               mat[3][3]=1.0;
	}
	
	public void setMatRotateXFix(double angel, double xReal, double yReal, double zReal) 
	{
		Matrix3D temp=new Matrix3D();
		setMatMove(-xReal, -yReal, -zReal);
		temp.setMatRotateX(angel);
		mullMatMat(temp);
		temp.setMatMove(xReal, yReal, zReal);
		mullMatMat(temp);
	}

	public void setMatRotateY(double angel)
	{
		mat[0][0]=Math.cos(angel);  mat[0][1]=0.0;   mat[0][2]=Math.sin(angel);  mat[0][3]=0.0;
		mat[1][0]=0.0;              mat[1][1]=1.0;   mat[1][2]=0.0;              mat[1][3]=0.0;
		mat[2][0]=-Math.sin(angel); mat[2][1]=0.0;   mat[2][2]=Math.cos(angel);  mat[2][3]=0.0;
		mat[3][0]=0.0;              mat[3][1]=0.0;   mat[3][2]=0.0;              mat[3][3]=1.0;
	}
	
	public void setMatRotateYFix(double angel, double xReal, double yReal, double zReal) 
	{
		Matrix3D temp=new Matrix3D();
		setMatMove(-xReal, -yReal, -zReal);
		temp.setMatRotateY(angel);
		mullMatMat(temp);
		temp.setMatMove(xReal, yReal, zReal);
		mullMatMat(temp);
	}
	
	public void setMatRotateZ(double angel)
	{
		mat[0][0]=Math.cos(angel);  mat[0][1]=Math.sin(angel);   mat[0][2]=0.0;  mat[0][3]=0.0;
		mat[1][0]=-Math.sin(angel); mat[1][1]=Math.cos(angel);   mat[1][2]=0.0;  mat[1][3]=0.0;
		mat[2][0]=0.0;              mat[2][1]=0.0;               mat[2][2]=1.0;  mat[2][3]=0.0;
		mat[3][0]=0.0;              mat[3][1]=0.0;               mat[3][2]=0.0;  mat[3][3]=1.0;
	}
	
	public void setMatRotateZFix(double angel, double xReal, double yReal,double zReal) 
	{
		Matrix3D temp=new Matrix3D();
		setMatMove(-xReal, -yReal, -zReal);
		temp.setMatRotateZ(angel);
		mullMatMat(temp);
		temp.setMatMove(xReal, yReal, zReal);
		mullMatMat(temp);
	}
		
	public void setMatRotateAxis(Point3D p1, Point3D p2 , double teta)
	{
		Matrix3D m1= new Matrix3D();
		double a,b,c,d,l;
		
	    a = p2.x-p1.x; b = p2.y-p1.y; c = p2.z-p2.z;
	    l = (float) Math.sqrt(a*a+b*b+c*c);
	    a = a/l;   b = b/l;   c = c/l;
	    d = (float) Math.sqrt(b*b+c*c);

	    if (d == 0)
	
	    	this.setMatRotateXFix(p1.x,p1.y,p1.z, teta);
	    else
	    {
	      setMatMove(-p1.x,-p1.y,-p1.z);

          m1.setIdentity();                     // x axis
	      m1.mat[1][1] = c/d;  m1.mat[1][2] = b/d;
	      m1.mat[2][1] = -b/d; m1.mat[2][2] = c/d;
	      mullMatMat(m1);

	      m1.setIdentity();                     // y axis
	      m1.mat[0][0] = d;  m1.mat[0][2] = a;
	      m1.mat[2][0] = -a; m1.mat[2][2] = d;
	      mullMatMat(m1);

	      m1.setMatRotateZ(teta);               // Z axis
	      mullMatMat(m1);

          m1.setIdentity();                     // y axis
	      m1.mat[0][0] = d;  m1.mat[0][2] = -a;
	      m1.mat[2][0] = a; m1.mat[2][2] = d;
	      mullMatMat(m1);


          m1.setIdentity();                     // x axis
	      m1.mat[1][1] = c/d;  m1.mat[1][2] = -b/d;
	      m1.mat[2][1] = b/d;  m1.mat[2][2] = c/d;
	      mullMatMat(m1);

	      m1.setMatMove(p1.x,p1.y,p1.z);
	      mullMatMat(m1);
	    }

		
	}
	

	public void mullMatMat(Matrix3D aMat)
	{
		Matrix3D temp=new Matrix3D();
		for (int i= 0; i<size;i++)
			for (int j=0; j<size;j++)
			{
				temp.mat[i][j]=0;
				for (int k=0;k<size;k++)
					temp.mat[i][j] += mat[i][k] * aMat.mat[k][j];
			}
		mat=temp.mat;
	}
	
	public void mullAllPoints(double xr[], double yr[],double zr[], int aNum)
	{
		double xTemp,yTemp,zTemp;
		for(int i=0;i<aNum;i++)
		{
			xTemp=xr[i]; yTemp= yr[i]; zTemp=zr[i];
			xr[i]=xTemp*mat[0][0] + yTemp*mat[1][0] + zTemp*mat[2][0] + 1*mat[3][0]; 
			yr[i]=xTemp*mat[0][1] + yTemp*mat[1][1] + zTemp*mat[2][1] + 1*mat[3][1]; 
			zr[i]=xTemp*mat[0][2] + yTemp*mat[1][2] + zTemp*mat[2][2] + 1*mat[3][2]; 
		}
		
	}
	


}
