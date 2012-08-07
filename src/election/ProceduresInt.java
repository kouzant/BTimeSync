package election;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProceduresInt extends Remote {
	public Nodes publishLeader() throws RemoteException;
}
