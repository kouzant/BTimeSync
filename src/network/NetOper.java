package network;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import election.Nodes;
import election.ProceduresInt;

public class NetOper implements NetOperInt{
	Logger log = Logger.getLogger(NetOper.class);
	private LinkedList<Nodes> nodesList = new LinkedList<Nodes>();
	
	public void addNode(Nodes node){
		nodesList.add(node);
		Collections.sort(nodesList);
		successors();
	}
	
	public void successors(){
		BasicConfigurator.configure();
		Nodes curNode, nxtNode;
		
		for(int index = 0; index < nodesList.size(); index++){
			if(index == (nodesList.size() - 1)){
				curNode = nodesList.get(index);
				nxtNode = nodesList.getFirst();
			}else{
				curNode = nodesList.get(index);
				nxtNode = nodesList.get(index + 1);
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
		}
		
	}
}
