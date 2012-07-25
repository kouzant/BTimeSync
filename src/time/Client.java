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
		String host = (args.length < 1) ? null : args[0];
		System.out.println(host);
		try{
			Registry reg = LocateRegistry.getRegistry(host, 2020);
			TimeOper timeStub = (TimeOper) reg.lookup("TimeOper");
			System.out.print("Current time: ");
			timeStub.printTime(timeStub.getTime());
			System.out.print("Wrong Time: ");
			timeStub.printTime(timeStub.appendError(timeStub.getTime(), 20));
		}catch (RemoteException e) {
			e.printStackTrace();
		}catch (NotBoundException e) {
			e.printStackTrace();
		}

	}

}
