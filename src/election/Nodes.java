package election;

public class Nodes implements Comparable<Nodes>{
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
		sb.append("IP Address: ").append(getIpAddr()).append("\n");
		sb.append("UID: ").append(getUID()).append("\n");
		return sb.toString();
	}

	@Override
	public int compareTo(Nodes o) {
		return this.UID.compareTo(o.UID);
	}
}
