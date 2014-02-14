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

package tw.edu.sinica.iis.GUI.Operate;

import java.util.LinkedList;

import javax.swing.table.AbstractTableModel;

public class HDFSTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1149438303003980093L;

	// public String[] fileList;
	public LinkedList<String> fileList;

	public HDFSTableModel() {
		fileList = new LinkedList<String>();
	}

	public HDFSTableModel(String[] f) {
		setFiles(f);
	}

	public void setFiles(String[] f) {
		fileList = new LinkedList<String>();
		for (int i = 0; i < f.length; i++) {
			fileList.add(f[i]);
		}
	}

	public void setFiles(LinkedList<String> f) {
		fileList = f;
	}

	public LinkedList<String> getFiles() {
		return fileList;
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public int getRowCount() {
		return fileList.size();
	}

	@Override
	public String getColumnName(int col) {
		if (col == 0) {
			return "Input File(s) :";
		}
		return "";
	}

	@Override
	public Object getValueAt(int row, int col) {

		return fileList.get(row);
	}

	/*
	 * Don't need to implement this method unless your table's editable.
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
		// Note that the data/cell address is constant,
		// no matter where the cell appears onscreen.
		return false;
	}

	/*
	 * Don't need to implement this method unless your table's data can change.
	 */
	@Override
	public void setValueAt(Object value, int row, int col) {

	}

}
