package network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Collections;
import java.util.Date;
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
		//Collections.sort(nodesList);
		successors();
	}
	public void setNodesList(LinkedList<Nodes> nodesList){
		log.debug("New nodesList, size: "+nodesList.size());
		this.nodesList = nodesList;
	}
	public void getRtt(){
		Iterator<Nodes> nodesIter = nodesList.iterator();
		Nodes indexNode;
		
		while(nodesIter.hasNext()){
			indexNode = nodesIter.next();
			//ICMP default packet size is 56 bytes
			byte[] packet = new byte[56];
			try{
				Registry reg = LocateRegistry.getRegistry(indexNode.getIpAddr(),
						indexNode.getRmiPort());
				NetOperInt netOper = (NetOperInt) reg.lookup("NetOper");
				netOper.prepareTcpServer();
				Socket cSocket = new Socket(indexNode.getIpAddr(), 8008);
				DataOutputStream write = new DataOutputStream(cSocket.getOutputStream());
				BufferedReader read = new BufferedReader(new InputStreamReader(
						cSocket.getInputStream()));
				Date now = new Date();
				write.write(packet);
				read.read();
				Date after = new Date();
				long nowLong = now.getTime();
				long afterLong = after.getTime();
				long rtt = afterLong - nowLong;
				log.debug("rtt: "+ rtt + "milliseconds");
				indexNode.setRtt(rtt);
			}catch (RemoteException e) {
				e.printStackTrace();
			}catch(NotBoundException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	public void prepareTcpServer(){
		try{
			//ICMP default packet size is 56 bytes
			byte[] packet = new byte[56];
			ServerSocket sSocket = new ServerSocket(8008);
			Socket socket = sSocket.accept();
			BufferedReader read = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			DataOutputStream write = new DataOutputStream(socket.getOutputStream());
			read.read();
			write.write(packet);
		}catch(IOException e){
			e.printStackTrace();
		}
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
				NetOperInt netOper = (NetOperInt) reg.lookup("NetOper");
				
				elProc.setNextNode(nxtNode);
				netOper.setNodesList(nodesList);
			}catch(RemoteException e){
				e.printStackTrace();
			}catch(NotBoundException e){
				e.printStackTrace();
			}
		}
		
	}
}
