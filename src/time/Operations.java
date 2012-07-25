package time;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;

public class Operations implements TimeOper{
	private int amount = 0;
	
	public void setAmount(int amount){
		this.amount = amount;
	}
	public Calendar getTime(){
		Calendar now = Calendar.getInstance();
		
		return now;
	}
	public void printTime(Calendar now){
		StringBuilder time = new StringBuilder();
		time.append(now.get(Calendar.HOUR_OF_DAY))
		.append(":").append(now.get(Calendar.MINUTE))
		.append(":").append(now.get(Calendar.SECOND)).append("\n");
		System.out.println(time.toString());
	}
	public Calendar appendError(int amount){
		Calendar now = getTime();
		now.add(Calendar.SECOND, amount);
		
		return now;
	}
	public void printWrongTime(){
		Calendar now = getTime();
		now.add(Calendar.SECOND, amount);
		printTime(now);
	}
	public void fixErrorAmount(int fix){
		setAmount(amount + fix);
	}
	
	public static void main(String[] args){
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
