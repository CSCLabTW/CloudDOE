package tw.edu.sinica.iis.GUI.Drawing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;

import zxaustin.game.shooting.interfaces.SBackGround;

public class BGCloud extends SBackGround{
	
	public BufferedImage bgImg;
	public BufferedImage cloud1;
	public BufferedImage cloud2;
	public BufferedImage cloud3;
	public double c1x;
	public double c1v;
	public double c2x;
	public double c2v;
	public double c3x;
	public double c3v;
	
	public BGCloud(){
		try {
			String root = this
			.getClass().getResource("/").toString()
			 + "Asset" + File.separator + "background00.png";
			bgImg = ImageIO.read(new URL(root));
			getCloud1();
			getCloud2();
			getCloud3();
			c1x = 0.0;
			c2x = 0.0;
			c3x = 0.0;
			c1v = 30.0;
			c2v = 20.0;
			c3v = 10.0;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	public void Drawline(Graphics2D g2d, int x,int start_y, int y){
		int colorBlack = 209;
		int alpha = 0;
		for(int i=start_y;i<=y;i++){
			g2d.setColor(new Color(colorBlack, colorBlack, colorBlack, alpha));
			
			g2d.fillRect(x, i, 1, 1);
			
			colorBlack++;
			colorBlack = Math.min(colorBlack, 255);
			alpha += 10;
			alpha = Math.min(alpha, 255);
		}
	}
	
	public BufferedImage getCloud1(){
		try {
			String root = this
			.getClass().getResource("/").toString()
			 + "Asset" + File.separator + "cloud1.png";
			cloud1 = ImageIO.read(new URL(root));

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return cloud1;
	}
	
	public BufferedImage getCloud2(){
		try {
			String root = this
			.getClass().getResource("/").toString()
			 + "Asset" + File.separator + "cloud2.png";
			cloud2 = ImageIO.read(new URL(root));

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return cloud2;
	}
	
	public BufferedImage getCloud3(){
		try {
			String root = this
			.getClass().getResource("/").toString()
			 + "Asset" + File.separator + "cloud3.png";
			cloud3 = ImageIO.read(new URL(root));

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return cloud3;
	}
	
	
	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
		g.drawImage(bgImg, 0, -100, null);
		int h = -130;
		g.drawImage(cloud3, (int)c3x, h, null);
		if(c3x > 0){
			g.drawImage(cloud3, (int)(c3x-1000), h, null);
		}
		g.drawImage(cloud2, (int)c2x, h, null);
		if(c2x > 0){
			g.drawImage(cloud2, (int)(c2x-1000), h, null);
		}
		g.drawImage(cloud1, (int)c1x, h, null);
		if(c1x > 0){
			g.drawImage(cloud1, (int)(c1x-1000), h, null);
		}
		
		
		
	}

	@Override
	public void update(long diffTime) {
		// TODO Auto-generated method stub
		c1x -= c1v * diffTime / 1000.0;
		c2x -= c2v * diffTime / 1000.0;
		c3x -= c3v * diffTime / 1000.0;
		if(c1x < -400){
			c1x += 1000;
		}
		if(c2x < -400){
			c2x += 1000;
		}
		if(c3x < -400){
			c3x += 1000;
		}
	}

}
