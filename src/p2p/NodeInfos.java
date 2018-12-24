package p2p;

public class NodeInfos {
	public NodeInfos(String ipAdress, int port) {
		this.ipAdress = ipAdress;
		this.port = port;
	}
	
	
	
	String ipAdress;
	int port;
	
	
	public String getIpAdress() {
		return ipAdress;
	}
	public void setIpAdress(String ipAdress) {
		this.ipAdress = ipAdress;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
}
