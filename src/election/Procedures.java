package election;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class Procedures implements ProceduresInt{
	private static Logger log = Logger.getLogger(Procedures.class);
	public Nodes publishLeader(){
		BasicConfigurator.configure();
		return Variables.getCurLeader();
	}
	public void setNextNode(Nodes nxtNode){
		Variables.setNextNode(nxtNode);
	}
	private void fwMessage(String uid, int messageCode){
		Nodes successor = Variables.getNextNode();
		try{
			Registry reg = LocateRegistry.getRegistry(successor.getIpAddr(), 
					successor.getRmiPort());
			ProceduresInt elProc = (ProceduresInt) reg.lookup("ElecProc");

			//Send election message (code 0)
			elProc.message(Variables.getUID(), messageCode);
		}catch(RemoteException e){
			e.printStackTrace();
		}catch(NotBoundException e){
			e.printStackTrace();
		}
	}
	public void setLeader(int code){
		Variables.setLeader(code);
	}
	public void newLeader(Nodes newLeader){
		Variables.setCurLeader(newLeader);
		Variables.setLeader(0);
		log.debug("The new leader is "+newLeader.getUID());
		Nodes successor = Variables.getNextNode();
		try{
			Registry reg = LocateRegistry.getRegistry(successor.getIpAddr(), 
					successor.getRmiPort());
			ProceduresInt elProc = (ProceduresInt) reg.lookup("ElecProc");

			if (elProc.isLeader() == false)
				elProc.newLeader(newLeader);
		}catch(RemoteException e){
			e.printStackTrace();
		}catch (NotBoundException e){
			e.printStackTrace();
		}
	}
	public boolean isLeader(){
		return Variables.isLeader();
	}
	public void electionMes(int threshold, int counter){
		Random rand = new Random();
		int myRand = rand.nextInt(threshold);
		counter +=myRand;
		
		if (counter >= threshold){
			//We have a leader
			log.debug("I am the new leader");
			Utilities utils = new Utilities();
			Nodes thisNode = new Nodes(utils.getIpAddr(), Variables.getUID(), 
					Variables.getRmiPort());
			Variables.setLeader(1);
			Variables.setCurLeader(thisNode);

			Nodes successor = Variables.getNextNode();
			try{
				Registry reg = LocateRegistry.getRegistry(successor.getIpAddr(), 
						successor.getRmiPort());
				ProceduresInt elProc = (ProceduresInt) reg.lookup("ElecProc");
				
				if(elProc.isLeader() == false){
					elProc.setLeader(0);
					elProc.newLeader(thisNode);
				}
			}catch(RemoteException e){
				e.printStackTrace();
			}catch(NotBoundException e){
				e.printStackTrace();
			}
		}else{
			//Forward the election message
			Nodes successor = Variables.getNextNode();
			try{
				Registry reg = LocateRegistry.getRegistry(successor.getIpAddr(),
						successor.getRmiPort());
				ProceduresInt elProc = (ProceduresInt) reg.lookup("ElecProc");
				
				elProc.electionMes(threshold, counter);
			}catch(RemoteException e){
				e.printStackTrace();
			}catch(NotBoundException e){
				e.printStackTrace();
			}
		}
	}
	public void message(String uid, int messageCode){
		log.debug("Election UID: "+uid);
		log.debug("Election code: "+messageCode);
		
		if(messageCode == Variables.ELECTION_MSG){
			if(Variables.getUID().compareTo(uid) > 0){
				fwMessage(uid, messageCode);
			}else if((Variables.getUID().compareTo(uid) < 0) && !Variables.
					isParticipant()){
				fwMessage(Variables.getUID(), messageCode);
			}else if((Variables.getUID().compareTo(uid) < 0) && Variables.
					isParticipant()){
				fwMessage(uid, messageCode);
			}else if(Variables.getUID().compareTo(uid) == 0){
				log.debug("We have a new leader");
				log.debug("UID: "+uid);
				Variables.setLeader(1);
				fwMessage(uid, Variables.ELECTED_MSG);
				Variables.setParticipant(0);
			}
		}else if(messageCode == Variables.ELECTED_MSG){
			if(!Variables.isLeader()){
				Variables.setParticipant(0);
				fwMessage(uid, Variables.ELECTED_MSG);
			}else{
				log.debug("ELECTED");
			}
		}
	}
}
