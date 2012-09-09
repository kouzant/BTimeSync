package time;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

import network.NetOper;
import network.NetOperInt;

import election.ElectionThread;
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
		Variables.setUID(utils.getUID());
		log.info("UID: " + Variables.getUID());
		
		//Parse configuration file
		for (int i = 0; i < args.length; i++){
			if (args[i].equals("-c")){
				configFile = args[i + 1];
			}
		}
		ConfigParser cp = new ConfigParser(configFile);
		Variables.setRmiPort(cp.rmiPort());
		Variables.setTcpPort(cp.tcpPort());
		Nodes thisNode = new Nodes(utils.getIpAddr(), Variables.getUID(), 
				Variables.getRmiPort());
		log.debug("This node:"+thisNode);
		if (cp.bootstrap().equals(" ")){
		//Bootstrap node
			log.debug("Bootstrap and Leader!");
			Variables.setLeader(1);	
			Variables.setCurLeader(thisNode);
		}else{
			log.debug("Not a leader!");
			Variables.setLeader(0);
		}
		Variables.setParticipant(0);
		amount = cp.timeError();
		
		//Prepare RMI
		try{
			TimeOper oper = new TimeOper();
			NetOper mess = new NetOper();
			Procedures elProc = new Procedures();
			//Create stub
			TimeOperInt timeStub = (TimeOperInt) UnicastRemoteObject.
					exportObject(oper, 0);
			NetOperInt netStub = (NetOperInt) UnicastRemoteObject.
					exportObject(mess, 0);
			ProceduresInt procStub = (ProceduresInt) UnicastRemoteObject.
					exportObject(elProc, 0);
			//Bind stub in the registry
			LocateRegistry.createRegistry(Variables.getRmiPort());
			Registry reg = LocateRegistry.getRegistry(Variables.getRmiPort());
			reg.bind("TimeOper", timeStub);
			reg.bind("NetOper", netStub);
			reg.bind("ElecProc", procStub);
			//Add amount of time error
			Registry reg1 = LocateRegistry.getRegistry("127.0.0.1", Variables.getRmiPort());
			TimeOperInt timeOper = (TimeOperInt) reg1.lookup("TimeOper");
			timeOper.setAmount(amount);
			timeOper.appendError();
			log.info("Server is up!");
		}catch(RemoteException e){
			e.printStackTrace();
		}catch(AlreadyBoundException e){
			e.printStackTrace();
		}catch(NotBoundException e){
			e.printStackTrace();
		}
		
		if (!cp.bootstrap().equals(" ")){
		//Node is not bootstrap
			try{
				Registry reg = LocateRegistry.getRegistry(cp.bootstrap(), cp.b_port());
				ProceduresInt elProc = (ProceduresInt) reg.lookup("ElecProc");
				//RMI call to boostrap node to get leader
				Variables.setCurLeader(elProc.publishLeader());
				//Connect to leader
				reg = LocateRegistry.getRegistry(Variables.getCurLeader().getIpAddr(),
						Variables.getCurLeader().getRmiPort());
				NetOperInt netOper = (NetOperInt) reg.lookup("NetOper");
				//Send current Node info to leader
				netOper.addNode(thisNode);
			}catch(RemoteException e){
				e.printStackTrace();
			}catch(NotBoundException e){
				e.printStackTrace();
			}
		}else{
			//Bootstrap
			//Add itself to the nodes list
			try{
				Registry reg = LocateRegistry.getRegistry("127.0.0.1", 
						Variables.getRmiPort());
				NetOperInt netOper = (NetOperInt) reg.lookup("NetOper");
				netOper.addNode(thisNode);
			}catch(RemoteException e){
				e.printStackTrace();
			}catch(NotBoundException e){
				e.printStackTrace();
			}
		}
		
		//If is leader, wake the election process
		
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new ElectionThread());
		exec.execute(new TimeSync());
		
		//For debugging purposes
		try{
			TimeUnit.SECONDS.sleep(20);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		System.out.println("Successor: "+Variables.getNextNode());
	}
}
