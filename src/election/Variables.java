package election;

import java.util.LinkedList;

public class Variables {
	private static int isParticipant;
	private static int leader;
	private static String uid;
	private static Nodes nextNode;
	private static Nodes curLeader;
	private static int rmiPort;
	private static LinkedList<Nodes> nodesList = new LinkedList<Nodes>();
	private static int tcpPort;
	public static int ELECTION_MSG = 0;
	public static int ELECTED_MSG = 1;
	public static int ELECTION_INTERVAL = 60; //For the time being, in seconds
	public static int TIME_POLL_INTERVAL = 33; //For the time being, in seconds
	
	public static void setTcpPort(int port){
		tcpPort = port;
	}
	public static int getTcpPort(){
		return tcpPort;
	}
	public static void setNodesList(LinkedList<Nodes> nodes){
		nodesList = nodes;
	}
	public static LinkedList<Nodes> getNodesList(){
		return nodesList;
	}
	public static void setParticipant(int participant){
		isParticipant = participant;
	}
	public static boolean isParticipant(){
		return (isParticipant == 1) ? true : false;
	}
	public static void setLeader(int curLeader){
		leader = curLeader;
	}
	public static boolean isLeader(){
		return (leader == 1) ? true : false;
	}
	public static void setUID(String myUid){
		uid = myUid;
	}
	public static void setRmiPort(int RMIPort){
		rmiPort = RMIPort;
	}
	public static String getUID(){
		return uid;
	}
	public static void setNextNode(Nodes myNextNode){
		nextNode = myNextNode;
	}
	public static Nodes getNextNode(){
		return nextNode;
	}
	public static void setCurLeader(Nodes myCurLeader){
		curLeader = myCurLeader;
	}
	public static Nodes getCurLeader(){
		return curLeader;
	}
	public static int getRmiPort(){
		return rmiPort;
	}
}
