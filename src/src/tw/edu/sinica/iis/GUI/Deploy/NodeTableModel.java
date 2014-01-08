package tw.edu.sinica.iis.GUI.Deploy;

import java.util.LinkedList;

import javax.swing.table.AbstractTableModel;

public abstract class NodeTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 9237543265L;

	private String[] columnDef = { "IP", "UserName", "Password" };

	public LinkedList<NodeStructure> Content = null;

	public NodeTableModel() {
		Content = new LinkedList<NodeStructure>();
	}

	public void add(String inIP, String User, String inPSW) {
		Content.add(new NodeStructure(false, inIP, User, inPSW));
	}

	public void add(LinkedList<NodeStructure> datas) {
		for (int i = 0; i < datas.size(); i++) {
			Content.add(datas.get(i));
		}
	}

	public void del(int i) {
		Content.remove(i+getNNCount());
	}
	
	public void clear(){
		Content.removeAll(Content);
	}
	
	public boolean checkPass(){
		if(getNNCount()>0 && getRowCount() > 0){
			return true;
		}
		return false;
	}
	
	/* TODO: should be more efficient */
	public boolean checkDup(final String ip) {
		for(int i = 0; i < Content.size(); i++) {
			if(ip.equals(Content.get(i).IP)) return true;
		}
		return false;
	}
	
	@Override
	public int getColumnCount() {
		return columnDef.length;
	}

	@Override
	public int getRowCount() {
		return Content.size()-getNNCount();
	}
	
	public int getNNCount(){
		for(int i=0;i<Content.size();i++){
			if(!Content.get(i).Master.booleanValue()){
				return i;
			}
		}
		return 0;
	}
	
	public NodeStructure getNN(){
		for(int i=0;i<Content.size();i++){
			if(Content.get(i).Master.booleanValue()){
				return Content.get(i);
			}
		}
		return null;
	}
	
	@Override
	public Object getValueAt(int x, int y) {
		int newX = x + getNNCount();
		if (newX >= 0 && newX < Content.size()) {
			if (y >= 0 && y < getColumnCount()) {
				if (y == 0) {
					return Content.get(newX).IP;
				} else if (y == 1) {
					return Content.get(newX).User;
				} else if (y == 2) {
					return Content.get(newX).Password;
				}
			}
		}
		return null;
	}

	@Override
	public String getColumnName(int idx) {
		if (idx < columnDef.length) {
			return columnDef[idx];
		}
		return "";
	}

	@Override
	public Class<?> getColumnClass(int arg0) {
		return super.getColumnClass(arg0);
	}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {
		return;
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

	@Override
	public void fireTableDataChanged() {
		super.fireTableDataChanged();
		ChangeEvent();
	}

	public abstract void ChangeEvent();
}
