package time;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;

import network.NetOperInt;

public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String hosts = "";
		String rmiPorts = "";
		//Parse command line arguments
		for (int i = 0; i < args.length; i++){
			if (args[i].equals("-h")){
				hosts = args[i + 1];
			}
			if (args[i].equals("-p")){
				rmiPorts = args[i + 1];
			}
		}
		if(hosts.equals("")){
			System.err.println("You did not specified a host.");
			System.exit(1);
		}
		if(rmiPorts.equals("")){
			System.err.println("You did not specified rmi port");
			System.exit(1);
		}
		//Parse hosts
		String[] hostsArray = hosts.split("[,]");
		//Parse rmi ports
		String[] portsArray = rmiPorts.split("[,]");
		
		for(int i = 0; i < hostsArray.length; i++){
			System.out.print("Host: "+hostsArray[i]+"\t");
			try{
				Registry reg = LocateRegistry.getRegistry(hostsArray[i],
						Integer.parseInt(portsArray[i]));
				TimeOperInt timeStub = (TimeOperInt) reg.lookup("TimeOper");
				System.out.print("Amount: "+timeStub.getAmount()+"\t");
				//Wrong time
				System.out.println("Time: "+timeStub.printTime()+"");
			}catch (RemoteException e) {
				e.printStackTrace();
			}catch (NotBoundException e) {
				e.printStackTrace();
			}
		}
	}

}
