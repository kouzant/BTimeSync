package network;

import java.rmi.Remote;
import java.rmi.RemoteException;

import election.Nodes;

public interface NetOperInt extends Remote {
	public void addNode(Nodes node) throws RemoteException;
	public void successors() throws RemoteException;
}
