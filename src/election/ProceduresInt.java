package election;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProceduresInt extends Remote {
	public Nodes publishLeader() throws RemoteException;
	public void setNextNode(Nodes nxtNode) throws RemoteException;
	public void message(String uid, int messageCode) throws RemoteException;
}
