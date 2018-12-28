package p2p.protocole;

public class Request {

	
	public Request(String paquet, String ipaddress, int port) {
		this.paquet = paquet;
		this.ipaddress = ipaddress;
		this.port = port;
		this.version = "1.0";
		this.flag = null;
		this.contacts = null;
	}

	public Request() {}
	
	public String rest;
	public String paquet;
	public String ipaddress; 
	public int port	;
	//TODO c'est quoi 
	public String version;
	public String flag;
	//quand flag = saturated on affecte les contacts de l'instance
	public String contacts;
	
	public String getPaquet() {
		return paquet;
	}

	public void setPaquet(String paquet) {
		this.paquet = paquet;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getRest() {
		return rest;
	}

	public void setRest(String rest) {
		this.rest = rest;
	}
	
	
}
