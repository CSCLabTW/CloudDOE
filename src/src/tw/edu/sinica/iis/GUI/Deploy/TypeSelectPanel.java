package tw.edu.sinica.iis.GUI.Deploy;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class TypeSelectPanel extends JPanel implements ItemListener{

	private static final long serialVersionUID = 238547832576L;
	public JComboBox<String> Selector = null;
	public final String[] Selections = {"open", "semi", "close"};
	public JRadioButton[] RadioSelections = null;
	public ButtonGroup grp = null;
	public JPanel CardBox = null;
	
	public TypeSelectPanel(){
		init();
	}
	
	public void init(){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		RadioSelections = new JRadioButton[Selections.length];
		grp = new ButtonGroup();
		JPanel RPanel = new JPanel();
		RPanel.setLayout(new FlowLayout());
		RPanel.setMaximumSize(new Dimension(600, 40));
		for(int i=0;i<RadioSelections.length;i++){
			if(i==0){
				RadioSelections[i] = new JRadioButton(Selections[i],true);
			}else{
				RadioSelections[i] = new JRadioButton(Selections[i],false);
			}
			RadioSelections[i].setActionCommand(Selections[i]);
			RadioSelections[i].addItemListener(this);
			grp.add(RadioSelections[i]);
			RPanel.add(RadioSelections[i]);
		}
		
		this.add(RPanel);
		
		
		Selector = new JComboBox<String>(Selections);
		Selector.setMaximumSize(new Dimension(200, 30));
		Selector.setAlignmentX(Component.CENTER_ALIGNMENT);
		Selector.addItemListener(this);
//		this.add(Selector);
		
		this.add(getCardBox());
	}
	
	public JPanel getCardBox(){
		if(CardBox == null){
			CardBox = new JPanel();
			CardBox.setBorder(BorderFactory.createTitledBorder("description"));
			CardBox.setLayout(new CardLayout());
			CardBox.setMaximumSize(new Dimension(800, 400));
			
			CardBox.add(getCard1(), Selections[0]);
			CardBox.add(getCard2(), Selections[1]);
			CardBox.add(getCard3(), Selections[2]);
		}
		return CardBox;
	}
	
	public JPanel getCard1(){
		JPanel tmp = new JPanel();
		tmp.add(new JLabel("開放式  選這個不需要填Bridge"));
		return tmp;
	}
	
	public JPanel getCard2(){
		JPanel tmp = new JPanel();
		tmp.add(new JLabel("半封閉式  選這個需要填Bridge"));
		return tmp;
	}
	
	public JPanel getCard3(){
		JPanel tmp = new JPanel();
		tmp.add(new JLabel("封閉式  選這個需要填Bridge"));
		return tmp;
	}
	
	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
		CardLayout cl = (CardLayout)CardBox.getLayout();
//		cl.show(CardBox, (String)arg0.getItem());
//		System.out.println(Selector.getSelectedIndex());
		if(grp.getSelection().isSelected()){
//			System.out.println(grp.getSelection().getActionCommand());
			cl.show(CardBox, grp.getSelection().getActionCommand());
		}
		
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame tmp = new JFrame("example");
		tmp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tmp.add(new TypeSelectPanel());
		tmp.setSize(600, 600);
//		tmp.pack();
		tmp.setVisible(true);
	}

	

	

}
