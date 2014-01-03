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
