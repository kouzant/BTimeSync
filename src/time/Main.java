package time;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import election.Variables;
import election.Utils;

public class Main {
	public static void main(String[] args) {
		try{
			Operations oper = new Operations();
			//First argument is the amount of the time error
			oper.setAmount(Integer.parseInt(args[0]));
			//Create stub
			TimeOper timeStub = (TimeOper) UnicastRemoteObject.exportObject(oper, 6789);
			//Bind stub in the registry
			Runtime.getRuntime().exec("rmiregistry 2020");
			LocateRegistry.createRegistry(2020);
			Registry reg = LocateRegistry.getRegistry(2020);
			reg.bind("TimeOper", timeStub);
			Variables vars = new Variables();
			Utils utils = new Utils();
			vars.setUID(utils.getUID());
			System.out.println("UID: "+vars.getUID());
			System.out.println("Server is up!");
		}catch(RemoteException e){
			e.printStackTrace();
		}catch(AlreadyBoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
