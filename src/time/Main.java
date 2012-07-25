package time;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import network.NetOper;
import network.NetOperInt;

import election.Nodes;
import election.Variables;
import election.Utils;

public class Main {
	public static void main(String[] args) {
		Variables vars = new Variables();
		Utils utils = new Utils();
		String configFile="config/client";
		int amount = 0;
		int rmiPort = 2020;
		vars.setUID(utils.getUID());
		System.out.println("UID: "+vars.getUID());
		
		//Parse configuration file
		for (int i = 0; i < args.length; i++){
			if (args[i].equals("-c")){
				configFile = args[i + 1];
				System.err.println("REACHED HERE!");
			}
		}
		ConfigParser cp = new ConfigParser(configFile);
		if (cp.isLeader() == 1){
			System.out.println("Leader!");
			vars.setLeader(1);
			Nodes curNode = new Nodes(utils.getIpAddr(), utils.getUID());
			vars.setCurLeader(curNode);
		}
		amount = cp.timeError();
		rmiPort = cp.rmiPort();
		
		try{
			TimeOper oper = new TimeOper();
			NetOper mess = new NetOper();
			//First argument is the amount of the time error
			oper.setAmount(amount);
			//Create stub
			TimeOperInt timeStub = (TimeOperInt) UnicastRemoteObject.exportObject(oper, 0);
			NetOperInt netStub = (NetOperInt) UnicastRemoteObject.exportObject(mess, 0);
			//Bind stub in the registry
			LocateRegistry.createRegistry(rmiPort);
			Registry reg = LocateRegistry.getRegistry(rmiPort);
			reg.bind("TimeOper", timeStub);
			reg.bind("NetOper", netStub);
			System.out.println("Server is up!");
		}catch(RemoteException e){
			e.printStackTrace();
		}catch(AlreadyBoundException e){
			e.printStackTrace();
		}
	}

}
