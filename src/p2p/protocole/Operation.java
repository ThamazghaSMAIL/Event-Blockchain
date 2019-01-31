package p2p.protocole;

public class Operation {

	public Operation(String paquet, String ipaddress, int port) {
		this.paquet = paquet;
		this.ipaddress = ipaddress;
		this.port = port;
		this.version = "1.0";
		this.flag = null;
		this.contacts = null;
	}

	public Operation() {}

	public String rest;
	public String paquet;
	public String ipaddress; 
	public int port	;
	//TODO c'est quoi 
	public String version;
	public String flag;
	//quand flag = saturated on affecte les contacts de l'instance
	public String contacts;
	public int upper_nounce;
	/**
	 * temps qui reste pour le minage
	 */
	public int time;
	/**
	 * le numéro du noeud ou il en sont dans le minage
	 */
	public int node_minage;

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
	@Override
	public String toString() {
		return "Operation [paquet=" + paquet + ", ipaddress=" + ipaddress + ", port=" + port + ", rest=" + rest
				+ ", version=" + version + ", flag=" + flag + ", contacts=" + contacts + "]";
	}

	public int getUpper_nounce() {
		return upper_nounce;
	}

	public void setUpper_nounce(int upper_nounce) {
		this.upper_nounce = upper_nounce;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getNode_minage() {
		return node_minage;
	}

	public void setNode_minage(int node_minage) {
		this.node_minage = node_minage;
	}
}
