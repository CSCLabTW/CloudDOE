package tw.edu.sinica.iis.GUI.Drawing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import zxaustin.game.shooting.core.SAnimation;
import zxaustin.game.shooting.core.SGameHost;
import zxaustin.game.shooting.interfaces.SGameUnit;

public class DrawingCloud extends SGameUnit{
	
	public LinkedList<SGameUnit> contents;
	
	public DrawingCloud(SGameHost m){
		super(m);
		contents = new LinkedList<SGameUnit>();
	}
	
	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		if(contents.size()==0){
			return;
		}
				
		Rectangle rec = null;
		for(int i=0;i<contents.size();i++){
			Rectangle tmpR = contents.get(i).getAABB();
			if(rec == null){
				if(tmpR != null){
					rec = new Rectangle(tmpR);
				}
			}else{
				rec = rec.union(tmpR);
			}
		}
		
		if(rec == null){
			return;
		}else{
			rec.grow(10, 10);
			g.setColor(new Color(200, 255, 100, 100));
			g.fillRoundRect(rec.x, rec.y, rec.width, rec.height, 20, 20);
		}
		
		
	}
	
	@Override
	public Rectangle getAABB() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public SAnimation[] loadImg(){
		return null;
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

	@Override
	public BufferedImage getNowImg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUnitID() {
		// TODO Auto-generated method stub
		return "DrawingCloud";
	}
}
