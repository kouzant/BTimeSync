package election;

public class Nodes {
	private String ipAddr;
	private String UID;
	
	public Nodes(String ipAddr, String UID){
		this.ipAddr = ipAddr;
		this.UID = UID;
	}
	
	public String getIpAddr(){
		return ipAddr;
	}
	public String getUID(){
		return UID;
	}
}
