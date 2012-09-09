package election;

import java.io.Serializable;

public class Nodes implements Comparable<Nodes>, Serializable{
	private static final long serialVersionUID = -2950675856529329217L;
	private String ipAddr;
	private String UID;
	private int rmiPort;
	private long rtt;
	private long time;
	private int fix;
	
	public Nodes(String ipAddr, String UID, int rmiPort){
		this.ipAddr = ipAddr;
		this.UID = UID;
		this.rmiPort = rmiPort;
		rtt = 0L;
		time = 0L;
		fix = 0;
	}
	
	public int getFix(){
		return fix;
	}
	public void setFix(int fix){
		this.fix = fix;
	}
	public long getRtt(){
		return rtt;
	}
	public void setRtt(long rtt){
		this.rtt = rtt;
	}
	public long getTime(){
		return time;
	}
	public void setTime(long time){
		this.time = time;
	}
	public String getIpAddr(){
		return ipAddr;
	}
	public String getUID(){
		return UID;
	}
	public int getRmiPort(){
		return rmiPort;
	}
	public boolean isLeader(){
		return Variables.isLeader();
	}
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Node:").append("\n");
		sb.append("IP Address: ").append(getIpAddr()).append("\n");
		sb.append("RMI Port: ").append(rmiPort).append("\n");
		sb.append("UID: ").append(getUID()).append("\n");
		return sb.toString();
	}

	@Override
	public int compareTo(Nodes o) {
		return this.UID.compareTo(o.UID);
	}
}
