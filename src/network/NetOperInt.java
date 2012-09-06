package network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

import election.Nodes;

public interface NetOperInt extends Remote {
	public void addNode(Nodes node) throws RemoteException;
	public void successors() throws RemoteException;
	public void prepareTcpServer() throws RemoteException;
	public void setNodesList(LinkedList<Nodes> nodesList) throws RemoteException;
	public int getTcpPort() throws RemoteException;
}
