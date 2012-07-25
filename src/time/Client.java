package time;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String host = "";
		int rmiPort = 2020;
		//Parse command line arguments
		for (int i = 0; i < args.length; i++){
			if (args[i].equals("-h")){
				host = args[i + 1];
			}
			if (args[i].equals("-p")){
				rmiPort = Integer.parseInt(args[i + 1]);
			}
		}
		if(host.equals("")){
			System.err.println("You did not specified a host.");
			System.exit(1);
		}
		
		System.out.println(host);
		try{
			Registry reg = LocateRegistry.getRegistry(host, rmiPort);
			TimeOper timeStub = (TimeOper) reg.lookup("TimeOper");
			//Real time
			System.out.print("Current time: ");
			timeStub.printTime(timeStub.getTime());
			//Wrong time
			System.out.print("Wrong Time before fix: ");
			timeStub.printWrongTime();
			//Wrong time after a fix
			int fix = -3;
			timeStub.fixErrorAmount(fix);
			System.out.print("Wrong time after fix: ");
			timeStub.printWrongTime();
		}catch (RemoteException e) {
			e.printStackTrace();
		}catch (NotBoundException e) {
			e.printStackTrace();
		}

	}

}
