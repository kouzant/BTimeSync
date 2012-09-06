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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import election.Nodes;
import election.ProceduresInt;
import election.Variables;

public class NetOper implements NetOperInt{
	Logger log = Logger.getLogger(NetOper.class);
	
	public void addNode(Nodes node){
		LinkedList<Nodes> nodesList = Variables.getNodesList();
		nodesList.add(node);
		//Collections.sort(nodesList);
		successors();
	}
	public void setNodesList(LinkedList<Nodes> nodesList){
		Variables.setNodesList(nodesList);
	}
	public void getRtt(){
		log.debug("Reached here!");
		LinkedList<Nodes> nodesList = Variables.getNodesList();
		Iterator<Nodes> nodesIter = nodesList.iterator();
		Nodes indexNode;
		
		while(nodesIter.hasNext()){
			log.debug("Inside list iteration");
			indexNode = nodesIter.next();
			//ICMP default packet size is 56 bytes
			byte[] packet = new byte[56];
			try{
				Registry reg = LocateRegistry.getRegistry(indexNode.getIpAddr(),
						indexNode.getRmiPort());
				NetOperInt netOper = (NetOperInt) reg.lookup("NetOper");
				netOper.prepareTcpServer();
				int tcpPort = netOper.getTcpPort();
				try{
					TimeUnit.SECONDS.sleep(1);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				log.debug("Connecting to port: "+tcpPort);
				Socket cSocket = new Socket(indexNode.getIpAddr(), tcpPort);
				DataOutputStream write = new DataOutputStream(cSocket.getOutputStream());
				BufferedReader read = new BufferedReader(new InputStreamReader(
						cSocket.getInputStream()));
				Date now = new Date();
				write.write(packet);
				read.read();
				write.close();
				read.close();
				cSocket.close();
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
	public int getTcpPort(){
		return Variables.getTcpPort();
	}
	public void prepareTcpServer(){
		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.execute(new TCPServer());
	}
	public void successors(){
		BasicConfigurator.configure();
		Nodes curNode, nxtNode;
		LinkedList<Nodes> nodesList = Variables.getNodesList();
		
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
