package tw.edu.sinica.iis.GUI.Drawing;

import java.awt.image.BufferedImage;
import zxaustin.game.shooting.core.SAnimation;
import zxaustin.game.shooting.core.SGameHost;
import zxaustin.game.shooting.interfaces.SGameUnit;

public abstract class DrawingUnit extends SGameUnit{
	
	private boolean Activated;
	
	public DrawingUnit(SGameHost m){
		super(m);
		Activated = false;
	}
	
	@Override
	public SAnimation[] loadImg() {
		// TODO Auto-generated method stub
		SAnimation[] frame = null;
		try {
			frame = new SAnimation[2];
			frame[0] = loadNormalPic();
			frame[1] = loadActivatedPic();
			getParent().getAsset().ImgAsset.put(getUnitID(), frame);
			return frame;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	public abstract SAnimation loadNormalPic();
	public abstract SAnimation loadActivatedPic();
	
	@Override
	public BufferedImage getNowImg() {
		// TODO Auto-generated method stub
		if(isActivated()){
			return getFrame(1).getImage();
		}else{
			return getFrame(0).getImage();
		}
	}
	
	@Override
	public boolean isAlive() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void defeated() {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void hit(SGameUnit u) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public boolean validHit(SGameUnit u) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setActivated(boolean activated) {
		Activated = activated;
	}

	public boolean isActivated() {
		return Activated;
	}
	
}
