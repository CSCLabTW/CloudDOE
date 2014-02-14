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

package tw.edu.sinica.iis.GUI.Deploy;

public class NodeStructure {
	public Boolean Master;
	public String IP;
	public String User;
	public String Password;

	public NodeStructure() {
		Master = new Boolean(false);
		IP = "";
		User = "";
		Password = "";
	}

	public NodeStructure(boolean isMaster, String inIP, String inUser,
			String inPassword) {
		Master = new Boolean(isMaster);
		IP = inIP;
		User = inUser;
		Password = inPassword;
	}

	public void setMaster(boolean b) {
		Master = new Boolean(b);
	}
}
