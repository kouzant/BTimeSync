package election;

import java.io.Serializable;

public class Nodes implements Comparable<Nodes>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2950675856529329217L;
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
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Node:").append("\n");
		sb.append("IP Address: ").append(getIpAddr()).append("\n");
		sb.append("UID: ").append(getUID()).append("\n");
		return sb.toString();
	}

	@Override
	public int compareTo(Nodes o) {
		return this.UID.compareTo(o.UID);
	}
}
