package election;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import network.NetOper;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class ElectionThread implements Runnable {
	Logger log = Logger.getLogger(ElectionThread.class);
	Procedures elProc = new Procedures();
	
	private void printer(){
		log.debug("Election Thread");
	}
	public void electionMsg(int message){
		Nodes successor = Variables.getNextNode();
		try{
			Registry reg = LocateRegistry.getRegistry(successor.getIpAddr(), 
					successor.getRmiPort());
			ProceduresInt elProc = (ProceduresInt) reg.lookup("ElecProc");
			
			//Send election message (code 0)
			elProc.message(Variables.getUID(), message);
			Variables.setParticipant(1);
		}catch(RemoteException e){
			e.printStackTrace();
		}catch(NotBoundException e){
			e.printStackTrace();
		}
	}
	public void sendElection(int threshold, int counter){
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
	@Override
	public void run() {
		BasicConfigurator.configure();
		while(true){
			try{
				TimeUnit.SECONDS.sleep(Variables.ELECTION_INTERVAL);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			
			if(Variables.isLeader()){
				//TEST
				NetOper netOper = new NetOper();
				netOper.getRtt();
				int counter = 0;
				Random rand = new Random();
				int threshold = rand.nextInt(Integer.MAX_VALUE);
				log.debug("Threshold = "+threshold);
				int myRand = rand.nextInt(threshold);
				counter += myRand;
				Variables.setLeader(0);

				if (counter == threshold){
					//Election is over. Leader is the current node
					Variables.setLeader(1);
					Utilities utils = new Utilities();
					Nodes thisNode = new Nodes(utils.getIpAddr(), Variables.getUID(), 
							Variables.getRmiPort());
					Variables.setCurLeader(thisNode);
					//inform next node who's the leader if not already informed
					Nodes successor = Variables.getNextNode();
					try{
						Registry reg = LocateRegistry.getRegistry(successor.getIpAddr(),
								successor.getRmiPort());
						ProceduresInt elProc = (ProceduresInt) reg.lookup("ElecProc");
						if(elProc.isLeader() == false)
							elProc.newLeader(thisNode);
					}catch(RemoteException e){
						e.printStackTrace();
					}catch(NotBoundException e){
						e.printStackTrace();
					}
				}else{
					sendElection(threshold, counter);
				}
				counter = 0;
			}
			//Variables.setLeader(0);
			//Send Election Message to successor
			//electionMsg(Variables.ELECTED_MSG);
			
			//Sleep
			Thread.yield();	
		}
	}

}
