package tw.edu.sinica.iis.GUI.Drawing;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

import javax.swing.JFrame;

import zxaustin.game.shooting.core.SAsset;
import zxaustin.game.shooting.core.SGameHost;
import zxaustin.game.shooting.core.SUnitControler;
import zxaustin.game.shooting.core.SUnitLayer;
import zxaustin.game.shooting.interfaces.SGameUnit;
import zxaustin.game.shooting.interfaces.SPlayerInfo;
import zxaustin.game.shooting.interfaces.SPlayerUI;

public class FlowChatCanvas extends SGameHost {

	public DrawingUnit UserPC;
	public DrawingLine PC2NN;
	public DrawingDataPoint PC2NN_Point;
	public DrawingUnit ControlPC;
	public DrawingLine[] NN2DN;
	public DrawingUnit[] ClusterPC;
	public DrawingDataPoint[] NN2DN_Point;
	public DrawingCloud BackCloud;
	
	
	public boolean MousePressing;
	public int MX;
	public int MY;
	public SGameUnit MovingSGU;

	public FlowChatCanvas() {
		super();
		MousePressing = false;
		MovingSGU = null;
		setFramePeriod(60);
	}

	public void setUserPCAct(boolean b) {
		UserPC.setActivated(b);
	}

	public void setControlPCAct(boolean b) {
		ControlPC.setActivated(b);
	}

	public void setClusterPCAct(boolean b) {
		for (int i = 0; i < ClusterPC.length; i++) {
			ClusterPC[i].setActivated(b);
		}
	}

	public SGameUnit getUnitFromPosition(double x, double y) {
		for (int i = getControler().getUnits().size() - 1; i >= 0; i--) {
			Rectangle tmp = getControler().getUnits().get(i).getAABB();
			if (tmp == null) {
				// return null;
			} else {
				if (tmp.contains(new Point((int) x, (int) y))) {
					return getControler().getUnits().get(i);
				}
			}
		}
		return null;
	}

	@Override
	public void gameOver() {
		// TODO Auto-generated method stub

	}

	@Override
	public void gamePause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void gameReady() {
		// TODO Auto-generated method stub
		if (getControler() != null) {
			getControler().dispose();
		}
		if (getLayer() != null) {
			getLayer().dispose();
		}

		setLayer(new SUnitLayer(this, 3));
		setControler(new SUnitControler(this));
		setPlayerInfos(new LinkedList<SPlayerInfo>());
		setPlayerUIs(new LinkedList<SPlayerUI>());

		drawElementsInit();

		// setUserPCAct(true);
	}

	public void drawElementsInit() {
		UserPC = new DrawingComputer(this);
		UserPC.setX(50);
		UserPC.setY(96);
		addUnit(UserPC, 2);

		BackCloud = new DrawingCloud(this);
		addUnit(BackCloud, 0);
		
		ControlPC = new DrawingNameNode(this);
		ControlPC.setX(200);
		ControlPC.setY(96);
		addUnit(ControlPC, 2);
		BackCloud.contents.add(ControlPC);
		
		
		PC2NN = new DrawingLine(this, UserPC, ControlPC);
		addUnit(PC2NN, 1);

		PC2NN_Point = new DrawingDataPoint(this, PC2NN);
		addUnit(PC2NN_Point, 1);

		int CN = 5;
		ClusterPC = new DrawingDataNode[CN];
		NN2DN = new DrawingLine[CN];
		NN2DN_Point = new DrawingDataPoint[CN];

		double ClusterCenterX = 450;
		double ClusterCenterY = 170;
		double randomR = 150;
		for (int i = 0; i < ClusterPC.length; i++) {
			ClusterPC[i] = new DrawingDataNode(this);
			switch (i) {
			case 0:
				ClusterPC[i].setX(274);
				ClusterPC[i].setY(44);
				break;
			case 1:
				ClusterPC[i].setX(423);
				ClusterPC[i].setY(42);
				break;
			case 2:
				ClusterPC[i].setX(536);
				ClusterPC[i].setY(70);
				break;
			case 3:
				ClusterPC[i].setX(468);
				ClusterPC[i].setY(110);
				break;
			case 4:
				ClusterPC[i].setX(350);
				ClusterPC[i].setY(126);
				break;
			default:
				ClusterPC[i].setX(ClusterCenterX
						+ (randomR * (2 * Math.random() - 1)));
				ClusterPC[i].setY(ClusterCenterY
						+ (randomR * (2 * Math.random() - 1)));
			}

			addUnit(ClusterPC[i], 2);
			BackCloud.contents.add(ClusterPC[i]);
			
			NN2DN[i] = new DrawingLine(this, ControlPC, ClusterPC[i]);
			addUnit(NN2DN[i], 1);

			NN2DN_Point[i] = new DrawingDataPoint(this, NN2DN[i]);
			addUnit(NN2DN_Point[i], 1);
		}
	}

	@Override
	public void gameResume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void gameStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initial() {
		// TODO Auto-generated method stub
		Canvas winCanvas = new Canvas();
		setWindowCanvas(winCanvas);
		setBg(new BGCloud());
		winCanvas.setBackground(new Color(180, 200, 255));
		winCanvas.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				MousePressing = false;
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

				MousePressing = true;
				MX = arg0.getX();
				MY = arg0.getY();
				MovingSGU = FlowChatCanvas.this.getUnitFromPosition(
						arg0.getX(), arg0.getY());
				// System.out.println(arg0.getX()+","+arg0.getY());
				// SGameUnit sgu =
				// FlowChatCanvas.this.getUnitFromPosition(arg0.getX(),
				// arg0.getY());
				// if(sgu!=null){
				// System.out.println(sgu);
				// MovingSGU = sgu;
				// }
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		winCanvas.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub
				// if(MovingSGU!=null){
				// MovingSGU.setX(arg0.getX());
				// MovingSGU.setY(arg0.getY());
				// }
				int tmpX = arg0.getX();
				int tmpY = arg0.getY();
				if(getWindowCanvas().getBounds().contains(new Point(tmpX, tmpY))){
					MX = arg0.getX();
					MY = arg0.getY();
				}
				
			}
		});

		setAsset(new SAsset());
	}

	@Override
	public boolean isGameOver() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update(long diffTime) {
		// TODO Auto-generated method stub
		if (MovingSGU != null) {
			MovingSGU.setX(MX);
			MovingSGU.setY(MY);
		}
		super.update(diffTime);
		
	}

	public void allOff() {
		UserPC.setActivated(false);
		PC2NN_Point.direct = true;
		PC2NN_Point.setActivated(false);

		ControlPC.setActivated(false);

		for (int i = 0; i < ClusterPC.length; i++) {
			ClusterPC[i].setActivated(false);
			NN2DN_Point[i].direct = true;
			NN2DN_Point[i].setActivated(false);
		}
	}

	public void showPublicNN() {
		UserPC.setActivated(true);
		PC2NN_Point.direct = true;
		PC2NN_Point.setActivated(true);

		ControlPC.setActivated(true);

		for (int i = 0; i < ClusterPC.length; i++) {
			ClusterPC[i].setActivated(false);
			NN2DN_Point[i].direct = true;
			NN2DN_Point[i].setActivated(false);
		}
	}

	public void showPrivateNN() {
		UserPC.setActivated(false);
		PC2NN_Point.direct = true;
		PC2NN_Point.setActivated(false);

		ControlPC.setActivated(true);

		for (int i = 0; i < ClusterPC.length; i++) {
			ClusterPC[i].setActivated(true);
			NN2DN_Point[i].direct = false;
			NN2DN_Point[i].setActivated(true);
		}
	}
	
	public void showPrivateDN() {
		UserPC.setActivated(false);
		PC2NN_Point.direct = true;
		PC2NN_Point.setActivated(false);

		ControlPC.setActivated(true);

		for (int i = 0; i < ClusterPC.length; i++) {
			ClusterPC[i].setActivated(true);
			NN2DN_Point[i].direct = true;
			NN2DN_Point[i].setActivated(true);
		}
	}

	public void showNN() {
		UserPC.setActivated(false);
		PC2NN_Point.direct = true;
		PC2NN_Point.setActivated(false);

		ControlPC.setActivated(true);

		for (int i = 0; i < ClusterPC.length; i++) {
			ClusterPC[i].setActivated(false);
			NN2DN_Point[i].direct = false;
			NN2DN_Point[i].setActivated(false);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FlowChatCanvas test = new FlowChatCanvas();
		// System.out.println(test.UserPC);
		window.add(test.getWindowCanvas());
		window.setSize(600,200);
		window.setVisible(true);

		test.start();
		test.getWindowCanvas().requestFocus();
		test.setUserPCAct(true);
	}
}
