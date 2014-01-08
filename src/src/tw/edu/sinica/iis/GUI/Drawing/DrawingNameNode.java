package tw.edu.sinica.iis.GUI.Drawing;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;

import zxaustin.game.shooting.core.SAnimation;
import zxaustin.game.shooting.core.SGameHost;

public class DrawingNameNode extends DrawingUnit {
	
	public DrawingNameNode(SGameHost m){
		super(m);
	}
	
	@Override
	public SAnimation loadActivatedPic() {
		// TODO Auto-generated method stub
		try {
			String root = this
			.getClass().getResource("/").toString()
			 + "Asset" + File.separator + "NameNode.png";
			SAnimation tmp = new SAnimation(ImageIO.read(new URL(root)),
					new long[] { 500,500,500,500 }, 4, 0, 0, 64, 64, 2, 2, null);
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
			String root = this
			.getClass().getResource("/").toString()
			 + "Asset" + File.separator + "NameNode.png";
			SAnimation tmp = new SAnimation(ImageIO.read(new URL(root)),
					new long[] { 1000 }, 1, 0, 0, 64, 64, 1, 1, null);
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
		return "DrawingNameNode";
	}
	
	
	
	public static void main(String[] arg) {
		
		
	}
}

