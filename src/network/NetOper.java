package network;

import java.util.Collections;
import java.util.LinkedList;

import election.Nodes;

public class NetOper implements NetOperInt{
	private LinkedList<Nodes> nodesList = new LinkedList<Nodes>();
	
	public void addNode(Nodes node){
		nodesList.add(node);
		Collections.sort(nodesList);
	}
	public String hello(){
		return "Hello";
	}
}
