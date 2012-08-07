package network;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import election.Nodes;
import election.ProceduresInt;

public class NetOper implements NetOperInt{
	private LinkedList<Nodes> nodesList = new LinkedList<Nodes>();
	
	public void addNode(Nodes node){
		nodesList.add(node);
		Collections.sort(nodesList);
		successors();
	}
	
	public void successors(){
		Iterator<Nodes> nodesIt = nodesList.iterator();
		Nodes curNode, nxtNode;
		int length = nodesList.size(), counter = 1;
		
		while(nodesIt.hasNext()){
			curNode = nodesIt.next();
			if(counter == length){
				nxtNode = nodesList.getFirst();
			}else{
				nxtNode = nodesIt.next();
			}
			
			//Connect to curNode and inform about the next node
			try{
				Registry reg = LocateRegistry.getRegistry(curNode.getIpAddr(), 
						curNode.getRmiPort());
				ProceduresInt elProc = (ProceduresInt) reg.lookup("ElecProc");
				elProc.setNextNode(nxtNode);
			}catch(RemoteException e){
				e.printStackTrace();
			}catch(NotBoundException e){
				e.printStackTrace();
			}
			counter++;
		}
		
	}
}
