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
