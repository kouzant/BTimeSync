package time;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Calendar;

public interface TimeOperInt extends Remote {
	public void setAmount(int amount) throws RemoteException;
	public Calendar getTime() throws RemoteException;
	public String printTime(Calendar now) throws RemoteException;
	public Calendar appendError(int amount) throws RemoteException;
	public String printWrongTime() throws RemoteException;
	public void fixErrorAmount(int fix) throws RemoteException;
}
