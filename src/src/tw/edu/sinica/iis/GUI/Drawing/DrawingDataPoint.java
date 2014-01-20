package tw.edu.sinica.iis.GUI.Drawing;

import java.awt.Rectangle;
import javax.imageio.ImageIO;

import zxaustin.game.shooting.core.SAnimation;
import zxaustin.game.shooting.core.SGameHost;

public class DrawingDataPoint extends DrawingUnit{

	public DrawingLine Line;
	public double posision;
	public double velocity;
	public boolean direct;
	
	public DrawingDataPoint(SGameHost m) {
		super(m);
		Line = null;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Rectangle getAABB() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public DrawingDataPoint(SGameHost m, DrawingLine l){
		super(m);
		Line = l;
		posision = 0.0;
		velocity = 0.7;
		direct = true;
	}

	@Override
	public SAnimation loadActivatedPic() {
		// TODO Auto-generated method stub
		try {
			SAnimation tmp = new SAnimation(ImageIO.read(getClass().getResource("/assets/point.png")),
					new long[] { 500, 500 }, 2, 0, 0, 16, 16, 1, 2, null);
			return tmp;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public SAnimation loadNormalPic() {
		// TODO Auto-generated method stub
		try {
			SAnimation tmp = new SAnimation(ImageIO.read(getClass().getResource("/assets/point.png")),
					new long[] { 100000 }, 1, 32, 0, 16, 16, 1, 1, null);
			return tmp;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public void update(long diffTime) {
		// TODO Auto-generated method stub
		super.update(diffTime);
		
		if(direct){
			posision += velocity * diffTime / 1000.0;
		}else{
			posision -= velocity * diffTime / 1000.0;
		}
		
		if(posision > 1.0){
			posision = 0.0;
		}
		if(posision < 0.0){
			posision = 1.0;
		}
		
		if(Line != null){
			double sx,sy,ex,ey;
			
			sx = Line.StartObj.getX();
			sy = Line.StartObj.getY();
			ex = Line.EndObj.getX();
			ey = Line.EndObj.getY();
			
			setX(sx + (ex - sx) * posision);
			setY(sy + (ey - sy) * posision);
		}
		
		
		
	}
	
	@Override
	public String getUnitID() {
		// TODO Auto-generated method stub
		return "DrawingDataPoint";
	}

}
