/*
 * (C) Copyright 2013 The CloudDOE Project and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *      Wei-Chun Chung (wcchung@iis.sinica.edu.tw)
 *      Yu-Chun Wang (zxaustin@iis.sinica.edu.tw)
 * 
 * CloudDOE Project:
 *      http://clouddoe.iis.sinica.edu.tw/
 */

package tw.edu.sinica.iis.GUI.Drawing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import zxaustin.game.shooting.core.SAnimation;
import zxaustin.game.shooting.core.SGameHost;
import zxaustin.game.shooting.interfaces.SGameUnit;

public class DrawingLine extends SGameUnit{
	public SGameUnit StartObj;
	public SGameUnit EndObj;
	public BufferedImage LinePic;
	
	public DrawingLine(SGameHost m,SGameUnit s, SGameUnit e){
		super(m);
		StartObj = s;
		EndObj = e;
		try {
			LinePic = ImageIO.read(getClass().getResource("/assets/line.png"));
		} catch (Exception e2) {
			// TODO: handle exception
			e2.printStackTrace();
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
	
	
	@Override
	public BufferedImage getNowImg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUnitID() {
		// TODO Auto-generated method stub
		return "DrawingLine";
	}


	@Override
	public SAnimation[] loadImg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		Graphics2D g2d = (Graphics2D)g.create();
		double CenterX = (StartObj.getX() + EndObj.getX())/2.0;
		double CenterY = (StartObj.getY() + EndObj.getY())/2.0;
		double angle = Math.atan2((EndObj.getY() - StartObj.getY()) , (EndObj.getX() - StartObj.getX())) ;
		double LineLength = Math.sqrt(Math.pow(EndObj.getY() - StartObj.getY(), 2.0) + Math.pow(EndObj.getX() - StartObj.getX(), 2.0));
		g2d.translate(CenterX, CenterY);
		g2d.rotate(angle);
		g2d.scale(LineLength / 16.0, 0.5);
		
		
		g2d.drawImage(LinePic, null, -LinePic.getWidth()/2, -LinePic.getHeight()/2);
		

	}

}
