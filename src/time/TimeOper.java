package time;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Calendar;

public interface TimeOper extends Remote {
	public Calendar getTime() throws RemoteException;
	public void printTime(Calendar now) throws RemoteException;
	public Calendar appendError(Calendar now, int amount) throws RemoteException;
}
