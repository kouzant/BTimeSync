package time;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

import network.NetOper;
import network.NetOperInt;

import election.Nodes;
import election.Procedures;
import election.ProceduresInt;
import election.Variables;
import election.Utilities;

public class Main {
	private static Logger log = Logger.getLogger(Main.class);
	public static void main(String[] args) {
		BasicConfigurator.configure();
		Utilities utils = new Utilities();
		String configFile="config/client";
		int amount = 0;
		int rmiPort = 2020;
		Variables.setUID(utils.getUID());
		log.debug("UID: " + Variables.getUID());
		
		//Parse configuration file
		for (int i = 0; i < args.length; i++){
			if (args[i].equals("-c")){
				configFile = args[i + 1];
			}
		}
		ConfigParser cp = new ConfigParser(configFile);
		if (cp.bootstrap().equals(" ")){
		//Bootstrap node
			log.debug("Bootstrap and Leader!");
			Variables.setLeader(1);
			Nodes curNode = new Nodes(utils.getIpAddr(), utils.getUID());
			Variables.setCurLeader(curNode);
			log.debug(Variables.getCurLeader());
		}else{
			log.debug("Not a leader!");
			Variables.setLeader(0);
		}
		Variables.setParticipant(0);
		amount = cp.timeError();
		rmiPort = cp.rmiPort();
		
		//Prepare RMI
		try{
			TimeOper oper = new TimeOper();
			NetOper mess = new NetOper();
			Procedures elProc = new Procedures();
			//First argument is the amount of the time error
			oper.setAmount(amount);
			//Create stub
			TimeOperInt timeStub = (TimeOperInt) UnicastRemoteObject.
					exportObject(oper, 0);
			NetOperInt netStub = (NetOperInt) UnicastRemoteObject.
					exportObject(mess, 0);
			ProceduresInt procStub = (ProceduresInt) UnicastRemoteObject.
					exportObject(elProc, 0);
			//Bind stub in the registry
			LocateRegistry.createRegistry(rmiPort);
			Registry reg = LocateRegistry.getRegistry(rmiPort);
			reg.bind("TimeOper", timeStub);
			reg.bind("NetOper", netStub);
			reg.bind("ElecProc", procStub);
			log.info("Server is up!");
		}catch(RemoteException e){
			e.printStackTrace();
		}catch(AlreadyBoundException e){
			e.printStackTrace();
		}

		if (!cp.bootstrap().equals(" ")){
			log.debug("Not bootstrap node");
		//Node is not bootstrap
			try{
				Registry reg = LocateRegistry.getRegistry(cp.bootstrap(), cp.b_port());
				log.debug("bootstrap: "+cp.bootstrap());
				log.debug("rmi port: "+cp.b_port());
				ProceduresInt elProc = (ProceduresInt) reg.lookup("ElecProc");
				//RMI call to boostrap node to get leader
				Variables.setCurLeader(elProc.publishLeader());
				log.debug(Variables.getCurLeader());
			}catch(RemoteException e){
				e.printStackTrace();
			}catch(NotBoundException e){
				e.printStackTrace();
			}
		}
	}

}
