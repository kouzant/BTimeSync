package election;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.TimeUnit;

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
	@Override
	public void run() {
		BasicConfigurator.configure();
		while(true){
			try{
				TimeUnit.SECONDS.sleep(Variables.ELECTION_INTERVAL);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			Variables.setLeader(0);
			//Send Election Message to successor
			electionMsg(Variables.ELECTED_MSG);
			
			//Sleep
			Thread.yield();	
		}
	}

}
