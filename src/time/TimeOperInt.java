package time;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

public interface TimeOperInt extends Remote {
	public void setAmount(int amount) throws RemoteException;
	public Date getTime() throws RemoteException;
	public String printTime() throws RemoteException;
	public void fixErrorAmount(int fix) throws RemoteException;
	public long getRunningTime() throws RemoteException;
	public void getNodesTime() throws RemoteException;
	public void computeFix() throws RemoteException;
	public void pushFix() throws RemoteException;
	public int getAmount() throws RemoteException;
	public Date appendError() throws RemoteException;
}
