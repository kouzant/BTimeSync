package election;

public class Variables {
	private int participant;
	private int leader;
	private String uid;
	
	public Variables(){
		participant = 0;
		leader = 0;
		uid = "";
	}
	
	public void setParticipant(int participant){
		this.participant = participant;
	}
	public boolean isParticipant(){
		return (participant == 1) ? true : false;
	}
	public void setLeader(int leader){
		this.leader = leader;
	}
	public boolean isLeader(){
		return (leader == 1) ? true : false;
	}
	public void setUID(String uid){
		this.uid = uid;
	}
	public String getUID(){
		return uid;
	}
}
