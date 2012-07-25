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
