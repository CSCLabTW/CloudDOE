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

import javax.imageio.ImageIO;

import zxaustin.game.shooting.core.SAnimation;
import zxaustin.game.shooting.core.SGameHost;

public class DrawingDataNode extends DrawingUnit {
	
	public DrawingDataNode(SGameHost m){
		super(m);
	}
	
	@Override
	public SAnimation loadActivatedPic() {
		// TODO Auto-generated method stub
		try {
			SAnimation tmp = new SAnimation(ImageIO.read(getClass().getResource("/assets/DataNode.png")),
					new long[] { 1000,300,200,300 }, 4, 0, 0, 48, 48, 2, 2, null);
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
			SAnimation tmp = new SAnimation(ImageIO.read(getClass().getResource("/assets/DataNode.png")),
					new long[] { 1000 }, 1, 0, 0, 48, 48, 1, 1, null);
			return tmp;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public String getUnitID() {
		// TODO Auto-generated method stub
		return "DrawingDataNode";
	}

	public static void main(String[] arg) {
		
		
	}
}

