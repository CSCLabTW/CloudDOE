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
