package election;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProceduresInt extends Remote {
	public Nodes publishLeader() throws RemoteException;
	public void setNextNode(Nodes nxtNode) throws RemoteException;
	public void message(String uid, int messageCode) throws RemoteException;
	public void electionMes(int threshold, int counter) throws RemoteException;
	public void newLeader(Nodes newLeader) throws RemoteException;
	public void setLeader(int code) throws RemoteException;
}
